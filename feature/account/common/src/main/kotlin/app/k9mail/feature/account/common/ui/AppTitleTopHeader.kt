package app.k9mail.feature.account.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import app.k9mail.core.ui.compose.designsystem.atom.text.TextDisplayMediumAutoResize
import app.k9mail.core.ui.compose.designsystem.template.ResponsiveWidthContainer
import net.thunderbird.core.ui.compose.theme2.MainTheme

@Composable
fun AppTitleTopHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    ResponsiveWidthContainer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = MainTheme.spacings.quadruple,
                bottom = MainTheme.spacings.default,
            )
            .then(modifier),
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .then(modifier),
            contentAlignment = Alignment.Center,
        ) {
            // [BJJGJ-CUSTOM] Remove the shared account-setup header logo so every setup screen starts with clean centered text only.
            TextDisplayMediumAutoResize(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MainTheme.spacings.double),
                textAlign = TextAlign.Center,
            )
        }
    }
}
