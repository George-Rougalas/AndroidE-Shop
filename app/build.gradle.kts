import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.map.secret)
}

android {
    namespace = "com.example.store"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.store"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())
        }
        val apiKey = localProperties.getProperty("API_KEY")
            ?: throw GradleException("Missing API_KEY in local.properties")
        manifestPlaceholders["apiKey"] = apiKey
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
    buildFeatures{
        viewBinding= true
    }
}



dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.firestore)
    implementation(libs.navigation.common)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.analytics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.gms.location)
    implementation(libs.annotation)
    implementation(libs.appcompat)
    implementation(libs.core)
    implementation(libs.play.services.maps) // Reference to the maps library
    implementation(libs.firebase.auth)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.makeramen.roundedimageview)
    implementation(platform(libs.firebase.bom)) // Firebase BOM (Manages Firebase versions)
    implementation(libs.firebase.database) // Firebase Realtime Database
    implementation(libs.glide) // Glide for Image Loading
    annotationProcessor(libs.glide.compiler) // Glide Compiler
    implementation(libs.gson)
}


