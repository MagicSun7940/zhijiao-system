@echo off
title Zhijiao System Manager
set "ROOT=%~dp0"

:START
echo ================================================
echo        Zhijiao System - Starting Services
echo ================================================
echo.

echo [1/2] Starting Backend (Spring Boot)...
start "Zhijiao Backend" cmd /k "cd /d %ROOT%zhijiao-backend && mvn spring-boot:run"

echo [2/2] Starting Frontend (Vue 3)...
start "Zhijiao Frontend" cmd /k "cd /d %ROOT%zhijiao-frontend && npm install && npm run dev"

echo.
echo Services launched:
echo   Backend  -^>  http://localhost:8080
echo   Frontend -^>  http://localhost:5173
echo.
echo Wait for "Started ZhijiaoApplication" in the backend window,
echo then open http://localhost:5173 in your browser.
echo.

:MENU
echo ------------------------------------------------
echo  [1] Restart All
echo  [2] Stop All
echo  [3] Exit
echo ------------------------------------------------
set /p choice=Select an option (1/2/3):

if "%choice%"=="1" goto RESTART
if "%choice%"=="2" goto STOP
if "%choice%"=="3" goto EXIT
echo Invalid option, please try again.
echo.
goto MENU

:RESTART
echo.
echo Stopping services...
taskkill /fi "WINDOWTITLE eq Zhijiao Backend*" /t /f >nul 2>&1
taskkill /fi "WINDOWTITLE eq Zhijiao Frontend*" /t /f >nul 2>&1
timeout /t 2 /nobreak >nul
echo.
goto START

:STOP
echo.
echo Stopping all services...
taskkill /fi "WINDOWTITLE eq Zhijiao Backend*" /t /f >nul 2>&1
taskkill /fi "WINDOWTITLE eq Zhijiao Frontend*" /t /f >nul 2>&1
echo Services stopped.
echo.
pause
goto EXIT

:EXIT
exit
