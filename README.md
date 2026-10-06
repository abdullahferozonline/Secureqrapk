# SecurePay Android

Standalone Android version of the supplied SecurePay Flask app.

## What changed
- Normal UPI QR flow is preserved.
- No Flask/Python/Termux runtime is required after installation.
- QR generation is done natively with ZXing inside the APK.
- 5-minute QR timer and lock behavior remain.
- Shop name and UPI ID are stored locally.
- Share and copy actions use Android's native APIs.

## Build on Android (no PC)
Use AndroidIDE or another Android Gradle IDE on your phone.

1. Extract this project.
2. Open the project root (`SecurePayAndroid`) in AndroidIDE.
3. Allow Gradle to download dependencies.
4. Build > Assemble Debug.
5. Install the generated APK.

The first build requires internet to download Gradle/Android dependencies. The installed app itself does not need Flask, Python, Termux, or your local server.
