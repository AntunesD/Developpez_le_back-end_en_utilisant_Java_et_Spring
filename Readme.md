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

### 2. Installer MySQL

Le projet utilise MySQL comme base de données. Si vous ne l'avez pas déjà installé, voici les étapes pour l'installer sur votre machine :

1. Téléchargez et installez MySQL depuis le site officiel : [MySQL Downloads](https://dev.mysql.com/downloads/).
2. Une fois MySQL installé, démarrez le service MySQL.

### 3. Importer le fichier `data.sql`

Le projet nécessite un fichier SQL pour initialiser la base de données avec des données de test. Ce fichier se trouve dans le dossier `src/main/resources`. Voici comment l'utiliser pour peupler votre base de données MySQL :

- Copiez le fichier `data.sql` situé dans `src/main/resources` dans votre répertoire de base de données MySQL ou exécutez-le manuellement via un client MySQL.

```sql
mysql -u root -p < src/main/resources/data.sql
```

### 4. Créer un fichier `.env` pour les variables d'environnement

Pour configurer votre application Spring Boot pour qu'elle utilise MySQL, créez un fichier `.env` à la racine de votre projet. Ce fichier contiendra les variables d'environnement nécessaires :

#### Exemple de fichier `.env` :

```env
MYSQL_DATABASE=Nom_de_votre_db
MYSQL_USERNAME=Votre_username
MYSQL_PASSWORD=Votre_password
```

### 5. Si cela ne fonctionne pas, exportez les variables d'environnement dans votre terminal

Si Spring Boot ne parvient pas à récupérer les variables depuis le fichier `.env`, vous pouvez les définir manuellement dans votre terminal avant de démarrer l'application.

#### Exemple sous Bash (Linux/MacOS) :

```bash
export MYSQL_DATABASE=Nom_de_votre_db
export MYSQL_USERNAME=Votre_username
export MYSQL_PASSWORD=Votre_password
```

#### Exemple sous Windows (cmd) :

```cmd
set MYSQL_DATABASE=Nom_de_votre_db
set MYSQL_USERNAME=Votre_username
set MYSQL_PASSWORD=Votre_password
```

### 6. Lancer l'application

Une fois que vous avez configuré MySQL et les variables d'environnement, vous pouvez lancer l'application en exécutant la commande suivante dans votre terminal :

```bash
mvn clean install
mvn spring-boot:run
```

Ou, si vous préférez exécuter le JAR directement :

```bash
java -jar target/ApiP3-0.0.1-SNAPSHOT.jar
```

### 7. Documentation de l'API

La documentation complète de l'API est disponible via Swagger à l'adresse suivante :  
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

Merci d'utiliser ce projet ! 🚀
