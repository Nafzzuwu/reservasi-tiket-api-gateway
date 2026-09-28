@echo off
title Start All Services
setlocal

set "SCRIPT_DIR=%~dp0"

REM Deteksi atau gunakan JDK 21
if "%JAVA_HOME%"=="" (
    if exist "C:\Users\LENOVO\.vscode\extensions\redhat.java-1.56.0-win32-x64\jre\21.0.12.1-win32-x86_64" (
        set "JAVA_HOME=C:\Users\LENOVO\.vscode\extensions\redhat.java-1.56.0-win32-x64\jre\21.0.12.1-win32-x86_64"
    )
)

echo ========================================================
echo Memulai Semua Microservices Reservasi Tiket...
echo JAVA_HOME: %JAVA_HOME%
echo ========================================================

echo Menjalankan User Service (Port 8101)...
start "User Service [Port 8101]" cmd /k "cd /d "%SCRIPT_DIR%user-service" && title User Service (8101) && .\mvnw.cmd spring-boot:run"
timeout /t 3 /nobreak >nul

echo Menjalankan Schedule Service (Port 8102)...
start "Schedule Service [Port 8102]" cmd /k "cd /d "%SCRIPT_DIR%schedule-service" && title Schedule Service (8102) && .\mvnw.cmd spring-boot:run"
timeout /t 3 /nobreak >nul

echo Menjalankan Booking Service (Port 8103)...
start "Booking Service [Port 8103]" cmd /k "cd /d "%SCRIPT_DIR%booking-service" && title Booking Service (8103) && .\mvnw.cmd spring-boot:run"
timeout /t 3 /nobreak >nul

echo Menjalankan Seat Service (Port 8104)...
start "Seat Service [Port 8104]" cmd /k "cd /d "%SCRIPT_DIR%seat-service" && title Seat Service (8104) && .\mvnw.cmd spring-boot:run"
timeout /t 3 /nobreak >nul

echo Menjalankan Payment Service (Port 8105)...
start "Payment Service [Port 8105]" cmd /k "cd /d "%SCRIPT_DIR%payment-service" && title Payment Service (8105) && .\mvnw.cmd spring-boot:run"
timeout /t 3 /nobreak >nul

echo Menjalankan Notification Service (Port 8106)...
start "Notification Service [Port 8106]" cmd /k "cd /d "%SCRIPT_DIR%notification-service" && title Notification Service (8106) && .\mvnw.cmd spring-boot:run"
timeout /t 4 /nobreak >nul

echo Menjalankan API Gateway (Port 8000)...
start "API Gateway [Port 8000]" cmd /k "cd /d "%SCRIPT_DIR%gateaway" && title API Gateway (8000) && .\mvnw.cmd spring-boot:run"

echo ========================================================
echo Semua service telah diluncurkan di jendela terminal masing-masing!
echo API Gateway: http://localhost:8000
echo ========================================================
pause
