#!/bin/bash

cd spring-boot
echo "Starting Order service (Spring boot web application)..."
JAVA_OPTS="-Dotel.otlp.endpoint=http://localhost:4317 -Dotel.exporter=otlp -Dotel.logs.exporter=otlp -Dotel.service.name=order_service"
java $JAVA_OPTS -javaagent:target/opentelemetry-agent/opentelemetry-javaagent.jar -jar target/spring-boot-service-0.0.1-SNAPSHOT.jar >>/tmp/order-service.log 2>&1 &

cd ../console
echo "Starting Inventory service (Kafka consumer service)..."
JAVA_OPTS="-Dotel.otlp.endpoint=http://localhost:4317 -Dotel.exporter=otlp -Dotel.logs.exporter=otlp -Dotel.service.name=inventory_service"
java $JAVA_OPTS -javaagent:target/opentelemetry-agent/opentelemetry-javaagent.jar -cp target/kafka-consumer-services-0.0.1-SNAPSHOT.jar com.oracle.sample.orderSystem.InventoryService >>/tmp/inventory-service.log 2>&1 &
echo "Starting Order history service (Kafka consumer service)..."
JAVA_OPTS="-Dotel.otlp.endpoint=http://localhost:4317 -Dotel.exporter=otlp -Dotel.logs.exporter=otlp -Dotel.service.name=order_history_service"
java $JAVA_OPTS -javaagent:target/opentelemetry-agent/opentelemetry-javaagent.jar -cp target/kafka-consumer-services-0.0.1-SNAPSHOT.jar com.oracle.sample.orderSystem.OrderHistoryService >>/tmp/order-history-service.log 2>&1 &
echo "Starting Shipment service (Kafka consumer service)..."
JAVA_OPTS="-Dotel.otlp.endpoint=http://localhost:4317 -Dotel.exporter=otlp -Dotel.logs.exporter=otlp -Dotel.service.name=shipment_service"
java $JAVA_OPTS -javaagent:target/opentelemetry-agent/opentelemetry-javaagent.jar -cp target/kafka-consumer-services-0.0.1-SNAPSHOT.jar com.oracle.sample.orderSystem.ShipmentService >>/tmp/shipment-service.log 2>&1 &

echo "All services started successfully!"