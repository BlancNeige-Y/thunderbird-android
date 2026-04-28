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
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.k9mail.core.ui.compose.designsystem.atom.Surface
import app.k9mail.core.ui.compose.designsystem.atom.button.ButtonFilled
import app.k9mail.core.ui.compose.designsystem.atom.button.ButtonText
import app.k9mail.core.ui.compose.designsystem.atom.text.TextBodyLarge
import app.k9mail.core.ui.compose.designsystem.atom.text.TextDisplayMediumAutoResize
import app.k9mail.core.ui.compose.designsystem.atom.text.TextTitleSmall
import app.k9mail.core.ui.compose.designsystem.template.ResponsiveContent
import app.k9mail.feature.onboarding.welcome.R
import net.thunderbird.core.ui.compose.common.modifier.testTagAsResourceId

private val WelcomeBackground = Color(0xFF121212)
private val WelcomePrimaryText = Color(0xFFF4F4F4)
private val WelcomeSecondaryText = Color(0xFFE8E8E8)
private val WelcomeFooterText = Color(0xFFD0D0D0)

@Composable
internal fun WelcomeContent(
    onStartClick: () -> Unit,
    onImportClick: () -> Unit,
    appName: String,
    showImportButton: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = WelcomeBackground,
        contentColor = WelcomePrimaryText,
    ) {
        ResponsiveContent { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
            ) {
                // [BJJGJ-CUSTOM] Rebuild the welcome page to match the approved black-background government-style layout.
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(R.drawable.bjjgj_police_badge),
                        contentDescription = appName,
                        modifier = Modifier.size(92.dp),
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    TitleBlock()
                    Spacer(modifier = Modifier.height(56.dp))
                    TextBodyLarge(
                        text = stringResource(id = R.string.onboarding_welcome_text),
                        color = WelcomeSecondaryText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(320.dp),
                    )
                    Spacer(modifier = Modifier.height(44.dp))
                    ButtonFilled(
                        text = stringResource(id = R.string.onboarding_welcome_start_button),
                        onClick = onStartClick,
                        modifier = Modifier
                            .width(200.dp)
                            .height(58.dp)
                            .testTagAsResourceId("onboarding_welcome_start_button"),
                    )

                    if (showImportButton) {
                        Spacer(modifier = Modifier.height(14.dp))
                        ButtonText(
                            text = stringResource(id = R.string.onboarding_welcome_import_button),
                            onClick = onImportClick,
                        )
                    }
                }

                TextTitleSmall(
                    text = stringResource(R.string.onboarding_welcome_developed_by),
                    color = WelcomeFooterText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 28.dp, start = 24.dp, end = 24.dp),
                )
            }
        }
    }
}

@Composable
private fun TitleBlock() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextDisplayMediumAutoResize(
            text = stringResource(R.string.onboarding_welcome_title_line_one),
            color = WelcomePrimaryText,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.82f),
        )
        TextDisplayMediumAutoResize(
            text = stringResource(R.string.onboarding_welcome_title_line_two),
            color = WelcomePrimaryText,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.72f),
        )
    }
}
