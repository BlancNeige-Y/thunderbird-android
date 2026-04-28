package app.k9mail.feature.account.setup.ui.autodiscovery

import app.k9mail.feature.account.common.domain.entity.AccountState
import app.k9mail.feature.account.common.domain.entity.AuthenticationType
import app.k9mail.feature.account.common.domain.entity.ConnectionSecurity
import app.k9mail.feature.account.common.domain.entity.IncomingProtocolType
import app.k9mail.feature.account.server.settings.ui.incoming.IncomingServerSettingsContract
import app.k9mail.feature.account.server.settings.ui.outgoing.OutgoingServerSettingsContract
import app.k9mail.feature.account.setup.ui.options.display.DisplayOptionsContract
import com.fsck.k9.mail.AuthType
import com.fsck.k9.mail.ConnectionSecurity as MailConnectionSecurity
import com.fsck.k9.mail.ServerSettings
import net.thunderbird.core.validation.input.NumberInputField
import net.thunderbird.core.validation.input.StringInputField

private const val BJJGJ_EMAIL_DOMAIN = "bjjgj.gov.cn" // [BJJGJ-CUSTOM]
private const val BJJGJ_POP3_SERVER = "172.26.82.125" // [BJJGJ-CUSTOM]
private const val BJJGJ_POP3_PORT = 110 // [BJJGJ-CUSTOM]
private const val BJJGJ_SMTP_SERVER = "172.26.82.125" // [BJJGJ-CUSTOM]
private const val BJJGJ_SMTP_PORT = 25 // [BJJGJ-CUSTOM]

internal fun AccountAutoDiscoveryContract.State.toAccountState(): AccountState {
    val fullEmailAddress = emailAddress.value.toFullEmailAddress()

    return AccountState(
        emailAddress = fullEmailAddress,
        incomingServerSettings = ServerSettings(
            type = IncomingProtocolType.POP3.defaultName,
            host = BJJGJ_POP3_SERVER,
            port = BJJGJ_POP3_PORT,
            connectionSecurity = MailConnectionSecurity.NONE,
            authenticationType = AuthType.PLAIN,
            username = fullEmailAddress,
            password = password.value,
            clientCertificateAlias = null,
        ),
        outgoingServerSettings = ServerSettings(
            type = "smtp",
            host = BJJGJ_SMTP_SERVER,
            port = BJJGJ_SMTP_PORT,
            connectionSecurity = MailConnectionSecurity.NONE,
            authenticationType = AuthType.PLAIN,
            username = fullEmailAddress,
            password = password.value,
            clientCertificateAlias = null,
        ),
        authorizationState = authorizationState,
        displayOptions = null,
        syncOptions = null,
    )
}

internal fun AccountAutoDiscoveryContract.State.toIncomingConfigState(): IncomingServerSettingsContract.State {
    val fullEmailAddress = emailAddress.value.toFullEmailAddress()

    return IncomingServerSettingsContract.State(
        protocolType = IncomingProtocolType.POP3,
        server = StringInputField(value = BJJGJ_POP3_SERVER),
        security = ConnectionSecurity.None,
        port = NumberInputField(value = BJJGJ_POP3_PORT.toLong()),
        authenticationType = AuthenticationType.PasswordCleartext,
        username = StringInputField(value = fullEmailAddress),
        password = StringInputField(value = password.value),
    )
}

internal fun AccountAutoDiscoveryContract.State.toOutgoingConfigState(): OutgoingServerSettingsContract.State {
    val fullEmailAddress = emailAddress.value.toFullEmailAddress()

    return OutgoingServerSettingsContract.State(
        server = StringInputField(value = BJJGJ_SMTP_SERVER),
        security = ConnectionSecurity.None,
        port = NumberInputField(value = BJJGJ_SMTP_PORT.toLong()),
        authenticationType = AuthenticationType.PasswordCleartext,
        username = StringInputField(value = fullEmailAddress),
        password = StringInputField(value = password.value),
    )
}

internal fun AccountAutoDiscoveryContract.State.toOptionsState(): DisplayOptionsContract.State {
    return DisplayOptionsContract.State(
        accountName = StringInputField(value = emailAddress.value.toFullEmailAddress()),
    )
}

internal fun String.toFullEmailAddress(): String {
    return "${trim()}@$BJJGJ_EMAIL_DOMAIN"
}
