pipeline {
    agent any

    options {
        timestamps()
    }

    environment {
        MAVEN_OPTS = "-Xmx1024m"
    }

    stages {

        /* =========================
           1. CHECKOUT
        ========================== */
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        /* =========================
           2. BUILD
        ========================== */
        stage('Build') {
            steps {
                sh '''
                chmod +x mvnw
                ./mvnw clean package -DskipTests
                '''
            }
        }

        /* =========================
           3. UNIT TESTS
        ========================== */
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

        /* =========================
           4. INTEGRATION TESTS
        ========================== */
        stage('Integration Tests') {
            steps {
                sh '''
                ./mvnw verify
                '''
            }
            post {
                always {
                    junit 'target/failsafe-reports/*.xml'
                }
            }
        }

        /* =========================
           5. RUN SYSTEM (DOCKER)
        ========================== */
        stage('Run System (Docker)') {
            steps {
                sh '''
                docker-compose up -d --build
                sleep 20
                '''
            }
        }

        /* =========================
           6. SYSTEM TESTS (SELENIUM)
        ========================== */

        stage('System Test - Senaryo 1') {
            steps {
                sh '''
                cd selenium-tests
                mvn -Dtest=Senaryo1_* test
                '''
            }
        }

        stage('System Test - Senaryo 2') {
            steps {
                sh '''
                cd selenium-tests
                mvn -Dtest=Senaryo2_* test
                '''
            }
        }

        stage('System Test - Senaryo 3') {
            steps {
                sh '''
                cd selenium-tests
                mvn -Dtest=Senaryo3_* test
                '''
            }
        }

        stage('System Test - Senaryo 4 (UI Smoke)') {
            steps {
                sh '''
                cd selenium-tests
                mvn -Dtest=Senaryo4_* test
                '''
            }
        }

        stage('System Test - Senaryo 5 (Dashboard UI)') {
            steps {
                sh '''
                cd selenium-tests
                mvn -Dtest=Senaryo5_* test
                '''
            }
        }
    }

    post {
        always {
            sh '''
            docker-compose down -v || true
            '''
        }
    }
}
