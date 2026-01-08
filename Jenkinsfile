pipeline {
  agent any

  options {
    timestamps()
    skipDefaultCheckout(true)
  }

  environment {
    DOCKER = "docker"
    COMPOSE = "docker compose"
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
          ${MVN} -DskipTests clean package
        '''
      }
    }

    stage('3-Unit Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ${MVN} test
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
        }
      }
    }

    stage('4-Integration Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ${MVN} verify
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: '**/target/failsafe-reports/*.xml'
        }
      }
    }

    stage('5-Run System on Docker') {
      steps {
        sh '''
          set -e
          ${DOCKER} version
          ${COMPOSE} version
          ${COMPOSE} up -d --build
          ${COMPOSE} ps
        '''
      }
    }

    stage('Wait Backend Healthy') {
      steps {
        sh '''
          set -e
          URL="http://localhost:8080"
          echo "Waiting for ${URL} (accept 200/301/302/401/403)..."

          for i in $(seq 1 30); do
            code=$(curl -s -o /dev/null -w "%{http_code}" "${URL}" || true)
            echo "HTTP ${code}"

            if [ "${code}" = "200" ] || [ "${code}" = "301" ] || \
               [ "${code}" = "302" ] || [ "${code}" = "401" ] || \
               [ "${code}" = "403" ]; then
              echo "Backend is up ✅"
              exit 0
            fi
            sleep 2
          done

          echo "Backend did not become ready ❌"
          ${COMPOSE} logs --no-color || true
          exit 1
        '''
      }
    }

    // =================================================
    // =============== SELENIUM STAGES =================
    // =================================================

    stage('6a-Selenium Scenario 1 (auto)') {
      steps {
        sh '''
          set -e
          if [ -f "selenium-tests/pom.xml" ]; then
            ROOT="selenium-tests/src/test/java"
            FILE=$(find "$ROOT" -type f -name "*Test.java" | head -n 1 || true)

            if [ -z "$FILE" ]; then
              echo "SKIP: Selenium test bulunamadı."
              exit 0
            fi

            CLASS=$(echo "$FILE" | sed 's|^selenium-tests/src/test/java/||' | sed 's|/|.|g' | sed 's|\\.java$||')
            echo "Running Selenium Test: $CLASS"

            cd selenium-tests
            mvn -Dtest="$CLASS" test
          else
            echo "SKIP: selenium-tests/pom.xml yok."
          fi
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'selenium-tests/target/surefire-reports/*.xml'
        }
      }
    }

    stage('6b-Selenium Scenario 2 (auto)') {
      steps {
        sh '''
          set -e
          if [ -f "selenium-tests/pom.xml" ]; then
            ROOT="selenium-tests/src/test/java"
            FILE=$(find "$ROOT" -type f -name "*Test.java" | sed -n '2p' || true)

            if [ -z "$FILE" ]; then
              echo "SKIP: Selenium test bulunamadı."
              exit 0
            fi

            CLASS=$(echo "$FILE" | sed 's|^selenium-tests/src/test/java/||' | sed 's|/|.|g' | sed 's|\\.java$||')
            echo "Running Selenium Test: $CLASS"

            cd selenium-tests
            mvn -Dtest="$CLASS" test
          else
            echo "SKIP: selenium-tests/pom.xml yok."
          fi
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'selenium-tests/target/surefire-reports/*.xml'
        }
      }
    }

    stage('6c-Selenium Scenario 3 (auto)') {
      steps {
        sh '''
          set -e
          if [ -f "selenium-tests/pom.xml" ]; then
            ROOT="selenium-tests/src/test/java"
            FILE=$(find "$ROOT" -type f -name "*Test.java" | sed -n '3p' || true)

            if [ -z "$FILE" ]; then
              echo "SKIP: Selenium test bulunamadı."
              exit 0
            fi

            CLASS=$(echo "$FILE" | sed 's|^selenium-tests/src/test/java/||' | sed 's|/|.|g' | sed 's|\\.java$||')
            echo "Running Selenium Test: $CLASS"

            cd selenium-tests
            mvn -Dtest="$CLASS" test
          else
            echo "SKIP: selenium-tests/pom.xml yok."
          fi
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
        set +e
        ${COMPOSE} logs --no-color --tail=200 || true
        ${COMPOSE} down -v || true
      '''
      archiveArtifacts allowEmptyArchive: true, artifacts: '**/target/**/*'
    }
  }
}
