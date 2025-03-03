
### Capas principales

1. **Data**:
   - **Local**: Persistencia de datos con Room.
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
