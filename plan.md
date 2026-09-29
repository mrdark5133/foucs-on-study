# Study Focus App — V1 Plan

## Problem
Concentration on study is broken by Instagram Reels and YouTube Shorts.
Willpower-based limits don't stick — need a device-level control.

## Vision (long-term, all phases)
An app that:
1. Limits daily usage time on distracting apps (Reels/Shorts)
2. Requires parent/guardian WhatsApp approval to extend time or change settings
3. Runs on both mobile and laptop, monitoring and enforcing usage across both

## Phased roadmap

| Phase | Scope | Status |
|-------|-------|--------|
| **V1** | Android only. Single user (self). Track + limit usage on Instagram & YouTube. Settings changed freely by the user. No backend, no approval. | **Current phase** |
| V2 | Add parent/guardian approval via WhatsApp for extension requests and settings changes. Requires backend + WhatsApp integration (Twilio/WhatsApp Cloud API or n8n). | Future |
| V3 | Multi-user support — one parent manages multiple children/family members from a single WhatsApp number. Requires accounts, per-child limits, dashboard. | Future |
| V4 | Laptop/browser agent — track and block Reels/Shorts-equivalent usage on desktop (browser extension + local daemon), synced to the same backend. | Future |

---

## V1 — Detailed Plan

### Goal
A working Android app, for personal use only, that:
- Tracks today's foreground time on Instagram and YouTube
- Lets the user set a daily time limit per app
- Blocks the app with an overlay once the limit is reached
- Lets the user change the limit at any time (self-managed, no approval gate yet)

### Target platform
- Android (Kotlin + Jetpack Compose)
- Min SDK: 26+ (Android 8.0), recommend targeting current stable SDK

### Architecture (V1)
Fully local — no backend, no network calls.

```
┌─────────────────────────────┐
│         Android App          │
│                               │
│  ┌─────────────────────┐    │
│  │ Usage Tracking        │    │
│  │ (UsageStatsManager,   │    │
│  │  polled via           │    │
│  │  foreground service /  │    │
│  │  WorkManager)          │    │
│  └──────────┬───────────┘    │
│             │                 │
│  ┌──────────▼───────────┐    │
│  │ Room Database          │    │
│  │ - daily usage records  │    │
│  │ - app limits           │    │
│  └──────────┬───────────┘    │
│             │                 │
│  ┌──────────▼───────────┐    │
│  │ Accessibility Service  │    │
│  │ - detects foreground   │    │
│  │   app                  │    │
│  │ - triggers overlay if   │    │
│  │   limit exceeded        │    │
│  └──────────┬───────────┘    │
│             │                 │
│  ┌──────────▼───────────┐    │
│  │ Block Overlay UI        │    │
│  └─────────────────────┘    │
│                               │
│  ┌─────────────────────┐    │
│  │ Settings Screen         │    │
│  │ (set/edit limits,       │    │
│  │  directly persisted)    │    │
│  └─────────────────────┘    │
│                               │
│  ┌─────────────────────┐    │
│  │ Home Screen              │    │
│  │ (today's usage per app,  │    │
│  │  progress bars)          │    │
│  └─────────────────────┘    │
└─────────────────────────────┘
```

### Key design decision for future-proofing
All "change limit" actions go through a single function/call site
(e.g. `LimitRepository.updateLimit(app, minutes)`), not scattered UI logic.
In V2, this call site gets wrapped with an approval-request check instead of
applying immediately — avoids a rewrite later.

### Permissions required (manual grant, not standard dialogs)
- `PACKAGE_USAGE_STATS` — user must enable in system Settings
- Accessibility Service — user must enable in system Settings
- Both need a guided onboarding flow with deep links to the right settings page

### Out of scope for V1
- Parent/guardian approval
- WhatsApp integration
- Multi-user / multi-device support
- Laptop/desktop monitoring
- Reel/Shorts-tab-specific blocking (blocking is at whole-app level: Instagram, YouTube)

### Success criteria for V1
- Accurately tracks daily time spent in Instagram and YouTube
- Reliably blocks access once the set limit is hit each day
- Resets correctly at midnight
- User can view today's usage and adjust limits without friction
