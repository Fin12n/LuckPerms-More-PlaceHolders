@echo off
setlocal EnableDelayedExpansion
title LuckPerms-More-Placeholders Build Script

echo ================================
echo  LuckPerms-More-Placeholders
echo  Build Script for Windows
echo  Author: quang1807
echo ================================
echo.

:: Check for Java
echo [INFO] Checking Java version...
where java >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Java not found! Please install Java 17 or higher.
    echo [INFO] Download Java from: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
    pause
    exit /b 1
)
for /f "tokens=3" %%i in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_VERSION=%%i
)
echo [INFO] Found Java version: !JAVA_VERSION!

:: Check for Maven
echo.
echo [INFO] Checking Maven...
where mvn >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Maven not found! Please install Maven 3.6 or higher.
    echo [INFO] Download Maven from: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)
for /f "tokens=3" %%i in ('mvn -version ^| findstr /i "Apache Maven"') do (
    set MAVEN_VERSION=%%i
)
echo [INFO] Found Maven version: !MAVEN_VERSION!

:: Clean previous builds
echo.
echo [INFO] Cleaning previous builds...
mvn clean > build.log 2>&1
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Clean failed! Check build.log for details.
    pause
    exit /b 1
)

:: Compile and package
echo.
echo [INFO] Compiling and packaging...
mvn package >> build.log 2>&1
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Build failed! Check build.log for details.
    pause
    exit /b 1
)

:: Check for JAR file
set JAR_FILE=target\luckperms-more-placeholders-1.0.0.jar
if not exist "!JAR_FILE!" (
    echo [ERROR] JAR file not found at: !JAR_FILE!
    pause
    exit /b 1
)

echo.
echo [SUCCESS] Build completed successfully!
echo [INFO] Plugin JAR file location: !JAR_FILE!
echo.
pause
endlocal