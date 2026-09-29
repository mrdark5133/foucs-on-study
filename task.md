# Study Focus App — V1 Task List

Scope: Android only, single user, self-managed settings, no backend.

---

## 1. Project setup
- [x] Create Android project (Kotlin + Jetpack Compose)
- [x] Set min SDK 26+, target latest stable SDK (SDK 35)
- [x] Add dependencies: Room, WorkManager, Jetpack Compose Material 3 & Navigation
- [x] Set up package structure: `data/`, `ui/`, `service/`, `util/`

## 2. Permissions & onboarding
- [x] Build onboarding screen explaining why permissions are needed
- [x] Add deep link / intent to open Usage Access settings (`PACKAGE_USAGE_STATS`)
- [x] Add deep link / intent to open Accessibility settings
- [x] Add runtime checks — detect if permissions are revoked and prompt again
- [x] Handle "permission denied" gracefully (app should explain, not crash)

## 3. Usage tracking
- [x] Implement `UsageStatsManager` query for today's foreground time
- [x] Filter to target packages: `com.instagram.android`, `com.google.android.youtube`
- [x] Robust fallback from `queryEvents` to `queryUsageStats`
- [x] Handle midnight reset (new day = new usage record in local timezone)
- [x] Write a debug/test screen that just prints today's minutes per app (verify accuracy before building UI on top)

## 4. Local data layer (Room)
- [x] Define `AppLimit` entity (package name, daily limit in minutes, enabled flag)
- [x] Define `UsageRecord` entity (package name, date, minutes used)
- [x] Create DAOs for both (`AppLimitDao`, `UsageRecordDao`)
- [x] Create `LimitRepository` with a single `updateLimit(app, minutes, isEnabled)` entry point (future-proofing for V2 approval gate)
- [x] Create `UsageRepository` for reading/writing usage records

## 5. Accessibility service & blocking
- [x] Implement `AccessibilityService` to detect foreground app changes
- [x] On foreground change: check if app is tracked AND today's usage ≥ limit
- [x] Build full-screen block overlay (`BlockOverlayActivity`)
- [x] Overlay UI: "Limit reached for today" message + button to return home
- [x] Test overlay dismiss/re-trigger behavior (prevent infinite loops / spam)

## 6. Settings screen
- [x] UI: list of tracked apps with toggle (enable/disable tracking) and limit slider + preset chips
- [x] Wire changes to `LimitRepository.updateLimit()`
- [x] Persist immediately (no approval in V1)
- [x] Validate input (clamping to min/max limits)

## 7. Home screen
- [x] Show today's usage per tracked app (progress bar: used vs. limit)
- [x] Show remaining time per app
- [x] Simple, glanceable — this is the daily-use screen

## 8. Testing & polish
- [x] Unit tests for limit calculations, remaining time, warning threshold
- [x] Unit tests for midnight reset across day boundary
- [x] Unit tests for UsageEvents duration calculation (completed and ongoing sessions)
- [x] Unit tests for LimitRepository update and clamping behavior
- [x] Battery/performance check — lifecycle-aware, event-driven, zero polling battery drain
- [x] App icon, modern Obsidian dark theme, onboarding polish

---

## Not in this task list (future phases)
- Backend/API setup
- WhatsApp/Twilio integration
- Multi-user accounts
- Parent approval flow
- Laptop/browser agent
