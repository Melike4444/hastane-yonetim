pipeline {
    agent any

    options {
        timestamps()
    }

    environment {
        MAVEN_OPTS = "-Xmx1024m"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh '''
                    chmod +x mvnw
                    ./mvnw clean compile
                '''
            }
        }

        stage('Unit Tests') {
            steps {
                sh '''
                    ./mvnw test
                '''
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Integration Tests') {
            steps {
                sh '''
                    ./mvnw verify -DskipTests=false
                '''
            }
            post {
                always {
                    junit 'target/failsafe-reports/*.xml'
                }
            }
        }

        stage('UI Tests (Selenium)') {
            when {
                expression { false }   // ❗ CI ortamında bilinçli olarak kapalı
            }
            steps {
                echo "Selenium UI tests are disabled in CI environment"
            }
        }
    }

    post {
        success {
            echo "✅ Build SUCCESS - All required tests passed"
        }
        failure {
            echo "❌ Build FAILED"
        }
    }
}
