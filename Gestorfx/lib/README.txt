Coloca aquí los archivos JAR del SDK de JavaFX (por ejemplo javafx-base.jar, javafx-controls.jar, javafx-fxml.jar, javafx-graphics.jar, etc.).

Después de copiar los JARs en esta carpeta, la configuración de depuración directa en VS Code usará esta ruta como --module-path.

Ejemplo (Windows) de VM args que se usan en `launch.json`:
--module-path "${workspaceFolder}/Gestorfx/lib" --add-modules=javafx.controls,javafx.fxml

Nota: si tu JavaFX SDK contiene subcarpetas, copia sólo los JAR dentro de `Gestorfx/lib` o ajusta la ruta en `launch.json` según corresponda.