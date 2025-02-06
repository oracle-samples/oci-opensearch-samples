#!/bin/bash

cd console
echo "Starting client to create order by calling spring boot service"
java -cp target/kafka-consumer-services-0.0.1-SNAPSHOT.jar com.oracle.sample.orderSystem.OrderClient >>/tmp/order-client.log 2>&1 &

echo "Client started!"