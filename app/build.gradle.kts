plugins {
    alias(libs.plugins.android.application)
<<<<<<< HEAD
    // Supprime kotlin.compose si tu es en Java pur
=======
>>>>>>> ac3ec7560996710622607813850b4c7fdae43535
}

android {
    namespace = "sn.esmt.finperso"
<<<<<<< HEAD
    compileSdk = 36   // <-- mise à jour
=======
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }
>>>>>>> ac3ec7560996710622607813850b4c7fdae43535

    defaultConfig {
        applicationId = "sn.esmt.finperso"
        minSdk = 24
<<<<<<< HEAD
        targetSdk = 36   // <-- mise à jour
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

=======
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
>>>>>>> ac3ec7560996710622607813850b4c7fdae43535
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

<<<<<<< HEAD

=======
>>>>>>> ac3ec7560996710622607813850b4c7fdae43535
dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
<<<<<<< HEAD
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    // ViewModel + LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata:2.7.0")

=======
>>>>>>> ac3ec7560996710622607813850b4c7fdae43535
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}