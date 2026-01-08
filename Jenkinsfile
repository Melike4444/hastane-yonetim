pipeline {
  agent any

  options {
    timestamps()
  }

  environment {
    // Jenkins container içinde docker varsa genelde PATH'ten bulunur.
    // Bulunamazsa /usr/bin/docker ya da /usr/local/bin/docker deneyebilirsin.
    DOCKER = "docker"

    // Testcontainers bazı ortamlarda bunu isteyebiliyor (zararı yok).
    TESTCONTAINERS_HOST_OVERRIDE = "host.docker.internal"
  }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Backend: Build') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -q -DskipTests clean package
        '''
      }
    }

    stage('Backend: Unit Tests') {
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

    stage('Backend: Integration Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true

          # Integration testleri ayrı pakette ise:
          # ./mvnw -q -Dtest='*IT' test
          # ya da Failsafe (integration-test/verify) kullanıyorsan:
          # ./mvnw -q verify

          # En güvenlisi: verify (Failsafe + Surefire hepsi)
          ./mvnw -q verify
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml, **/target/failsafe-reports/*.xml'
        }
      }
    }

    stage('Selenium Tests (non-blocking)') {
      steps {
        dir('selenium-tests') {
          sh '''
            set +e
            mvn -q test
            exit 0
          '''
        }
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'selenium-tests/target/surefire-reports/*.xml'
        }
      }
    }
  }

  post {
    always {
      echo "Pipeline finished."
    }
  }
}
	
