# time-manager

Un gestionnaire de tâches personnel inspiré des méthodes de Fabien Olicard, avec matrice d'Eisenhower étendue, synchronisation d'agendas pro/perso et planification intelligente sur 5 jours.

## Stack

- Backend : Spring Boot 3.2 / Java 17 / PostgreSQL 15
- Frontend : React 18 / TypeScript / Vite
- Architecture : Hexagonale (domain, application, infrastructure, interfaces)

## Lancer en local

1. Démarrer PostgreSQL : `docker compose up -d`
2. Lancer le backend : `cd /tmp/workspace/lcocault/time-manager/backend && mvn spring-boot:run`
3. Lancer le frontend : `cd /tmp/workspace/lcocault/time-manager/frontend && npm install && npm run dev`

## Endpoints principaux

- `POST /api/tasks`
- `GET /api/tasks`
- `PATCH /api/tasks/{id}`
- `DELETE /api/tasks/{id}`
- `GET /api/planning/5days`
