# InMenu Release APK (Reverse Engineering - Static & Dynamic)

This directory contains the signed release version of the **InMenu** Android application used for Group Research Assignment Part-2 in the **CS-GY 9223 Mobile Security** course.

## Purpose

`InMenu.apk` is a proof-of-concept (PoC) malicious app designed to simulate how real-world Android malware can:
- Stealthily intercept OTP (One-Time Password) SMS messages using `BroadcastReceiver`
- Log and exfiltrate OTPs in the background using HTTP POST requests
- Demonstrate clear misuse of permissions (e.g., `RECEIVE_SMS`)

This second part we explore both **static** (e.g., APKTool) and **dynamic** (e.g., Burp Suite) analysis techniques to reverse engineer and validate malicious behaviors inside an APK.

## APK Build Process

The release APK was generated from source code using **Android Studio** with the following steps:

1. Navigate to the project directory:
   ```bash
   cd /AndroidStudioProjects/InMenu
   ```
2. Generate the signed APK:
    Android Studio > Build > Generate Signed Bundle / APK
    Select APK > Provide keystore & credentials
    Choose release build type
3. Output APK is stored under:
   ```bash
   app/release/InMenu-release.apk
   ```

## Analysis Steps

- Installed on Android emulator for testing.
- OTP interception and background exfiltration tested using ADB and Logcat.
- Network traffic monitored via Burp Suite (configured as proxy).
- APK reverse engineered using APKTool to confirm static code behavior.

## Files Included

- InMenu-release.apk: Signed APK for emulator testing and reverse engineering
- screenshots/: Folder with annotated images showing runtime behavior and analysis tools
- server.py: Flask server script to receive and log POSTed OTP data

## Flowchart


```bash
+--------------------+
| Android Emulator   |
| +----------------+ |
| | InMenu App     | |
| |  - SMS Capture | |
| |  - OkHttp POST | |
| +----------------+ |
+--------|-----------+
         v
  +---------------+       +--------------------+
  | Burp Suite    |<----->| Flask Server       |
  | (Proxy 8080)  |       | (10.0.2.2:5050)    |
  +---------------+       +--------------------+
```


⚠️ Disclaimer: This APK is for academic purposes only and should never be deployed to production environments or real devices without explicit consent. All were done in contained environments.
  
