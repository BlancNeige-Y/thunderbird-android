package app.k9mail.feature.account.setup.ui.autodiscovery

import android.content.res.Resources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import app.k9mail.core.ui.compose.designsystem.atom.text.TextBodyMedium
import app.k9mail.core.ui.compose.designsystem.atom.text.TextTitleSmall
import app.k9mail.core.ui.compose.designsystem.molecule.input.PasswordInput
import app.k9mail.core.ui.compose.designsystem.molecule.input.TextInput
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
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(responsiveWidthPadding),
            ) {
                AppTitleTopHeader(title = brandName)
                Spacer(modifier = Modifier.weight(1f))
                ContentView(
                    state = state,
                    onEvent = onEvent,
                    oAuthViewModel = oAuthViewModel,
                    resources = LocalResources.current,
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
        verticalArrangement = Arrangement.spacedBy(MainTheme.spacings.double),
    ) {
        TextBodyMedium(
            text = stringResource(R.string.account_setup_bjjgj_description),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        TextInput(
            text = state.emailAddress.value,
            label = stringResource(R.string.account_setup_bjjgj_local_part_label),
            errorMessage = state.emailAddress.error?.toAutoDiscoveryValidationErrorString(resources),
            onTextChange = { onEvent(Event.EmailAddressChanged(it)) },
            contentPadding = PaddingValues(),
            modifier = Modifier.testTagAsResourceId("account_setup_email_address_input"),
        )

        TextTitleSmall(
            text = stringResource(R.string.account_setup_bjjgj_full_email_label),
            modifier = Modifier.fillMaxWidth(),
        )
        TextBodyMedium(
            text = stringResource(R.string.account_setup_bjjgj_full_email_value, state.emailAddress.value.trim()),
            modifier = Modifier.fillMaxWidth(),
        )

        PasswordInput(
            password = state.password.value,
            errorMessage = state.password.error?.toAutoDiscoveryValidationErrorString(resources),
            onPasswordChange = { onEvent(Event.PasswordChanged(it)) },
            contentPadding = PaddingValues(),
            modifier = Modifier.testTagAsResourceId("account_setup_password_input"),
        )
    }
}
