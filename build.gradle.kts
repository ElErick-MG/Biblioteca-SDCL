plugins {
    id("com.android.application") version "9.0.1" apply false
    id("org.jetbrains.kotlin.multiplatform") version "2.4.10" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.10" apply false
    id("org.jetbrains.compose") version "1.11.1" apply false
    id("app.cash.sqldelight") version "2.4.1" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
