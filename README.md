# Simplified SPA Frontend as Example for StackOverflow question

Start with: 
```
./mvnw spring-boot:run
```

Listens on http://localhost:8080

## Content

### SPA Frontend

- [index.html](src/main/webapp/index.html)

### Handling of custom SSO token:

- [AuthFilter](src/main/java/com/github/acme42/frontend/sso/AuthFilter.java)

- [AuthService](src/main/java/com/github/acme42/frontend/sso/AuthService.java)

- [User](src/main/java/com/github/acme42/frontend/sso/User.java)

### Zuul Configuration

- [application.yml](src/main/resources/application.yml)

- [VehiclesZuulFilter](src/main/java/com/github/acme42/frontend/VehiclesZuulFilter.java)

## Related Projects

- [Super Simplified Single Sign-On Portal](https://github.com/acme42/portal)

- [Super Simple Backend Service](https://github.com/acme42/backend)
