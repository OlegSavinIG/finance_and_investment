
# Проект "Finance_and_Investment" (work in progress)

## Обзор
**Finance_and_Investment** — это платформа для управления финансовыми инвестициями и отслеживания статистики пользовательских активностей. Проект состоит из нескольких микросервисов:

- **Основной сервис управления инвестициями**: Управляет всеми операциями, связанными с инвестициями, пользователями и статистикой.
- **Сервис статистики**: Отвечает за сбор и анализ статистических данных, таких как пользовательская активность и финансовая эффективность.

Проект построен на микросервисной архитектуре и предоставляет REST API для взаимодействия с различными операциями.

## Структура проекта
- **investment/**: Бизнес-логика, связанная с инвестициями, пользователями, транзакциями и управлением портфелем.
- **statistics/**: Микросервис, обрабатывающий статистику и метрики.
- **common_dto/**: Общие DTO для обмена данными между сервисами.
- **config/**: Конфигурационные файлы для различных сервисов.
- **docker-compose.yml**: Конфигурация для развертывания проекта с использованием Docker.
- **pom.xml**: Файл конфигурации для управления зависимостями и сборки проекта с помощью Maven.
- **.github/workflows/**: CI/CD конфигурации для GitHub Actions.

## Требования
- **Java**: Версия 11 или выше.
- **Maven**: Для управления зависимостями и сборки проекта.
- **Docker**: Для контейнеризации и развертывания микросервисов.
- **PostgreSQL**: Основная используемая база данных.

## Дополнительно
- **.env**: Содержит конфигурации окружения для Docker и микросервисов.
- **exception/**: Модуль обработки исключений.
- **eureka/** и **eureka-server/**: Реализация сервиса для регистрации и обнаружения микросервисов.
