# Gestion du restaurant

Application Java Swing permettant de gérer une partie des activités d’un restaurant : réservations, commandes, tables, recherches et validations métier.

Ce projet a été réalisé dans le cadre du cours de développement d’application Java avec base de données relationnelle.

---

## Membres du groupe

| Nom | Prénom | Email |
|---|---|---|
| NOM | Prénom | email@exemple.com |
| NOM | Prénom | email@exemple.com |

---

## Objectif du projet

L’objectif est de développer une application respectant une architecture en couches, avec une interface graphique, un accès à une base de données relationnelle, une gestion propre des exceptions, des validations métier, des recherches SQL complexes, des tests unitaires et un thread supplémentaire.

L’application permet notamment de :

- gérer les réservations ;
- gérer les tables du restaurant ;
- créer des commandes à emporter ou sur place ;
- suivre les commandes en cours ;
- modifier le statut des produits d’une commande ;
- rechercher des réservations, commandes et produits selon plusieurs critères ;
- valider certaines règles métier comme la capacité d’une réservation.

---

## Technologies utilisées

- Java
- Java Swing
- FlatLaf
- MariaDB / MySQL
- JDBC
- JUnit 5
- IntelliJ IDEA
- Git / GitHub

---

## Architecture du projet

Le projet respecte une architecture en couches :

```txt
View
 ↓
Controller
 ↓
Business
 ↓
DataAccess
 ↓
Database
