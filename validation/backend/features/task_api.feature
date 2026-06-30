Feature: Validation API backend
  Les endpoints REST de base doivent repondre correctement.

  Scenario: Lister les taches
    Given le backend API est initialise
    When je fais une requete GET sur /api/tasks
    Then le statut HTTP est 200
    And la reponse contient au moins 1 taches

  Scenario: Creer une tache
    Given le backend API est initialise
    When je cree une tache avec le titre "Scenario Cucumber" et 45 minutes
    Then le statut HTTP est 201
    And la reponse de creation contient le titre "Scenario Cucumber"

  Scenario: Recuperer le planning sur 5 jours
    Given le backend API est initialise
    When je fais une requete GET sur /api/planning/5days
    Then le statut HTTP est 200
    And la reponse planning contient au moins 1 creneaux
