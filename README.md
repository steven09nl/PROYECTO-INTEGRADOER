# Proyecto integrador - prácticas profesionales

Proyecto base en Java + JavaScript + Oracle para los casos de uso y diagramas entregados.

## Tecnologías
- Java 17
- Spring Boot 3
- JDBC para Oracle
- JavaScript/HTML/CSS para la vista
- Oracle Database

## Estructura
- `src/main/java`: lógica de negocio, controladores, repositorios y entidades
- `src/main/resources/static`: vista web
- `sql/schema.sql`: script de referencia con tablas, secuencias e índices

## Ejecución
1. Instala Java 17 y Maven.
2. Abre la carpeta en VS Code.
3. Ejecuta:

```bash
mvn spring-boot:run
```

4. Abre en el navegador:

`http://localhost:8080/`

## Prueba de conexión
La página principal llama al endpoint:

`http://localhost:8080/api/health`

## Credenciales de base de datos
Las credenciales están en `src/main/resources/application.properties`:
- usuario: `practicas`
- contraseña: `practicas`

## Notas
- La base de datos ya incluye tablas, secuencias, triggers e índices según el archivo proporcionado.
- El proyecto usa inserciones compatibles con los triggers; después de insertar recupera el id usando `CURRVAL` de la secuencia.
- Si cambias IP, puerto o SID de Oracle, ajusta `app.oracle.url`.
