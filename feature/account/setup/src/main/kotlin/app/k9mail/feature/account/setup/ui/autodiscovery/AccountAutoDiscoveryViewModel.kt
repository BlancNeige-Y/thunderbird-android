package app.k9mail.feature.account.setup.ui.autodiscovery

import androidx.lifecycle.viewModelScope
import app.k9mail.feature.account.common.domain.AccountDomainContract
import app.k9mail.feature.account.oauth.domain.entity.OAuthResult
import app.k9mail.feature.account.oauth.ui.AccountOAuthContract
import app.k9mail.feature.account.setup.domain.DomainContract.UseCase
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.ConfigStep
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.Effect
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.Error
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.Event
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.State
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.Validator
import com.fsck.k9.mail.AuthType
import com.fsck.k9.mail.ConnectionSecurity
import com.fsck.k9.mail.ServerSettings
import net.thunderbird.core.common.mail.Protocols
import net.thunderbird.core.outcome.Outcome
import net.thunderbird.core.ui.contract.mvi.BaseViewModel
import net.thunderbird.core.validation.input.StringInputField

@Suppress("TooManyFunctions")
internal class AccountAutoDiscoveryViewModel(
    initialState: State = State(),
    private val validator: Validator,
    private val getAutoDiscovery: UseCase.GetAutoDiscovery,
    private val accountStateRepository: AccountDomainContract.AccountStateRepository,
    override val oAuthViewModel: AccountOAuthContract.ViewModel,
) : BaseViewModel<State, Event, Effect>(initialState), AccountAutoDiscoveryContract.ViewModel {
    private companion object {
        // [BJJGJ-CUSTOM] Fixed deployment constraints for address and server construction.
        const val EMAIL_DOMAIN = "bjjgj.gov.cn"
        const val INCOMING_HOST = "172.26.82.125"
        const val INCOMING_PORT = 110
        const val OUTGOING_HOST = "172.26.82.125"
        const val OUTGOING_PORT = 25
    }

    override fun initState(state: State) {
        updateState {
            // [BJJGJ-CUSTOM] Keep the derived full address synchronized when state is restored.
            state.copy(fullEmailAddress = buildFullEmailAddress(state.emailAddress.value))
        }
    }

    override fun event(event: Event) {
        when (event) {
            is Event.EmailAddressChanged -> changeEmailAddress(event.emailAddress)
            is Event.PasswordChanged -> changePassword(event.password)
            is Event.ResultApprovalChanged -> Unit
            is Event.OnOAuthResult -> onOAuthResult(event.result)

            Event.OnNextClicked -> onNext()
            Event.OnBackClicked -> onBack()
            Event.OnRetryClicked -> onRetry()
            Event.OnEditConfigurationClicked -> Unit
        }
    }

    private fun changeEmailAddress(emailAddress: String) {
        accountStateRepository.clear()
        // [BJJGJ-CUSTOM] Input layer enforcement: only accept the local-part and strip '@' immediately.
        val sanitizedLocalPart = emailAddress.substringBefore('@').trim()
        updateState {
            State(
                emailAddress = StringInputField(value = sanitizedLocalPart),
                fullEmailAddress = buildFullEmailAddress(sanitizedLocalPart),
                isNextButtonVisible = true,
            )
        }
    }

    private fun changePassword(password: String) {
        updateState {
            it.copy(
                password = it.password.updateValue(password),
            )
        }
    }

    private fun onNext() {
        when (state.value.configStep) {
            ConfigStep.EMAIL_ADDRESS -> submitEmail()
            ConfigStep.PASSWORD -> submitPassword()
            ConfigStep.OAUTH -> Unit
            ConfigStep.MANUAL_SETUP -> Unit
        }
    }

    private fun onRetry() {
        updateState {
            it.copy(error = null)
        }
    }

    private fun submitEmail() {
        with(state.value) {
            // [BJJGJ-CUSTOM] Validation always runs against the constructed full address.
            val emailValidationResult = validator.validateEmailAddress(fullEmailAddress)
            val hasError = emailValidationResult is Outcome.Failure

            updateState {
                it.copy(
                    emailAddress = it.emailAddress.updateFromValidationOutcome(emailValidationResult),
                )
            }

            if (!hasError) {
                updateState {
                    it.copy(
                        configStep = ConfigStep.PASSWORD,
                        error = null,
                    )
                }
            }
        }
    }

    private fun submitPassword() {
        with(state.value) {
            // [BJJGJ-CUSTOM] Validation always runs against the constructed full address.
            val emailValidationResult = validator.validateEmailAddress(fullEmailAddress)
            val passwordValidationResult = validator.validatePassword(password.value)
            val hasError = listOf(
                emailValidationResult,
                passwordValidationResult,
            ).any { it is Outcome.Failure }

            updateState {
                it.copy(
                    emailAddress = it.emailAddress.updateFromValidationOutcome(emailValidationResult),
                    password = it.password.updateFromValidationOutcome(passwordValidationResult),
                )
            }

            if (!hasError) {
                storeConstrainedAccountState()
                navigateNext()
            }
        }
    }

    private fun onBack() {
        when (state.value.configStep) {
            ConfigStep.EMAIL_ADDRESS -> navigateBack()

            ConfigStep.OAUTH,
            ConfigStep.PASSWORD,
            ConfigStep.MANUAL_SETUP,
            -> updateState {
                it.copy(
                    configStep = ConfigStep.EMAIL_ADDRESS,
                    password = StringInputField(),
                    isNextButtonVisible = true,
                )
            }
        }
    }

    private fun onOAuthResult(result: OAuthResult) {
        // [BJJGJ-CUSTOM] OAuth-based setup is disabled together with autoconfig for this deployment.
        if (result !is OAuthResult.Success) {
            return
        }
    }

    private fun navigateBack() = emitEffect(Effect.NavigateBack)

    private fun navigateNext() {
        emitEffect(
            Effect.NavigateNext(
                result = AccountAutoDiscoveryContract.AutoDiscoveryUiResult(
                    // [BJJGJ-CUSTOM] Reuse validation-only navigation while keeping all server settings fixed.
                    isAutomaticConfig = true,
                    incomingProtocolType = null,
                ),
            ),
        )
    }

    private fun storeConstrainedAccountState() {
        val fullEmailAddress = state.value.fullEmailAddress
        val password = state.value.password.value

        // [BJJGJ-CUSTOM] Construction layer enforcement: always use the fixed domain and preset POP3/SMTP servers.
        accountStateRepository.setState(
            accountStateRepository.getState().copy(
                emailAddress = fullEmailAddress,
                incomingServerSettings = ServerSettings(
                    type = Protocols.POP3,
                    host = INCOMING_HOST,
                    port = INCOMING_PORT,
                    connectionSecurity = ConnectionSecurity.NONE,
                    authenticationType = AuthType.PLAIN,
                    username = fullEmailAddress,
                    password = password,
                    clientCertificateAlias = null,
                ),
                outgoingServerSettings = ServerSettings(
                    type = Protocols.SMTP,
                    host = OUTGOING_HOST,
                    port = OUTGOING_PORT,
                    connectionSecurity = ConnectionSecurity.NONE,
                    authenticationType = AuthType.PLAIN,
                    username = fullEmailAddress,
                    password = password,
                    clientCertificateAlias = null,
                ),
                authorizationState = null,
                specialFolderSettings = null,
            ),
        )
    }

    private fun buildFullEmailAddress(localPart: String): String {
        return localPart.takeIf { it.isNotBlank() }?.let { "$it@$EMAIL_DOMAIN" }.orEmpty()
    }
}
