## Talency - Gestión de RRHH
---
### Descripción
Talency es una aplicación web para la gestión de empleados y recursos humanos dentro de una empresa. Permite gestionar usuarios, asignar roles y mantener un registro básico de información laboral. Este proyecto está pensado como aprendizaje y práctica personal con Spring Boot 3.5.6, Java 21, MySQL y Angular.

### Tecnologías

- Backend: Spring Boot 3.5.6, Java 21

- Base de datos: MySQL (Workbench)

- Frontend: Angular (proyecto separado)

### Dependencias destacadas:

- Spring Web

- Spring Data JPA

- Validation (Bean Validation / Hibernate Validator)

- Lombok

### Estructura del proyecto
```bash
src/main/java/com/tuempresa/rrhh
 ├── user
 │    ├── controller      → UserController
 │    ├── service         → UserService
 │    ├── repository      → UserRepository, RoleRepository
 │    ├── model           → User, Role
 │    └── dto             → UserDTO (opcional)
 ├── employee             → Módulo futuro de empleados
 ├── config               → Configuración de seguridad y CORS
 └── RrhhApplication.java → Clase principal Spring Boot
```
### Funcionalidades actuales

- CRUD de usuarios (User)

- Asignación de roles (Role)

- API REST básica lista para integrarse con Angular

- Validación de datos con Bean Validation

### Configuración

Clonar el repositorio.

Configurar la base de datos MySQL en src/main/resources/application.properties:

```bash
spring.application.name=talency
spring.datasource.url=jdbc:mysql://localhost:3306/{db_name}?useSSL=false&serverTimezone=UTC
spring.datasource.username={username}
spring.datasource.password={password}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Ejecutar la aplicación con:

> mvn spring-boot:run

#### Endpoints principales (Users)

- GET /api/users → Listar todos los usuarios

- GET /api/users/{id} → Obtener usuario por ID

- POST /api/users → Crear usuario

- PUT /api/users/{id} → Actualizar usuario

- DELETE /api/users/{id} → Eliminar usuario

### Notas

Las contraseñas deben ser encriptadas antes de guardar (recomendado BCrypt).

Proyecto personal / de práctica, no pensado para producción por ahora.
