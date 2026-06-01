-- ============================================================
--  TIPO_PRACTICA — Script completo para Oracle
--  Ejecutar en el mismo schema donde está el resto de tablas
--  (usuario PRACTICAS u otro según tu conexión)
-- ============================================================

-- ── 1. SECUENCIA ─────────────────────────────────────────────
CREATE SEQUENCE SEQ_TIPO_PRACTICA
    MINVALUE 1
    MAXVALUE 999999999999999999999999999
    INCREMENT BY 1
    START WITH 1
    NOCACHE
    NOORDER
    NOCYCLE;


-- ── 2. TABLA ─────────────────────────────────────────────────
CREATE TABLE TIPO_PRACTICA (
    ID_TIPO         NUMBER          NOT NULL,
    NOMBRE          VARCHAR2(100)   NOT NULL,
    DESCRIPCION     VARCHAR2(300),
    ACTIVO          VARCHAR2(10)    DEFAULT 'SI'     NOT NULL,
    FECHA_CREACION  DATE            DEFAULT SYSDATE  NOT NULL,
    CONSTRAINT PK_TIPO_PRACTICA     PRIMARY KEY (ID_TIPO),
    CONSTRAINT UQ_TIPO_NOMBRE       UNIQUE (NOMBRE),
    CONSTRAINT CHK_TIPO_ACTIVO      CHECK (ACTIVO IN ('SI','NO'))
);

COMMENT ON TABLE  TIPO_PRACTICA             IS 'Catálogo de tipos de práctica del programa';
COMMENT ON COLUMN TIPO_PRACTICA.ID_TIPO     IS 'Clave primaria generada por SEQ_TIPO_PRACTICA';
COMMENT ON COLUMN TIPO_PRACTICA.NOMBRE      IS 'Nombre del tipo de práctica (único)';
COMMENT ON COLUMN TIPO_PRACTICA.DESCRIPCION IS 'Descripción opcional del tipo';
COMMENT ON COLUMN TIPO_PRACTICA.ACTIVO      IS 'SI = vigente, NO = desactivado';
COMMENT ON COLUMN TIPO_PRACTICA.FECHA_CREACION IS 'Fecha en que se registró el tipo';


-- ── 3. TRIGGER — auto-ID desde la secuencia ──────────────────
CREATE OR REPLACE TRIGGER TRG_TIPO_PRACTICA
    BEFORE INSERT ON TIPO_PRACTICA
    FOR EACH ROW
BEGIN
    IF :NEW.ID_TIPO IS NULL THEN
        SELECT SEQ_TIPO_PRACTICA.NEXTVAL
        INTO   :NEW.ID_TIPO
        FROM   DUAL;
    END IF;
END;
/
ALTER TRIGGER TRG_TIPO_PRACTICA ENABLE;


-- ── 4. ÍNDICE — búsqueda rápida por nombre ───────────────────
CREATE INDEX IDX_TIPO_PRACTICA_NOMBRE
    ON TIPO_PRACTICA (UPPER(NOMBRE));


-- ── 5. DATOS INICIALES ────────────────────────────────────────
INSERT INTO TIPO_PRACTICA (NOMBRE, DESCRIPCION)
    VALUES ('Docente',
            'Práctica orientada a actividades de enseñanza y apoyo académico');
INSERT INTO TIPO_PRACTICA (NOMBRE, DESCRIPCION)
    VALUES ('Comunitaria',
            'Práctica de servicio y proyección a la comunidad');
INSERT INTO TIPO_PRACTICA (NOMBRE, DESCRIPCION)
    VALUES ('Investigación',
            'Práctica vinculada a proyectos de investigación institucional');
INSERT INTO TIPO_PRACTICA (NOMBRE, DESCRIPCION)
    VALUES ('Empresarial',
            'Práctica en empresa privada o pública del sector productivo');
INSERT INTO TIPO_PRACTICA (NOMBRE, DESCRIPCION)
    VALUES ('Social',
            'Práctica con enfoque de responsabilidad social y voluntariado');
COMMIT;


-- ── 6. VISTA — solo activos (para los ComboBox de la app) ────
CREATE OR REPLACE VIEW V_TIPOS_ACTIVOS AS
    SELECT ID_TIPO,
           NOMBRE,
           DESCRIPCION,
           FECHA_CREACION
    FROM   TIPO_PRACTICA
    WHERE  ACTIVO = 'SI'
    ORDER BY NOMBRE;

COMMENT ON TABLE V_TIPOS_ACTIVOS IS
    'Vista de tipos de práctica activos — usada por la aplicación para los ComboBox';


-- ── 7. RELACIÓN con PRACTICA (FK opcional, añadir si se desea) ─
-- Si quieres que la columna TIPO_PRACTICA de la tabla PRACTICA
-- apunte a esta tabla, ejecuta también:
--
-- ALTER TABLE PRACTICA
--     ADD CONSTRAINT FK_PRACTICA_TIPO
--     FOREIGN KEY (TIPO_PRACTICA)
--     REFERENCES TIPO_PRACTICA (NOMBRE)
--     ON DELETE SET NULL;
--
-- NOTA: esto requiere que los valores existentes en PRACTICA.TIPO_PRACTICA
--       coincidan exactamente con los NOMBRE de esta tabla.
--       Descomenta solo cuando la migración de datos esté lista.

-- ── FIN DEL SCRIPT ────────────────────────────────────────────
