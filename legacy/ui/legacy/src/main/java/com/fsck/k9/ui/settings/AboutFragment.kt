package com.fsck.k9.ui.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.k9mail.core.ui.compose.designsystem.atom.Surface
import app.k9mail.core.ui.compose.designsystem.atom.text.TextBodyMedium
import app.k9mail.core.ui.compose.designsystem.atom.text.TextTitleMedium
import app.k9mail.core.ui.compose.designsystem.atom.text.TextTitleSmall
import com.fsck.k9.ui.R
import com.fsck.k9.ui.settings.AboutContract.Effect
import com.fsck.k9.ui.settings.AboutContract.Event
import kotlinx.collections.immutable.ImmutableList
import net.thunderbird.core.ui.compose.theme2.MainTheme
import net.thunderbird.core.ui.contract.mvi.observe
import net.thunderbird.core.ui.theme.api.FeatureThemeProvider
import org.koin.android.ext.android.inject
import app.k9mail.core.ui.legacy.designsystem.R as DesignSystemR

class AboutFragment : Fragment() {
    private val themeProvider: FeatureThemeProvider by inject()
    private val viewModel: AboutViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val (state, dispatch) = viewModel.observe { effect ->
                    when (effect) {
                        Effect.OpenChangeLog -> {
                            findNavController().navigate(R.id.action_aboutScreen_to_changelogScreen)
                        }

                        Effect.OpenLibraries -> {
                            findNavController().navigate(R.id.action_aboutScreen_to_librariesScreen)
                        }

                        is Effect.OpenUrl -> context.openUrl(effect.url)
                    }
                }

                themeProvider.WithTheme {
                    AboutScreen(
                        versionNumber = state.value.version,
                        onOpenLicense = {
                            dispatch(Event.OnSectionContentClick(getString(R.string.app_license_url)))
                        },
                        onOpenLibraries = { dispatch(Event.OnLibrariesClick) },
                    )
                }
            }
        }
    }
}

private fun Context.openUrl(url: String) {
    try {
        val viewIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(viewIntent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, R.string.error_activity_not_found, Toast.LENGTH_SHORT).show()
    }
}

@Composable
internal fun LibraryList(
    libraries: ImmutableList<Library>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        libraries.forEach { library ->
            LibraryItem(library = library)
        }
    }
}

@Composable
internal fun LibraryItem(
    library: Library,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .clickable(onClick = { context.openUrl(library.url) })
            .padding(
                horizontal = MainTheme.spacings.double,
                vertical = MainTheme.spacings.oneHalf,
            )
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        TextTitleMedium(
            text = library.name,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )
        TextBodyMedium(
            text = library.license,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )
    }
}

@Composable
internal fun AboutScreen(
    versionNumber: String,
    onOpenLicense: () -> Unit,
    onOpenLibraries: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            SectionTitle(title = stringResource(R.string.bjjgj_about_org_name))
            SectionContent(
                sectionLabel = stringResource(R.string.bjjgj_about_version, versionNumber),
                sectionText = "",
                sectionImageId = DesignSystemR.drawable.ic_info,
            )
            SectionContent(
                sectionLabel = stringResource(R.string.bjjgj_about_open_source_licenses),
                sectionText = stringResource(R.string.app_license),
                sectionImageId = DesignSystemR.drawable.ic_code,
                onClick = onOpenLicense,
            )
            SectionContent(
                sectionLabel = stringResource(R.string.bjjgj_about_libraries_entry),
                sectionText = stringResource(R.string.about_libraries),
                sectionImageId = DesignSystemR.drawable.ic_group,
                onClick = onOpenLibraries,
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    TextTitleSmall(
        text = title,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = MainTheme.spacings.double,
                end = MainTheme.spacings.double,
                top = MainTheme.spacings.double,
                bottom = MainTheme.spacings.default,
            ),
    )
}

@Composable
private fun SectionContent(
    sectionLabel: String,
    sectionText: String,
    sectionImageId: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(horizontal = MainTheme.spacings.double, vertical = MainTheme.spacings.default)
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = sectionImageId),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(MainTheme.spacings.triple))
        Column(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight(),
        ) {
            TextTitleMedium(
                text = sectionLabel,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            )

            if (sectionText.isNotEmpty()) {
                TextBodyMedium(
                    text = sectionText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                )
            }
        }
    }
}
