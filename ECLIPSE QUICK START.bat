@ECHO OFF
ECHO Preparing Eclipse...
call gradlew eclipse
ECHO Preparing Eclipse Runs...
call gradlew genEclipseRuns
pause