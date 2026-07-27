pipeline {

    agent any

    environment {
        DOCKER_USERNAME = 'sujal5210'
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

                docker build -t $DOCKER_USERNAME/employee-management:latest .
                '''
            }
        }

        stage('Push Image to Docker Hub') {
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

                    echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin

                    echo "========================================"
                    echo "Pushing Docker Image..."
                    echo "========================================"

                    docker push $DOCKER_USERNAME/employee-management:latest
                    '''
                }
            }
        }

        stage('Deploy to Web02') {

            steps {

                sh '''
                echo "========================================"
                echo "Connecting to Docker Server (web02)..."
                echo "========================================"

                ssh -o StrictHostKeyChecking=no vagrant@192.168.56.12 << 'EOF'

                echo "Starting PostgreSQL..."

                docker start postgres || docker run -d \
                  --name postgres \
                  --network employee-network \
                  -e POSTGRES_DB=employee_db \
                  -e POSTGRES_USER=postgres \
                  -e POSTGRES_PASSWORD=postgres \
                  postgres:17

                echo "Pulling latest application image..."

                docker pull sujal5210/employee-management:latest

                echo "Stopping old container..."

                docker stop employee-management || true

                docker rm employee-management || true

                echo "Running latest container..."

                docker run -d \
                  --name employee-management \
                  --network employee-network \
                  -p 8080:8080 \
                  sujal5210/employee-management:latest

                EOF
                '''
            }
        }

    }

    post {

        success {
            echo "Application deployed successfully."
        }

        failure {
            echo "Pipeline failed."
        }

        always {
            echo "Pipeline execution completed."
        }
    }
}