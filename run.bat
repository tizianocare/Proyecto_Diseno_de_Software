@echo off
setlocal

if not exist out mkdir out
javac -encoding UTF-8 -d out src\subasta\*.java
if errorlevel 1 exit /b %errorlevel%

java -Dfile.encoding=UTF-8 -cp out subasta.Main
