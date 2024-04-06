# CSC2007
SIT CSC2007 (Mobile Application Development) Assignment

### Table of Contents
1. [Team](#inf2007-mobile-application-development-team-25)
2. [Overview](#overview)
3. [Functionalities](#functionalities)
4. [Repository Structure](#repository-structure)
5. [Program Usage](#program-usage)
6. [Demonstration Video](#denmostration-video)

### CSC3102 Data Analytics Team 2
---
- Poh Kuang Yi (*2201354*) 
- Zaw Wana (*2201190*)
- Aaron Ti Yu Ren (*1902943*) 
- Tan Yu Jie (*2201782*)
- Ang Hui Lun (*2201377*)

### Overview
---
The goal of his project to to revolutionize the way students are able to take notes through their mobile application. Allowing users to quickly comprehend complex information through our AI summarizaion tool and take notes via Audio-to-Text, files, and images. Students are also able to exchange information with one another via a chatting feature and interact with their own personalized avatar which they can obtain items via the Gachapon mechanism. 

### Functionalities
The following lists the functionalities that was implemented within the mobile application:
1. Login, Sign Up
    - Ability to sign up and login
2. Modules, Notes and Individual Notes Pages
    - Creation of modules which are able to store individual notes for better organization of notes
3. One Time Password (OTP) 2 Factor Authentication
    - Security feature to prevent bruteforcing of password
4. Optical Charactare Recognition
    - Allows users to upload notes via images, and files into our application and converts them into text
5. Speech to Text
    - Captures the audio from the microphone and converts them to text
6. Chatting/Group Chat
    - Facilitates the sharing of information with other students and able to upload files/notes
7. Pomodoro Timer
    - Promotes good study habits with the Pomodoro Timer
8. Avatar
    - Incentivizes and motivates students to study through the ability to customize their personalized avatar
9. Settings
    - Updating of personal information, timers for the Pomodoro Timer, and notifications

### Repository Structure
---
```
./app/src/main/java/com/csc2007/notetaker/database/repository (contains the list of repositories to interact with the data)

./app/src/main/java/com/csc2007/notetaker/database/viewmodel (contains the list of viewmodels)

./app/src/main/java/com/csc2007/notetaker/database/dao (contains the list of daos)

./app/src/main/java/com/csc2007/notetaker/ui (used to store individual UI pages)

./app/src/main/java/com/csc2007/notetaker/Components.kt (contains the globally used composables)

README.md (this file)
```

### Third Party Libraries
The following lists the third party libraries that were incorporated within our project which can be found in the `AndroidManifest.xml` file.
- `implementation("androidx.room:room-ktx:2.6.1")`
- `implementation("androidx.datastore:datastore-core:1.0.0")`
- `implementation("com.google.firebase:firebase-firestore:24.10.3")`
- `implementation("com.google.firebase:firebase-storage:20.3.0")`
- `implementation("com.google.firebase:firebase-firestore-ktx:24.10.3")`
- `implementation("androidx.test:monitor:1.6.1")`
- `implementation("androidx.test.ext:junit-ktx:1.1.5")`
- `ksp("androidx.room:room-compiler:2.6.1")`
- `implementation("io.coil-kt:coil-compose:2.4.0")`
- `implementation("io.coil-kt:coil-gif:2.4.0")`
- `implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")`
- `implementation("androidx.camera:camera-core:${camerax_version}")`
- `implementation("androidx.camera:camera-camera2:${camerax_version}")`
- `implementation("androidx.camera:camera-lifecycle:${camerax_version}")`
- `implementation("androidx.camera:camera-video:${camerax_version}")`
- `implementation("androidx.camera:camera-view:${camerax_version}")`
- `implementation("androidx.camera:camera-extensions:${camerax_version}")`
- `implementation("com.google.android.gms:play-services-mlkit-text-recognition:16.0.0")`
- `implementation("androidx.navigation:navigation-compose:$nav_version")`
- `implementation("br.com.devsrsouza.compose.icons:font-awesome:1.1.0")`
- `implementation("com.airbnb.android:lottie-compose:5.2.0")`
- `implementation("com.github.shubham0204:Text2Summary-Android:alpha-05")`
- `implementation("dev.turingcomplete:kotlin-onetimepassword:2.4.0")`
- `implementation("commons-codec:commons-codec:1.7")`
- `implementation("com.lightspark:compose-qr-code:1.0.1")`
- `implementation("com.pspdfkit:pspdfkit:$pspdfkit_version")`
- `implementation("com.pspdfkit:pspdfkit-ocr:$pspdfkit_version")`
- `api("com.pspdfkit:pspdfkit-ocr-english:$pspdfkit_version")`


### Program Usage
---
The final fine-tuned UWB models are available in the `/export` folder. However, you can train or test the model by following the instructions below. Do note that data processing and model training/testing may take a while to run due to the complexities and nature of the dataset.

1. Run the mobile application 

2. Enter the following SQL commands to insert the gachapon items into the Room Database:
    ```SQL
    INSERT INTO item_table VALUES (0, "Penguin Hat", "Hat", "Rare", "hat_1")
    INSERT INTO item_table VALUES (1, "Santa Boy Hat", "Hat", "Epic", "santa_boy_hat")
    INSERT INTO item_table VALUES (2, "Doggy Mouth", "Accessory", "Legendary", "doggy_mouth")
    INSERT INTO item_table VALUES (3, "White Shirt", "Shirt", "Epic", "white_shirt")
    ```
3. Restart/Rebuild the mobile application

### Denmostration Video

[Link To Video](https://www.youtube.com/watch?v=1enO8tr-CoM&feature=youtu.be)