pipeline {
  agent any

  options { timestamps() }

  environment {
    APP_SERVICE      = 'app'
    SELENIUM_SERVICE = 'hastane-yonetim-selenium'
    MAVEN_IMAGE      = 'maven:3.9.9-eclipse-temurin-21'
  }

  stages {

    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Backend: Build + Unit + Integration (verify)') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -B -ntp clean verify
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

    stage('Wait App Ready (inside compose network)') {
      steps {
        sh '''
          set +e
          NET=$(docker compose ps -q | head -n 1 | xargs docker inspect --format '{{range $k,$v := .NetworkSettings.Networks}}{{println $k}}{{end}}' | head -n 1)
          echo "Detected network: $NET"
          echo "Waiting for app at http://${APP_SERVICE}:8080 ..."

          for i in $(seq 1 60); do
            CODE=$(docker run --rm --network "$NET" curlimages/curl:8.6.0 -s -o /dev/null -w "%{http_code}" "http://${APP_SERVICE}:8080")
            echo "Try #$i => HTTP $CODE"
            if [ "$CODE" = "200" ] || [ "$CODE" = "302" ] || [ "$CODE" = "401" ] || [ "$CODE" = "403" ]; then
              echo "APP is reachable (HTTP $CODE)."
              exit 0
            fi
            sleep 2
          done

          echo "App did not become ready in time."
          exit 1
        '''
      }
    }

    stage('Wait Selenium Ready') {
      steps {
        sh '''
          set +e
          NET=$(docker compose ps -q | head -n 1 | xargs docker inspect --format '{{range $k,$v := .NetworkSettings.Networks}}{{println $k}}{{end}}' | head -n 1)
          echo "Detected network: $NET"
          echo "Waiting for Selenium Grid..."

          for i in $(seq 1 60); do
            CODE=$(docker run --rm --network "$NET" curlimages/curl:8.6.0 -s -o /dev/null -w "%{http_code}" "http://${SELENIUM_SERVICE}:4444/status")
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

    // ✅ Selenium senaryoları: Maven'i repo kökünden (pom.xml olan yer) çalıştırıyoruz
    stage('Selenium Scenario 1 - App Opens') {
      steps {
        sh '''
          set -e
          NET=$(docker compose ps -q | head -n 1 | xargs docker inspect --format '{{range $k,$v := .NetworkSettings.Networks}}{{println $k}}{{end}}' | head -n 1)

          docker run --rm --network "$NET" \
            -v "$WORKSPACE":/work -w /work \
            ${MAVEN_IMAGE} mvn -q \
            -DbaseUrl="http://${APP_SERVICE}:8080" \
            -DremoteUrl="http://${SELENIUM_SERVICE}:4444/wd/hub" \
            -Dtest=Senaryo1_UygulamaAciliyorMuTest test
        '''
      }
    }

    stage('Selenium Scenario 2 - Patient Page') {
      steps {
        sh '''
          set -e
          NET=$(docker compose ps -q | head -n 1 | xargs docker inspect --format '{{range $k,$v := .NetworkSettings.Networks}}{{println $k}}{{end}}' | head -n 1)

          docker run --rm --network "$NET" \
            -v "$WORKSPACE":/work -w /work \
            ${MAVEN_IMAGE} mvn -q \
            -DbaseUrl="http://${APP_SERVICE}:8080" \
            -DremoteUrl="http://${SELENIUM_SERVICE}:4444/wd/hub" \
            -Dtest=Senaryo2_HastaSayfasiTest test
        '''
      }
    }

    stage('Selenium Scenario 3 - Doctor Page') {
      steps {
        sh '''
          set -e
          NET=$(docker compose ps -q | head -n 1 | xargs docker inspect --format '{{range $k,$v := .NetworkSettings.Networks}}{{println $k}}{{end}}' | head -n 1)

          docker run --rm --network "$NET" \
            -v "$WORKSPACE":/work -w /work \
            ${MAVEN_IMAGE} mvn -q \
            -DbaseUrl="http://${APP_SERVICE}:8080" \
            -DremoteUrl="http://${SELENIUM_SERVICE}:4444/wd/hub" \
            -Dtest=Senaryo3_DoktorSayfasiTest test
        '''
      }
    }

    stage('Selenium Scenario 4 - UI Smoke') {
      steps {
        sh '''
          set -e
          NET=$(docker compose ps -q | head -n 1 | xargs docker inspect --format '{{range $k,$v := .NetworkSettings.Networks}}{{println $k}}{{end}}' | head -n 1)

          docker run --rm --network "$NET" \
            -v "$WORKSPACE":/work -w /work \
            ${MAVEN_IMAGE} mvn -q \
            -DbaseUrl="http://${APP_SERVICE}:8080" \
            -DremoteUrl="http://${SELENIUM_SERVICE}:4444/wd/hub" \
            -Dtest=Senaryo4_UiSmokeTest test
        '''
      }
    }

    stage('Selenium Scenario 5 - API Smoke') {
      steps {
        sh '''
          set -e
          NET=$(docker compose ps -q | head -n 1 | xargs docker inspect --format '{{range $k,$v := .NetworkSettings.Networks}}{{println $k}}{{end}}' | head -n 1)

          docker run --rm --network "$NET" \
            -v "$WORKSPACE":/work -w /work \
            ${MAVEN_IMAGE} mvn -q \
            -DbaseUrl="http://${APP_SERVICE}:8080" \
            -DremoteUrl="http://${SELENIUM_SERVICE}:4444/wd/hub" \
            -Dtest=Senaryo5_ApiSmokeTest test
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

