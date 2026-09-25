# Trámite — Case Tracking for Any Organization

Trámite is a lightweight Java Swing desktop application for registering, finding, prioritizing, tracking, and closing cases or requests. Its labels and workflow are intentionally organization-neutral: a business, nonprofit, public office, or other team can use it as a starting point for its own case-tracking process.

## Origin and credits

**Created by Renato Rodríguez Oshiro (`@wcopic`) for the Data Structures I (*Estructuras de Datos 1*) course.** I conceived and originally built the application, including its custom singly, doubly, and circular linked lists.

**AI assistance:** After the original implementation, I used ChatGPT to review and clean up the code, correct parts of the application logic, and redesign the Swing interface. The original project and academic work are mine.

## What it does

- Displays cases in a searchable list with status and priority filters. Search by case ID, subject, requester name, or identification number, then select a case to see its details and movement history.
- Offers four selectable priority levels: **Low, Medium, High, and Very High** (shown in Spanish in the application). Open cases appear first, ordered by priority and then by creation time. The dashboard highlights open high-priority cases.
- Records movements through general-purpose stages and closes a case with a result-document reference. Closed cases retain their history and cannot be changed again.
- Assigns consecutive IDs such as `EXP-001`. Document references are text entries; the application does not upload or store files.

## Run it

Requires **JDK 21**. The project uses Ant and can be opened in NetBeans; it has no external dependencies or account setup.

Open the repository as a project in NetBeans and select **Run Project**, or run:

```sh
ant clean jar
java -jar dist/tramite.jar
```

The entry point is `tramite.MainMenu`, which opens the dashboard directly.

## Current scope

This is an academic prototype that any organization can run and adapt, **not a production deployment**. Cases are held only in memory: closing the application clears them and resets the ID counter. There is no database, user authentication, role-based access control, or file storage. Avoid entering real sensitive records until those capabilities are implemented.

## Run the regression checks

```sh
mkdir -p build/test-manual
javac -encoding UTF-8 -d build/test-manual src/tramite/*.java test/tramite/GestorExpedientesTest.java
java -ea -cp build/test-manual tramite.GestorExpedientesTest
```

The Swing interface is implemented directly in Java. `build.xml` and `nbproject/` contain the Ant/NetBeans project configuration; generated output and local settings are excluded from Git.
