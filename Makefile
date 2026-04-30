# ENV
GATEWAY=gateway
USER_SERVICE=user-service
AUTH_SERVICE=auth-service
INFRA=infra

MVN=./mvnw
DOCKER_COMPOSE=docker compose

# WAIT
.PHONY: wait
wait:
	@echo "Waiting for containers to be ready..."
	sleep 10


# APP COMPLETO
.PHONY: start-app stop-app restart-app

start-app: start-infra wait start-services

stop-app: stop-services stop-infra

restart-app: stop-app start-app


# INFRA
.PHONY: start-infra stop-infra restart-infra

start-infra:
	cd $(INFRA) && $(DOCKER_COMPOSE) up -d

stop-infra:
	cd $(INFRA) && $(DOCKER_COMPOSE) down

restart-infra: stop-infra start-infra

# ALL SERVICES
.PHONY: start-services stop-services restart-services
start-services:
	$(MAKE) start-users & \
	$(MAKE) start-auth & \
	$(MAKE) start-gateway & \
	wait

stop-services:
	pkill -f "$(USER_SERVICE).*spring-boot:run" || true
	pkill -f "$(AUTH_SERVICE).*spring-boot:run" || true
	pkill -f "$(GATEWAY).*spring-boot:run" || true

restart-services: stop-services start-services

# INDIVIDUAL
.PHONY: start-users start-auth start-gateway
.PHONY: stop-users stop-auth stop-gateway
.PHONY: restart-users restart-auth restart-gateway

start-users:
	cd $(USER_SERVICE) && $(MVN) spring-boot:run

start-auth:
	cd $(AUTH_SERVICE) && $(MVN) spring-boot:run

start-gateway:
	cd $(GATEWAY) && $(MVN) spring-boot:run

stop-users:
	pkill -f "$(USER_SERVICE).*spring-boot:run" || true

stop-auth:
	pkill -f "$(AUTH_SERVICE).*spring-boot:run" || true

stop-gateway:
	pkill -f "$(GATEWAY).*spring-boot:run" || true

restart-users: stop-users start-users
restart-auth: stop-auth start-auth
restart-gateway: stop-gateway start-gateway