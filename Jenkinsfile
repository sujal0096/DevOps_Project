pipeline {

    agent any

    environment {
        IMAGE_NAME = "sujal5210/employee-management:latest"
        SERVER = "vagrant@192.168.56.12"
    }

    stages {

        stage('Checkout Source') {
            steps {
                checkout scm
            }
        }

        stage('Build Maven Project') {
            steps {
                sh '''
                echo "======================================"
                echo "Building Maven Project"
                echo "======================================"

                mvn clean package -DskipTests
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                echo "======================================"
                echo "Building Docker Image"
                echo "======================================"

                docker build -t $IMAGE_NAME .
                '''
            }
        }

        stage('Push Docker Image') {

            steps {

                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {

                    sh '''
                    echo "======================================"
                    echo "Logging into Docker Hub"
                    echo "======================================"

                    echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin

                    echo "======================================"
                    echo "Pushing Docker Image"
                    echo "======================================"

                    docker push $IMAGE_NAME
                    '''
                }
            }
        }

        stage('Deploy to Web02') {

            steps {

                sh '''
                echo "======================================"
                echo "Deploying Application"
                echo "======================================"

                ssh -o StrictHostKeyChecking=no $SERVER "
                docker network create employee-network || true

                docker start postgres || docker run -d \
                  --name postgres \
                  --network employee-network \
                  -e POSTGRES_DB=employee_db \
                  -e POSTGRES_USER=postgres \
                  -e POSTGRES_PASSWORD=postgres \
                  postgres:17

                docker pull $IMAGE_NAME

                docker stop employee-management || true

                docker rm employee-management || true

                docker run -d \
                  --name employee-management \
                  --network employee-network \
                  -p 8080:8080 \
                  $IMAGE_NAME
                "
                '''
            }
        }
    }

    post {

        success {

            echo '''
=========================================
Deployment Successful
=========================================
'''
        }

        failure {

            echo '''
=========================================
Pipeline Failed
=========================================
'''
        }

        always {

            echo '''
=========================================
Pipeline Completed
=========================================
'''
        }
    }
}