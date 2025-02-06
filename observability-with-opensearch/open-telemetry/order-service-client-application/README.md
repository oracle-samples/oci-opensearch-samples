## Sample for OpenTelemetry data ingestion into OCI OpenSearch

This sample module sets up E2E OpenTelemetry pipeline for observability analytics with OCI OpenSearch. The flow is 
```
Sample Ordering system (comprising of spring boot web service+downstream Kafka consumer services)->OTel Java Agent->OTel Collector->Data Prepper->OpenSearch (analytics through OpenSearch dashboard)
```
Let's setup the required components and see this in action!

### Setup OpenSearch cluster

Launch an OpenSearch cluster (ver >=2.15) using [instructions](https://docs.oracle.com/en-us/iaas/Content/search-opensearch/Tasks/creatingsearchclusters.htm)

### Create OCI VM instance

To access the opensearch cluster created above, you need to create a VM instance in the same VCN as the cluster. Follow [instructions](https://docs.oracle.com/en-us/iaas/Content/Compute/Tasks/launchinginstance.htm) to create a VM instance.
You can also access your opensearch locally by creating an ssh tunnel to establish connection between your local machine, the VM and the opensearch cluster. 
Follow [instructions](https://docs.oracle.com/en/learn/oci-opensearch/index.html#task-3-test-the-connection-to-oci-search-service--opensearch-endpoint) to create and test the ssh tunneling with your VM instance.

On this instance, perform following setup steps to install and start required components.

### Setup JDK

Install JDK ver 11 which is required to build data-prepper version 2.9.0 below

### Setup GO

GO is a pre-requisite for running OTel collector below. Based on your instance, install GO from https://go.dev/doc/install

### Setup Kafka

Download Kafka version kafka_2.13-3.9.0 from https://kafka.apache.org/downloads and extract to a directory
Start Kafka
```
cd <dir>/kafka_2.13-3.9.0

bin/zookeeper-server-start.sh config/zookeeper.properties >> /tmp/zookeeper.log 2>&1 &

bin/kafka-server-start.sh config/server.properties >> /tmp/kafka-server.log 2>&1 &
```

### Setup data-prepper

1. Clone source code and checkout branch 2.9.
```
git clone https://github.com/opensearch-project/data-prepper.git
cd <local_repo_dir>
git checkout -b 2.9 origin/2.9
```
2. From the local repo dir, build data-prepper
```
./gradlew assemble
```

This will generate release/archives/linux/build/install/opensearch-data-prepper-2.9.0-linux-x64/

3. Take pipelines.yaml from configs/data-prepper directory of this project. Replace placeholders with your OpenSearch cluster API endpoint, username and password.
Then copy the yaml file to a directory on the instance. 
4. Copy data-prepper-config.yaml from configs/data-prepper directory to same directory on instance as for step above.
5. Go to release/archives/linux/build/install/opensearch-data-prepper-2.9.0-linux-x64/ directory and start data-prepper using the configs created above:

```
./bin/data-prepper <path>/pipelines.yaml <path>/data-prepper-config.yaml >> /tmp/data-prepper.log 2>&1 &
```

### Setup OTel Collector

1. Download [OTel collector version 0.113.0](https://github.com/open-telemetry/opentelemetry-collector-releases/releases/tag/v0.113.0) suitable for you instance and extract to directory
```
curl --proto '=https' --tlsv1.2 -fOL https://github.com/open-telemetry/opentelemetry-collector-releases/releases/download/v0.113.0/<tar_ball_name>.tar.gz
tar -xvf <tar_ball_name>.tar.gz
```
2. Go to the above directory where OTel collector is extracted. Use otel-collector-to-data-prepper-config.yaml in configs/otel-collector of this project 
and start OTel collector:
```
./otelcol --config=<path>/otel-collector-to-data-prepper-config.yaml >>/tmp/otelcol.log 2>&1 &
```

### Build and start order service client application
1. Clone this repo on the instance. 
2. Go to order-service-client-application directory, build the client application jars:
```
mvn clean package
```
This will generate two jars:
a) spring-boot/target/spring-boot-service-0.0.1-SNAPSHOT.jar: This is the spring boot web application which creates Order after receiving request from client.
It then passes the Order for processing to its downstream services using Kafka.
b) console/target/kafka-consumer-services-0.0.1-SNAPSHOT.jar: This contains three downstream services which processes Kafka message(Order) sent by spring boot web application.

3. Run the services start shell script
```
sh run-services.sh
```
This will start the mesh of micro-services. The java processes are auto-instrumented using OTel java agent. So, once started the OTel signals will be automatically emitted
by the client services all the way to OpenSearch cluster.

4. Wait for 5-10 secs to let the above spring boot web application come up. Then, run client shell script
```
sh run-client.sh
```
This will start a simple java client which will create orders periodically by calling spring boot web service. With this setup the metrics from the client application will reach the OpenSearch cluster and should be ready for analytics.

### Analyze observability data with OpenSearch Dashboards

The above setup will create 3 different indexes on OpenSearch cluster:
1. **otel-logs-%{yyyy.MM.dd}** for logs
2. **otel-metrics-%{yyyy.MM.dd}** for metrics
3. **otel-v1-apm-service-map** and **otel-v1-apm-span-*** for traces

You can use these indexes to deeply analyze observability data using [OpenSearch observability guide](https://opensearch.org/docs/latest/observing-your-data/)


