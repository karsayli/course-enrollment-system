@echo off
cd /d "%~dp0"
echo ========================================
echo RESTART - With Fixes
echo ========================================
echo.
echo 1. Stopping old containers...
docker-compose down

echo.
echo 2. Cleaning old containers (optional)...
docker-compose rm -f

echo.
echo 3. Rebuilding and starting the project...
echo    (This may take 3-5 minutes)
echo.
echo Do not close this terminal!
echo Wait for "Started CourseenrollmentsystemApplication" message.
echo.

docker-compose up --build

pause
