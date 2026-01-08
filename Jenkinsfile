pipeline {
  agent any

  options { timestamps() }

  stages {

    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Backend: Unit Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -q -DskipITs=true test
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
        }
      }
    }

    stage('Docker Up') {
      steps {
        sh '''
          set -e
          docker --version
          docker-compose --version
          docker-compose up -d --build
          docker ps
        '''
      }
    }

    stage('Wait App Healthy') {
      steps {
        sh '''
          set -e
          APP_C="hastane-yonetim-app-1"
          echo "Waiting for application..."

          for i in $(seq 1 60); do
            if docker exec "$APP_C" curl -fsS http://localhost:8080 >/dev/null; then
              echo "Application is UP ✅"
              exit 0
            fi
            sleep 2
          done

          echo "Application did not start ❌"
          docker logs --tail=200 "$APP_C" || true
          exit 1
        '''
      }
    }

    stage('Backend: Integration Tests (Testcontainers)') {
      steps {
        sh '''
          set -e
          echo "Running INTEGRATION tests with Testcontainers..."

          # Testcontainers'i doğru docker stratejisine zorla
          export DOCKER_HOST="unix:///var/run/docker.sock"
          export TESTCONTAINERS_DOCKER_CLIENT_STRATEGY="org.testcontainers.dockerclient.UnixSocketClientProviderStrategy"
          export TESTCONTAINERS_HOST_OVERRIDE="host.docker.internal"
          export TESTCONTAINERS_RYUK_DISABLED="true"

          docker ps >/dev/null

          chmod +x mvnw || true
          ./mvnw -q -DskipITs=false verify
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml, target/failsafe-reports/*.xml'
        }
      }
    }

    stage('UI: Selenium Tests (ARM64 safe)') {
      steps {
        sh '''
          set -e
          echo "Preparing Selenium environment..."

          APP_C="hastane-yonetim-app-1"
          NET="$(docker inspect -f '{{range $k,$v := .NetworkSettings.Networks}}{{printf "%s" $k}}{{end}}' "$APP_C")"
          echo "Using network: $NET"

          docker network connect "$NET" jenkins 2>/dev/null || true

          docker rm -f selenium-chrome >/dev/null 2>&1 || true

          # ARM64 uyumlu image
          docker run -d --name selenium-chrome \
            --shm-size=2g \
            --network "$NET" \
            -p 4444:4444 \
            seleniarm/standalone-chromium:latest

          echo "Waiting for Selenium..."
          for i in $(seq 1 40); do
            if curl -fsS http://localhost:4444/wd/hub/status >/dev/null; then
              echo "Selenium is UP ✅"
              break
            fi
            sleep 2
          done

          chmod +x mvnw || true
          ./mvnw -q -f selenium-tests/pom.xml \
            -DbaseUrl="http://$APP_C:8080" \
            -DseleniumRemoteUrl="http://selenium-chrome:4444/wd/hub" \
            test
        '''
      }
      post {
        always {
          sh '''
            set +e
            docker logs --tail=200 selenium-chrome || true
            docker rm -f selenium-chrome || true
          '''
          junit allowEmptyResults: true, testResults: 'selenium-tests/target/surefire-reports/*.xml'
          archiveArtifacts allowEmptyArchive: true, artifacts: 'selenium-tests/target/surefire-reports/**'
        }
      }
    }
  }

  post {
    always {
      sh '''
        set +e
        echo "Global cleanup..."

        docker rm -f selenium-chrome >/dev/null 2>&1 || true

        docker-compose ps || true
        docker-compose logs --tail=200 || true

        docker-compose down -v --remove-orphans || true
      '''
    }
  }
}
