# Descripció - Enunciat de l'exercici
Aquest projecte consisteix en el desenvolupament d'una API REST per a gestionar un joc de Blackjack. L'objectiu principal és implementar una arquitectura de microserveis o modular que permeti:

Registrar i modificar jugadors.

Jugar partides de Blackjack (lògica de cartes i punts).

Emmagatzemar l'historial de partides i estadístiques.

Gestionar rànquings de guanyadors i perdedors.

La particularitat tècnica d'aquest exercici és la persistència híbrida de dades:

MySQL: Per emmagatzemar les dades dels jugadors (Relacional).

MongoDB: Per emmagatzemar les partides jugades (NoSQL).

# Tecnologies Utilitzades
El projecte s'ha desenvolupat utilitzant les següents tecnologies i eienes:

Llenguatge: Java 21 (Eclipse Temurin).

Framework: Spring Boot (Spring Data JPA, Spring Data MongoDB).

Bases de Dades:

MySQL 8.0

MongoDB 6.0

Contenidorització: Docker i Docker Compose.

Documentació API: Swagger / OpenAPI (SpringDoc).

Build Tool: Maven.

Repositori d'Imatges: Docker Hub.

# Requisits
Per a executar aquest projecte en local, es necessiten els següents requisits previs:

Docker Desktop instal·lat i en execució (Imprescindible per a les bases de dades).

Git (per clonar el repositori).

(Opcional) Java 21 i Maven instal·lats si es vol executar fora de Docker.

(Opcional) IntelliJ IDEA o Eclipse per al desenvolupament.

# Instal·lació
Segueix aquests passos per configurar l'entorn en local:

Clonar el repositori:
git clone <URL_DEL_TEU_REPOSITORI>
cd blackjack-api

Generar l'artefacte (.jar): És necessari compilar el projecte abans de construir la imatge Docker.
./mvnw clean package -DskipTests

Construir la imatge Docker (Opcional si es fa servir Docker Hub):
docker build -t blackjack-api .

# Execució
Hi ha dues maneres d'executar el projecte.

Opció A: Entorn Complet amb Docker Compose (Recomanat)
Això aixecarà l'API, MySQL i MongoDB automàticament.

Executa la comanda:
docker-compose up -d

L'API estarà disponible al port 8082.
Swagger UI: http://localhost:8082/webjars/swagger-ui/index.html

Opció B: Entorn de Desenvolupament (Híbrid)
Si vols desenvolupar amb IntelliJ però utilitzant les bases de dades de Docker.

Assegura't que els contenidors de DB estan corrent (docker-compose up -d).

Executa l'aplicació des del teu IDE (IntelliJ).

L'API de desenvolupament estarà disponible al port 8081.

# Desplegament
La imatge del projecte està pujada i disponible a Docker Hub. Per a desplegar l'aplicació en qualsevol servidor amb Docker instal·lat, no cal clonar el codi, només baixar la imatge:

Descarregar la imatge:
docker pull spook242/blackjack-api:v1.0

Execució: Per al correcte funcionament, s'ha d'executar connectant-la a les bases de dades corresponents (o utilitzant un fitxer docker-compose.yml que referenciï aquesta imatge).

Exemple bàsic d'execució (requereix xarxa configurada amb bases de dades):
docker run -p 8080:8081 --name blackjack-app spook242/blackjack-api:v1.0


