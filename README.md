Gestion du restaurant
Application Java Swing permettant de gérer une partie des activités d’un restaurant : réservations, commandes, tables, recherches et validations métier.
Ce projet a été réalisé dans le cadre du cours de développement d’application Java avec base de données relationnelle.
---
Membres du groupe
Nom	Prénom	Email
À compléter	À compléter	À compléter
À compléter	À compléter	À compléter
---
Objectif du projet
L’objectif est de développer une application respectant une architecture en couches, avec :
une interface graphique ;
un accès à une base de données relationnelle ;
une gestion propre des exceptions ;
des validations métier ;
des recherches SQL complexes ;
des tests unitaires ;
un thread supplémentaire.
L’application permet notamment de :
gérer les réservations ;
gérer les tables du restaurant ;
créer des commandes à emporter ou sur place ;
suivre les commandes en cours ;
modifier le statut des produits d’une commande ;
rechercher des réservations, commandes et produits selon plusieurs critères ;
valider certaines règles métier comme la capacité d’une réservation.
---
Technologies utilisées
Java
Java Swing
FlatLaf
MariaDB / MySQL
JDBC
JUnit 5
IntelliJ IDEA
Git / GitHub
---
Architecture du projet
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
```
Organisation des packages
```txt
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
```
Rôle des couches
Couche	Rôle
`viewPackage`	Interface graphique Swing
`controllerPackage`	Point d’entrée entre la vue et la couche métier
`businessPackage`	Règles métier et validations
`dataAccessPackage.interfaces`	Interfaces DAO
`dataAccessPackage.db`	Requêtes SQL et accès à la base de données
`modelPackage`	Classes métiers
`exceptionPackage`	Exceptions personnalisées
`viewPackage.ui`	Composants graphiques réutilisables
---
Fonctionnalités principales
Réservations
L’application permet de gérer un CRUD complet sur les réservations :
lister les réservations par date ;
ajouter une réservation ;
modifier une réservation ;
supprimer une réservation avec confirmation ;
vérifier qu’une table n’est pas déjà réservée à la même date et à la même heure ;
vérifier que le nombre de personnes ne dépasse pas la capacité de la table.
La clé d’une réservation repose sur des informations réelles :
```txt
bookDate, bookHour, idTable, nameCustomer
```
Commandes
L’application permet de :
créer une commande à emporter ;
créer une commande liée à une table ;
sélectionner les produits de la commande ;
afficher les commandes en cours ;
consulter le détail d’une commande ;
modifier le statut de chaque produit d’une commande ;
supprimer une commande en cours avec ses lignes de commande associées.
Les statuts SQL des produits sont :
```txt
Pending
InPreparation
Ready
Served
```
Ils sont affichés en français dans l’interface :
```txt
En attente
En préparation
Prête
Servie
```
Le statut global d’une commande est calculé à partir des statuts de ses lignes :
Situation	Statut global
Au moins une ligne en attente ou en préparation	En préparation
Toutes les lignes sont prêtes ou servies	Prête
Toutes les lignes sont servies	Servie
Tables
L’application permet de :
afficher les tables du restaurant ;
afficher le nombre de places ;
afficher le statut de chaque table ;
modifier le statut d’une table ;
accéder aux commandes liées à une table occupée.
Les statuts SQL des tables sont :
```txt
Available
Reserved
Occupied
```
Ils sont affichés en français :
```txt
Libre
Réservée
Occupée
```
---
Recherches SQL obligatoires
Le projet contient trois recherches principales.
1. Recherche de réservations
Recherche des réservations selon :
le client ;
la date.
Tables utilisées :
```txt
Book
RestaurantTable
Status
```
2. Recherche de commandes
Recherche des commandes selon :
l’employé ;
le statut de ligne de commande.
Tables utilisées :
```txt
CustomerOrder
LineOrder
Employee
Status
Product
```
3. Recherche de produits
Recherche des produits selon :
le type de produit ;
l’allergène.
Tables utilisées :
```txt
Product
Type
IngredientProduct
Ingredient
ListAllergy
Allergy
```
---
Tâche métier
Une tâche métier a été ajoutée dans la couche `businessPackage`.
Elle permet de valider qu’une réservation respecte la capacité de la table :
```java
validateBookingCapacity(Book booking)
```
Règle métier :
```txt
nombre de personnes <= nombre de places de la table
```
Cette méthode est placée dans la couche Business car elle ne dépend ni de l’interface graphique ni directement de la base de données.
---
Tests unitaires
Des tests unitaires JUnit ont été réalisés sur la méthode métier :
```java
validateBookingCapacity(...)
```
Les tests vérifient notamment :
réservation acceptée si la capacité est suffisante ;
réservation refusée si le nombre de personnes dépasse la capacité ;
réservation refusée si la table est absente ;
réservation refusée si le nombre de personnes est invalide ;
réservation refusée si la capacité de la table est invalide.
Ces tests ne nécessitent pas d’accès à la base de données.
---
Thread supplémentaire
Le projet contient un thread supplémentaire dans :
```txt
KitchenAnimationPanel
```
Ce thread anime un texte sur l’écran d’accueil :
```txt
La cuisine se prépare...
```
Il ne fait aucun accès à la base de données.
Comme Swing n’est pas thread-safe, les mises à jour graphiques sont faites avec :
```java
SwingUtilities.invokeLater(...)
```
---
Base de données
La base de données utilisée est une base MariaDB / MySQL.
Les scripts SQL sont fournis séparément :
```txt
create_tables.sql
insert_data.sql
```
Tables principales
```txt
Allergy
Book
CustomerOrder
Employee
Ingredient
IngredientProduct
JobType
LineOrder
ListAllergy
Product
RestaurantTable
Status
Type
```
---
Configuration de la connexion à la base de données
Pour des raisons de sécurité, les identifiants de connexion ne sont pas écrits directement dans le code.
L’application utilise des variables d’environnement :
```txt
PROJETJAVA_DB_URL
PROJETJAVA_DB_USER
PROJETJAVA_DB_PASSWORD
```
Exemple :
```txt
PROJETJAVA_DB_URL=jdbc:mariadb://mysql-projetjava.alwaysdata.net:3306/projetjava_bd
PROJETJAVA_DB_USER=nom_utilisateur
PROJETJAVA_DB_PASSWORD=mot_de_passe
```
Dans IntelliJ IDEA :
```txt
Run > Edit Configurations > Environment variables
```
Ajouter ensuite les trois variables ci-dessus.
Les identifiants réels de connexion sont fournis dans le fichier de remise séparé destiné aux professeurs.
---
Lancement du projet
Prérequis
Java installé ;
IntelliJ IDEA ;
driver MariaDB JDBC ;
accès à la base de données configuré via variables d’environnement.
Étapes
Cloner le repository :
```bash
git clone URL_DU_REPOSITORY
```
Ouvrir le projet dans IntelliJ IDEA.
Ajouter les variables d’environnement :
```txt
PROJETJAVA_DB_URL
PROJETJAVA_DB_USER
PROJETJAVA_DB_PASSWORD
```
Vérifier que le driver MariaDB est bien présent dans le projet.
Lancer la classe :
```txt
Main.java
```
---
Sécurité et validation
Le projet applique plusieurs règles de sécurité et de validation :
utilisation de `PreparedStatement` pour éviter les injections SQL ;
validation des champs obligatoires côté interface ;
validation métier dans la couche Business ;
gestion des erreurs avec des exceptions personnalisées ;
aucun affichage utilisateur dans la couche Business ou DataAccess ;
aucun SQL dans la couche View ou Business ;
les identifiants de base de données ne sont pas stockés dans le code.
---
Gestion des exceptions
Les exceptions sont centralisées dans le package :
```txt
exceptionPackage
```
Exemples :
```txt
BookingException
ConnectionException
LineOrderException
OrderException
ProductException
SearchException
TableException
```
Les erreurs SQL sont capturées dans la couche DataAccess, puis transformées en exceptions personnalisées.
La View affiche ensuite les messages à l’utilisateur avec `JOptionPane`.
---
Interface utilisateur
L’interface est réalisée en Java Swing.
Les éléments visibles par l’utilisateur sont en français.
Le code reste en anglais pour respecter les conventions de clean code.
Les styles graphiques sont centralisés dans :
```txt
viewPackage.ui.AppTheme
```
Les composants réutilisables sont centralisés dans :
```txt
ButtonFactory
CardFactory
FormFactory
TableFactory
StatusHelper
DateHelper
LoadingHelper
```
---
Points importants pour l’évaluation
Le projet contient :
un CRUD complet ;
trois recherches SQL avec jointures ;
une architecture en couches ;
le pattern DAO avec interfaces ;
des exceptions personnalisées ;
des validations côté View et Business ;
une tâche métier testable ;
des tests unitaires JUnit ;
un thread supplémentaire ;
une interface graphique en Swing ;
une connexion sécurisée via variables d’environnement ;
des requêtes SQL avec `PreparedStatement`.
---
Repository GitHub
Lien du repository :
```txt
URL_DU_REPOSITORY
```
---
Remarques
Les identifiants de connexion à la base de données ne sont volontairement pas placés dans ce README afin d’éviter de les exposer publiquement sur GitHub.
Ils sont transmis séparément dans le fichier de remise demandé.
