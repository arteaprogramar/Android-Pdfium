### PDFium Android by Arte al Programar

PDFium by "Arte al Programar": It's a library that has origin on [PdfiumAndroid](https://github.com/barteksc/PdfiumAndroid) with the updates of [android-support-pdfium](https://github.com/benjinus/android-support-pdfium)

### News
- Support from Android API 16+
- Target Sdk Version 30
- Minimal integration of the [AndroidX Collections library](https://developer.android.com/jetpack/androidx/releases/collection)
- Two classes of exceptions have been added
- Package Restructuring
- The extractText(int page) method has been added.
- Supported android architectures: 'arm64-v8a', 'armeabi-v7a', 'x86', 'x86_64'
- Ndk Version : 21.3.6528147

## Installation

Include library in your project.

```
build.gradle (Project)

allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}


build.gradle (Module: app)

dependencies {
    ...
    implementation 'com.github.arteaprogramar:Android-Pdfium:2.1'
}
```
