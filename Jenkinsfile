pipeline {
    agent any

    environment {
        DOCKER_USERNAME = 'sujal5210'
        IMAGE_NAME = 'employee-management'
        DOCKER_SERVER = 'vagrant@192.168.56.12'
    }

    stages {

        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Build Maven Project') {
            steps {
                sh '''
                    echo "========================================"
                    echo "Building Maven Project..."
                    echo "========================================"

                    mvn clean package -DskipTests
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    echo "========================================"
                    echo "Building Docker Image..."
                    echo "========================================"

                    docker build \
                    -t $DOCKER_USERNAME/$IMAGE_NAME:$BUILD_NUMBER \
                    -t $DOCKER_USERNAME/$IMAGE_NAME:latest .
                '''
            }
        }

        stage('Login & Push to Docker Hub') {
            steps {

                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub',
                    usernameVariable: 'DOCKER_USERNAME',
                    passwordVariable: 'DOCKER_PASSWORD'
                )]) {

                    sh '''
                        echo "========================================"
                        echo "Logging into Docker Hub..."
                        echo "========================================"

                        echo "$DOCKER_PASSWORD" | docker login \
                        -u "$DOCKER_USERNAME" \
                        --password-stdin

                        echo "========================================"
                        echo "Pushing Build Image..."
                        echo "========================================"

                        docker push $DOCKER_USERNAME/$IMAGE_NAME:$BUILD_NUMBER

                        echo "========================================"
                        echo "Pushing Latest Image..."
                        echo "========================================"

                        docker push $DOCKER_USERNAME/$IMAGE_NAME:latest
                    '''
                }
            }
        }

        stage('Deploy to Docker Server') {
        steps {
        sh """
        ssh -o StrictHostKeyChecking=no ${DOCKER_SERVER} '
            cd ~/DevOps_Project

            echo "========================================"
            echo "Pulling latest source code..."
            echo "========================================"
            git pull origin main

            echo "========================================"
            echo "Pulling latest Docker image..."
            echo "========================================"
            docker compose pull

            echo "========================================"
            echo "Stopping old containers..."
            echo "========================================"
            docker compose down

            echo "========================================"
            echo "Starting updated containers..."
            echo "========================================"
            docker compose up -d

            echo "========================================"
            echo "Deployment Successful!"
            echo "========================================"
        '
        """
            }
        }

    }

    post {

        success {
            echo 'CI/CD Pipeline Completed Successfully!'
        }

        failure {
            echo 'CI/CD Pipeline Failed..!!!'
        }

    }
}