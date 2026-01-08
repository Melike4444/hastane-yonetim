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
          junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
        }
      }
    }

    stage('Backend: Integration Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true

          # DinD (tcp) ile Docker'a bağlan
          export DOCKER_HOST=tcp://dind:2375

          # Testcontainers socket override kullanma (tcp modunda hata çıkarabiliyor)
          unset TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE

          # Bazı ortamlarda gerekli olabiliyor (zararı yok)
          export TESTCONTAINERS_HOST_OVERRIDE=host.docker.internal

          echo DOCKER_HOST=$DOCKER_HOST
          ./mvnw -q verify
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
          junit allowEmptyResults: true, testResults: 'target/failsafe-reports/*.xml'
        }
      }
    }

    stage('Selenium Tests (non-blocking)') {
      steps {
        sh '''
          set +e

          if [ -d "selenium-tests" ] && [ -f "selenium-tests/pom.xml" ]; then
            cd selenium-tests

            # Eğer bu modülde mvnw varsa onu kullan, yoksa mvn kullan
            if [ -f "./mvnw" ]; then
              chmod +x mvnw || true
              ./mvnw -q test
            else
              mvn -q test
            fi
          else
            echo "selenium-tests klasörü veya pom.xml bulunamadı, stage atlanıyor."
          fi

          exit 0
        '''
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
