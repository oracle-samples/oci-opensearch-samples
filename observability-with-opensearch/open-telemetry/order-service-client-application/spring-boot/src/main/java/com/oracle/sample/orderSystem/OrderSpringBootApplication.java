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
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;


@SpringBootApplication
public class OrderSpringBootApplication {

    public static void main(String[] args) {
        initGlobalOpenTelemetry();
        SpringApplication.run(OrderSpringBootApplication.class, args);
    }

    private static void initGlobalOpenTelemetry() {
        OpenTelemetrySdk openTelemetry = OpenTelemetrySdk.builder()
                .setMeterProvider(SdkMeterProvider.builder()
                        .registerMetricReader(
                                PeriodicMetricReader.builder(
                                                OtlpGrpcMetricExporter.builder()
                                                        .setEndpoint("http://localhost:4317")
                                                        .build()
                                        ).setInterval(Duration.ofSeconds(10))
                                        .build()
                        ).build())
                .build();

        // Register OpenTelemetry globally
        GlobalOpenTelemetry.set(openTelemetry);
    }

}
