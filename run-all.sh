#!/bin/bash

echo "Starting Eureka..."
java -jar eureka-server/target/*.jar &

sleep 10

echo "Starting API Gateway..."
java -jar api-gateway/target/*.jar &

sleep 10

echo "Starting Flight Service..."
java -jar flight-service/target/*.jar &

sleep 10

echo "Starting Booking Service..."
java -jar booking-service/target/*.jar &

sleep 10

echo "Starting Security Service..."
java -jar simple-security/target/*.jar &

echo "All services started"
