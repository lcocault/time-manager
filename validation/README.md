# Validation BDD

Ce dossier contient des scenarios Gherkin executes avec:

- Backend: Cucumber (JVM) via Maven dans `validation/backend`
- Frontend: Cucumber.js + Playwright dans `validation/frontend`

## Backend

Les features sont dans `validation/backend/features`.

Execution:

```bash
cd validation/backend
mvn test
```

Prerequis: le backend API doit etre demarre (par exemple `cd backend && mvn spring-boot:run`).

## Frontend

Les features sont dans `validation/frontend/features`.

Execution:

```bash
cd validation/frontend
npm install
npm run install:browsers
npm run test:local
```

`test:local` demarre automatiquement le frontend Vite sur `http://127.0.0.1:4173`, lance les scenarios Cucumber.js/Playwright puis arrete le serveur.
