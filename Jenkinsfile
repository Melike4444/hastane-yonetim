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

    stage('Backend: Unit Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw
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
          chmod +x mvnw
          # Eğer integration testlerin ayrı paket/isimlendirme ile ayrılmıyorsa bu stage yine çalışır.
          # Ayrım gerekiyorsa burada -Dtest='**/*IT' gibi filtre uygulanır.
          ./mvnw -q test
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
        }
      }
    }

    stage('Backend: Package') {
      steps {
        sh '''
          set -e
          chmod +x mvnw
          ./mvnw -q -DskipTests package
        '''
      }
    }

    stage('Docker Up') {
      steps {
        sh '''
          set -e
          docker compose down -v || true
          docker compose build --no-cache
          docker compose up -d

          echo "Waiting app endpoint..."
          for i in $(seq 1 60); do
            if curl -fsS http://localhost:8080/api/hastalar >/dev/null 2>&1; then
              echo "APP OK ✅"
              break
            fi
            sleep 2
          done

          # Son kontrol
          curl -fsS http://localhost:8080/api/hastalar >/dev/null 2>&1 || (echo "APP not ready ❌" && docker compose ps && docker compose logs --no-color | tail -n 200 && exit 1)

          echo "Selenium ready check..."
          for i in $(seq 1 60); do
            if curl -fsS http://localhost:4444/status >/dev/null 2>&1; then
              echo "SELENIUM OK ✅"
              exit 0
            fi
            sleep 1
          done

          echo "Selenium not ready ❌"
          docker compose ps
          docker compose logs --no-color selenium | tail -n 200
          exit 1
        '''
      }
    }

    stage('Selenium Scenario 1') {
      steps {
        sh '''
          set -e
          SEL_HOST=$(docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' hastane-yonetim-selenium)
          REMOTE_URL="http://$SEL_HOST:4444/wd/hub"
          echo "Remote URL: $REMOTE_URL"

          cd selenium-tests
          chmod +x ../mvnw
          ../mvnw -q -DbaseUrl=http://app:8080 -DremoteUrl=$REMOTE_URL -Dtest=Senaryo1_UygulamaAciliyorMuTest test
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'selenium-tests/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Selenium Scenario 2') {
      steps {
        sh '''
          set -e
          SEL_HOST=$(docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' hastane-yonetim-selenium)
          REMOTE_URL="http://$SEL_HOST:4444/wd/hub"
          echo "Remote URL: $REMOTE_URL"

          cd selenium-tests
          ../mvnw -q -DbaseUrl=http://app:8080 -DremoteUrl=$REMOTE_URL -Dtest=Senaryo2_DoktorlarEndpointTest test
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'selenium-tests/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Selenium Scenario 3') {
      steps {
        sh '''
          set -e
          SEL_HOST=$(docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' hastane-yonetim-selenium)
          REMOTE_URL="http://$SEL_HOST:4444/wd/hub"
          echo "Remote URL: $REMOTE_URL"

          cd selenium-tests
          ../mvnw -q -DbaseUrl=http://app:8080 -DremoteUrl=$REMOTE_URL -Dtest=Senaryo3_RandevularEndpointTest test
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
      sh '''
        docker compose ps || true
        docker compose logs --no-color | tail -n 200 || true
      '''
    }
    cleanup {
      sh 'docker compose down -v || true'
    }
  }
}
