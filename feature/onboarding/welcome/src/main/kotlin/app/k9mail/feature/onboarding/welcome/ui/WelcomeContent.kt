package app.k9mail.feature.onboarding.welcome.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(
                        horizontal = MainTheme.spacings.quadruple,
                        vertical = MainTheme.spacings.double,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .widthIn(max = 420.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    WelcomeHeaderSection(
                        modifier = Modifier.fillMaxWidth(),
                    )
                    WelcomeMessageItem(
                        modifier = Modifier.padding(top = MainTheme.spacings.triple),
                    )
                    WelcomeFooterSection(
                        showImportButton = showImportButton,
                        onStartClick = onStartClick,
                        onImportClick = onImportClick,
                        modifier = Modifier.padding(top = MainTheme.spacings.triple),
                    )
                }

                WelcomeServicePhone(
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun WelcomeHeaderSection(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .defaultItemModifier()
            .widthIn(max = 360.dp),
        verticalArrangement = Arrangement.spacedBy(MainTheme.spacings.double),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WelcomeBadge(
            modifier = Modifier.fillMaxWidth(),
        )
        WelcomeTitleItem(
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun WelcomeBadge(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        // [BJJGJ-CUSTOM] Use the provided police badge asset as the formal welcome-page visual anchor.
        Image(
            painter = painterResource(id = R.drawable.bjjgj_police_badge),
            contentDescription = null,
            modifier = Modifier.size(92.dp),
            contentScale = ContentScale.Fit,
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
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // [BJJGJ-CUSTOM] Keep the formal welcome title in a balanced two-line composition under the badge.
        TextTitleLarge(
            text = WELCOME_TITLE_LINE_1,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 280.dp),
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
        TextTitleLarge(
            text = WELCOME_TITLE_LINE_2,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 220.dp),
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
            modifier = Modifier.widthIn(max = 300.dp),
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
            modifier = Modifier
                .widthIn(min = 128.dp)
                .testTagAsResourceId("onboarding_welcome_start_button"),
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
    Column(
        modifier = modifier
            .padding(horizontal = MainTheme.spacings.double)
            .padding(bottom = MainTheme.spacings.triple),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(MainTheme.spacings.double))
        TextBodySmall(
            text = stringResource(R.string.onboarding_welcome_developed_by),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}

private fun Modifier.defaultItemModifier() = composed {
    fillMaxWidth()
        .padding(MainTheme.spacings.default)
}
