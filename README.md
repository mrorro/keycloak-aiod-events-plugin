```bash
mvn clean package

docker cp target/keycloak-eventpusher-1.0.0.jar keycloak:/opt/keycloak/providers/

docker cp src/main/resources/META-INF/eventpusher.properties  keycloak:/opt/keycloak/data/
```
