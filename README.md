# 🎮 Clon de Steam - Backend

## Descripción
API REST construída con Java + Spring Boot para un clon de Steam. Expone endpoints de autenticación (registro/login), gestión de usuarios, CRUD de juegos, comentarios y posts de comunidad. 
Usa JWT para autenticación, Spring Security para autorización y MySQL como base de datos.

## Tabla de contenidos
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Funcionalidades Principales](#funcionalidades-principales)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Requisitos](#requisitos)
- [Como Ejecutar](#como-ejecutar)
  
  
## Tecnologías Utilizadas
- Java 21 + Spring Boot 3.5.6
- Spring Security + JWT (jjwt 0.11.5)
- Spring Data JPA (Hibernate)
- MySQL 8.0+ (base de datos)
- Maven
- Docker
- BCrypt (para hash de contraseñas)
- Postman

## Funcionalidades Principales
- 🔐 **Autenticación y Autorización con JWT**
  - Registro de usuarios con contraseñas hasheadas (BCrypt)
  - Login con generación de token JWT
  - Roles de usuario (USER, ADMIN)
  - Protección de endpoints por rol y método HTTP
- 👥 **Gestión de Usuarios:** registro, login y roles
- 🎮 **CRUD de Juegos:** creación, lectura, actualización y eliminación
- 🎯 **CRUD de Tipos de Juego:** categorización de juegos
- 💬 **Sistema de Comentarios:** para juegos y posts de comunidad
- 📝 **Posts de Comunidad:** creación y gestión de publicaciones
- 🖼️ **Carga de Imágenes:** para juegos
- 🌐 **CORS configurado globalmente**
- **Persistencia de datos en MySQL**
- **Contenerización completa con Docker**

---

## Estructura del Proyecto
```
react/
├── src/
│   ├── main/
│   │   ├── java/uade/TPO/react/
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── GameController.java
│   │   │   │   ├── CommentController.java
│   │   │   │   └── ...
│   │   │   ├── dto/
│   │   │   │   ├── AuthResponse.java
│   │   │   │   ├── LoginRequest.java
│   │   │   │   └── RegisterRequest.java
│   │   │   ├── entity/
│   │   │   │   ├── User.java
│   │   │   │   ├── Role.java
│   │   │   │   ├── Game.java
│   │   │   │   └── ...
│   │   │   ├── filter/
│   │   │   │   └── JwtFilter.java
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── util/
│   │   │       └── JwtUtil.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/images/
│   └── test/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── target/
```

## Requisitos
Asegurate de tener instalados:
- [x] **Java 21**
- [x] **Maven 3.9+**
- [x] **Docker Desktop y Docker Compose**
- [x] **MySQL 8.0+** (local o container)
- [x] **Postman** (para probar la API)


## Como Ejecutar

### Ejecución con Docker 🐳

1️⃣ Asegurate de tener MySQL corriendo en tu máquina local con:
   - Base de datos: `react`
   - Usuario: `root`
   - Contraseña: `root`

2️⃣ Construir la imagen
```bash
cd react
docker build -t back_tpo_api_grupo4:latest .
```

3️⃣ Levantar el contenedor
```bash
docker-compose up --build
```

4️⃣ Probar que el backend es accesible en:
```bash
http://localhost:8080
```

### Ejecución local con Maven

1️⃣ Asegurate de tener MySQL corriendo localmente

2️⃣ Ejecutar con Maven
```bash
cd react
mvn spring-boot:run
```

O usar el wrapper de Maven:
```bash
./mvnw spring-boot:run  # Linux/Mac
.\mvnw.cmd spring-boot:run  # Windows
```

### Pruebas con Postman 🟠

Probar los endpoints desde Postman:

1️⃣ Importá la colección `Postman_Collection_Security_Tests.json` (incluida en el proyecto)

2️⃣ Asegurate de que el backend esté corriendo en `http://localhost:8080`

3️⃣ Para acceder a endpoints protegidos:
   - Primero registrate con **POST** a `/api/auth/register`
   - Luego hacé login con **POST** a `/api/auth/login` para obtener el token JWT
   - En Postman, agregá en los headers de las requests protegidas:
     ```
     Authorization: Bearer <tu_token_jwt>
     ```

#### Endpoints Principales

##### Autenticación (Públicos)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/register` | Registro de nuevos usuarios |
| POST | `/api/auth/login` | Login y generación de token JWT |

##### Juegos
| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| GET | `/api/games` | Listado de juegos | ❌ No requerida |
| GET | `/api/games/{id}` | Obtener un juego por ID | ❌ No requerida |
| POST | `/api/games` | Crear un nuevo juego | ✅ JWT requerido |
| PUT | `/api/games/{id}` | Actualizar un juego | ✅ JWT requerido |
| DELETE | `/api/games/{id}` | Eliminar un juego | ✅ JWT requerido |

##### Tipos de Juego
| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| GET | `/api/gametypes` | Listado de tipos | ❌ No requerida |
| POST | `/api/gametypes` | Crear tipo | ✅ JWT requerido |
| PUT | `/api/gametypes/{id}` | Actualizar tipo | ✅ JWT requerido |
| DELETE | `/api/gametypes/{id}` | Eliminar tipo | ✅ JWT requerido |

##### Comentarios
| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| GET | `/api/comments` | Listado de comentarios | ❌ No requerida |
| GET | `/api/comments/game/{gameId}` | Comentarios de un juego | ❌ No requerida |
| POST | `/api/comments/game/{gameId}` | Crear comentario | ✅ JWT requerido |
| PUT | `/api/comments/{id}` | Actualizar comentario | ✅ JWT requerido |
| DELETE | `/api/comments/{id}` | Eliminar comentario | ✅ JWT requerido |

##### Posts de Comunidad
| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| GET | `/api/community-posts` | Listado de posts | ❌ No requerida |
| POST | `/api/community-posts` | Crear post | ✅ JWT requerido |
| PUT | `/api/community-posts/{id}` | Actualizar post | ✅ JWT requerido |
| DELETE | `/api/community-posts/{id}` | Eliminar post | ✅ JWT requerido |

##### Admin (Solo rol ADMIN)
| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| ALL | `/api/admin/**` | Endpoints administrativos | ✅ JWT + Rol ADMIN |

## Configuración de Seguridad

### JWT
- **Expiración:** 24 horas (86400000 ms)
- **Algoritmo:** HMAC SHA256
- **Secret:** Configurado en `application.properties`

### Roles
- **USER:** Usuario estándar (asignado por defecto al registrarse)
- **ADMIN:** Administrador con permisos completos

### Endpoints Públicos
- GET en `/api/games/**`, `/api/gametypes/**`, `/api/comments/**`, `/api/community-posts/**`
- POST en `/api/auth/**` (registro y login)

### Endpoints Protegidos
- POST, PUT, DELETE requieren autenticación con token JWT válido
- `/api/admin/**` requiere rol ADMIN


<br>

## Autores 👨‍💻👩‍💻

- *Grupo 4*

📌 Este proyecto fue desarrollado como **Trabajo Práctico Integrador** para la materia **Aplicaciones Interactivas**, dictada en la **Universidad Argentina de la Empresa (UADE)** durante segundo semestre del año 2025.
