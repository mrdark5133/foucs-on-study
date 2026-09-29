# 📱 Focus on Study

> A modern, distraction-free Android application engineered to help students regain focus by tracking and enforcing daily usage limits on distracting apps (like Instagram & YouTube).

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-purple.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Android Min SDK](https://img.shields.io/badge/Min%20SDK-26%2B%20%28Android%208.0%2B%29-brightgreen.svg)](https://developer.android.com)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-35-blue.svg)](https://developer.android.com)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-4285F4.svg?logo=android)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%2B%20Clean%20Arch-orange.svg)]()
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](LICENSE)

---

## 🎯 Problem & Solution

Studying and deep work are frequently interrupted by endless feeds such as **Instagram Reels** and **YouTube Shorts**. Willpower alone is often insufficient against addictive algorithms. 

**Focus on Study** provides reliable, device-level enforcement:
- Accurately tracks active foreground time per distracting app.
- Alerts you when you approach your daily quota.
- Automatically launches a full-screen block overlay when limits are exceeded, routing you back to your study goals.

---

## ✨ Key Features (V1)

- 📊 **Real-time App Usage Monitoring**: Precise foreground duration tracking powered by Android `UsageStatsManager` with event-level session calculations and fallback handling.
- ⏱️ **Customizable Daily Time Limits**: Flexible per-app limit controls with interactive sliders and quick-preset chips (15m, 30m, 45m, 60m).
- 🛡️ **Instant Blocking Overlay**: High-priority accessibility interception (`FocusAccessibilityService` + `BlockOverlayActivity`) that instantly restricts access when today's limit is reached.
- 🌙 **Modern Obsidian Dark Theme**: Clean, OLED-friendly Jetpack Compose Material 3 UI with glanceable progress indicators, status chips, and smooth transitions.
- 🔄 **Automatic Midnight Reset**: Smart day-boundary detection and calendar-aligned usage resets.
- 🔋 **Battery & Performance Optimized**: Purely event-driven foreground detection with zero background polling loops or unnecessary wake locks.
- 🔒 **100% Privacy Focused**: Runs completely locally on device with Room Database — no analytics, no external servers, and no tracking.

---

## 🏗️ Architecture & Tech Stack

The project adheres to Google's recommended Modern Android Development (MAD) practices and Clean Architecture:

```
d:/focus on study
├── app/
│   ├── src/main/java/com/focusstudy/app/
│   │   ├── data/
│   │   │   ├── local/          # Room DB, DAOs (AppLimitDao, UsageRecordDao)
│   │   │   ├── model/          # AppLimit, UsageRecord, TrackedAppInfo
│   │   │   └── repository/     # LimitRepository, UsageRepository
│   │   ├── service/            # FocusAccessibilityService, BootReceiver
│   │   ├── ui/
│   │   │   ├── components/     # AppCard, UsageProgressBar, PermissionCard
│   │   │   ├── home/           # HomeScreen, HomeViewModel, HomeUiState
│   │   │   ├── settings/       # SettingsScreen, SettingsViewModel
│   │   │   ├── onboarding/     # OnboardingScreen (Guided permissions flow)
│   │   │   ├── overlay/        # BlockOverlayActivity
│   │   │   ├── debug/          # Diagnostics & raw event viewer
│   │   │   ├── navigation/     # NavGraph & typed Screen routes
│   │   │   └── theme/          # Material 3 Color palette, Typography & Theme
│   │   └── util/               # UsageStatsHelper, UsageCalculator, DateUtils
│   └── src/test/               # Comprehensive Unit Test suite
```

### Tech Stack:
- **Language**: Kotlin 2.0+ with Coroutines & StateFlow
- **UI Framework**: Jetpack Compose (Material 3)
- **Local Storage**: Room Persistence Library (SQLite)
- **Architecture**: MVVM (Model-View-ViewModel) + Repository Pattern
- **System APIs**: `UsageStatsManager`, `AccessibilityService`, `PowerManager`
- **Testing**: JUnit 4, Kotlinx Coroutines Test

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug | 2024.2.1+ or newer
- JDK 17 or higher
- Android Device / Emulator running Android 8.0 (API level 26) or higher

### Installation & Build

1. **Clone the repository**:
   ```bash
   git clone https://github.com/mrdark5133/foucs-on-study.git
   cd foucs-on-study
   ```

2. **Open in Android Studio** and sync Gradle.

3. **Build and Run**:
   ```bash
   ./gradlew assembleDebug
   ```
   Or click **Run** (`Shift + F10`) in Android Studio.

---

## 🔑 Permissions Setup

To enforce limits and calculate usage accurately, the app requires two special Android permissions during the first-launch onboarding:

1. **Usage Access (`PACKAGE_USAGE_STATS`)**: Enables the app to query daily foreground time from Android's system usage statistics.
2. **Accessibility Service (`FocusAccessibilityService`)**: Enables instant detection of foreground app launches to display the block overlay when a limit is exceeded.

> 💡 *The app includes deep-link buttons directly inside the onboarding and home screens to guide you straight to the relevant Android Settings pages.*

---

## 🗺️ Roadmap & Future Phases

- [x] **V1 (Current)**: Local tracking and limit enforcement for Instagram & YouTube with self-managed settings.
- [ ] **V2**: Parent / Guardian approval gate via WhatsApp (requests for time extensions or limit changes require OTP / approval).
- [ ] **V3**: Multi-device family dashboard for parents managing multiple student profiles.
- [ ] **V4**: Cross-platform desktop daemon & browser extension for monitoring laptop / PC usage.

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!
Feel free to check the [Issues](https://github.com/mrdark5133/foucs-on-study/issues) page.

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).
