package app.k9mail.feature.account.setup.ui.autodiscovery

import app.k9mail.feature.account.common.domain.AccountDomainContract
import app.k9mail.feature.account.common.domain.entity.IncomingProtocolType
import app.k9mail.feature.account.oauth.domain.entity.OAuthResult
import app.k9mail.feature.account.oauth.ui.AccountOAuthContract
import app.k9mail.feature.account.setup.domain.DomainContract.UseCase
import app.k9mail.feature.account.setup.domain.usecase.ValidateEmailAddress
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.AutoDiscoveryUiResult
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.Effect
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.Event
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.State
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.Validator
import net.thunderbird.core.outcome.Outcome
import net.thunderbird.core.ui.contract.mvi.BaseViewModel

@Suppress("TooManyFunctions")
internal class AccountAutoDiscoveryViewModel(
    initialState: State = State(),
    private val validator: Validator,
    private val getAutoDiscovery: UseCase.GetAutoDiscovery,
    private val accountStateRepository: AccountDomainContract.AccountStateRepository,
    override val oAuthViewModel: AccountOAuthContract.ViewModel,
) : BaseViewModel<State, Event, Effect>(initialState), AccountAutoDiscoveryContract.ViewModel {

    override fun initState(state: State) {
        updateState { state.copy() }
    }

    override fun event(event: Event) {
        when (event) {
            is Event.EmailAddressChanged -> changeEmailAddress(event.emailAddress)
            is Event.PasswordChanged -> changePassword(event.password)
            is Event.ResultApprovalChanged -> Unit
            is Event.OnOAuthResult -> onOAuthResult(event.result)
            Event.OnNextClicked -> submitCredentials()
            Event.OnBackClicked -> emitEffect(Effect.NavigateBack)
            Event.OnRetryClicked -> Unit
            Event.OnEditConfigurationClicked -> Unit
        }
    }

    private fun changeEmailAddress(emailAddress: String) {
        accountStateRepository.clear()
        val sanitizedLocalPart = emailAddress.substringBefore("@").trim()

        updateState {
            it.copy(
                emailAddress = it.emailAddress.updateValue(sanitizedLocalPart),
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

    private fun submitCredentials() {
        with(state.value) {
            val fullEmailAddress = emailAddress.value.toFullEmailAddress()
            val emailValidationResult = if (emailAddress.value.contains("@")) {
                Outcome.failure(ValidateEmailAddress.ValidateEmailAddressError.NotAllowed)
            } else {
                validator.validateEmailAddress(fullEmailAddress)
            }
            val passwordValidationResult = validator.validatePassword(password.value)
            val hasError = listOf(emailValidationResult, passwordValidationResult).any { it is Outcome.Failure }

            updateState {
                it.copy(
                    emailAddress = it.emailAddress.updateFromValidationOutcome(emailValidationResult),
                    password = it.password.updateFromValidationOutcome(passwordValidationResult),
                )
            }

            if (!hasError) {
                accountStateRepository.setState(state.value.toAccountState())
                emitEffect(
                    Effect.NavigateNext(
                        AutoDiscoveryUiResult(
                            isAutomaticConfig = true,
                            incomingProtocolType = IncomingProtocolType.POP3,
                        ),
                    ),
                )
            }
        }
    }

    private fun onOAuthResult(result: OAuthResult) {
        updateState {
            it.copy(
                authorizationState = (result as? OAuthResult.Success)?.authorizationState,
            )
        }
    }
}
