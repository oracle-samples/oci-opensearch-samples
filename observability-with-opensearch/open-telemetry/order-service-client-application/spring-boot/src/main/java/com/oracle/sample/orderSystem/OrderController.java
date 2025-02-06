/*
  Copyright (c) 2025, Oracle and/or its affiliates.

  This software is dual-licensed to you under the Universal Permissive License
  (UPL) 1.0 as shown at https://oss.oracle.com/licenses/upl or Apache License
  2.0 as shown at http://www.apache.org/licenses/LICENSE-2.0. You may choose
  either license.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     https://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package com.oracle.sample.orderSystem;

import com.google.gson.Gson;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static io.opentelemetry.api.common.AttributeKey.stringKey;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {
    private static final Tracer tracer = GlobalOpenTelemetry.getTracer("order_service");

    private static final Meter meter = GlobalOpenTelemetry.getMeter("order_service");
    private static final String SHIPMENT_TOPIC_NAME = "shipments";
    private static final String ORDER_HISTORY_TOPIC_NAME = "order_history";
    private static final String INVENTORY_TOPIC_NAME = "inventory";
    private static final AttributeKey<String> CATEGORY_KEY = stringKey("category");
    private static final Random random = new Random();
    private final KafkaProducer<String, String> producer;
    private final Gson gson;

    private final LongCounter orderCounter;
    private final LongCounter shipmentCounter;
    private final LongCounter inventoryCounter;

    private final LongCounter orderCategoryCounter;


    public OrderController() {
        log.info("Starting Kafka Producer");

        String bootstrapServers = "127.0.0.1:9092";

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        producer = new KafkaProducer<>(properties);
        gson = new Gson();

        orderCategoryCounter = meter.counterBuilder("order_category_count")
                .setDescription("Tracks the number of orders per category")
                .setUnit("orders")
                .build();

        orderCounter = meter.counterBuilder("order_count")
                .setDescription("Tracks total orders received")
                .setUnit("orders")
                .build();

        shipmentCounter = meter.counterBuilder("shipment_counter")
                .setDescription("Tracks total shipments created")
                .setUnit("shipments")
                .build();

        inventoryCounter = meter.counterBuilder("inventory_counter")
                .setDescription("Tracks total inventory changes")
                .setUnit("inventory_deltas")
                .build();
    }

    private void publishToShipmentTopic(String payload) throws InterruptedException {
        Span span = tracer.spanBuilder("publishToShipmentTopic").startSpan();
        Thread.sleep(200);
        ProducerRecord<String, String> shipmentRecord =
                new ProducerRecord<>(SHIPMENT_TOPIC_NAME, payload);
        producer.send(shipmentRecord);
        producer.flush();
        //Mock errors
        if (random.nextDouble() < 0.3) {
            log.error("Encountered error sending payload {} to shipment service", payload);
        } else {
            log.info("Sent order {} to shipment service", payload);
        }
        span.end();
        shipmentCounter.add(1);
    }

    private void publishToInventoryTopic(String payload) throws InterruptedException {
        Span span = tracer.spanBuilder("publishToInventoryTopic").startSpan();
        Thread.sleep(1000);
        ProducerRecord<String, String> shipmentRecord =
                new ProducerRecord<>(INVENTORY_TOPIC_NAME, payload);
        producer.send(shipmentRecord);
        producer.flush();
        if (random.nextDouble() < 0.25) {
            log.error("Encountered error sending payload {} to inventory service", payload);
        } else {
            log.info("Sent order {} to inventory service", payload);
        }

        span.end();
        inventoryCounter.add(1);
    }

    private void publishToOrderHistoryTopic(String payload) throws InterruptedException {
        Span span = tracer.spanBuilder("publishToOrderHistoryTopic").startSpan();
        Thread.sleep(500);
        ProducerRecord<String, String> shipmentRecord =
                new ProducerRecord<>(ORDER_HISTORY_TOPIC_NAME, payload);
        producer.send(shipmentRecord);
        producer.flush();
        if (random.nextDouble() < 0.2) {
            log.error("Encountered error sending payload {} to order history service", payload);
        } else {
            log.info("Sent order {} to order history service", payload);
        }

        span.end();
        shipmentCounter.add(1);
    }

    private void writeToDB() throws InterruptedException {
        //Mock writing to a DB
        Span span = tracer.spanBuilder("writeToDB").startSpan();
        Thread.sleep(100);
        if (random.nextDouble() < 0.2) {
            log.error("Encountered error writing order metadata to DB");
        } else {
            log.info("Successfully written order metadata to DB");
        }

        span.end();
    }

    private void trackOrderCategory(String category) {
        orderCategoryCounter.add(1, Attributes.of(CATEGORY_KEY, category));
    }

    @PostMapping
    public void createOrder(@RequestBody Order order) throws InterruptedException {
        orderCounter.add(1);
        trackOrderCategory(order.getCategory());
        writeToDB();
        order.setService(Order.Service.SHIPMENTS);
        publishToShipmentTopic(gson.toJson(order));
        order.setService(Order.Service.INVENTORY);
        publishToInventoryTopic(gson.toJson(order));
        order.setService(Order.Service.ORDER_HISTORY);
        publishToOrderHistoryTopic(gson.toJson(order));
    }
}
