# Sistema de Gestión de Inventario

Stockify es una aplicación web para administrar un inventario medianamente completo, cuenta con administración para: cartegorías, proveedores, productos y movimientos de stock, 
con seguridad por usuarios y roles, y un tablero de control con indicadores y gráficos.

- **Backend:** API REST con Spring Boot.
- **Frontend:** Aplicación de página con Angular.
- **Base de datos:** PostgreSQL, versiones de esquemas con Liquibase.
- **Despliegue:** Docker Compose - Backend, Frontend, Base de datos y Jaeger.

Desarrollador: [breynerbd - Breyner Benitez](https://github.com/breynerbd)

---

## 1. Tecnologías

| Capa | Tenología |
|---|---|
| Backend | Java 25, Spring Boot 4.1.2, Spring Security / JWT, Spring Data JPA / Hibernate |
| Base de datos | PostgreSQL 17, Liquibase |
| Frontend | Angular 22, TypeScript 5.9.3, Chart.js 4.5 |
| Contenedores | Docker, Docker Compose, Nginx |
| Telemetría | Spring Boot Actuator, OpenTelemetry, Jaeger 2.5.0 |
| Pruebas | JUnit 5 y Mockito |

---

# 2. Como ejecutarlo con Docker - Instalación
 
No necesitas instalar Java, Node ni PostgreSQL, solo Docker.
 
### Requisitos
 
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instálalo y ábrelo.
- Git.

### Paso a paso

**1. Clonar el repositorio**
```bash
git clone https://github.com/breynerbd/gestion-inventario.git
cd gestion-inventario
```

**2. Crear el archivo `.env`**

Copia el archivo de ejemplo:

```bash
cp. .env.example .env
```

Ábrelo y cambia los valores con `replace_with_...`:

```env
POSTGRES_DB=inventory_management
POSTGRES_USER=inventory_user
POSTGRES_PASSWORD=pon_aqui_una_clave
 
DB_USERNAME=inventory_user
DB_PASSWORD=pon_aqui_la_misma_clave
 
JWT_SECRET=pon_aqui_un_texto_largo_y_aleatorio
```

Importante:
- `POSTGRES_USER` y `DB_USERNAME` tienen que ser **iguales**.
- `POSTGRES_PASSWORD` y `DB_PASSWORD` tienen que ser **iguales**.
- `JWT_SECRET` tiene que tener **al menos 32 caracteres**. Se puede generar uno con:

```bash
  openssl rand -base64 48
```
 
**3. Construir y levantar todo**
 
```bash
docker compose up --build -d
```
 
La primera vez tarda varios minutos porque descarga imágenes y dependencias.
 
**4. Esperar a que el backend se construya**
 
```bash
docker compose ps
```
 
Espera hasta que `inventory-backend` aparezca como `healthy`. Liquibase crea las tablas y carga los datos iniciales automaticamente.
 
**5. Abrir la aplicación**
 
Entra a **http://localhost** e inicia sesión con el usuario inicial.
 
### Servicios que se levantan
 
| Servicio | Contenedor | Acceso | Descripción |
|---|---|---|---|
| `frontend` | `inventory-frontend` | http://localhost | Angular. Redirige `/api/` al backend. |
| `backend` | `inventory-backend` | Solo dentro de Docker | API REST en el puerto 8080. Se usa desde el frontend. |
| `database` | `inventory-database` | Solo dentro de Docker | PostgreSQL 17. Los datos se guardan en el volumen `postgres_data`. |
| `jaeger` | `inventory-jaeger` | http://localhost:16686 | Visualizador de trazas. |
 
### Comandos útiles usados
 
| Actividad | Comando |
|---|---|
| Ver el estado de los servicios | `docker compose ps` |
| Ver los logs del backend | `docker compose logs -f backend` |
| Apagar todo | `docker compose down` |
| Apagar todo y borrar la base de datos | `docker compose down -v` |
| Reconstruir después de cambiar código | `docker compose up --build -d` |
| Comprobar la salud del backend | `docker compose exec backend curl -s http://localhost:8080/actuator/health` |
 
---
 
## 3. Cómo ejecutarlo sin Docker - Local
 
### Requisitos
 
- JDK 25
- Node.js 22 y npm
- PostgreSQL 17
- Google Chrome - Frontend
 
### Paso a paso
 
**1. Crear la base de datos**
 
```sql
CREATE DATABASE inventory_management;
```
 
**2. Definir las variables de entorno**
 
El backend lee estas tres variables y no tienen valor por defecto.
 
Git Bash:
 
```bash
export DB_USERNAME=postgres
export DB_PASSWORD=tu_contrasena
export JWT_SECRET=un_texto_aleatorio_de_al_menos_32_caracteres
```
 
PowerShell:
 
```powershell
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="tu_contrasena"
$env:JWT_SECRET="un_texto_aleatorio_de_al_menos_32_caracteres"
```
 
**3. Levantar el backend**
 
```bash
cd backend
./mvnw spring-boot:run
```
 
El backend queda en **http://localhost:8080**. Al arrancar, Liquibase crea las tablas y carga los datos iniciales.
 
> Puede aparecer un aviso por las trazas. No afecta en nada. Si quieres evitarlo, levanta solo Jaeger con `docker compose up -d jaeger` si tienes instalado Docker Deesktop.
 
**4. Crear el proxy del frontend**
 
Los servicios de Angular llaman a rutas como `/api/products`. En Docker, Nginx las redirige al backend. En desarrollo local necesitas un proxy. Crea un archivo `frontend/proxy.conf.json`:
 
```json
{
  "/api": {
    "target": "http://localhost:8080",
    "secure": false
  }
}
```
 
**5. Levantar el frontend**
 
```bash
cd frontend
npm install
npx ng serve --proxy-config proxy.conf.json
```
 
Abre **http://localhost:4200**.
 
---
 
## 4. Variables de entorno
 
| Variable | Uso | Descripción |
|---|---|---|
| `POSTGRES_DB` | Docker - Base de datos | Nombre de la base de datos. |
| `POSTGRES_USER` | Docker - Base de datos | Usuario de PostgreSQL. |
| `POSTGRES_PASSWORD` | Docker - Base de datos | Contraseña de PostgreSQL. |
| `DB_USERNAME` | Backend | Usuario con el que el backend se conecta. Igual a `POSTGRES_USER`. |
| `DB_PASSWORD` | Backend | Contraseña con la que el backend se conecta. Igual a `POSTGRES_PASSWORD`. |
| `JWT_SECRET` | Backend | Clave para firmar los tokens JWT - Mínimo 32 caracteres. |
 
Los tiempos de vida de los tokens están en `backend/src/main/resources/application.properties`: el token de acceso dura 15 minutos y el renovado 7 días.
 
---
 
## 5. Usuario inicial y roles

Liquibase al ejecutar todo crea un usuario administrador:
 
| Campo | Valor |
|---|---|
| Usuario | `admin` |
| Contraseña | `Admin#2026` |
 
### Roles y permisos
 
| Módulo | ADMINISTRADOR | SUPERVISOR | OPERADOR |
|---|---|---|---|
| Usuarios | Todo | Sin acceso | Sin acceso |
| Roles y permisos | Todo | Sin acceso | Sin acceso |
| Categorías | Todo | Todo | Solo consultar |
| Proveedores | Todo | Todo | Solo consultar |
| Productos | Todo | Todo | Solo consultar |
| Movimientos de stock | Todo | Solo consultar | Consultar y registrar |
| Tablero de control | Consultar | Consultar | Consultar |
 
"Todo" significa crear, consultar, editar y cambiar estado. El editar y el cambio de estado de movimientos son solo del ADMINISTRADOR.
 
Nada se elimina. Los registros se **inactivan** cambiando su estado.
 
---
 
## 6. Estructura del proyecto

```text
gestion-inventario/
|-- backend/
|   |-- Dockerfile
|   |-- pom.xml
|   |-- src/
|       |-- main/java/com/inventorymanagement/backend/
|       |   |-- domain/           Entidades y excepciones
|       |   |-- application/      Servicios, DTOs y mappers
|       |   |-- infrastructure/   Repositorios y seguridad
|       |   |-- presentation/     Controladores y manejo de errores
|       |-- main/resources/
|       |   |-- application.properties
|       |   |-- db/changelog/     Liquibase
|       |-- test/                 Pruebas
|-- frontend/
|   |-- Dockerfile
|   |-- nginx.conf
|   |-- src/app/
|       |-- core/                 JWT y autenticación
|       |-- shared/               Componentes reutilizables
|       |-- features/             Un módulo por pantalla - entidad
|-- docker-compose.yml
|-- .env.example
|-- README.md
```


El backend sigue una arquitectura por capas. Cada capa solo depende de las de adentro.
 
```text
presentation  ->  application  ->  domain
infrastructure ----------------->  domain
```
 
Los controladores nunca devuelven entidades, usan DTOs, y un mapper convierte entre ellos.
 
---

## 7. Modelo de datos
 
El esquema se crea con Liquibase `backend/src/main/resources/db/changelog/`:
 
| Archivo | Contenido |
|---|---|
| `001_categories.yaml` | Tabla de categorías |
| `002_suppliers.yaml` | Tabla de proveedores |
| `003_roles.yaml` | Tabla de roles |
| `004_permissions.yaml` | Tabla de permisos |
| `005_users.yaml` | Tabla de usuarios |
| `006_products.yaml` | Tabla de productos |
| `007_stock_movements.yaml` | Tabla de movimientos de stock |
| `008_role_permissions.yaml` | Tabla intermedia entre roles y permisos |
| `009_initial_role_permission_data.yaml` | Roles, permisos y usuario administrador iniciales |
| `010_binnacle.yaml` | Bitácora de inicio de sesión |
| `011_entity_dates.yaml` | Fechas de creación y modificación |
| `012_stock_movement_modification_date.yaml` | Fecha de modificación en movimientos |

## 8. API REST
 
Todas las rutas empiezan con `/api`. Solo `/api/auth/**` no, todas usan el encabezado `Authorization: Bearer <token>`.
 
### Autenticación pública
 
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/auth/login` | Inicia sesión y devuelve el tokens. |
| POST | `/api/auth/refresh` | Renueva el token de acceso con el token de renovación. |
| POST | `/api/auth/register` | Crea una cuenta nueva como OPERADOR. |
 
### Módulos
 
Cada módulo tiene las mismas cinco operaciones:
 
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/<módulo>` | Listar. |
| GET | `/api/<módulo>/{id}` | Consultar un registro. |
| POST | `/api/<módulo>` | Crear un registro. |
| PUT | `/api/<módulo>/{id}` | Actualizar un registro. |
| PATCH | `/api/<módulo>/{id}/status` | Activar o inactivar un registro. |
 
| Módulo | Ruta base | Notas |
|---|---|---|
| Usuarios | `/api/users` | |
| Roles | `/api/roles` | |
| Permisos | `/api/permissions` | |
| Categorías | `/api/categories` | |
| Proveedores | `/api/suppliers` | |
| Productos | `/api/products` | Listado con filtros por código, nombre, categoría, proveedor y estado. |
| Movimientos | `/api/movements` | Listado con filtros por producto, tipo, fechas y usuario. |
 
Los listados aceptan los parámetros de paginación `page` y `size`.
 
### Rutas adicionales
 
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/roles/{roleId}/permissions` | Permisos asignados a un rol. |
| PUT | `/api/roles/{roleId}/permissions` | Reemplaza el conjunto completo de permisos de un rol. |
| GET | `/api/products/low-stock` | Productos con stock igual o menor al mínimo. |
| GET | `/api/products/products-category` | Cantidad de productos por categoría. |
| GET | `/api/products/active-products` | Total de productos activos. |
| GET | `/api/products/inventory-value` | Valor estimado del inventario. |
| GET | `/api/products/inventory-investment` | Inversión total del inventario. |
| GET | `/api/suppliers/active-suppliers` | Total de proveedores activos. |
| GET | `/api/movements/summary` | Entradas y salidas por periodo. |
| GET | `/api/movements/movements-value` | Valor de los movimientos. |
| GET | `/api/movements/products-movement` | Movimientos por producto. |
 
---
 
## 9. Seguridad
 
- **Autenticación con JWT:** cuando se inicia sesión se entrega un token de acceso que dura 15 minutos y uno de renovación que dura 7 días. El frontend renueva el token automáticamente.
- **Contraseñas** se guardan con hash BCrypt, nunca en texto plano.
- **Bloqueo de cuenta:** después de 5 intentos fallidos consecutivos la cuenta pasa a `BLOQUEADO`. Un administrador la tiene que activar desde el módulo de usuarios.
- **Bitácora:** cada inicio de sesión, exitoso o fallido, queda registrado con usuario, fecha, hora e IP.
- **Autorización por rol:** el backend valida el rol en cada ruta. En el frontend, las rutas ocultan lo que el rol no puede usar.
- **Secretos:** la clave JWT y las credenciales de la base de datos vienen de variables de entorno, no del código.
---
 
## 10. Pruebas
 
### Backend
 
```bash
cd backend
./mvnw test
```
 
El backend tiene 13 clases de prueba con 137 tests, listados, creación, actualización, validaciones de movimientos stock y productos, autenticación y JWT. Con JUnit 5 y Mockito sin conectarse a la base de datos.
 
### Frontend
 
No tiene pruebas unitarias.

---
 
## 11. Monitoreo con Jaeger
 
El backend tiene trazas con OpenTelemetry a Jaeger. Con contenedor de Docker levantado.
 
1. Abre **http://localhost:16686**.
2. En *Service* elige `inventory_management`.
3. En *Find Traces* para ver las peticiones y cuánto tardó cada una.
El backend también expone los puntos de Actuator `health`, `info` y `metrics`.
 
---
 
## 12. Problemas frecuentes - experiencia propia
 
| Problema | Causa probable y solución |
|---|---|
| `docker compose up` falla porque faltan variables | No existe el archivo `.env`. El copiado de `.env.example`. |
| El backend nunca pasa a `healthy` | Revisa `docker compose logs backend`. Casi siempre es una variable vacía o una contraseña distinta entre `POSTGRES_PASSWORD` y `DB_PASSWORD`. |
| Cambié la contraseña de la base de datos en `.env` y ya no conecta | PostgreSQL conserva la contraseña de la primera vez en el volumen. Ejecuta `docker compose down -v` y vuelve a levantar. |
| El puerto 80 está ocupado | En `docker-compose.yml`, cambia `"80:80"` por `"8081:80"` y entra a http://localhost:8081. |
| La cuenta aparece como bloqueada | Superó 5 intentos fallidos. Un administrador debe reactivarla desde Usuarios. |