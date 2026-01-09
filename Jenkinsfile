pipeline {
  agent any

  environment {
    BASE_URL = "http://host.docker.internal:8080"
    SELENIUM_GRID_URL = "http://hastane-yonetim-selenium:4444/wd/hub"
  }

  options {
    timestamps()
  }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
      }
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

    stage('Backend: Tests (Unit+Integration)') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw test
        '''
      }
    }

    stage('Docker Compose Up') {
      steps {
        sh '''
          set -e
          docker compose down -v || true
          docker compose up -d --build
          docker compose ps
        '''
      }
    }

    stage('Wait App Ready') {
      steps {
        sh '''
          set +e
          echo "Waiting for app to be reachable at ${BASE_URL}"
          for i in $(seq 1 60); do
            CODE=$(docker run --rm curlimages/curl:8.6.0 -s -o /dev/null -w "%{http_code}" "${BASE_URL}" || echo 000)
            echo "Try #$i => HTTP $CODE"
            if [ "$CODE" = "200" ] || [ "$CODE" = "302" ] || [ "$CODE" = "401" ] || [ "$CODE" = "403" ]; then
              echo "APP is reachable (HTTP $CODE). Continuing..."
              exit 0
            fi
            sleep 2
          done
          echo "App did not become reachable in time."
          exit 1
        '''
      }
    }

    stage('Wait Selenium Ready') {
      steps {
        sh '''
          set +e
          echo "Waiting for Selenium Grid to be ready..."
          # compose network adını otomatik bulalım (docker compose default network)
          NET=$(docker compose ps -q | head -n 1 | xargs docker inspect --format '{{range $k,$v := .NetworkSettings.Networks}}{{println $k}}{{end}}' | head -n 1)
          echo "Detected network: $NET"

          for i in $(seq 1 60); do
            CODE=$(docker run --rm --network "$NET" curlimages/curl:8.6.0 -s -o /dev/null -w "%{http_code}" "http://hastane-yonetim-selenium:4444/status" || echo 000)
            echo "Try #$i => Selenium /status HTTP $CODE"
            if [ "$CODE" = "200" ]; then
              echo "Selenium Grid is ready."
              exit 0
            fi
            sleep 2
          done

          echo "Selenium did not become ready in time."
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
          ../mvnw -q -Dtest=Senaryo2_HastaSayfasiTest test
        '''
      }
    }

    stage('Selenium Scenario 3') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          ../mvnw -q -Dtest=Senaryo3_DoktorSayfasiTest test
        '''
      }
    }

    stage('Selenium Scenario 4') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          ../mvnw -q -Dtest=Senaryo4_UiSmokeTest test
        '''
      }
    }

    stage('Selenium Scenario 5') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          ../mvnw -q -Dtest=Senaryo5_ApiSmokeTest test
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
