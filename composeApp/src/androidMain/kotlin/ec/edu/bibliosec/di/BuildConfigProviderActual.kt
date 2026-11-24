package ec.edu.bibliosec.di

import ec.edu.bibliosec.BuildConfig

/** El flavor decide qué implementación de repositorio se inyectará. */
actual val secureMode: Boolean = BuildConfig.SECURE_MODE
