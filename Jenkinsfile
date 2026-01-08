pipeline {
  agent any

  options {
    timestamps()
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
          ./mvnw clean package -DskipTests
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
          ./mvnw verify
        '''
      }
      post {
        always {
          junit 'target/failsafe-reports/*.xml'
        }
      }
    }

    stage('Run System (Docker)') {
      steps {
        sh '''
          docker-compose up -d --build
          sleep 20
        '''
      }
    }

    stage('System Test - Senaryo 1') {
      steps {
        sh '''
          cd selenium-tests
          chmod +x mvnw
          ./mvnw -Dtest=Senaryo1_* test
        '''
      }
    }

    stage('System Test - Senaryo 2') {
      steps {
        sh '''
          cd selenium-tests
          ./mvnw -Dtest=Senaryo2_* test
        '''
      }
    }

    stage('System Test - Senaryo 3') {
      steps {
        sh '''
          cd selenium-tests
          ./mvnw -Dtest=Senaryo3_* test
        '''
      }
    }

    stage('System Test - Senaryo 4 (UI Smoke)') {
      steps {
        sh '''
          cd selenium-tests
          ./mvnw -Dtest=Senaryo4_* test
        '''
      }
    }

    stage('System Test - Senaryo 5 (Dashboard UI)') {
      steps {
        sh '''
          cd selenium-tests
          ./mvnw -Dtest=Senaryo5_* test
        '''
      }
    }
  }

  post {
    always {
      sh 'docker-compose down -v || true'
    }
  }
}
