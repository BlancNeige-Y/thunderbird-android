# BJJGJ 定制变更日志

## 2026-04-28 02:02:58 +08:00

### 本次摘要

- 构建结果：`BUILD SUCCESSFUL`
- 执行命令：`.\gradlew.bat --no-configuration-cache :app-thunderbird:assembleDebug --stacktrace`
- 说明：本次是在 `main` 基础上，重新落实到“欢迎页警徽 + 自适应图标前景层修正”为止的一轮完整定制。

### 架构概览

- `app-thunderbird`
  - 负责应用名、启动器图标、自适应图标资源接线。
- `feature:onboarding:*`
  - 负责欢迎页文案、首页结构、开始按钮直达账号设置。
- `feature:account:common`
  - 负责账号设置流共用顶部标题区。
- `feature:account:setup`
  - 负责邮箱输入限制、固定 POP3/SMTP、禁用自动配置与手动配置。
- `legacy:core`
  - 负责导入账号时的合规校验。
- `legacy:ui:legacy`
  - 负责 About 页、设置页、开源许可与库列表页面。

### 本次界面与品牌修改

- 欢迎页
  - 使用根目录提供的 `jh.png` 作为首页顶部警徽。
  - 页面结构调整为：警徽 / 两行标题 / 副标题 / 开始按钮 / 底部服务电话。
  - 移除原先 Thunderbird 风格顶部视觉区。
  - 保留最终中文文案：
    - `北京市监狱管理局`
    - `专用邮件客户端`
    - `面向内部办公场景的安全邮件应用`
    - `服务电话：53860032`
- 应用名称
  - 安装后的桌面名称改为 `京狱邮件`。
  - 应用内组织展示保留 `北京市监狱管理局`。
- 启动器图标
  - 使用根目录 `icon.svg` 制作启动器前景层资源。
  - 对前景图形做了小幅右移修正，用于解决桌面图标中间 `mail` 图案视觉偏左的问题。
  - 同步更新单色 launcher 图标资源，保证主题图标模式下也一致。

### About / 设置页修改

- About 主页面只保留：
  - `北京市监狱管理局`
  - `版本：V1.0`
  - `开源许可`
  - `库`
- `库` 改为进入二级页面显示库清单，不再直接堆在 About 主页面。
- `开源许可` 入口保留，满足 Apache 2.0 合规可访问性要求。
- 已移除或隐藏以下用户侧入口：
  - `获取帮助`
  - `支持 北京市监狱管理局`
  - `调试：功能标志`

### 引导与账号设置行为

- 点击 `开始` 直接进入账号设置。
- 跳过中间引导步骤。
- 用户只能输入邮箱前缀，本地自动拼接成 `localPart@bjjgj.gov.cn`。
- 账号设置相关页面统一改为中文可见文案。
- 账号设置页共用顶部图标已移除，只保留干净的文字标题区。

### 邮件配置强制规则

- 收件协议固定为 POP3：
  - 服务器：`172.26.82.125`
  - 端口：`110`
  - 安全类型：`NONE`
- 发件协议固定为 SMTP：
  - 服务器：`172.26.82.125`
  - 端口：`25`
  - 安全类型：`NONE`
- 认证规则保持为：
  - 用户名 = 完整邮箱地址
  - 密码 = 用户输入密码
- 自动配置 / 自动发现逻辑已绕过，不能替换掉预设服务器配置。
- 正常设置流程中已禁用手动编辑服务器路径。
- 导入账号时会校验：
  - 域名必须是 `@bjjgj.gov.cn`
  - 收发件服务器必须是 `172.26.82.125`

### 本次修改文件

- `app-thunderbird/src/main/res/values/strings.xml`
  - 修改内容：将应用名改为 `京狱邮件`，补充品牌显示相关字符串。
  - 修改原因：让安装后桌面名称更简洁、更像正式应用名。
- `app-thunderbird/src/debug/res/values/strings.xml`
  - 修改内容：同步调整 debug 可见应用名为 `京狱邮件`。
  - 修改原因：保证调试安装包的显示名称与正式定制一致。
- `app-thunderbird/src/main/res/mipmap-anydpi-v26/ic_launcher.xml`
  - 修改内容：切换到定制 launcher 前景层与 monochrome 图层，并调整 inset。
  - 修改原因：让新图标资源真正用于桌面启动器。
- `app-thunderbird/src/main/res/drawable/bjjgj_launcher_foreground.xml`
  - 修改内容：新增由 `icon.svg` 转出的 launcher 前景层矢量资源，并加入右移修正。
  - 修改原因：修复桌面图标视觉偏左。
- `app-thunderbird/src/main/res/drawable/ic_app_logo_monochrome.xml`
  - 修改内容：新增配套单色图标资源。
  - 修改原因：保证主题图标/单色图标模式表现一致。
- `app-thunderbird/src/main/res/drawable-nodpi/bjjgj_in_app_logo.png`
  - 修改内容：新增应用内 logo 资源。
  - 修改原因：替换上游 Thunderbird 应用内图形品牌。
- `core/ui/compose/theme2/thunderbird/src/commonMain/composeResources/drawable/core_ui_theme2_thunderbird_logo.xml`
  - 修改内容：移除上游 compose logo XML。
  - 修改原因：停止使用 Thunderbird 默认 logo。
- `core/ui/compose/theme2/thunderbird/src/commonMain/composeResources/drawable/core_ui_theme2_thunderbird_logo.png`
  - 修改内容：新增替代 compose logo 图片资源。
  - 修改原因：让 compose 场景下的品牌资源一致。
- `feature/onboarding/main/src/main/kotlin/app/k9mail/feature/onboarding/main/navigation/OnboardingNavHost.kt`
  - 修改内容：`开始` 按钮改为直接跳账号设置。
  - 修改原因：跳过中间引导流程。
- `feature/onboarding/welcome/src/main/kotlin/app/k9mail/feature/onboarding/welcome/ui/WelcomeContent.kt`
  - 修改内容：重做欢迎页结构与排版，接入警徽、两行标题、副标题、按钮、底部电话。
  - 修改原因：首页改成更正式、均衡的内部办公应用风格。
- `feature/onboarding/welcome/src/main/res/values/strings.xml`
  - 修改内容：替换欢迎页英文/上游宣传文案为最终中文文案，并拆分两行标题。
  - 修改原因：去除 Thunderbird/Mozilla 宣传内容。
- `feature/onboarding/welcome/src/main/res/values-zh-rCN/strings.xml`
  - 修改内容：同步提供中文本地化文案。
  - 修改原因：避免首页混用中英文。
- `feature/onboarding/welcome/src/main/res/drawable/bjjgj_police_badge.png`
  - 修改内容：加入根目录提供的 `jh.png` 警徽资源。
  - 修改原因：作为首页顶部正式视觉标识。
- `feature/account/common/src/main/kotlin/app/k9mail/feature/account/common/ui/AppTitleTopHeader.kt`
  - 修改内容：移除账号设置流共用顶部图标，改为纯文字标题布局。
  - 修改原因：清掉账号设置页顶部多余品牌图标。
- `feature/account/setup/src/main/kotlin/app/k9mail/feature/account/setup/navigation/AccountSetupNavHost.kt`
  - 修改内容：固定走自动配置结果路径，绕开手动设置路由。
  - 修改原因：防止用户进入手动服务器编辑流程。
- `feature/account/setup/src/main/kotlin/app/k9mail/feature/account/setup/ui/autodiscovery/AccountAutoDiscoveryContent.kt`
  - 修改内容：改为只输入本地前缀、显示完整邮箱、输入密码的简单表单。
  - 修改原因：在输入层强制 `@bjjgj.gov.cn`。
- `feature/account/setup/src/main/kotlin/app/k9mail/feature/account/setup/ui/autodiscovery/AccountAutoDiscoveryStateMapper.kt`
  - 修改内容：固定 POP3/SMTP 服务器、端口、安全类型和认证方式。
  - 修改原因：确保只能使用预定义邮件配置。
- `feature/account/setup/src/main/kotlin/app/k9mail/feature/account/setup/ui/autodiscovery/AccountAutoDiscoveryViewModel.kt`
  - 修改内容：替换自动发现逻辑，改为本地域名校验和固定状态构造。
  - 修改原因：禁用 autoconfig，同时保留正常登录业务流程。
- `feature/account/setup/src/main/res/values/strings.xml`
  - 修改内容：新增本地前缀、完整邮箱显示和校验提示文案。
  - 修改原因：支撑改造后的账号输入界面。
- `feature/account/setup/src/main/res/values-zh-rCN/strings.xml`
  - 修改内容：补齐当前可见账号设置中文文案。
  - 修改原因：保证设置流中文化。
- `legacy/core/src/main/java/com/fsck/k9/preferences/SettingsImporter.kt`
  - 修改内容：新增导入账号时的域名与服务器强校验。
  - 修改原因：防止通过导入绕过邮箱域名和服务器限制。
- `legacy/ui/legacy/src/main/java/com/fsck/k9/ui/settings/SettingsListFragment.kt`
  - 修改内容：隐藏 `获取帮助`，移除支持/捐助入口。
  - 修改原因：清理用户侧不需要的设置项。
- `legacy/ui/legacy/src/main/java/com/fsck/k9/ui/settings/general/GeneralSettingsFragment.kt`
  - 修改内容：移除 `调试：功能标志` 入口。
  - 修改原因：不让终端用户看到调试项。
- `legacy/ui/legacy/src/main/java/com/fsck/k9/ui/settings/AboutContract.kt`
  - 修改内容：新增进入库列表页的事件和效果。
  - 修改原因：支持 About 二级页面导航。
- `legacy/ui/legacy/src/main/java/com/fsck/k9/ui/settings/AboutViewModel.kt`
  - 修改内容：对外显示版本固定为 `V1.0`，并支持库页面导航。
  - 修改原因：隐藏上游 snapshot 版本号，改成正式展示。
- `legacy/ui/legacy/src/main/java/com/fsck/k9/ui/settings/AboutFragment.kt`
  - 修改内容：重建 About 页面结构，移除顶部有问题的 logo 区，保留四个入口项。
  - 修改原因：满足你要求的简洁 About 结构，同时保留开源许可可访问性。
- `legacy/ui/legacy/src/main/java/com/fsck/k9/ui/settings/LibrariesFragment.kt`
  - 修改内容：新增二级库列表页。
  - 修改原因：把完整库清单移出 About 主页面。
- `legacy/ui/legacy/src/main/res/navigation/navigation_settings.xml`
  - 修改内容：新增 About 到库列表页的导航。
  - 修改原因：接通新的二级页面。
- `legacy/ui/legacy/src/main/res/values/bjjgj_strings.xml`
  - 修改内容：新增 About 定制文案资源。
  - 修改原因：支撑新的 About 页面结构。
- `legacy/ui/legacy/src/main/res/values-zh-rCN/bjjgj_strings.xml`
  - 修改内容：新增中文 About 定制文案资源。
  - 修改原因：保证 About 页中文化。
- `legacy/ui/legacy/src/debug/kotlin/com/fsck/k9/ui/settings/AboutScreenPreview.kt`
  - 修改内容：更新预览参数，适配新的 About 组件签名。
  - 修改原因：防止 debug 预览编译失败。

### 已移除或被隐藏的功能

- Thunderbird 欢迎页宣传文案
- 欢迎页原顶部视觉/logo 区
- 账号设置流顶部共用小图标
- 正常设置流程中的手动服务器配置入口
- 自动发现替换服务器配置的路径
- About 主页面直接展开的库清单
- `获取帮助`
- `支持 北京市监狱管理局`
- `调试：功能标志`

### 风险说明

- 账号设置已变成严格策略模式，任何非 `@bjjgj.gov.cn` 的邮箱都会被拒绝，包括导入账号。
- POP3/SMTP 已改为固定策略，用户在正常流程里不能再手动修改。
- 欢迎页、About 页和设置页都做了较大 UI 定制，建议仍在真机上做一次视觉验收。
- 启动器图标的偏移修正值是基于当前提供的 `icon.svg` 调出来的，如果以后换图，可能需要重新微调。

### 最终状态

- 当前正式定制包构建状态：成功
- APK 生成状态：成功
- 推荐安装包路径：
  - `C:\Users\Ye\Desktop\thunderbird-android\app-thunderbird\build\outputs\apk\foss\debug\app-thunderbird-foss-debug.apk`
- 备用安装包路径：
  - `C:\Users\Ye\Desktop\thunderbird-android\app-thunderbird\build\outputs\apk\full\debug\app-thunderbird-full-debug.apk`
- 当前可用性结论：
  - 首页欢迎页已替换为定制风格
  - `开始` 直达账号设置
  - 域名限制与固定 POP3/SMTP 已生效
  - 手动配置路径已封闭
  - About / 开源许可 / 库页面结构已按要求调整
  - 自适应图标前景层居中修正已完成

## 2026-04-28 02:16:17 +08:00

### 本次摘要

- 构建结果：`BUILD SUCCESSFUL`
- 执行命令：`.\gradlew.bat --no-configuration-cache :app-thunderbird:assembleDebug --stacktrace`
- 说明：根据参考图微调欢迎页首页结构，并将点击 `开始` 后的顶部标题统一改为 `京狱邮件`。

### 本次修改内容

- 欢迎页首页
  - 按参考图改为黑色背景。
  - 调整警徽、标题、副标题、按钮、服务电话的上下位置关系。
  - 放大并收紧两行标题排版，使其更接近参考图中的视觉比例。
  - 扩大 `开始` 按钮宽高，使中部操作区更稳定。
  - 保持服务电话固定在底部居中显示。
- 账号设置流顶部标题
  - 将 `BrandNameProvider` 最终展示名称改为 `京狱邮件`。
  - 因此点击 `开始` 后进入账号设置及后续相关页面时，顶部文字不再显示 `北京市监狱管理局`，而是显示 `京狱邮件`。

### 本次修改文件

- `feature/onboarding/welcome/src/main/kotlin/app/k9mail/feature/onboarding/welcome/ui/WelcomeContent.kt`
  - 修改内容：欢迎页重构为黑底居中布局，重新调整标题字号、位置、按钮宽高、底部电话位置。
  - 修改原因：让首页更贴近你提供的参考图，修正文字大小和位置不理想的问题。
- `app-thunderbird/src/main/res/values/strings.xml`
  - 修改内容：将 `brand_name` 规范化调整为 `京狱邮件`，并同步校正主资源文件编码内容。
  - 修改原因：让开始后的顶部标题显示为应用名而不是组织名。
- `app-thunderbird/src/debug/res/values/strings.xml`
  - 修改内容：同步校正 debug 资源中的应用名文本编码和内容。
  - 修改原因：避免调试包显示名与主包资源不一致。

### 本次结果

- 欢迎页已按参考图方向收口为黑底正式版式。
- 点击 `开始` 后，顶部标题现为 `京狱邮件`。
- 本次修改后构建通过，APK 已重新生成。

## 2026-04-28 02:26:34 +08:00

### 本次摘要

- 构建结果：`BUILD SUCCESSFUL`
- 执行命令：`.\gradlew.bat --no-configuration-cache :app-thunderbird:assembleDebug --stacktrace`
- 说明：修正点击 `开始` 后账号设置流仍显示英文的问题，并将应用显示语言固定为简体中文。

### 本次修改内容

- 应用语言
  - 在 Thunderbird 应用启动时，强制将应用语言设置为 `zh_CN`。
  - 这样现有上游中文资源会统一生效，不再因为默认语言回退而显示英文。
- 账号设置流中文化
  - 将当前实际会走到的账号设置链路默认文案改为中文，包括：
    - `下一步` / `返回`
    - 账号输入页说明文案
    - 收件/发件服务器校验中的“正在检查”“验证成功”“失败”“认证错误”等提示
    - 显示选项 / 同步选项 / 创建账户提示
  - 这样即使某些页面没有命中 `values-zh-rCN`，也不会再显示英文默认值。

### 本次修改文件

- `app-thunderbird/src/main/kotlin/net/thunderbird/android/ThunderbirdApp.kt`
  - 修改内容：应用启动后调用语言管理器，将应用语言固定为 `zh_CN`。
  - 修改原因：从根源上避免页面回退到英文资源。
- `feature/account/common/src/main/res/values/strings.xml`
  - 修改内容：将账号设置流程共用按钮和服务器错误提示改为中文。
  - 修改原因：修正 `Next` / `Back` / 服务器提示信息仍是英文的问题。
- `feature/account/server/validation/src/main/res/values/strings.xml`
  - 修改内容：将服务器校验页面默认字符串改为中文。
  - 修改原因：修正“Checking incoming server settings”“Authentication error”等提示仍为英文的问题。
- `feature/account/setup/src/main/res/values/strings.xml`
  - 修改内容：将账号输入、显示选项、同步选项、创建账户等默认字符串改为中文。
  - 修改原因：修正开始后账号设置流大量默认英文文案。

### 本次结果

- 应用已固定为简体中文显示。
- 点击 `开始` 后的账号设置相关流程，不再应出现成片英文回退。
- 本次修改后构建通过，APK 已重新生成。

### 后续记录规则

- 从这一条开始，后续每一次新的修改都按“新增时间段”的方式追加。
- 每次追加都至少包含：
  - 变更时间
  - 执行命令
  - 修改内容
  - 影响范围
  - 构建结果
