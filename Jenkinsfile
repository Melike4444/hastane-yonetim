pipeline {
  agent any

  options { timestamps() }

  environment {
    MAVEN_OPTS = "-Dmaven.repo.local=.m2"
  }

  stages {

    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build') {
      steps {
        sh '''
          set -e
          chmod +x mvnw
          ./mvnw -q -DskipTests clean package
        '''
      }
    }

    stage('Unit Tests') {
      steps {
        sh '''
          set -e
          # Selenium raporları eski kalmasın diye temizle
          rm -rf selenium-tests/target || true

          # selenium-tests modülünü çalıştırma
          ./mvnw -q -pl '!selenium-tests' -am test
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml, !**/selenium-tests/**'
        }
      }
    }

    stage('Integration Tests') {
      steps {
        sh '''
          set -e
          rm -rf selenium-tests/target || true

          # selenium-tests modülünü çalıştırma
          ./mvnw -q -pl '!selenium-tests' -am failsafe:integration-test failsafe:verify
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: '**/target/failsafe-reports/*.xml, !**/selenium-tests/**'
        }
      }
    }

    stage('Docker Build') {
      steps {
        sh 'docker build -t hastane-yonetim:latest .'
      }
    }
  }

  post {
    always {
      sh 'docker compose down -v || true'
    }
  }
}
