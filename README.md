Video de Youtube mostrando el funcionamiento: https://youtu.be/yROtSFMwZZA

```text
articulo-api-mysql/
├── database/schema_y_datos.sql              # Script SQL opcional para phpMyAdmin
├── postman/API_Articulos_MySQL_Completa...  # Colección de endpoints lista para importar
└── src/main/
    ├── java/com/techlab/articulo/
    │   ├── ArticuloApiApplication.java      # Clase principal y precarga inicial de datos
    │   ├── controller/                      # Endpoints REST (/api/articulos, /api/categorias)
    │   ├── service/                         # Lógica de negocio y manejo polimórfico
    │   ├── repository/                      # Interfaces JpaRepository
    │   └── model/                           # Entidades JPA (Articulo, hijas y Categoria)
    └── resources/
        ├── application.properties           # Conexión a MySQL (articulos_db)
        └── static/                          # Frontend web (index.html, styles.css, app.js)
```

## Endpoints de la API REST

| Método | Ruta | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/articulos` | Lista todos los artículos persistidos |
| `GET` | `/api/articulos/{id}` | Consulta un artículo por su código |
| `POST` | `/api/articulos` | Crea un artículo (`GENERAL`, `ELECTRONICO` o `ALIMENTICIO`) |
| `PUT` | `/api/articulos/{id}` | Actualiza los datos de un artículo existente |
| `DELETE` | `/api/articulos/{id}` | Elimina un artículo por su código |
| `GET` / `POST` | `/api/categorias` | Lista o crea categorías |

## Ejecución rápida

1. **Iniciar MySQL:** Abrí **XAMPP Control Panel** y activá **Apache** y **MySQL** (la base `articulos_db` y las tablas se crean automáticamente al iniciar la app, o podés importar `database/schema_y_datos.sql`).
2. **Levantar el servidor:** Desde la raíz del proyecto (`articulo-api-mysql`), ejecutá:
   ```bash
   mvn spring-boot:run
   ```
3. **Probar el sistema:**
   * **Frontend Web:** Ingresá a `http://localhost:8080` en tu navegador.
   * **Postman:** Importá el archivo JSON de la carpeta `postman/` y ejecutá las peticiones.
