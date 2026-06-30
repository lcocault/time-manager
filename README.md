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

## Tests de validation (BDD)

Les scenarios Gherkin sont centralises dans `validation/`:

- Backend: Cucumber (JVM) via Maven dans `validation/backend`
- Frontend: Cucumber.js + Playwright dans `validation/frontend`

### 1) Validation backend

Pre-requis: l'API backend doit etre demarree (par defaut sur `http://localhost:8080`).

```bash
cd backend
mvn spring-boot:run
```

Dans un autre terminal:

```bash
cd validation/backend
mvn test
```

Option: cibler une autre URL backend:

```bash
cd validation/backend
BACKEND_BASE_URL=http://localhost:8080 mvn test
```

### 2) Validation frontend

```bash
cd validation/frontend
npm install
npm run install:browsers
npm run test:local
```

Le script `test:local` demarre automatiquement Vite sur `http://127.0.0.1:4173`, execute les scenarios puis arrete le serveur.

### 3) Tout lancer avec docker compose

Si besoin de la base locale:

```bash
docker compose up -d
```
