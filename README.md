This is a fork of the [ODK Collect app](https://github.com/opendatakit/collect) and thus should be regularly updated and tested when the ODK Collect app is updated.


# collect

This project is __*actively maintained*__

It is the ODK 1.0 Android application.

The developer [wiki](https://github.com/opendatakit/opendatakit/wiki) (including release notes) and
[issues tracker](https://github.com/opendatakit/opendatakit/issues) are located under
the [**opendatakit**](https://github.com/opendatakit/opendatakit) project.

The Google group for software engineering questions is: [opendatakit-developers@](https://groups.google.com/forum/#!forum/opendatakit-developers)

## Setting up your environment

This project depends upon the gradle-config project. The necessary gradle files will be downloaded and used automatically from the github repository at the tag specified in settings.gradle. If you wish to modify your gradle-config yourself, you must clone the project into the same parent directory as Collect. Your directory structure should resemble the following: 

        |-- odk
            |-- gradle-config
            |-- collect

The `gradle-config` project should be checked out at the tag number declared at the 
top of the `collect/settings.gradle` file.

Then, import the `collect/build.gradle` file into Android Studio.
>>>>>>> d546772ae3d2d2a31c62a7e5b99c18a61c6b02eb

Currently, the operators use the [regular ODK Collect app](https://play.google.com/store/apps/details?id=org.aguaclara.post.collect&hl=en) rather than this fork. However, with a fork, we could make installation simpler, eliminate some of the unneccessary options and automatically update the forms on the operators' local device. 
