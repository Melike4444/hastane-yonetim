pipeline {
  agent any

  options {
    timestamps()
  }

  environment {
    MAVEN_OPTS = "-Dmaven.repo.local=/var/jenkins_home/.m2/repository"
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
          ./mvnw -q -DskipTests=true clean package
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
          // ✅ Unit test raporlarını Jenkins "Test Result" kısmında gösterir
          junit testResults: '**/target/surefire-reports/*.xml',
                allowEmptyResults: true
        }
      }
    }

    stage('Integration Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -q verify
        '''
      }
      post {
        always {
          // ✅ Integration test raporlarını Jenkins'te gösterir (Failsafe)
          junit testResults: '**/target/failsafe-reports/*.xml',
                allowEmptyResults: true
        }
      }
    }

    stage('Run System (Docker)') {
      steps {
        sh '''
          set -e
          docker compose up -d --build
        '''
      }
    }

    stage('System Test - Senaryo 1') {
      steps {
        sh '''
          set -e
          # örnek: backend API smoke / curl vs
          echo "Senaryo 1 burada koşuyor"
        '''
      }
    }

    stage('System Test - Senaryo 2') {
      steps {
        sh '''
          set -e
          echo "Senaryo 2 burada koşuyor"
        '''
      }
    }

    stage('System Test - Senaryo 3') {
      steps {
        sh '''
          set -e
          echo "Senaryo 3 burada koşuyor"
        '''
      }
    }

    stage('System Test - Senaryo 4 (UI Smoke)') {
      steps {
        sh '''
          set -e
          # selenium-tests klasörün varsa oradan test koştur
          if [ -d "selenium-tests" ]; then
            cd selenium-tests
            chmod +x mvnw || true
            ./mvnw -q test
          else
            echo "selenium-tests klasörü yok, atlandı"
          fi
        '''
      }
      post {
        always {
          // ✅ Selenium test raporları (Jenkins’te görünür)
          junit testResults: 'selenium-tests/**/target/surefire-reports/*.xml',
                allowEmptyResults: true
        }
      }
    }

    stage('System Test - Senaryo 5 (Dashboard / API)') {
      steps {
        sh '''
          set -e
          echo "Senaryo 5 burada koşuyor"
        '''
      }
    }
  }

  post {
    always {
      sh '''
        set +e
        docker compose down -v --remove-orphans || true
      '''
      // İstersen tüm raporları tek seferde de toplayabilirsin (opsiyonel)
      junit testResults: '**/target/surefire-reports/*.xml, **/target/failsafe-reports/*.xml, selenium-tests/**/target/surefire-reports/*.xml',
            allowEmptyResults: true
    }
  }
}

          docker compose down -v || true
          docker compose build --no-cache
          docker compose up -d

          echo "Waiting app health..."
          for i in $(seq 1 60); do
            if curl -fsS http://localhost:8080 >/dev/null 2>&1; then
              echo "APP OK ✅"
              exit 0
            fi
            sleep 2
          done

          echo "APP did not become healthy ❌"
          docker compose ps
          docker compose logs --no-color | tail -n 200
          exit 1
        '''
      }
    }

    stage('Selenium Scenario 1') {
      steps {
        sh '''
          set -e

          # Selenium container remote URL
          SEL_HOST=$(docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' hastane-yonetim-selenium)
          REMOTE_URL="http://$SEL_HOST:4444/wd/hub"

          echo "Remote URL: $REMOTE_URL"

          cd selenium-tests
          chmod +x ../mvnw

          # baseUrl: Selenium container, compose network içindeki "app" servisine gider
          ../mvnw -DbaseUrl=http://app:8080 -DremoteUrl=$REMOTE_URL -Dtest=Senaryo1_UygulamaAciliyorMuTest test
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

          cd selenium-tests
          ../mvnw -DbaseUrl=http://app:8080 -DremoteUrl=$REMOTE_URL -Dtest=Senaryo2_DoktorlarEndpointTest test
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

          cd selenium-tests
          ../mvnw -DbaseUrl=http://app:8080 -DremoteUrl=$REMOTE_URL -Dtest=Senaryo3_RandevularEndpointTest test
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'selenium-tests/target/surefire-reports/*.xml'
        }
      }
    }

  } // stages

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
