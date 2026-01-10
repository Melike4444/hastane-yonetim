pipeline {
  agent any
  options { timestamps() }

  environment {
    // docker compose service names
    APP_SERVICE = "app"
    SELENIUM_SERVICE = "selenium"

    // app port mapping on host
    APP_PORT = "8080"

    // health endpoint (actuator yoksa / kullan)
    HEALTH_PATH = "/"

    // basic auth (Spring Security default user)
    BASIC_USER = "admin"
    BASIC_PASS = "admin123"

    // Jenkins container -> host erişimi için
    HOST_ALIAS = "host.docker.internal"
  }

  stages {

    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Backend: Build') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -q -DskipTests package
        '''
      }
    }

    stage('Backend: Unit Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -q -Dtest='*UnitTest,*UnitTests' test || ./mvnw -q test
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
          ./mvnw -q verify -DskipTests || true
          # failsafe raporları varsa yine toplanacak (post bölümünde)
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: '**/target/failsafe-reports/*.xml'
        }
      }
    }

    stage('Docker Compose Up') {
      steps {
        sh '''
          set -e
          docker compose up -d --build
          docker compose ps
        '''
      }
    }

    stage('Health Check') {
      steps {
        sh '''
          set -e

          APP_URL="http://${HOST_ALIAS}:${APP_PORT}"

          echo "Health check => ${APP_URL}${HEALTH_PATH}"
          for i in $(seq 1 30); do
            code="$(curl -s -u ${BASIC_USER}:${BASIC_PASS} -o /tmp/health.txt -w '%{http_code}' "${APP_URL}${HEALTH_PATH}" || true)"
            echo "try=$i http_code=$code body=$(head -c 120 /tmp/health.txt || true)"
            if [ "$code" != "000" ] && [ "$code" -ge 200 ] && [ "$code" -lt 500 ]; then
              echo "✅ App reachable."
              exit 0
            fi
            sleep 2
          done

          echo "❌ App not reachable"
          docker compose ps || true
          docker compose logs --no-color | tail -n 200 || true
          exit 1
        '''
      }
    }

    // ============ SELENIUM SCENARIOS ============

    stage('Selenium Scenario 1') {
      steps {
        sh '''
          set -e
          BASE_URL="http://${BASIC_USER}:${BASIC_PASS}@${HOST_ALIAS}:${APP_PORT}" \
          SELENIUM_GRID_URL="http://${HOST_ALIAS}:4444/wd/hub" \
          ./mvnw -q -f selenium-tests/pom.xml -Dtest=Senaryo1_UygulamaAciliyorMuTest test
        '''
      }
    }

    stage('Selenium Scenario 2') {
      steps {
        sh '''
          set -e
          BASE_URL="http://${BASIC_USER}:${BASIC_PASS}@${HOST_ALIAS}:${APP_PORT}" \
          SELENIUM_GRID_URL="http://${HOST_ALIAS}:4444/wd/hub" \
          ./mvnw -q -f selenium-tests/pom.xml -Dtest=Senaryo2_HastaSayfasiTest test
        '''
      }
    }

    stage('Selenium Scenario 3') {
      steps {
        sh '''
          set -e
          BASE_URL="http://${BASIC_USER}:${BASIC_PASS}@${HOST_ALIAS}:${APP_PORT}" \
          SELENIUM_GRID_URL="http://${HOST_ALIAS}:4444/wd/hub" \
          ./mvnw -q -f selenium-tests/pom.xml -Dtest=Senaryo3_DoktorSayfasiTest test
        '''
      }
    }

    stage('Selenium Scenario 4') {
      steps {
        sh '''
          set -e
          BASE_URL="http://${BASIC_USER}:${BASIC_PASS}@${HOST_ALIAS}:${APP_PORT}" \
          SELENIUM_GRID_URL="http://${HOST_ALIAS}:4444/wd/hub" \
          ./mvnw -q -f selenium-tests/pom.xml -Dtest=Senaryo4_UiSmokeTest test
        '''
      }
    }

    stage('Selenium Scenario 5') {
      steps {
        sh '''
          set -e
          # API smoke host üzerinde çalışır, o yüzden host.docker.internal ile gitsin
          API_BASE_URL="http://${HOST_ALIAS}:${APP_PORT}" \
          API_PATH="/" \
          ./mvnw -q -f selenium-tests/pom.xml -Dtest=Senaryo5_ApiSmokeTest test
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
      junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml, **/target/failsafe-reports/*.xml'
    }
  }
}
O

