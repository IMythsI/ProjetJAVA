GESTION DU RESTAURANT
=====================

Application Java Swing permettant de gérer une partie des activités d’un restaurant : réservations, commandes, tables, recherches et validations métier.

Ce projet a été réalisé dans le cadre du cours de développement d’application Java avec base de données relationnelle.


1. MEMBRES DU GROUPE
====================

Nom : À compléter
Prénom : À compléter
Email : À compléter

Nom : À compléter
Prénom : À compléter
Email : À compléter


2. OBJECTIF DU PROJET
=====================

L’objectif est de développer une application respectant une architecture en couches, avec :

- une interface graphique ;
- un accès à une base de données relationnelle ;
- une gestion propre des exceptions ;
- des validations métier ;
- des recherches SQL complexes ;
- des tests unitaires ;
- un thread supplémentaire.

L’application permet notamment de :

- gérer les réservations ;
- gérer les tables du restaurant ;
- créer des commandes à emporter ou sur place ;
- suivre les commandes en cours ;
- modifier le statut des produits d’une commande ;
- rechercher des réservations, commandes et produits selon plusieurs critères ;
- valider certaines règles métier comme la capacité d’une réservation.


3. TECHNOLOGIES UTILISÉES
=========================

- Java
- Java Swing
- FlatLaf
- MariaDB / MySQL
- JDBC
- JUnit 5
- IntelliJ IDEA
- Git / GitHub


4. ARCHITECTURE DU PROJET
=========================

Le projet respecte une architecture en couches :

View
 ↓
Controller
 ↓
Business
 ↓
DataAccess
 ↓
Database

Organisation des packages :

src
├── businessPackage
├── controllerPackage
├── dataAccessPackage
│   ├── db
│   └── interfaces
├── exceptionPackage
├── lib
├── modelPackage
└── viewPackage
    ├── Booking
    ├── Dashboard
    ├── Order
    ├── Search
    ├── Table
    └── ui

Rôle des couches :

- viewPackage : interface graphique Swing.
- controllerPackage : point d’entrée entre la vue et la couche métier.
- businessPackage : règles métier et validations.
- dataAccessPackage.interfaces : interfaces DAO.
- dataAccessPackage.db : requêtes SQL et accès à la base de données.
- modelPackage : classes métiers.
- exceptionPackage : exceptions personnalisées.
- viewPackage.ui : composants graphiques réutilisables.


5. FONCTIONNALITÉS PRINCIPALES
==============================

5.1 Réservations
----------------

L’application permet de gérer un CRUD complet sur les réservations :

- lister les réservations par date ;
- ajouter une réservation ;
- modifier une réservation ;
- supprimer une réservation avec confirmation ;
- vérifier qu’une table n’est pas déjà réservée à la même date et à la même heure ;
- vérifier que le nombre de personnes ne dépasse pas la capacité de la table.

La clé d’une réservation repose sur des informations réelles :

bookDate, bookHour, idTable, nameCustomer


5.2 Commandes
-------------

L’application permet de :

- créer une commande à emporter ;
- créer une commande liée à une table ;
- sélectionner les produits de la commande ;
- afficher les commandes en cours ;
- consulter le détail d’une commande ;
- modifier le statut de chaque produit d’une commande ;
- supprimer une commande en cours avec ses lignes de commande associées.

Les statuts SQL des produits sont :

- Pending
- InPreparation
- Ready
- Served

Ils sont affichés en français dans l’interface :

- En attente
- En préparation
- Prête
- Servie

Le statut global d’une commande est calculé à partir des statuts de ses lignes :

- au moins une ligne en attente ou en préparation : En préparation ;
- toutes les lignes sont prêtes ou servies : Prête ;
- toutes les lignes sont servies : Servie.


5.3 Tables
----------

L’application permet de :

- afficher les tables du restaurant ;
- afficher le nombre de places ;
- afficher le statut de chaque table ;
- modifier le statut d’une table ;
- accéder aux commandes liées à une table occupée.

Les statuts SQL des tables sont :

- Available
- Reserved
- Occupied

Ils sont affichés en français :

- Libre
- Réservée
- Occupée


6. RECHERCHES SQL OBLIGATOIRES
==============================

Le projet contient trois recherches principales.

6.1 Recherche de réservations
-----------------------------

Recherche des réservations selon :

- le client ;
- la date.

Tables utilisées :

- Book
- RestaurantTable
- Status


6.2 Recherche de commandes
--------------------------

Recherche des commandes selon :

- l’employé ;
- le statut de ligne de commande.

Tables utilisées :

- CustomerOrder
- LineOrder
- Employee
- Status
- Product


6.3 Recherche de produits
-------------------------

Recherche des produits selon :

- le type de produit ;
- l’allergène.

Tables utilisées :

- Product
- Type
- IngredientProduct
- Ingredient
- ListAllergy
- Allergy


7. TÂCHE MÉTIER
===============

Une tâche métier a été ajoutée dans la couche businessPackage.

Elle permet de valider qu’une réservation respecte la capacité de la table :

validateBookingCapacity(Book booking)

Règle métier :

nombre de personnes <= nombre de places de la table

Cette méthode est placée dans la couche Business car elle ne dépend ni de l’interface graphique ni directement de la base de données.


8. TESTS UNITAIRES
==================

Des tests unitaires JUnit ont été réalisés sur la méthode métier :

validateBookingCapacity(...)

Les tests vérifient notamment :

- réservation acceptée si la capacité est suffisante ;
- réservation refusée si le nombre de personnes dépasse la capacité ;
- réservation refusée si la table est absente ;
- réservation refusée si le nombre de personnes est invalide ;
- réservation refusée si la capacité de la table est invalide.

Ces tests ne nécessitent pas d’accès à la base de données.


9. THREAD SUPPLÉMENTAIRE
========================

Le projet contient un thread supplémentaire dans :

KitchenAnimationPanel

Ce thread anime un texte sur l’écran d’accueil :

La cuisine se prépare...

Il ne fait aucun accès à la base de données.

Comme Swing n’est pas thread-safe, les mises à jour graphiques sont faites avec :

SwingUtilities.invokeLater(...)


10. BASE DE DONNÉES
===================

La base de données utilisée est une base MariaDB / MySQL.

Les scripts SQL sont fournis séparément :

- create_tables.sql
- insert_data.sql

Tables principales :

- Allergy
- Book
- CustomerOrder
- Employee
- Ingredient
- IngredientProduct
- JobType
- LineOrder
- ListAllergy
- Product
- RestaurantTable
- Status
- Type


11. CONFIGURATION DE LA CONNEXION À LA BASE DE DONNÉES
======================================================

Pour des raisons de sécurité, les identifiants de connexion ne sont pas écrits directement dans le code.

L’application utilise des variables d’environnement :

- PROJETJAVA_DB_URL
- PROJETJAVA_DB_USER
- PROJETJAVA_DB_PASSWORD

Exemple :

PROJETJAVA_DB_URL=jdbc:mariadb://mysql-projetjava.alwaysdata.net:3306/projetjava_bd
PROJETJAVA_DB_USER=nom_utilisateur
PROJETJAVA_DB_PASSWORD=mot_de_passe

Dans IntelliJ IDEA :

Run > Edit Configurations > Environment variables

Ajouter ensuite les trois variables ci-dessus.

Les identifiants réels de connexion sont fournis dans le fichier de remise séparé destiné aux professeurs.


12. LANCEMENT DU PROJET
=======================

Prérequis :

- Java installé ;
- IntelliJ IDEA ;
- Driver MariaDB JDBC ;
- accès à la base de données configuré via variables d’environnement.

Étapes :

1. Cloner le repository :

git clone URL_DU_REPOSITORY

2. Ouvrir le projet dans IntelliJ IDEA.

3. Ajouter les variables d’environnement :

PROJETJAVA_DB_URL
PROJETJAVA_DB_USER
PROJETJAVA_DB_PASSWORD

4. Vérifier que le driver MariaDB est bien présent dans le projet.

5. Lancer la classe :

Main.java


13. SÉCURITÉ ET VALIDATION
==========================

Le projet applique plusieurs règles de sécurité et de validation :

- utilisation de PreparedStatement pour éviter les injections SQL ;
- validation des champs obligatoires côté interface ;
- validation métier dans la couche Business ;
- gestion des erreurs avec des exceptions personnalisées ;
- aucun affichage utilisateur dans la couche Business ou DataAccess ;
- aucun SQL dans la couche View ou Business ;
- les identifiants de base de données ne sont pas stockés dans le code.


14. GESTION DES EXCEPTIONS
==========================

Les exceptions sont centralisées dans le package :

exceptionPackage

Exemples :

- BookingException
- ConnectionException
- LineOrderException
- OrderException
- ProductException
- SearchException
- TableException

Les erreurs SQL sont capturées dans la couche DataAccess, puis transformées en exceptions personnalisées.

La View affiche ensuite les messages à l’utilisateur avec JOptionPane.


15. INTERFACE UTILISATEUR
=========================

L’interface est réalisée en Java Swing.

Les éléments visibles par l’utilisateur sont en français.

Le code reste en anglais pour respecter les conventions de clean code.

Les styles graphiques sont centralisés dans :

viewPackage.ui.AppTheme

Les composants réutilisables sont centralisés dans :

- ButtonFactory
- CardFactory
- FormFactory
- TableFactory
- StatusHelper
- DateHelper
- LoadingHelper


16. POINTS IMPORTANTS POUR L’ÉVALUATION
=======================================

Le projet contient :

- un CRUD complet ;
- trois recherches SQL avec jointures ;
- une architecture en couches ;
- le pattern DAO avec interfaces ;
- des exceptions personnalisées ;
- des validations côté View et Business ;
- une tâche métier testable ;
- des tests unitaires JUnit ;
- un thread supplémentaire ;
- une interface graphique en Swing ;
- une connexion sécurisée via variables d’environnement ;
- des requêtes SQL avec PreparedStatement.


17. REPOSITORY GITHUB
=====================

Lien du repository :

URL_DU_REPOSITORY


18. REMARQUES
=============

Les identifiants de connexion à la base de données ne sont volontairement pas placés dans ce fichier afin d’éviter de les exposer publiquement sur GitHub.

Ils sont transmis séparément dans le fichier de remise demandé.
