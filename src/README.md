# README

ipp-ementa can be run in devices equal or superior to **Android Kitkat 4.4 (API 19)** 

## Technologies

- [Room](https://developer.android.com/topic/libraries/architecture/room) for managing the map of offline data in a SQLite database
- [Mapsforge](https://github.com/mapsforge/mapsforge) for the render of a map view containing canteens location as POI
- [Graphhopper](https://github.com/graphhopper/graphhopper) for the render of routes starting on device location and ending on the location of a canteen
- [Firebase Cloud Messaging](https://firebase.google.com/products/cloud-messaging/) for receiving push-notifications regarding favorite dishes availability and nearby user canteens (with the use of geofencing)
- [Google Play Services](https://developer.android.com/training/location) for geofencing to detect if user is nearby a canteen
- [Bluetooth (Classic + Low Energy)](https://developer.android.com/guide/topics/connectivity/bluetooth) for the simulation of a dish purchase. It supports both classic and low energy but uses the default implementation of the device.
- [NFC](https://developer.android.com/guide/topics/connectivity/nfc) also for the simulation of a dish purchase.

## Styling

The app includes two options of styling, a light and dark theme, being the ligth theme the default theme used. These themes are both declared on `style.xml` and can be switched in runtime in the settings page of the app. Both of the themes also extend [Material Design components](https://material.io/develop/android/docs/getting-started/) in order to style the app using Material Design guidelines.

Note: No activity extends by default these themes, so to use theme it is necessary to override the `getTheme(resId)` method on each activity to apply the theme.

## Firebase SDK Integration Setup

To run the app locally from scratch it is necessary to first setup the integration with Firebase SDK. To do so it is necessary to first register the app in [Firebase Console](https://console.firebase.google.com/?pli=1) by creating a project with Firebase Cloud Messaging services. Once done, download `google-services.json` and place it in the [app module](app).

## Run and Debug Locally

Import the project in Android Studio and run the application in a device which API version is greater or equal than 19 (KitKat 4.4).