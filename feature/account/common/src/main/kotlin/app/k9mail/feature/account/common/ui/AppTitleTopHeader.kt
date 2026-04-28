package app.k9mail.feature.account.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // [BJJGJ-CUSTOM] Remove the shared account-setup logo so all setup screens use a clean text-only header.
            TextDisplayMediumAutoResize(text = title)
        }
    }
}
