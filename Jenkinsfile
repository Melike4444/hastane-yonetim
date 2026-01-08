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
    }

    stage('Integration Tests') {
      steps {
        sh '''
          ./mvnw verify -DskipTests=false
        '''
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
          chmod +x ../mvnw
          ../mvnw -Dtest=com.hastane.selenium.Senaryo1_* test \
            -Dsurefire.failIfNoSpecifiedTests=false
        '''
      }
    }

    stage('System Test - Senaryo 2') {
      steps {
        sh '''
          cd selenium-tests
          ../mvnw -Dtest=com.hastane.selenium.Senaryo2_* test \
            -Dsurefire.failIfNoSpecifiedTests=false
        '''
      }
    }

    stage('System Test - Senaryo 3') {
      steps {
        sh '''
          cd selenium-tests
          ../mvnw -Dtest=com.hastane.selenium.Senaryo3_* test \
            -Dsurefire.failIfNoSpecifiedTests=false
        '''
      }
    }

    stage('System Test - Senaryo 4 (UI Smoke)') {
      steps {
        sh '''
          cd selenium-tests
          ../mvnw -Dtest=com.hastane.selenium.Senaryo4_* test \
            -Dsurefire.failIfNoSpecifiedTests=false
        '''
      }
    }

    stage('System Test - Senaryo 5 (Dashboard / API)') {
      steps {
        sh '''
          cd selenium-tests
          ../mvnw -Dtest=com.hastane.selenium.Senaryo5_* test \
            -Dsurefire.failIfNoSpecifiedTests=false
        '''
      }
    }
  }

  post {
    always {
      // ✅ Jenkins "Test Results" için JUnit raporlarını yayınla
      // allowEmptyResults: true => rapor yoksa pipeline bozulmasın
      junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
      junit allowEmptyResults: true, testResults: '**/target/failsafe-reports/*.xml'
      junit allowEmptyResults: true, testResults: 'selenium-tests/target/surefire-reports/*.xml'

      // ✅ ortamı temizle (senin mevcut davranışın)
      sh 'docker-compose down -v || true'
    }
  }
}
