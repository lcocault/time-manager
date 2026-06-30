Feature: Validation frontend avec Cucumber.js et Playwright
  Le formulaire doit creer une tache et l afficher dans la liste.

  Scenario: Ajouter une tache depuis l interface
    Given que je charge l application frontend avec une API simulee
    When je saisis une nouvelle tache "BDD Front" de 30 minutes et je valide
    Then je vois la tache "BDD Front" dans la liste priorisee
    And le statut affiche est "API connectée"
