@echo off
echo Iniciando Sistema de Alquiler de Vestimentas...
echo.

set JAVAFX_PATH=javafx-lib
set MAIN_JAR=SistemaAlquilerVestimentas-1.0.jar

java --module-path %JAVAFX_PATH% --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base -jar %MAIN_JAR%

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: La aplicacion no pudo iniciarse correctamente.
    echo Verifica que Java 21 este instalado.
    echo.
)

pause