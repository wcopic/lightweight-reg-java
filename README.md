# Trámite · Registro de expedientes

Aplicación de escritorio en Java Swing para registrar, consultar, priorizar, mover y finalizar expedientes. Está pensada para organizaciones de cualquier tipo y usa listas enlazadas simple, doble y circular como ejercicio de estructuras de datos.

## Ejecutar

Requiere **JDK 21**. El proyecto usa Ant (compatible con NetBeans), sin dependencias externas ni configuración de cuentas.

Abre esta carpeta como proyecto en NetBeans y ejecuta **Run Project**, o usa:

```sh
ant clean jar
java -jar dist/tramite.jar
```

La clase principal es `tramite.MainMenu` y abre directamente el panel.

## Uso

- El panel muestra todos los expedientes, sus estados y un detalle con el historial. Busca por ID, asunto, nombre o identificación; filtra por estado y prioridad.
- Al crear un expediente elige **Baja, Media, Alta o Muy alta**. Los abiertos aparecen primero, ordenados por prioridad descendente y, en caso de empate, por antigüedad. El panel cuenta los abiertos de prioridad alta o muy alta.
- Registra movimientos mediante etapas genéricas o finaliza el expediente indicando un documento de resultado. Una etapa final también requiere ese documento. Un expediente finalizado conserva su historial y ya no admite cambios.
- Las referencias de documentos son **texto**, no archivos adjuntos. No se copia ni se almacena ningún archivo.

Los datos existen **solo durante la sesión**: al cerrar la aplicación se pierden y los IDs vuelven a empezar. No hay base de datos, cuentas de usuario ni control de acceso por roles. No introduzcas datos reales o sensibles en este prototipo.

## Pruebas

```sh
mkdir -p build/test-manual
javac -encoding UTF-8 -d build/test-manual src/tramite/*.java test/tramite/GestorExpedientesTest.java
java -ea -cp build/test-manual tramite.GestorExpedientesTest
```

La interfaz se construye con Swing directamente en las clases Java. `nbproject` y `build.xml` configuran el proyecto Ant; `build/`, `dist/` y `nbproject/private/` se generan localmente y están ignorados por Git.
