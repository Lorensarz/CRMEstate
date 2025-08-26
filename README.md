CRMEstate - CRM для риелторского агентства
Проект представляет собой распределенную систему для управления недвижимостью, состоящую из нескольких микросервисов.

Архитектура системы

ContentLoaderAdapter → Kafka → ContentProcessor → PostgreSQL
→ PriceHistory     → PostgreSQL
→ NotificationService → SMS/Email
Сервисы
1. ContentLoaderAdapter (Порт: 8081)
   Загрузка и парсинг XLSX файлов с данными о недвижимости

Отправка данных в Kafka батчами

2. ContentProcessor (Порт: 8082)
   Потребление данных из Kafka

Batch-обработка и обновление данных в PostgreSQL

UPSERT операции для эффективного обновления

3. PriceHistory (В разработке) (Порт: 8083)
   Хранение истории изменений цен

Временные ряды данных

4. Core CRM (В разработке) (Порт: 8084)
   Основная CRM система

Управление задачами и пользователями

5. NotificationService (В разработке) (Порт: 8085)
   Отправка уведомлений

Retry-логика для надежной доставки

Технологический стек
Java 21 + Spring Boot 3.2

Apache Kafka - messaging

PostgreSQL - база данных

Docker + Docker Compose - контейнеризация

Maven - сборка проектов

Быстрый старт
1. Клонирование репозитория
   git clone https://github.com/Lorensarz/CRMEstate
   cd CRMEstate
2. Запуск инфраструктуры
# Запуск всех сервисов инфраструктуры
docker-compose up -d

# Проверка статуса
docker-compose ps

# Логи Kafka
docker-compose logs kafka
3. Сборка и запуск сервисов
   ContentLoaderAdapter
   cd content-loader-adapter
   mvn clean package
   java -jar target/content-loader-adapter-1.0.0.jar
   ContentProcessor
   cd content-processor  
   mvn clean package
   java -jar target/content-processor-1.0.0.jar
4. Docker сборка
# Сборка образов
docker build -t content-loader-adapter ./content-loader-adapter
docker build -t content-processor ./content-processor

# Запуск контейнеров
docker run -d -p 8081:8081 --network estate-network \
-e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092 \
content-loader-adapter

docker run -d -p 8082:8082 --network estate-network \
-e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092 \
-e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-history:5432/history_db \
content-processor

API Endpoints

ContentLoaderAdapter
Загрузка файла:

curl -X POST -F "file=@data.xlsx" http://localhost:8081/api/load
Пример файла data.xlsx должен содержать столбцы:
cadastr_number
source (Циан/Росреестр/Домклик)
price
type
square
room_count
floor
total_floors
address

Конфигурация
Переменные окружения
ContentLoaderAdapter:

SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
SERVER_PORT=8081
ContentProcessor:

SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-history:5432/history_db
SPRING_DATASOURCE_USERNAME=history_user
SPRING_DATASOURCE_PASSWORD=history_password
SERVER_PORT=8082
Kafka топики
raw-estate-data - основные данные о недвижимости

notifications - уведомления (в разработке)

Мониторинг
Проверка Kafka топиков

docker-compose exec kafka kafka-topics --bootstrap-server localhost:9092 --list
docker-compose exec kafka kafka-topics --bootstrap-server localhost:9092 --describe --topic raw-estate-data
Просмотр логов

# Логи конкретного сервиса
docker-compose logs content-loader-adapter
docker-compose logs content-processor

# Локи в реальном времени
docker-compose logs -f kafka
Проверка базы данных

# Подключение к CRM БД
docker-compose exec postgres-crm psql -U crm_user -d crm_db

# Подключение к history БД
docker-compose exec postgres-history psql -U history_user -d history_db
Примеры запросов
Проверка здоровья сервисов

# ContentLoaderAdapter
curl http://localhost:8081/actuator/health

# ContentProcessor
curl http://localhost:8082/actuator/health
Проверка данных в БД

-- В history_db
SELECT * FROM estates LIMIT 10;
SELECT COUNT(*) FROM estates;
SELECT source, COUNT(*) FROM estates GROUP BY source;