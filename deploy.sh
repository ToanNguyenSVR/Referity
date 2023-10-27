#!/bin/bash

APP_NAME="referity"
REMOTE_USER="root"
REMOTE_HOST="178.128.84.51"

echo "Building app..."
./mvnw clean package

echo "Deploy files to server..."
scp -r -i ~/.ssh/referity target/referity.jar root@178.128.84.51:/var/www/referity-backend/

echo "Restarting the application on the remote server..."
# Check if the application is running
ssh -i ~/.ssh/referity $REMOTE_USER@$REMOTE_HOST "systemctl is-active $APP_NAME.service"

if [ $? -eq 0 ]; then
    # Application is running, restart it
    restart_output=$(ssh -i ~/.ssh/referity $REMOTE_USER@$REMOTE_HOST "systemctl restart $APP_NAME" 2>&1)
    if [ $? -eq 0 ]; then
        # Wait for a short while to allow the application to start
        # Check if the application is still running
        status_output=$(ssh -i ~/.ssh/referity $REMOTE_USER@$REMOTE_HOST "systemctl is-active $APP_NAME.service")
        if [ $? -eq 0 ]; then
            echo "Application restarted successfully!"
        else
            echo "Failed to restart the application."
            echo "Status output: $status_output"
        fi
    else
        echo "Failed to restart the application."
        echo "Restart output: $restart_output"
    fi
else
    # Application is not running, start it
    start_output=$(ssh -i ~/.ssh/referity $REMOTE_USER@$REMOTE_HOST "systemctl start $APP_NAME" 2>&1)
    if [ $? -eq 0 ]; then
        # Wait for a short while to allow the application to start
#        sleep 10
        # Check if the application is running
        status_output=$(ssh -i ~/.ssh/referity $REMOTE_USER@$REMOTE_HOST "systemctl is-active $APP_NAME.service")
        if [ $? -eq 0 ]; then
            echo "Application started successfully!"
        else
            echo "Failed to start the application."
            echo "Status output: $status_output"
        fi
    else
        echo "Failed to start the application."
        echo "Start output: $start_output"
    fi
fi

echo "Deployment complete!"