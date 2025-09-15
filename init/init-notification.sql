-- Создание таблицы для хранения истории отправленных уведомлений
CREATE TABLE IF NOT EXISTS notification_history (
    id BIGSERIAL PRIMARY KEY,
    task_id BIGINT NOT NULL,
    recipient VARCHAR(255) NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    subject VARCHAR(500),
    message TEXT,
    status VARCHAR(50) NOT NULL,
    error_message TEXT,
    retry_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    sent_at TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для быстрого поиска
CREATE INDEX IF NOT EXISTS idx_notification_history_task_id ON notification_history(task_id);
CREATE INDEX IF NOT EXISTS idx_notification_history_recipient ON notification_history(recipient);
CREATE INDEX IF NOT EXISTS idx_notification_history_status ON notification_history(status);
CREATE INDEX IF NOT EXISTS idx_notification_history_created_at ON notification_history(created_at);

-- Таблица для хранения конфигурации уведомлений
CREATE TABLE IF NOT EXISTS notification_config (
    id BIGSERIAL PRIMARY KEY,
    config_key VARCHAR(100) NOT NULL UNIQUE,
    config_value TEXT NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Начальные конфигурационные данные
INSERT INTO notification_config (config_key, config_value, description) VALUES
('email.enabled', 'true', 'Включение/выключение email уведомлений'),
('sms.enabled', 'true', 'Включение/выключение SMS уведомлений'),
('retry.max_attempts', '5', 'Максимальное количество попыток отправки'),
('retry.backoff_delay', '2000', 'Задержка между попытками в миллисекундах'),
('retry.backoff_multiplier', '2', 'Множитель для экспоненциальной задержки')
ON CONFLICT (config_key) DO NOTHING;

-- Таблица для DLQ сообщений (если нужно хранить локально)
CREATE TABLE IF NOT EXISTS dlq_messages (
    id BIGSERIAL PRIMARY KEY,
    original_message JSONB NOT NULL,
    error_message TEXT NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    retry_attempts INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_dlq_messages_created_at ON dlq_messages(created_at);
CREATE INDEX IF NOT EXISTS idx_dlq_messages_processed ON dlq_messages(processed);