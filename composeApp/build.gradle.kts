plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(libs.sqldelight.runtime)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.sqldelight.android.driver)
        }
    }
}

android {
    namespace = "ec.edu.bibliosec"
    compileSdk = 36   // Compose 1.11 / androidx.activity 1.13 exigen API 36 para compilar

    defaultConfig {
        applicationId = "ec.edu.bibliosec"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    // Dos variantes instalables a la vez: .vulnerable y .segura (F0).
    flavorDimensions += "modo"
    productFlavors {
        create("vulnerable") {
            dimension = "modo"
            applicationIdSuffix = ".vulnerable"
            versionNameSuffix = "-vuln"
            resValue("string", "app_name", "BiblioSec (VULN)")
            buildConfigField("boolean", "SECURE_MODE", "false")
        }
        create("segura") {
            dimension = "modo"
            applicationIdSuffix = ".segura"
            versionNameSuffix = "-seg"
            resValue("string", "app_name", "BiblioSec (SEGURA)")
            buildConfigField("boolean", "SECURE_MODE", "true")
        }
    }

    buildFeatures {
        buildConfig = true
        resValues = true   // AGP 9 desactiva resValue por defecto; necesario para app_name por flavor
    }

    buildTypes {
        getByName("debug") { }

        getByName("release") {
            isMinifyEnabled = true      // mitigación VULN-08 (F5)
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

sqldelight {
    databases {
        create("BibliotecaDatabase") {
            packageName.set("ec.edu.bibliosec.db")
        }
    }
}
