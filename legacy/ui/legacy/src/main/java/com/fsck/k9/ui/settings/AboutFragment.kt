package com.fsck.k9.ui.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.k9mail.core.ui.compose.designsystem.atom.Surface
import app.k9mail.core.ui.compose.designsystem.atom.text.TextBodyMedium
import app.k9mail.core.ui.compose.designsystem.atom.text.TextTitleMedium
import app.k9mail.core.ui.compose.designsystem.atom.text.TextTitleSmall
import com.fsck.k9.ui.R
import net.thunderbird.core.common.provider.AppNameProvider
import net.thunderbird.core.ui.compose.theme2.MainTheme
import net.thunderbird.core.ui.theme.api.FeatureThemeProvider
import org.koin.android.ext.android.inject
import app.k9mail.core.ui.legacy.designsystem.R as DesignSystemR
import app.k9mail.core.ui.legacy.theme2.common.R as Theme2CommonR

class AboutFragment : Fragment() {
    private val themeProvider: FeatureThemeProvider by inject()
    private val appNameProvider: AppNameProvider by inject()
    private val viewModel: AboutViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            val appLogoResId = resolveAppLogoResId(requireContext())
            val organizationName = appNameProvider.appName

            setContent {
                val versionNumber = viewModel.state.value.version

                themeProvider.WithTheme {
                    AboutScreen(
                        organizationName = organizationName,
                        versionNumber = versionNumber,
                        appLogoResId = appLogoResId,
                        displayLicense = {
                            findNavController().navigate(R.id.action_aboutScreen_to_openSourceLicensesScreen)
                        },
                        displayLibraries = {
                            findNavController().navigate(R.id.action_aboutScreen_to_librariesScreen)
                        },
                    )
                }
            }
        }
    }
}

internal fun Context.openUrl(url: String) {
    try {
        val viewIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(viewIntent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, R.string.error_activity_not_found, Toast.LENGTH_SHORT).show()
    }
}

@Composable
internal fun LibraryList(
    libraries: List<Library>,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
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
            .clickable(
                onClick = { context.openUrl(library.url) },
            )
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
    organizationName: String,
    versionNumber: String,
    appLogoResId: Int,
    modifier: Modifier = Modifier,
    displayLicense: () -> Unit = {},
    displayLibraries: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            AppLogo(logoResId = appLogoResId)
            SectionTitle(title = organizationName)
            SectionContent(
                sectionLabel = "\u7248\u672c\uff1a$versionNumber",
                sectionText = "",
                sectionImageId = DesignSystemR.drawable.ic_info,
            )
            // [BJJGJ-CUSTOM] Keep open-source attribution accessible from a secondary page without cluttering the About landing screen.
            SectionContent(
                sectionLabel = "\u5f00\u6e90\u8bb8\u53ef",
                sectionText = "Apache License 2.0",
                secondarySectionText = "\u67e5\u770b\u5f00\u6e90\u8bf4\u660e\u4e0e Thunderbird \u7f72\u540d",
                sectionImageId = DesignSystemR.drawable.ic_code,
                onClick = displayLicense,
            )
            SectionContent(
                sectionLabel = "\u5e93",
                sectionText = "\u67e5\u770b\u7b2c\u4e09\u65b9\u5e93\u4e0e\u8bb8\u53ef\u5217\u8868",
                sectionImageId = DesignSystemR.drawable.ic_group,
                onClick = displayLibraries,
            )
        }
    }
}

@Composable
internal fun AppLogo(
    logoResId: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = MainTheme.spacings.double),
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = logoResId),
            modifier = Modifier.size(size = 100.dp),
            contentDescription = null,
        )
    }
}

@Composable
internal fun SectionTitle(title: String, modifier: Modifier = Modifier) {
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
internal fun SectionContent(
    sectionLabel: String,
    sectionText: String,
    sectionImageId: Int,
    modifier: Modifier = Modifier,
    secondarySectionText: String? = null,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(horizontal = MainTheme.spacings.double, vertical = MainTheme.spacings.default)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = sectionImageId),
            modifier = Modifier
                .size(MainTheme.sizes.icon),
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

            if (sectionText.isNotBlank()) {
                TextBodyMedium(
                    text = sectionText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                )
            }

            secondarySectionText?.let {
                TextBodyMedium(
                    text = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                )
            }
        }
    }
}

fun resolveAppLogoResId(context: Context): Int {
    val typedValue = TypedValue()
    val resolved = context.theme.resolveAttribute(
        Theme2CommonR.attr.appLogo,
        typedValue,
        true,
    )

    return if (resolved && typedValue.resourceId != 0) {
        typedValue.resourceId
    } else {
        0
    }
}
