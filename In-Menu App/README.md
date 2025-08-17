# InMenu App

## Team Member Group 2 - Numaan Cheema, Manveer Singh, Pow Chang

A proof-of-concept Android app demonstrating OTP theft after decryption and display in the UI.

## Use Case

This app simulates a "restaurant reservation" form. Upon form submission:
1. An OTP is injected via `adb` (emulator).
2. OTP is parsed from the SMS and displayed.
3. A stealth function exfiltrates the OTP to a Flask server via HTTP POST.

## Features

- BroadcastReceiver for SMS OTP
- Regex-based OTP extraction
- UI rendering with OTP
- Background exfiltration to Flask server
- ProGuard obfuscation

## Screenshots

(screenshots/otp_received.png)

## Flask Server (for testing)

Navigate to `server/` and run:

```bash
python server.py
