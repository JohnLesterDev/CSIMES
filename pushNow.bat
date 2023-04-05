:: Written by JohnLesterDev :>

@echo off

echo [CSIMES] Building the Jar file...
call gradle build -x test

echo [CSIMES] Commiting changes with message (%1)...
git add .
git commit -m "%~1"

echo Pushing to remote main...
git push -f origin main

echo All processes done!
pause
