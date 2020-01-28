# SyncImages-RB3-AndroidApp

The project is designed to show the strength of Qualcomm’s Robotics RB3 camera feature by capturing photos and uploading them to the AWS S3 bucket. Robotics RB3 device has a camera attached to it, using which the images captured can be uploaded to the cloud on AWS S3 storage. This Android application helps the user to view the uploaded images from Robotics RB3 using AWS using credentials and syncing the images.

## Pre-requisites
* Android Phone with version 8.0 and above.
* ADB installed in the Windows/ Linux system. Instructions to install ADB in the system can be found here – https://developer.android.com/studio/command-line/adb.html
* Credentials.csv file located in Internal storage of Android Device. (This file contains AWS access key and secret key)


## How to Install
1. Download the apk (rb3cameraimages.apk) from Output directory
2. ADB tool can be used to install the Application (on both Windows and Linux) adb install rb3cameraimages.apk
3. Run the Application in the phone


## How to Use
1. Launch the application in the android phone and provide the permissions required, the app proceeds searching the configuration file(credentials.csv) required.
2. Accessing the configuration file, the user requested to provide the Region Endpoint where S3 is created.
3. On successful AWS connection, the user can see the images(.jpg,.png,.jpeg) are displayed as a thumbnail.