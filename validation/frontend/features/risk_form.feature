Feature: Validation frontend du registre des risques
  Le formulaire risque doit creer une entree et l afficher dans le registre.

  Scenario: Ajouter un risque depuis l interface
    Given que je charge l application frontend avec une API simulee
    When je saisis un nouveau risque "Risque BDD" et je valide
    Then je vois le risque "Risque BDD" dans le registre
    And je vois l action de risque "Ajouter une action preventive"
    And le statut affiche est "API connectée"