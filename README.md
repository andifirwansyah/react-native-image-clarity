# react-native-image-clarity

A **React Native package** to detect whether an image is completely black (dark) or blurry.  
Currently **supports Android only**. iOS is **not supported yet**.

---

## ✨ Features

- ✅ Detect if an image is too dark/black
- ✅ Detect if an image is blurry
- ⚡ Fast processing on-device (native Android)
- 🛠 Easy to use with `async/await`

---

## 📦 Installation

```bash
npm install react-native-image-clarity
```
or
```bash
yarn add react-native-image-clarity
```

### ⚠ Important Notes
- Platform Support: Android ✅, iOS ❌ (not supported yet, planned in future updates)
- The image path must be a local path, not a remote URL
- Ensure proper file access permissions in Android ```(READ_EXTERNAL_STORAGE / READ_MEDIA_IMAGES)```

---

### 📜 Required Android Permissions
Add the following permissions to your AndroidManifest.xml:
```
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
```
### 📌 Note:
- On Android 13+ (READ_MEDIA_IMAGES) is required to read images from the gallery.
- On Android 10+ you may need to handle scoped storage permissions.
- Request runtime permissions before using this package.

---

### 🚀 Usage Example
```
import { isImageBlack, isImageBlurry } from 'react-native-image-clarity';

async function checkImageQuality(pathLocal) {
  try {
    const black = await isImageBlack(pathLocal);
    const blur = await isImageBlurry(pathLocal);

    console.log(`Black: ${black}`);
    console.log(`Blur: ${blur}`);

    if (black) {
      console.warn("The image is too dark/black!");
    }

    if (blur) {
      console.warn("The image is blurry!");
    }
  } catch (e) {
    console.error(`Error: ${e.message}`);
  }
}
```

---

### 📂 Example Local Path
```
const pathLocal = '/storage/emulated/0/DCIM/Camera/IMG_12345.jpg';
await checkImageQuality(pathLocal);
```

---

### ⚙ API Reference
```isImageBlack(path: string): Promise<boolean>```

Checks if the image is completely black/dark.
- path: Local file path of the image (string)
- returns: true if the image is black, false otherwise

---

### 🛠 Development
If you want to contribute:
See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.
```bash
git clone https://github.com/username/react-native-image-clarity.git
cd react-native-image-clarity
npm install
```

---

### 📜 License
MIT