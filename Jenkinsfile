pipeline {
  agent any
  options { timestamps() }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
      }
    }

        stage('JUnit Report') {
      steps {
        junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml, selenium-tests/target/surefire-reports/*.xml'
      }
    }

    stage('Selenium Scenario 1') {
      steps {
        sh '''
          set -e
          ./mvnw -f selenium-tests/pom.xml -Dtest=Senaryo1_UygulamaAciliyorMuTest test
        '''
      }
    }

    stage('Selenium Scenario 2') {
      steps {
        sh '''
          set -e
          ./mvnw -f selenium-tests/pom.xml -Dtest=Senaryo2_DoktorlarEndpointTest test
        '''
      }
    }

    stage('Selenium Scenario 3') {
      steps {
        sh '''
          set -e
          ./mvnw -f selenium-tests/pom.xml -Dtest=Senaryo3_RandevularEndpointTest test
        '''
      }
    }

    stage('Selenium Scenario 4') {
      steps {
        sh '''
          set -e
          ./mvnw -f selenium-tests/pom.xml -Dtest=Senaryo4_UiSmokeTest test
        '''
      }
    }

    stage('Selenium Scenario 5') {
      steps {
        sh '''
          set -e

          APP_PORT=8090
          JAR_FILE=$(ls -1 target/*.jar | head -n 1)

          echo "Backend jar: $JAR_FILE"
          echo "Backend $APP_PORT portunda başlatılıyor..."

          java -jar "$JAR_FILE" --server.port=$APP_PORT > backend_jenkins.log 2>&1 &
          APP_PID=$!

          trap "echo 'Backend kapatılıyor...'; kill $APP_PID >/dev/null 2>&1 || true" EXIT

          echo "Backend hazır mı kontrol ediliyor..."
          for i in $(seq 1 30); do
            if curl -s -o /dev/null -w "%{http_code}" http://localhost:$APP_PORT/api/hastalar | grep -q "200"; then
              echo "Backend ayakta ✅"
              break
            fi
            sleep 1
          done

          ./mvnw -f selenium-tests/pom.xml \
            -Dtest=Senaryo5_HastalarEndpointTest \
            -DapiBaseUrl=http://localhost:$APP_PORT test
        '''
      }
    }

    stage('Archive Jar') {
      steps {
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
      }
    }
  }

  post {
    always {
      sh '''
        set +e
        if command -v docker >/dev/null 2>&1; then
          if docker compose version >/dev/null 2>&1; then
            docker compose -f docker-compose.yml down
          elif command -v docker-compose >/dev/null 2>&1; then
            docker-compose -f docker-compose.yml down
          fi
        fi
      '''
    }
  }
}

