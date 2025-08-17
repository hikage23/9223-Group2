# InMenu App: Post-Decryption OTP Theft Simulation in Android

## Group 2 Team Members

Numaan Cheema  
Manveer Singh  
Pow Chang

## 1. Introduction

One-Time Passwords (OTPs) have become a cornerstone of secure user authentication, especially in mobile-based two-factor authentication systems. Delivered in real time via SMS or encrypted messaging channels, OTPs offer a convenient second factor beyond static passwords. In finance-grade apps for banking, payments, and sensitive account management, strong attention is paid not only to protecting the transport of OTPs but also to securing them in memory and on-screen.

By contrast, non-financial mobile applications such as loyalty programs, rewards portals, and membership platforms often treat OTP delivery as an afterthought. While these apps may use SMS encryption or TLS for transport, they frequently expose the OTP in plaintext within UI components or leave it lingering in process memory without secure deletion. Once an OTP is decrypted and rendered on-screen, an adversary with local access, whether through accessibility services, dynamic instrumentation, or malicious overlays, can silently harvest it.

Prior work has extensively examined SMS interception using SIM swapping, broadcast-receiver abuse, and permission-based attacks on Android's RECEIVE_SMS and READ_SMS protections. However, the post-decryption window where the OTP resides in cleartext is underexplored. This project introduces a controlled emulator-based malware simulation targeting this final stage of the OTP lifecycle. The findings emphasize that true end-to-end security requires strong client-side handling of secrets, not just secure transport.

## 2. Background

Android’s open architecture and dominant market share have made it a favored target for mobile malware. Historically, attackers exploited SMS permissions and global broadcast intents. Modern Android versions now restrict these vectors by requiring explicit user consent or elevated privileges to access SMS content.

Once the OTP is decrypted and rendered by a legitimate app, it is often stored in memory or displayed on-screen without adequate protection. Attackers may exploit accessibility services, screen overlays, or memory inspection tools to harvest the OTP at this stage.

Although recent studies have highlighted these risks in non-financial apps, few demonstrate how such vulnerabilities can be exploited in practice. Our project fills this gap by simulating a full attack chain from OTP receipt to covert exfiltration.

## 3. Methodology

### 3.1 Environment Setup

All testing was conducted in a controlled Android Virtual Device running Android 10 (API 29). Tools used include:

- Android Studio and AVD Emulator
- Python Flask server at `http://10.0.2.2:5000/otp`
- Frida for runtime instrumentation
- APKTool and JADX-GUI for static analysis
- Wireshark for monitoring HTTP traffic

### 3.2 InMenu App Simulation

The InMenu app simulates a restaurant reservation process with embedded malware functionality. Flow:

1. User fills out a reservation form
2. App displays "Waiting for OTP"
3. Emulator injects an SMS using `adb emu sms send`
4. BroadcastReceiver extracts the OTP and displays it in a TextView
5. A hidden method sends the OTP to the Flask server
6. Obfuscation and dummy methods complicate reverse engineering

### 3.3 Reverse Engineering and Monitoring

- Decompiled the APK to locate the `sendSecretOTP()` routine
- Monitored HTTP POST traffic with Wireshark
- Optional: Frida scripts hook `TextView.getText()` and `HttpURLConnection.connect()` to simulate AV detection

## 4. Discussion

### 4.1 Significance

Unlike prior studies focused on SMS interception, our work explores OTP theft after decryption and display. The simulation reflects a real-world scenario where attackers exploit the window between decryption and OTP usage.

### 4.2 Limitations

- Testing was limited to virtual environments
- Malware triggers were hardcoded
- No antivirus or anomaly detection was tested

### 4.3 Future Work

- Expand simulation to other app types such as healthcare and education
- Integrate dynamic UI parsing
- Build an automated APK scanner for OTP exposure
- Develop client-side behavior detection tools


## 5. Project Structure

```bash
InMenu-App/
├── app/               Android Studio project
├── APK/               Compiled APKs
|-- references         Add references directory with supporting research papers
├── server/            Flask server for OTP logging
├── screenshots/       Screenshots of emulator test
├── README.md          Project overview
└── .gitignore         Git exclusions





