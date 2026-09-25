# Registro de expedientes (Java)

Aplicación de escritorio hecha con Java Swing y NetBeans para registrar, mover, consultar y finalizar expedientes. Usa listas enlazadas simple, doble y circular como ejercicio de estructuras de datos.

## Requisitos y ejecución

- JDK 21 y NetBeans (proyecto Ant, sin dependencias externas).
- Define las variables de entorno `TRAMITE_USER` y `TRAMITE_PASSWORD` antes de iniciar NetBeans o ejecutar el JAR. Por ejemplo, en PowerShell:

  ```powershell
  $env:TRAMITE_USER = "usuario-local"
  $env:TRAMITE_PASSWORD = "elige-una-clave"
  ```

- Abre la carpeta del repositorio como proyecto en NetBeans y ejecuta **Run Project**. También puedes usar `ant clean jar` y `java -jar dist/tramite.jar` con Ant y JDK 21 instalados.

## Funcionamiento

Al registrar un expediente se le asigna un ID consecutivo (`EXP-001`, etc.) y aparece en las alertas mientras esté abierto. Registrar una etapa final o usar la opción de finalizar requiere un documento de resultado; después se conserva el historial y ya no se aceptan movimientos ni cierres adicionales.

Los expedientes se guardan **solo en memoria**: se pierden al cerrar la aplicación y los IDs vuelven a empezar. El login usa credenciales locales del entorno para evitar contraseñas publicadas en el código; **no es un sistema de autenticación para producción**. Esta aplicación no cuenta con base de datos, cuentas de usuario ni control de acceso por roles.

## Pruebas de la lógica

Desde la raíz del proyecto, con JDK 21:

```sh
mkdir -p build/test-manual
javac -encoding UTF-8 -d build/test-manual src/tramite/*.java test/tramite/GestorExpedientesTest.java
java -ea -cp build/test-manual tramite.GestorExpedientesTest
```

El directorio `src/tramite` conserva los formularios `.form` junto a sus clases, para que NetBeans pueda seguir editándolos. `nbproject` y `build.xml` contienen la configuración del proyecto Ant; `build/`, `dist/` y `nbproject/private/` son archivos locales generados y están excluidos de Git.
