pipeline {
    agent any

    environment {
        DOCKER_USERNAME = 'sujal5210'
        IMAGE_NAME = 'employee-management'
        K8S_SERVER = 'vagrant@192.168.56.12'
    }

    stages {

        stage('Checkout Source Code') {
            steps {
                checkout scm
            }
        }

        stage('Build & Upload JAR to Nexus') {
            steps {
                sh '''
                echo "========================================"
                echo "Building Maven Project..."
                echo "========================================"

                mvn clean deploy \
                -s /var/lib/jenkins/tools/hudson.tasks.Maven_MavenInstallation/MAVEN3.9/conf/settings.xml \
                -DskipTests

                echo "========================================"
                echo "JAR Uploaded to Nexus"
                echo "========================================"
                '''
            }
        }

        stage('SonarQube Analysis') {
        steps {
            withSonarQubeEnv('SonarQube') {
                sh '''
                mvn sonar:sonar \
                -Dsonar.projectKey=employee-management \
                -Dsonar.projectName=employee-management
                '''
                }
            }
        }

        stage('Quality Gate') {
        steps {
            timeout(time: 5, unit: 'MINUTES') {
                waitForQualityGate abortPipeline: true
                }
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

        stage('Trivy Scan') {
        steps {
                sh '''
                echo "========================================"
                echo "Scanning Docker Image using Trivy..."
                echo "========================================"

                trivy image \
                --timeout 20m \
                --scanners vuln \
                --severity HIGH,CRITICAL \
                --exit-code 0 \
                $DOCKER_USERNAME/$IMAGE_NAME:latest
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

        /*stage('Deploy to Docker Server') {

            steps {

                sh """
                ssh -o StrictHostKeyChecking=no ${DOCKER_SERVER} '

                    cd ~/DevOps_Project

                    echo "========================================"
                    echo "Pulling Latest Source Code..."
                    echo "========================================"

                    git pull origin main

                    echo "========================================"
                    echo "Pulling Latest Docker Image..."
                    echo "========================================"

                    docker compose pull

                    echo "========================================"
                    echo "Stopping Existing Containers..."
                    echo "========================================"

                    docker compose down

                    echo "========================================"
                    echo "Starting Updated Containers..."
                    echo "========================================"

                    docker compose up -d

                    echo "========================================"
                    echo "Removing Unused Images..."
                    echo "========================================"

                    docker image prune -f

                    echo "========================================"
                    echo "Deployment Successful!"
                    echo "========================================"

                '
                """
            }
        }*/
    }

    stage('Deploy to Kubernetes (k3s)') {
        
    steps {

            sh """
            ssh -o StrictHostKeyChecking=no ${K8S_SERVER} '

            cd ~/DevOps_Project

            echo "========================================"
            echo "Pulling Latest Source Code..."
            echo "========================================"

            git pull origin main

            echo "========================================"
            echo "Moving to Kubernetes Manifests..."
            echo "========================================"

            cd k3s

            echo "========================================"
            echo "Applying Kubernetes Manifests..."
            echo "========================================"

            kubectl apply -f .

            echo "========================================"
            echo "Restarting Deployment..."
            echo "========================================"

            kubectl rollout restart deployment employee-management

            echo "========================================"
            echo "Waiting for Rollout..."
            echo "========================================"

            kubectl rollout status deployment employee-management

            echo "========================================"
            echo "Verifying Pods..."
            echo "========================================"

            kubectl get pods

            echo "========================================"
            echo "Verifying Services..."
            echo "========================================"

            kubectl get svc

            echo "========================================"
            echo "Kubernetes Deployment Successful!"
            echo "========================================"

            '
            """
        }
    }

    post {

        success {

            echo "========================================"
            echo "CI/CD Pipeline Completed Successfully!"
            echo "========================================"
        }

        failure {

            echo "========================================"
            echo "CI/CD Pipeline Failed!"
            echo "========================================"
        }
    }
}