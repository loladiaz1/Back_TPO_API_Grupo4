# 🎮 Clon de Steam - Backend

## Descripción
API REST construída con Java + Spring Boot para un clon de Steam. Expone endpoints de autenticación (registro/login), gestión de usuarios y CRUD de juegos. 
Usa JWT para autenticación y MySQL como base de datos.

## Tabla de contenidos
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Funcionalidades Principales](#funcionalidades-principales)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Requisitos](#requisitos)
- [Como Ejecutar](#como-ejecutar)
  
  
## Tecnologías Utilizadas
- Java 21 + Spring Boot
- Spring Security + JWT
- Spring Data JPA (Hibernate)
- MySQL (base de datos local)
- Maven
- Docker
- Postman

## Funcionalidades Principales
- 🔐 **Autenticación y registro con JWT**
- **Gestión de Usuarios:** registro, login, actualización y eliminacion
- 🎮 **CRUD de Juegos:** creación, lectura, actualización y eliminación
- **Persistencia de datos en MySQL**
- **Contenerización completa con Docker**
- **Endpoints protegidos:** `/auth/register`, `/auth/login`, `/games`, `/users`
- Persistencia en MySQL

---

## Estructura del Proyecto
```
react/
├── src/
│ ├── main/
│ │ ├── java/uade/TPO/react/
│ │ │ ├── config/
│ │ │ ├── controllers/
│ │ │ ├── entity/
│ │ │ ├── repository/
│ │ │ └── service/
│ │ └── resources/
│ │ └── application.properties
| └── test
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── target/ #Archivo jar
```

## Requisitos
Asegurate de tener instalados:
- [x] **Java 21**
- [x] **Maven 3.9+**
- [x] **Docker Desktop, extension de Docker para IDE y Docker Compose**
- [x] **MySQL Workbench** (local o container)
- [x] **Postman**


## Como Ejecutar

### Ejecución con Docker 🐳

1️⃣ Construir la imagen
```bash
docker build -t back_tpo_api_grupo4:latest .
```

2️⃣ Levantar el contenedor
```bash
docker-compose up --build
```
3️⃣ Probar que el backend es accesible en:
```bash
http://localhost:8080
```
Tambien se puede probar con postman enviando alguna request.

### Pruebas con Postman 🟠

Probar los endpoints desde Postman:

1️⃣ Importá la colección (si esta creada) o crea una request.

2️⃣ Asegurate de que el backend esté corriendo en http://localhost:8080.

3️⃣ Para acceder a endpoints protegidos:

   - Recordá incluir el token JWT en los headers.  
   - El token se obtiene al hacer **POST** a `/auth/login` desde Postman.  
   - En Postman, agregá en los headers:
     ````
     Authorization: Bearer <tu_token_jwt>
     ````

#### Endpoints Principales

| Método | Endpoint         | Descripción                          | Autenticación |
|--------|------------------|--------------------------------------|---------------|
| POST   | `/auth/register` | Registro de nuevos usuarios          | ❌ No requerida |
| POST   | `/auth/login`    | Login y generación de token JWT      | ❌ No requerida |
| POST   | `/auth/logout`   | Cerrar sesion                        | ❌ No requerida |
| GET    | `/auth/users`     | Listado de usuarios registrados      | ✅ JWT |
| GET    | `/auth/me`       | Listado de usuarios logeados         | ✅ JWT |
| DELETE | `/auth/users/{id}`| Borrar usuario registrado           | ✅ JWT |
| GET    | `/games`         | Listado de juegos disponibles        | ✅ JWT |
| POST   | `/games`         | Creación de un nuevo juego           | ✅ JWT |
| PUT    | `/games/{id}`    | Actualización de un juego existente  | ✅ JWT |
| DELETE | `/games/{id}`    | Eliminación de un juego              | ✅ JWT |


<br>

## Autores 👨‍💻👩‍💻

- *Grupo 4*

📌 Este proyecto fue desarrollado como **Trabajo Práctico Integrador** para la materia **Aplicaciones Interactivas**, dictada en la **Universidad Argentina de la Empresa (UADE)** durante segundo semestre del año 2025.
