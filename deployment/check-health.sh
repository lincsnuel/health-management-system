#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo "--- Checking Health Management System Services ---"

# 1. Keycloak
if curl -s --head  --request GET http://localhost:8080/health/live | grep "200 OK" > /dev/null; then
    echo -e "Keycloak: ${GREEN}UP${NC}"
else
    echo -e "Keycloak: ${RED}DOWN${NC} (Check logs: docker logs keycloak)"
fi

# 2. Postgres (Patient DB) - Checking if port is accepting connections
if nc -z localhost 15432; then
    echo -e "Patient DB: ${GREEN}UP${NC}"
else
    echo -e "Patient DB: ${RED}DOWN${NC}"
fi

# 3. Kafka (Checking the Metadata via the External Port)
if nc -z localhost 9094; then
    echo -e "Kafka: ${GREEN}UP${NC}"
else
    echo -e "Kafka: ${RED}DOWN${NC}"
fi

# 4. Redis
if redis-cli -p 6379 ping 2>/dev/null | grep "PONG" > /dev/null; then
    echo -e "Redis: ${GREEN}UP${NC}"
else
    echo -e "Redis: ${RED}DOWN${NC}"
fi

# 5. Zipkin
if curl -s http://localhost:9411/health | grep "UP" > /dev/null; then
    echo -e "Zipkin: ${GREEN}UP${NC}"
else
    echo -e "Zipkin: ${RED}DOWN${NC}"
fi