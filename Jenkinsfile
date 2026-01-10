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

    stage('Unit Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -q test
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Integration Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -q failsafe:integration-test failsafe:verify
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: '**/target/failsafe-reports/*.xml'
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
