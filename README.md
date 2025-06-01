# Мониторинг Системы на Jetpack Compose Desktop

## Описание

Проект реализует десктопное приложение для мониторинга системных ресурсов (ЦП, ОЗУ, диск, процессы) на Kotlin с использованием Jetpack Compose Desktop. Для сбора данных применяется библиотека Oshi.

## Требования

- **JDK 21** (Можно скачать в IDE, либо https://jdk.java.net/21/ или https://www.oracle.com/java/technologies/downloads)
- **Android Studio**
- **Интернет** (для загрузки зависимостей Gradle)

## Запуск
1. Откройте терминал в корневой директории проекта.
2. Запустите клиент:
    ```bash
    ./gradlew :composeApp:run
    ```