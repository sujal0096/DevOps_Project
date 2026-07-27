pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                echo 'Source code has been checked out from GitHub.'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

    }
}