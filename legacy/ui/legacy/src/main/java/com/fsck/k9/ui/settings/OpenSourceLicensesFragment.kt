package com.fsck.k9.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import app.k9mail.core.ui.compose.designsystem.atom.Surface
import net.thunderbird.core.ui.compose.theme2.MainTheme
import net.thunderbird.core.ui.theme.api.FeatureThemeProvider
import org.koin.android.ext.android.inject
import app.k9mail.core.ui.legacy.designsystem.R as DesignSystemR

class OpenSourceLicensesFragment : Fragment() {
    private val themeProvider: FeatureThemeProvider by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                themeProvider.WithTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(vertical = MainTheme.spacings.default),
                        ) {
                            // [BJJGJ-CUSTOM] Keep Apache 2.0 access and Thunderbird attribution available on a secondary legal screen.
                            SectionTitle(title = "\u5f00\u6e90\u8bb8\u53ef")
                            SectionContent(
                                sectionLabel = "\u5f00\u6e90\u8bf4\u660e",
                                sectionText = "\u672c\u5e94\u7528\u57fa\u4e8e Thunderbird for Android \u5f00\u6e90\u9879\u76ee\u5b9a\u5236\u5f00\u53d1\u3002",
                                secondarySectionText = "\u7248\u6743\u4e0e\u5f00\u6e90\u7f72\u540d\u4fdd\u7559\uff0c\u9002\u7528 Apache License 2.0\u3002",
                                sectionImageId = DesignSystemR.drawable.ic_info,
                            )
                            SectionContent(
                                sectionLabel = "Apache License 2.0",
                                sectionText = "\u67e5\u770b\u8bb8\u53ef\u8bc1\u5168\u6587",
                                secondarySectionText = "https://www.apache.org/licenses/LICENSE-2.0",
                                sectionImageId = DesignSystemR.drawable.ic_code,
                                onClick = {
                                    requireContext().openUrl("https://www.apache.org/licenses/LICENSE-2.0")
                                },
                            )
                            SectionContent(
                                sectionLabel = "Thunderbird for Android",
                                sectionText = "\u67e5\u770b\u9879\u76ee\u4e3b\u9875",
                                secondarySectionText = "https://github.com/thunderbird/thunderbird-android",
                                sectionImageId = DesignSystemR.drawable.ic_link,
                                onClick = {
                                    requireContext().openUrl("https://github.com/thunderbird/thunderbird-android")
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
