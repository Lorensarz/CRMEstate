-- Таблица истории цен
CREATE TABLE IF NOT EXISTS price_history (
    id SERIAL PRIMARY KEY,
    cadastr_number VARCHAR(50) NOT NULL,
    source VARCHAR(100) NOT NULL,
    price DECIMAL(15,2) NOT NULL,
    type VARCHAR(100),
    square DECIMAL(10,2),
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание индексов для оптимизации запросов
CREATE INDEX IF NOT EXISTS idx_price_history_cadastr ON price_history(cadastr_number);
CREATE INDEX IF NOT EXISTS idx_price_history_source ON price_history(source);
CREATE INDEX IF NOT EXISTS idx_price_history_cadastr_source ON price_history(cadastr_number, source);
CREATE INDEX IF NOT EXISTS idx_price_history_recorded_at ON price_history(recorded_at);