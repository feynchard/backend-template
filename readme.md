## quick start
### requirement
- postgres 14.x
- gradle 8.x
- jdk 21

### how to run project
0. sql schema is in the folder (./src/main/resources/db/changelog) and is managed by liquibase.
1. create a new database in postgres.
2. configure db source in application.properties (./src/main/resources/)
3. configure jwt secret key (HMAC256)
4. gradle bootRun in project folder

Now the project is started.
You can open browser to visit http://127.0.0.1:8080/swagger-ui.html

Register a new system user and login using swagger api.

