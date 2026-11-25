plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.detekt)
}

detekt {
    config.setFrom(files("../config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    // El plugin no descubre automáticamente los source sets KMP.
    source = files(
        "src/commonMain/kotlin",
        "src/androidMain/kotlin",
        "src/jvmMain/kotlin",
        "src/jvmTest/kotlin",
    )
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    // Target JVM sin UI: permite probar la BD con JdbcSqliteDriver en memoria (F1/F6).
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.preview)
            implementation(libs.sqldelight.runtime)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.sqldelight.android.driver)
        }

        jvmMain.dependencies {
            implementation(libs.sqldelight.sqlite.driver)
        }

        jvmTest.dependencies {
            implementation(libs.junit)
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
