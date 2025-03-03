Arquitectura:

/app
  ├── src
      ├── main
            ├── java/com/tuempresa/tuapp
            │      ├── data
            │      │     ├── local         // Configuración de Room, entidades, DAOs
            │      │     ├── remote        // Configuración de Retrofit, servicios API
            │      │     └── repository    // Implementación de repositorios
            │      ├── domain
            │      │     ├── model         // Entidades o modelos de negocio
            │      │     └── usecase       // Casos de uso
            │      ├── presentation
            │      │     ├── ui            // Actividades, fragments o composables (si usas Jetpack Compose)
            │      │     └── viewmodel     // Clases ViewModel
            │      └── di                  // Configuración de inyección de dependencias (Hilt, Koin, etc.)
            └── res                         // Recursos (layouts, drawables, etc.)

# KoruCapital_App
