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

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

@Slf4j
public class OrderHistoryService {
    private static final Tracer tracer = GlobalOpenTelemetry.getTracer("order_history_service");

    private static void writeToDataLake() throws InterruptedException {
        Span span = tracer.spanBuilder("writeToDataLake").startSpan();
        Thread.sleep(150);
        log.info("Written inventory info to DB");
        span.end();
    }

    public static void main(String[] args) throws InterruptedException {
        String bootstrapServers = "127.0.0.1:9092";
        final String topicName = "order_history";
        log.info("Starting Kafka Consumer for topic {}", topicName);

        Properties consumerProperties = new Properties();
        consumerProperties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProperties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, topicName + "GroupId");
        consumerProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties);

        final Thread mainThread = Thread.currentThread();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Gracefully shutting down consumer");
            consumer.wakeup();

            try {
                mainThread.join();
            } catch (InterruptedException e) {
                log.error("Caught interrupted exception", e);
            }
        }));

        try {

            consumer.subscribe(Arrays.asList(topicName));

            while (true) {
                ConsumerRecords<String, String> records =
                        consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, String> record : records) {
                    log.info("Record Key: {} , Value: {}, Partition: {}, Offset: {}", record.key(), record.value(),
                            record.partition(), record.offset());
                    writeToDataLake();
                }
            }
        } finally {
            consumer.close();
            log.info("Consumer gracefully shutdown");
        }
    }
}

