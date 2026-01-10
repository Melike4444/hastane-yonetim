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

    stage('Unit Tests (no selenium-tests)') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          rm -rf selenium-tests/target || true
          ./mvnw -q -pl '!selenium-tests' -am test
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Integration Tests (no selenium-tests)') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          rm -rf selenium-tests/target || true
          ./mvnw -q -pl '!selenium-tests' -am failsafe:integration-test failsafe:verify
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
