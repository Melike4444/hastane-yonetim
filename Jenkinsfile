pipeline {
  agent any

  options {
    timestamps()
    skipDefaultCheckout(true)
  }

  environment {
    // Jenkins container içinde docker çalışıyorsa genelde /usr/bin/docker veya /usr/local/bin/docker olur
    DOCKER = "docker"
    COMPOSE = "docker compose"
    // Maven wrapper varsa bunu kullanacağız
    MVN = "./mvnw"
  }

  stages {

    stage('1-Checkout') {
      steps {
        checkout scm
        sh 'ls -la'
      }
    }

    stage('2-Build') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ${MVN} -q -DskipTests clean package
        '''
      }
    }

    stage('3-Unit Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          # Unit test: surefire (src/test/java ... *Test)
          ${MVN} -q test
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
        }
      }
    }

    stage('4-Integration Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          # En yaygın yaklaşım: Failsafe ile integration testler (*IT)
          # pom.xml içinde failsafe yoksa bu stage başarısız olur.
          ${MVN} -q -DskipTests=false verify
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'target/failsafe-reports/*.xml'
        }
      }
    }

    stage('5-Run System on Docker') {
      steps {
        sh '''
          set -e
          ${DOCKER} version
          ${COMPOSE} version

          # compose dosyan hangisiyse biri çalışır
          if [ -f docker-compose.yml ] || [ -f compose.yml ]; then
            ${COMPOSE} up -d --build
          else
            echo "ERROR: docker-compose.yml / compose.yml bulunamadı."
            exit 1
          fi

          ${COMPOSE} ps
        '''
      }
    }

    stage('Wait Backend Healthy') {
      steps {
        sh '''
          set -e
          # Backend’iniz 8080 ise (container içinde) veya compose ile dışarı açılıyorsa buna göre ayarlayın.
          # Burada localhost:8080 varsayıyorum. Sizde 9091 gibi ise değiştirin.
          URL="http://localhost:8080"
          echo "Waiting for ${URL} ..."
          for i in $(seq 1 30); do
            if curl -fsS "${URL}" > /dev/null; then
              echo "Backend is up ✅"
              exit 0
            fi
            sleep 2
          done
          echo "Backend did not become ready in time ❌"
          ${COMPOSE} logs --no-color || true
          exit 1
        '''
      }
    }

    // 6. madde: En az 3 Selenium senaryosu
    // Her stage ayrı "selenium paketi" (klasör) çalıştırır.

    stage('6a-Selenium Scenario 1 (Login)') {
      steps {
        sh '''
          set -e
          if [ -d "selenium-tests/login-test" ]; then
            cd selenium-tests/login-test
            chmod +x ../../mvnw || true
            mvn -q test || ${MVN} -q test
          else
            echo "SKIP: selenium-tests/login-test yok."
          fi
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'selenium-tests/login-test/**/surefire-reports/*.xml'
        }
      }
    }

    stage('6b-Selenium Scenario 2 (Hasta Ekleme)') {
      steps {
        sh '''
          set -e
          if [ -d "selenium-tests/hasta-ekleme-test" ]; then
            cd selenium-tests/hasta-ekleme-test
            mvn -q test || ${MVN} -q test
          else
            echo "SKIP: selenium-tests/hasta-ekleme-test yok."
          fi
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'selenium-tests/hasta-ekleme-test/**/surefire-reports/*.xml'
        }
      }
    }

    stage('6c-Selenium Scenario 3 (Randevu)') {
      steps {
        sh '''
          set -e
          if [ -d "selenium-tests/randevu-test" ]; then
            cd selenium-tests/randevu-test
            mvn -q test || ${MVN} -q test
          else
            echo "SKIP: selenium-tests/randevu-test yok."
          fi
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'selenium-tests/randevu-test/**/surefire-reports/*.xml'
        }
      }
    }
  }

  post {
    always {
      // Container logları istenirse kolay olsun
      sh '''
        set +e
        if [ -f docker-compose.yml ] || [ -f compose.yml ]; then
          ${COMPOSE} ps || true
          ${COMPOSE} logs --no-color --tail=200 || true
          ${COMPOSE} down -v || true
        fi
      '''
      archiveArtifacts allowEmptyArchive: true, artifacts: '**/target/surefire-reports/*, **/target/failsafe-reports/*'
    }
  }
}
