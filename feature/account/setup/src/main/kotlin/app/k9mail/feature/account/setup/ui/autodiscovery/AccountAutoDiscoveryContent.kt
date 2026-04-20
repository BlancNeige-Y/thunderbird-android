package app.k9mail.feature.account.setup.ui.autodiscovery

import android.content.res.Resources
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import app.k9mail.core.ui.compose.designsystem.atom.text.TextBodyMedium
import app.k9mail.core.ui.compose.designsystem.atom.textfield.TextFieldOutlined
import app.k9mail.core.ui.compose.designsystem.molecule.ContentLoadingErrorView
import app.k9mail.core.ui.compose.designsystem.molecule.ErrorView
import app.k9mail.core.ui.compose.designsystem.molecule.LoadingView
import app.k9mail.core.ui.compose.designsystem.molecule.input.InputLayout
import app.k9mail.core.ui.compose.designsystem.molecule.input.PasswordInput
import app.k9mail.core.ui.compose.designsystem.template.ResponsiveWidthContainer
import app.k9mail.feature.account.common.ui.AppTitleTopHeader
import app.k9mail.feature.account.common.ui.WizardNavigationBar
import app.k9mail.feature.account.common.ui.WizardNavigationBarState
import app.k9mail.feature.account.oauth.ui.AccountOAuthContract
import app.k9mail.feature.account.setup.R
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.Event
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.State
import net.thunderbird.core.ui.compose.common.modifier.testTagAsResourceId
import net.thunderbird.core.ui.compose.theme2.MainTheme

@Composable
internal fun AccountAutoDiscoveryContent(
    state: State,
    onEvent: (Event) -> Unit,
    oAuthViewModel: AccountOAuthContract.ViewModel,
    brandName: String,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    ResponsiveWidthContainer(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .consumeWindowInsets(contentPadding)
            .imePadding()
            .testTagAsResourceId("AccountAutoDiscoveryContent"),
    ) { responsiveWidthPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(responsiveWidthPadding),
            ) {
                AppTitleTopHeader(
                    title = brandName,
                )
                Spacer(modifier = Modifier.weight(1f))
                AutoDiscoveryContent(
                    state = state,
                    onEvent = onEvent,
                    oAuthViewModel = oAuthViewModel,
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            WizardNavigationBar(
                onNextClick = { onEvent(Event.OnNextClicked) },
                onBackClick = { onEvent(Event.OnBackClicked) },
                state = WizardNavigationBarState(showNext = state.isNextButtonVisible),
            )
        }
    }
}

@Composable
internal fun AutoDiscoveryContent(
    state: State,
    onEvent: (Event) -> Unit,
    oAuthViewModel: AccountOAuthContract.ViewModel,
    modifier: Modifier = Modifier,
) {
    val resources = LocalResources.current

    ContentLoadingErrorView(
        state = state,
        loading = {
            LoadingView(
                message = stringResource(id = R.string.account_setup_auto_discovery_loading_message),
                modifier = Modifier.fillMaxSize(),
            )
        },
        error = { error ->
            ErrorView(
                title = stringResource(id = R.string.account_setup_auto_discovery_loading_error),
                message = error.toAutoDiscoveryErrorString(resources),
                onRetry = { onEvent(Event.OnRetryClicked) },
                modifier = Modifier.fillMaxSize(),
            )
        },
        content = { contentState ->
            @Suppress("ViewModelForwarding")
            ContentView(
                state = contentState,
                onEvent = onEvent,
                oAuthViewModel = oAuthViewModel,
                resources = resources,
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
    )
}

@Composable
internal fun ContentView(
    state: State,
    onEvent: (Event) -> Unit,
    oAuthViewModel: AccountOAuthContract.ViewModel,
    resources: Resources,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MainTheme.spacings.quadruple)
            .then(modifier),
    ) {
        InputLayout(
            errorMessage = state.emailAddress.error?.toAutoDiscoveryValidationErrorString(resources),
            contentPadding = PaddingValues(),
        ) {
            TextFieldOutlined(
                value = state.emailAddress.value,
                onValueChange = { onEvent(Event.EmailAddressChanged(it)) },
                label = stringResource(id = R.string.account_setup_local_part_label),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                hasError = state.emailAddress.error != null,
                modifier = Modifier.testTagAsResourceId("account_setup_email_address_input"),
            )
        }

        // [BJJGJ-CUSTOM] Show the final enforced address so the real account identifier is always visible.
        TextBodyMedium(
            text = if (state.fullEmailAddress.isNotBlank()) {
                stringResource(id = R.string.account_setup_local_part_full_email, state.fullEmailAddress)
            } else {
                stringResource(id = R.string.account_setup_local_part_hint)
            },
            modifier = Modifier.padding(top = MainTheme.spacings.default),
        )

        if (state.configStep == AccountAutoDiscoveryContract.ConfigStep.PASSWORD) {
            Spacer(modifier = Modifier.height(MainTheme.spacings.double))
            PasswordInput(
                password = state.password.value,
                errorMessage = state.password.error?.toAutoDiscoveryValidationErrorString(resources),
                onPasswordChange = { onEvent(Event.PasswordChanged(it)) },
                contentPadding = PaddingValues(),
                modifier = Modifier.testTagAsResourceId("account_setup_password_input"),
            )
        }
    }
}
