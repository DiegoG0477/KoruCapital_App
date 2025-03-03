# KoruCapital App

Aplicación Android desarrollada con [Kotlin](https://kotlinlang.org/) y [Jetpack Compose](https://developer.android.com/jetpack/compose).

## Arquitectura

El proyecto sigue una arquitectura limpia (Clean Architecture) con las siguientes capas:

/app
├── src
│ ├── main
│ │ ├── java/com/koru/capital
│ │ │ ├── data
│ │ │ │ ├── local # Configuración de Room, entidades, DAOs
│ │ │ │ ├── remote # Configuración de Retrofit, servicios API
│ │ │ │ └── repository # Implementación de repositorios
│ │ │ ├── domain
│ │ │ │ ├── model # Entidades o modelos de negocio
│ │ │ │ └── usecase # Casos de uso
│ │ │ ├── presentation
│ │ │ │ ├── ui # Actividades, fragments o composables (Jetpack Compose)
│ │ │ │ └── viewmodel # Clases ViewModel
│ │ │ └── di # Configuración de inyección de dependencias (Hilt, Koin, etc.)
│ │ └── res # Recursos (layouts, drawables, etc.)

### Capas principales

1. **Data**:
   - **Remote**: Comunicación con APIs mediante Retrofit.
   - **Repository**: Implementación de repositorios que unifican el acceso a datos locales y remotos.

2. **Domain**:
   - **Model**: Entidades de negocio.
   - **UseCase**: Lógica de negocio encapsulada en casos de uso.

3. **Presentation**:
   - **UI**: Interfaces de usuario construidas con Jetpack Compose.
   - **ViewModel**: Estado y lógica de presentación.

4. **DI**:
   - Configuración de inyección de dependencias con Hilt o Koin.

## Tecnologías utilizadas

- **Lenguaje**: Kotlin
- **UI**: Jetpack Compose
- **Inyección de dependencias**: Hilt
- **Networking**: Retrofit
- **Arquitectura**: MVVM + Clean Architecture

## Cómo ejecutar el proyecto

1. Clona el repositorio:
   ```bash
   git clone https://github.com/DiegoG0477/KoruCapital_App.git
