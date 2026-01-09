pipeline {
  agent any

  environment {
    DOCKER  = "/usr/local/bin/docker"
    COMPOSE = "docker compose"
    BASE_URL = "http://host.docker.internal:8080"
  }

  options { timestamps() }

  stages {

    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build Backend (skip tests)') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -DskipTests clean package
        '''
      }
    }

    stage('Docker Compose Up') {
      steps {
        sh '''
          set -e
          docker compose down -v || true
          docker compose up -d --build
        '''
      }
    }

    stage('Wait App Ready') {
      steps {
        sh '''
          set +e
          echo "Waiting for app to be ready at ${BASE_URL}"
          for i in $(seq 1 60); do
            # 403 de gelse "ayakta" say (Jenkins auth vs. yüzünden)
            CODE=$(docker run --rm curlimages/curl:8.6.0 -s -o /dev/null -w "%{http_code}" ${BASE_URL} || echo "000")
            echo "Try #$i => HTTP $CODE"

            if [ "$CODE" = "200" ] || [ "$CODE" = "302" ] || [ "$CODE" = "401" ] || [ "$CODE" = "403" ]; then
              echo "APP is reachable (HTTP $CODE). Continuing..."
              exit 0
            fi

            sleep 2
          done

          echo "APP did not become reachable"
          exit 1
        '''
      }
    }

    stage('Selenium Scenario 1') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          ../mvnw -q -Dtest=Senaryo1_UygulamaAciliyorMuTest test
        '''
      }
    }

    stage('Selenium Scenario 2') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          ../mvnw -q -Dtest=Senaryo2_* test
        '''
      }
    }

    stage('Selenium Scenario 3') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          ../mvnw -q -Dtest=Senaryo3_* test
        '''
      }
    }

    stage('Selenium Scenario 4') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          ../mvnw -q -Dtest=Senaryo4_* test
        '''
      }
    }

    stage('Selenium Scenario 5') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          ../mvnw -q -Dtest=Senaryo5_* test
        '''
      }
    }
  }

  post {
    always {
      sh '''
        docker compose ps || true
        docker compose logs --no-color | tail -n 200 || true
        docker compose down -v || true
      '''
      junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
    }
  }
}
