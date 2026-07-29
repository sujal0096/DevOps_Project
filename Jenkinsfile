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
                    echo "Pushing Versioned Image..."
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
                    docker start postgres || true

                    docker pull ${DOCKER_USERNAME}/${IMAGE_NAME}:latest

                    docker stop employee-management || true
                    docker rm employee-management || true

                    docker run -d \
                    --name employee-management \
                    --network employee-network \
                    -p 8080:8080 \
                    ${DOCKER_USERNAME}/${IMAGE_NAME}:latest

                    echo Deployment Successful
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
            echo 'CI/CD Pipeline Failed!'
        }
    }
}