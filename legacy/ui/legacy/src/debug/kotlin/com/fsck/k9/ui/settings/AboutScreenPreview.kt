package com.fsck.k9.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.k9mail.core.ui.compose.designsystem.PreviewWithThemesLightDark
import app.k9mail.core.ui.legacy.designsystem.atom.icon.Icons

@Composable
@PreviewLightDark
internal fun AboutScreenPreview() {
    PreviewWithThemesLightDark {
        AboutScreen(
            organizationName = "北京市监狱管理局",
            versionNumber = "V1.0",
            appLogoResId = Icons.Outlined.Star,
        )
    }
}
