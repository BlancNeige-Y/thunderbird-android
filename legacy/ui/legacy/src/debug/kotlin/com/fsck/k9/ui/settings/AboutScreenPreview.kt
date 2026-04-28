package com.fsck.k9.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.k9mail.core.ui.compose.designsystem.PreviewWithThemesLightDark

@Composable
@PreviewLightDark
internal fun AboutScreenPreview() {
    PreviewWithThemesLightDark {
        AboutScreen(
            versionNumber = "V1.0",
            onOpenLicense = {},
            onOpenLibraries = {},
        )
    }
}
