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
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class OrderClient {
    private static final String[] orderCategories = {
            "Apparel, Shoes and Jewelry",
            "Home and  Kitchen",
            "Electronics",
            "Beauty and Personal Care",
            "Toys and Games",
            "Books",
            "Sports and Outdoors"
    };

    private static final Random random = new Random();

    private static String getRandomOrderCategory() {
        int randomIndex = random.nextInt(orderCategories.length);
        return orderCategories[randomIndex];
    }

    private static Order getMockOrder(int suffix) {
        Order order = Order.builder()
                .productName("testProduct" + suffix)
                .customerName("testCustomer" + suffix)
                .address("testAddressSuffix" + suffix)
                .quantity(1)
                .service(Order.Service.ORDER)
                .category(getRandomOrderCategory())
                .dateCreated(new Date().toString()).build();

        return order;
    }

    private static void createOrder(int suffix) throws IOException {
        String postUrl = "http://localhost:8080/orders";
        Gson gson = new Gson();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(postUrl);
        StringEntity postingString = new StringEntity(gson.toJson(getMockOrder(suffix)));
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(post);
        log.info("Created order with suffix {}", suffix);
    }

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        AtomicInteger orderCount = new AtomicInteger(1);
        Runnable postTask = () -> {
            try {
                createOrder(orderCount.getAndIncrement());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        scheduler.scheduleAtFixedRate(postTask, 0, 10, TimeUnit.SECONDS);
    }
}
