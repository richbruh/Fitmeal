plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.fitmeal"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.fitmeal"
        minSdk = 24
        targetSdk = 34
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    viewBinding {
        enable = true
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:33.5.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore")
    implementation ("com.google.android.gms:play-services-auth")
    implementation ("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-storage")

    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")


    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.1")
    implementation("com.google.firebase:firebase-firestore-ktx:24.8.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation ("com.google.code.gson:gson:2.8.9")


}

