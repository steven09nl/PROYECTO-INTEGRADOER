BASE DE DATOS PROYECTO INTEGRADOR

connect practicas/practicas;

TABLAS:

-- USUARIO
CREATE TABLE Usuario (
    id_usuario NUMBER PRIMARY KEY,
    nombre VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) NOT NULL,
    password VARCHAR2(100) NOT NULL,
    rol VARCHAR2(50) NOT NULL,
    estado VARCHAR2(20) NOT NULL
);

-- PRACTICA
CREATE TABLE Practica (
    id_practica NUMBER PRIMARY KEY,
    nombre VARCHAR2(100) NOT NULL,
    tipo_practica VARCHAR2(50),
    horas_reglamentarias NUMBER,
    estado VARCHAR2(20),
    fecha_inicio DATE,
    fecha_fin DATE,
    semestre VARCHAR2(20)
);

-- BITACORA
CREATE TABLE Bitacora (
    id_bitacora NUMBER PRIMARY KEY,
    id_estudiante NUMBER,
    id_practica NUMBER,
    estado VARCHAR2(20),
    modalidad VARCHAR2(50),
    fecha_envio DATE,
    calificacion NUMBER,
    CONSTRAINT fk_bitacora_usuario FOREIGN KEY (id_estudiante) REFERENCES Usuario(id_usuario),
    CONSTRAINT fk_bitacora_practica FOREIGN KEY (id_practica) REFERENCES Practica(id_practica)
);

-- PREGUNTA
CREATE TABLE Pregunta (
    id_pregunta NUMBER PRIMARY KEY,
    id_practica NUMBER,
    enunciado VARCHAR2(255),
    tipo_pregunta VARCHAR2(50),
    obligatoria NUMBER(1),
    orden NUMBER,
    CONSTRAINT fk_pregunta_practica FOREIGN KEY (id_practica) REFERENCES Practica(id_practica)
);

-- RESPUESTA
CREATE TABLE Respuesta (
    id_respuesta NUMBER PRIMARY KEY,
    id_pregunta NUMBER,
    id_bitacora NUMBER,
    texto_respuesta VARCHAR2(255),
    fecha_respuesta DATE,
    retroalimentacion VARCHAR2(255),
    CONSTRAINT fk_resp_preg FOREIGN KEY (id_pregunta) REFERENCES Pregunta(id_pregunta),
    CONSTRAINT fk_resp_bit FOREIGN KEY (id_bitacora) REFERENCES Bitacora(id_bitacora)
);

-- EVIDENCIA
CREATE TABLE Evidencia (
    id_evidencias NUMBER PRIMARY KEY,
    id_bitacora NUMBER,
    url_archivo VARCHAR2(255),
    fecha_carga DATE,
    descripcion VARCHAR2(255),
    CONSTRAINT fk_evidencia_bitacora FOREIGN KEY (id_bitacora) REFERENCES Bitacora(id_bitacora)
);

-- OBSERVACION
CREATE TABLE Observacion (
    id_observacion NUMBER PRIMARY KEY,
    id_bitacora NUMBER,
    id_asesor NUMBER,
    texto VARCHAR2(255),
    fecha DATE,
    CONSTRAINT fk_obs_bit FOREIGN KEY (id_bitacora) REFERENCES Bitacora(id_bitacora),
    CONSTRAINT fk_obs_usuario FOREIGN KEY (id_asesor) REFERENCES Usuario(id_usuario)
);

-- CONTROL HORAS
CREATE TABLE Control_Horas (
    id_registro NUMBER PRIMARY KEY,
    id_bitacora NUMBER,
    fecha DATE,
    hora_entrada DATE,
    hora_salida DATE,
    horas_cumplidas NUMBER,
    CONSTRAINT fk_control_bit FOREIGN KEY (id_bitacora) REFERENCES Bitacora(id_bitacora)
);

-- INFORME
CREATE TABLE Informe (
    id_informe NUMBER PRIMARY KEY,
    id_usuario_gen NUMBER,
    tipo_informe VARCHAR2(50),
    fecha_generacion DATE,
    periodo VARCHAR2(50),
    url_archivo VARCHAR2(255),
    CONSTRAINT fk_inf_usuario FOREIGN KEY (id_usuario_gen) REFERENCES Usuario(id_usuario)
);

SECUENCIAS:

CREATE SEQUENCE seq_usuario START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_practica START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_bitacora START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_pregunta START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_respuesta START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_evidencia START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_observacion START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_control START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_informe START WITH 1 INCREMENT BY 1;

TRIGGERS:

CREATE OR REPLACE TRIGGER trg_usuario
BEFORE INSERT ON Usuario
FOR EACH ROW
BEGIN
    SELECT seq_usuario.NEXTVAL INTO :NEW.id_usuario FROM dual;
END;
/

CREATE OR REPLACE TRIGGER trg_practica
BEFORE INSERT ON Practica
FOR EACH ROW
BEGIN
    SELECT seq_practica.NEXTVAL INTO :NEW.id_practica FROM dual;
END;
/

CREATE OR REPLACE TRIGGER trg_bitacora
BEFORE INSERT ON Bitacora
FOR EACH ROW
BEGIN
    SELECT seq_bitacora.NEXTVAL INTO :NEW.id_bitacora FROM dual;
END;
/

CREATE OR REPLACE TRIGGER trg_pregunta
BEFORE INSERT ON Pregunta
FOR EACH ROW
BEGIN
    SELECT seq_pregunta.NEXTVAL INTO :NEW.id_pregunta FROM dual;
END;
/

CREATE OR REPLACE TRIGGER trg_respuesta
BEFORE INSERT ON Respuesta
FOR EACH ROW
BEGIN
    SELECT seq_respuesta.NEXTVAL INTO :NEW.id_respuesta FROM dual;
END;
/

CREATE OR REPLACE TRIGGER trg_evidencia
BEFORE INSERT ON Evidencia
FOR EACH ROW
BEGIN
    SELECT seq_evidencia.NEXTVAL INTO :NEW.id_evidencias FROM dual;
END;
/

CREATE OR REPLACE TRIGGER trg_observacion
BEFORE INSERT ON Observacion
FOR EACH ROW
BEGIN
    SELECT seq_observacion.NEXTVAL INTO :NEW.id_observacion FROM dual;
END;
/

CREATE OR REPLACE TRIGGER trg_control
BEFORE INSERT ON Control_Horas
FOR EACH ROW
BEGIN
    SELECT seq_control.NEXTVAL INTO :NEW.id_registro FROM dual;
END;
/

CREATE OR REPLACE TRIGGER trg_informe
BEFORE INSERT ON Informe
FOR EACH ROW
BEGIN
    SELECT seq_informe.NEXTVAL INTO :NEW.id_informe FROM dual;
END;
/

ÍNDICES:

CREATE INDEX idx_bitacora_usuario ON Bitacora(id_estudiante);
CREATE INDEX idx_bitacora_practica ON Bitacora(id_practica);
CREATE INDEX idx_respuesta_bitacora ON Respuesta(id_bitacora);
CREATE INDEX idx_evidencia_bitacora ON Evidencia(id_bitacora);