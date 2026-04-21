package com.fsck.k9.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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

class LibrariesFragment : Fragment() {
    private val themeProvider: FeatureThemeProvider by inject()
    private val viewModel: AboutViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val libraries = viewModel.state.value.libraries

                themeProvider.WithTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(vertical = MainTheme.spacings.default),
                        ) {
                            // [BJJGJ-CUSTOM] Move the full dependency/license inventory off the About landing page.
                            SectionTitle(
                                title = "\u5e93",
                                modifier = Modifier.fillMaxWidth(),
                            )
                            LibraryList(libraries = libraries)
                        }
                    }
                }
            }
        }
    }
}
