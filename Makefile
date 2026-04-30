# ENV
GATEWAY=gateway
USER_SERVICE=user-service
AUTH_SERVICE=auth-service
INFRA=infra

MVN=./mvnw
DOCKER_COMPOSE=docker compose

# INFRA
.PHONY: start-infra stop-infra restart-infra

start-infra:
	cd $(INFRA) && $(DOCKER_COMPOSE) up -d

stop-infra:
	cd $(INFRA) && $(DOCKER_COMPOSE) down

restart-infra: stop-infra start-infra


# SERVICES
.PHONY: start-users start-auth start-gateway
.PHONY: start-services stop-services restart-services

start-users:
	cd $(USER_SERVICE) && $(MVN) spring-boot:run

start-auth:
	cd $(AUTH_SERVICE) && $(MVN) spring-boot:run

start-gateway:
	cd $(GATEWAY) && $(MVN) spring-boot:run


# ALL SERVICES
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


# APP COMPLETO
.PHONY: start-app stop-app restart-app

start-app: start-infra start-services

stop-app: stop-services stop-infra

restart-app: stop-app start-app