package app.k9mail.feature.onboarding.welcome.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.k9mail.core.ui.compose.designsystem.atom.Surface
import app.k9mail.core.ui.compose.designsystem.atom.button.ButtonFilled
import app.k9mail.core.ui.compose.designsystem.atom.button.ButtonText
import app.k9mail.core.ui.compose.designsystem.atom.text.TextBodyLarge
import app.k9mail.core.ui.compose.designsystem.atom.text.TextBodySmall
import app.k9mail.core.ui.compose.designsystem.atom.text.TextTitleLarge
import app.k9mail.core.ui.compose.designsystem.template.ResponsiveContent
import app.k9mail.feature.onboarding.welcome.R
import net.thunderbird.core.ui.compose.common.modifier.testTagAsResourceId
import net.thunderbird.core.ui.compose.theme2.MainTheme

private const val WELCOME_TITLE_LINE_1 = "\u5317\u4eac\u5e02\u76d1\u72f1\u7ba1\u7406\u5c40"
private const val WELCOME_TITLE_LINE_2 = "\u4e13\u7528\u90ae\u4ef6\u5ba2\u6237\u7aef"

@Composable
internal fun WelcomeContent(
    onStartClick: () -> Unit,
    onImportClick: () -> Unit,
    appName: String,
    showImportButton: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
    ) {
        ResponsiveContent { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(
                        horizontal = MainTheme.spacings.quadruple,
                        vertical = MainTheme.spacings.double,
                    ),
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .widthIn(max = 420.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    WelcomeHeaderSection(
                        appName = appName,
                    )
                    WelcomeMessageItem(
                        modifier = Modifier.padding(top = MainTheme.spacings.double),
                    )
                    WelcomeFooterSection(
                        showImportButton = showImportButton,
                        onStartClick = onStartClick,
                        onImportClick = onImportClick,
                        modifier = Modifier.padding(top = MainTheme.spacings.triple),
                    )
                }

                WelcomeServicePhone(
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
            }
        }
    }
}

@Composable
private fun WelcomeHeaderSection(
    appName: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .defaultItemModifier()
            .widthIn(max = 360.dp),
        verticalArrangement = Arrangement.spacedBy(MainTheme.spacings.half),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WelcomeTitleItem(
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun WelcomeTitleItem(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        WelcomeTitle(
            modifier = Modifier.defaultItemModifier(),
        )
    }
}

@Composable
private fun WelcomeTitle(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MainTheme.spacings.default),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // [BJJGJ-CUSTOM] Force the formal welcome title into a stable two-line composition without reintroducing the removed top logo block.
        TextTitleLarge(
            text = WELCOME_TITLE_LINE_1,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
        TextTitleLarge(
            text = WELCOME_TITLE_LINE_2,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

@Composable
private fun WelcomeMessageItem(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        WelcomeMessage(
            modifier = Modifier.defaultItemModifier(),
        )
    }
}

@Composable
private fun WelcomeMessage(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MainTheme.spacings.double),
        contentAlignment = Alignment.Center,
    ) {
        TextBodyLarge(
            text = stringResource(id = R.string.onboarding_welcome_subtitle),
            modifier = Modifier.widthIn(max = 320.dp),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun WelcomeFooterSection(
    showImportButton: Boolean,
    onStartClick: () -> Unit,
    onImportClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WelcomeFooter(
            showImportButton = showImportButton,
            onStartClick = onStartClick,
            onImportClick = onImportClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun WelcomeFooter(
    showImportButton: Boolean,
    onStartClick: () -> Unit,
    onImportClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MainTheme.spacings.default),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ButtonFilled(
            text = stringResource(id = R.string.onboarding_welcome_start_button),
            onClick = onStartClick,
            modifier = Modifier.testTagAsResourceId("onboarding_welcome_start_button"),
        )
        if (showImportButton) {
            ButtonText(
                text = stringResource(id = R.string.onboarding_welcome_import_button),
                onClick = onImportClick,
            )
        }

    }
}

@Composable
private fun WelcomeServicePhone(
    modifier: Modifier = Modifier,
) {
    TextBodySmall(
        text = stringResource(R.string.onboarding_welcome_developed_by),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MainTheme.spacings.double)
            .padding(bottom = MainTheme.spacings.triple),
        textAlign = TextAlign.Center,
    )
}

private fun Modifier.defaultItemModifier() = composed {
    fillMaxWidth()
        .padding(MainTheme.spacings.default)
}
