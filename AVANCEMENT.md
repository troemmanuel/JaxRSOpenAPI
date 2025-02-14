# README - Projet

## Sommaire

- [État d'avancement](#état-davancement)
- [Fonctionnalités opérationnelles et non opérationnelles](#fonctionnalités-opérationnelles-et-non-opérationnelles)
- [Démarrage du projet](#démarrage-du-projet)
    - [Lancement de la base de données](#lancement-de-la-base-de-données)
    - [Lancement de l'application](#lancement-de-lapplication)
- [Prochaines tâches](#prochaines-tâches)

## État d'avancement

- Réalisation des diagrammes de classe
- Création de la base de données avec PostgreSQL
- Implémentation des modèles
- Implémentation des DAO (Data Access Objects)
- Implémentation des ressources avec les CRUD élémentaires :
    - Administrateur
    - Organisateur
    - Utilisateur

## Fonctionnalités opérationnelles et non opérationnelles

Pour l'instant, tout ce qui a été commencé fonctionne correctement.

## Démarrage du projet

### Lancement de la base de données

```sh
# Démarrer PostgreSQL (si ce n'est pas déjà fait)
sudo systemctl start postgresql

# Création de la base de données
dropdb --if-exists nom_de_la_bd
createdb nom_de_la_bd

# Exécution du script de création des tables
psql -d nom_de_la_bd -f chemin/vers/script.sql
```

### Lancement de l'application

```sh
# Installation des dépendances
mvn clean install  # Si le projet utilise Maven
npm install        # Si le projet inclut un front en Node.js

# Démarrer le backend
mvn spring-boot:run  # Si c'est une application Spring Boot

# Démarrer le frontend
npm run dev  # Si c'est une application React/Vue.js
```

## Prochaines tâches

- Implémentation des ressources :
    - Événements
    - Tickets
- Développement du frontend

