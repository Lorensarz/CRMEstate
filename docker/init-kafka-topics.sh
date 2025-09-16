#!/bin/bash

echo 'Waiting for Kafka to be ready...'

# Ждем пока Kafka станет полностью доступна
until kafka-topics --bootstrap-server kafka:9092 --list; do
  echo 'Kafka is not ready yet...'
  sleep 5
done

echo 'Creating topics...'
kafka-topics --bootstrap-server kafka:9092 --create --if-not-exists --topic raw-estate-data --partitions 3 --replication-factor 1 --config retention.ms=604800000
kafka-topics --bootstrap-server kafka:9092 --create --if-not-exists --topic notifications --partitions 3 --replication-factor 1 --config retention.ms=604800000
kafka-topics --bootstrap-server kafka:9092 --create --if-not-exists --topic notifications.DLT --partitions 3 --replication-factor 1 --config retention.ms=2592000000

echo 'Topics created:'
kafka-topics --bootstrap-server kafka:9092 --list
echo 'Kafka initialization completed successfully'