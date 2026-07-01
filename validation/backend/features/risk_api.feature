Feature: Validation API risques backend
  Les endpoints REST de gestion des risques doivent repondre correctement.

  Scenario: Creer et lister un risque
    Given le backend API est initialise
    When je cree un risque avec le facteur "Risque Cucumber"
    Then le statut HTTP est 201
    And la reponse de creation risque contient le facteur "Risque Cucumber"
    When je fais une requete GET sur /api/risks
    Then le statut HTTP est 200
    And la reponse contient au moins 1 risques

  Scenario: Mettre a jour un risque
    Given le backend API est initialise
    When je cree un risque avec le facteur "Risque avant mise a jour"
    Then le statut HTTP est 201
    When je modifie le dernier risque cree avec le facteur "Risque apres mise a jour"
    Then le statut HTTP est 200
    And la reponse de mise a jour risque contient le facteur "Risque apres mise a jour"

  Scenario: Supprimer un risque
    Given le backend API est initialise
    When je cree un risque avec le facteur "Risque a supprimer"
    Then le statut HTTP est 201
    When je supprime le dernier risque cree
    Then le statut HTTP est 204
    When je fais une requete GET sur /api/risks
    Then le statut HTTP est 200
    And la liste des risques ne contient pas le dernier risque supprime