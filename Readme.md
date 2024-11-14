# Développez le back-end en utilisant Java et Spring

Ce projet a été réalisé dans le cadre d'une formation OpenClassrooms. Le but est de développer un back-end en utilisant Java et le framework Spring. Un front-end a été fourni pour ce projet, vous pouvez le récupérer ici :  
_([https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring](https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring))_

## Installation

### 1. Cloner le projet

Commencez par cloner le dépôt sur votre machine locale :

```bash
git clone https://github.com/AntunesD/Developpez_le_back-end_en_utilisant_Java_et_Spring.git
cd ApiP3
```

### 2. Installer les dépendances

Le projet utilise Maven pour la gestion des dépendances. Pour installer toutes les dépendances nécessaires, exécutez la commande suivante :

```bash
mvn clean install
```

### 3. Configuration de la clé JWT

Le projet utilise JSON Web Tokens (JWT) pour l'authentification. Vous devez configurer une clé secrète pour JWT dans le fichier `application.properties`, situé dans :

```bash
src/main/resources/application.properties
```

Ajoutez ou modifiez la ligne suivante en remplaçant `<votre-clé-jwt>` par une clé de votre choix :

```properties
jwt.secret.key=<votre-clé-jwt>
```

## Lancer l'application

Pour démarrer l'application, ouvrez la classe principale (`Application`) dans votre IDE et appuyez sur le bouton **Run** ou utilisez la commande suivante :

```bash
mvn spring-boot:run -Pdev
```

Une fois l'application démarrée, elle sera accessible à l'adresse suivante :  
[http://localhost:8080](http://localhost:8080)

## Documentation de l'API

La documentation complète de l'API est disponible via Swagger à l'adresse suivante :  
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Accès à la base de données

Le projet utilise une base de données H2 en mémoire. Pour accéder à la console H2, rendez-vous à l'adresse suivante :  
[http://localhost:8080/h2-console](http://localhost:8080/h2-console)

Vous pourrez consulter et manipuler les données directement via cette interface.

---

Merci d'utiliser ce projet ! 🚀
