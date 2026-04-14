# 🎨 ColorValue App

A modern, responsive Android application built with Kotlin and Jetpack Compose. ColorValue allows users to visually mix Red, Green, and Blue (RGB) values to create custom colors, view their exact Hexadecimal codes, and save them to a local database for future reference.

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/3e86e3f6-5df2-4ffa-9e9a-a67c4f534be2" alt="Color List Screen" width="300"/></td>
    <td><img src="https://github.com/user-attachments/assets/91d2fc5f-cd28-4746-b790-562ac30ee480" alt="Add Color Screen" width="300"/></td>
  </tr>
</table>



## ✨ Features

* **Interactive RGB Sliders:** Mix colors in real-time using fluid Material Design sliders.
* **Live Preview:** Instantly see the resulting color and its generated Hex code (`#RRGGBB`).
* **Persistent Storage:** Save your favorite colors with custom names. Data is safely stored locally on the device.
* **Reactive UI:** The main screen automatically updates to display newly added colors using Kotlin Flow.

## 🛠️ Tech Stack & Architecture

This project strictly follows Google's recommended **MVVM (Model-View-ViewModel)** architecture to ensure maintainability and scalability.

* **UI Toolkit:** Jetpack Compose (Material 3)
* **Language:** Kotlin
* **Local Database:** Room Database with KSP (Kotlin Symbol Processing)
* **Asynchronous Programming:** Kotlin Coroutines & Flow
* **Architecture Components:** ViewModel, Repository Pattern

## 🏗️ Project Structure

* `data/`: Contains the Room `ColorDatabase`, `Color` Entity, and `ColorDao`.
* `repository/`: Contains the `ColorRepository` handling data operations.
* `viewmodel/`: Contains the `ColorViewModel` and its Factory managing the UI state.
* `ui/`: Contains all Jetpack Compose screens (`ColorListScreen`, `AddColorScreen`) and navigation logic.

## 🚀 How to Run the App

1. Clone this repository to your local machine.
2. Open the project in **Android Studio** (Koala or newer recommended).
3. Allow Gradle to sync and download the necessary dependencies (Compose, Room, Coroutines).
4. Connect an Android device or start an Android Emulator (API 26+).
5. Click the **Run** button (▶️) in Android Studio.

## 📝 License

This project is created for educational purposes and coursework submission.
