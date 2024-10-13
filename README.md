# big-dt-hk241-spark-springboot

install java 17

install maven

open cmd,  run command

mvn clean install

install mysql

Install Docker
Option 1: Pull Spark Image from DockerHub
You can pull the image from the Bitnami repository or Apache Spark:

```bash
# Pull Bitnami Spark image
docker pull bitnami/spark

# Or, pull from official Apache Spark image
docker pull apache/spark
```
Run Spark in Standalone Mode Using Docker

Step 1: Start Spark Master Container
To start the Spark Master container

```bash
docker run -d --name spark-master -h spark-master \
-p 8080:8080 -p 7077:7077 \
bde2020/spark-master:latest
```
- -d: Run container in the background.
- --name: Name the container as spark-master.
- -h: Set hostname as spark-master
- -p: Expose the necessary ports. Port 8080 is for the Spark Web UI, and 7077 is for the Spark cluster communication.


```bash
docker run --name spark bitnami/spark:latest
````

````bash
docker run -it --rm \
  --name spark-master \
  -p 8080:8080 \
  -p 7077:7077 \
  bitnami/spark:latest \
  /bin/bash -c "start-spark.sh"
````

````bash
docker run -it --rm --name spark-master -p 8080:8080 -p 7077:7077 bitnami/spark:latest /opt/bitnami/spark/bin/spark-class org.apache.spark.deploy.master.Master

````
````bash
docker ps
````

Step 2: Start Spark Worker Container
To start the Worker containers, run:

```bash
docker run -d --name spark-worker-1 --link spark-master:spark-master \
    -p 8081:8081 \
    bde2020/spark-worker:latest
```

You can launch multiple workers by changing the name and port for each worker:

```bash
docker run -d --name spark-worker-2 --link spark-master:spark-master \
-p 8082:8081 \
bde2020/spark-worker:latest
```
The --link option connects the workers to the Spark master.
The workers' Web UIs will be available at http://localhost:8081 and http://localhost:8082

Step 3: Access Spark Web UI

Once the containers are running, you can access the Spark Web UI at
- Master UI: http://localhost:8080
- Worker 1 UI: http://localhost:8081
- Worker 2 UI: http://localhost:8082

4. Submit Spark Jobs Using Docker
   You can submit a Spark job to the cluster using spark-submit. First, run a shell within the spark-master container:

````bash
docker exec -it spark-master bash

````

Then, you can submit a job using the spark-submit command inside the container:

````bash
spark-submit \
  --master spark://spark-master:7077 \
  --class org.apache.spark.examples.SparkPi \
  /spark/examples/jars/spark-examples_2.12-3.0.1.jar 100
````
This will run the SparkPi example and submit it to the Spark cluster.

5. Optional: Use Docker Compose for Spark Cluster

````yaml
version: '3'
services:
  spark-master:
    image: bde2020/spark-master:latest
    container_name: spark-master
    hostname: spark-master
    ports:
      - "8080:8080"
      - "7077:7077"
  spark-worker-1:
    image: bde2020/spark-worker:latest
    container_name: spark-worker-1
    hostname: spark-worker-1
    environment:
      SPARK_MASTER: spark://spark-master:7077
    links:
      - spark-master
    ports:
      - "8081:8081"
  spark-worker-2:
    image: bde2020/spark-worker:latest
    container_name: spark-worker-2
    hostname: spark-worker-2
    environment:
      SPARK_MASTER: spark://spark-master:7077
    links:
      - spark-master
    ports:
      - "8082:8081"

````

To launch the Spark cluster using Docker Compose, run:

````bash

docker-compose up

````

This will bring up the Spark master and two workers automatically.

6. Stopping the Cluster

````bash
docker stop spark-master spark-worker-1 spark-worker-2
````

Or if you're using Docker Compose:

````bash
docker-compose down
````
