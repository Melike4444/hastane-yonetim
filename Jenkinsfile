pipeline {
  agent any

  environment {
    DOCKER = "/usr/bin/docker"
    DOCKER_CONFIG = "${WORKSPACE}/.docker"
    BASE_URL = "http://host.docker.internal:8080"
    SEL_URL  = "http://host.docker.internal:4444"
  }

  options { timestamps() }

  stages {

    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Sanity: Tools') {
      steps {
        sh '''
          set -e
          echo "Docker:" 
          ${DOCKER} --version
          ${DOCKER} compose version
          chmod +x mvnw || true
          ./mvnw -v
        '''
      }
    }

    stage('Fix Docker Credentials (Jenkins)') {
      steps {
        sh '''
          set -e
          mkdir -p "$DOCKER_CONFIG"
          cat > "$DOCKER_CONFIG/config.json" <<'JSON'
          { "auths": {} }
JSON
          echo "Docker config ready: $DOCKER_CONFIG/config.json"
        '''
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
        always { junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml' }
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
          export DOCKER_CONFIG="$DOCKER_CONFIG"

          ${DOCKER} compose down -v --remove-orphans || true
          ${DOCKER} compose up -d --build

          echo "Waiting APP..."
          for i in $(seq 1 60); do
            ${DOCKER} run --rm curlimages/curl:8.6.0 -fsS ${BASE_URL}/api/hastalar >/dev/null && break || true
            sleep 2
            if [ "$i" = "60" ]; then
              echo "APP did not become ready"; exit 1
            fi
          done

          echo "Waiting Selenium..."
          for i in $(seq 1 60); do
            ${DOCKER} run --rm curlimages/curl:8.6.0 -fsS ${SEL_URL}/status >/dev/null && break || true
            sleep 2
            if [ "$i" = "60" ]; then
              echo "Selenium did not become ready"; exit 1
            fi
          done

          ${DOCKER} ps
        '''
      }
    }

    stage('Selenium Scenario 1') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          chmod +x ../mvnw
          ../mvnw -q -DbaseUrl=${BASE_URL} -DremoteUrl=${SEL_URL} -Dtest=Senaryo1_UygulamaAciliyorMuTest test
        '''
      }
      post { always { junit allowEmptyResults: true, testResults: 'selenium-tests/target/surefire-reports/*.xml' } }
    }

    stage('Selenium Scenario 2') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          ../mvnw -q -DbaseUrl=${BASE_URL} -DremoteUrl=${SEL_URL} -Dtest=Senaryo2_DoktorlarEndpointTest test
        '''
      }
      post { always { junit allowEmptyResults: true, testResults: 'selenium-tests/target/surefire-reports/*.xml' } }
    }

    stage('Selenium Scenario 3') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          ../mvnw -q -DbaseUrl=${BASE_URL} -DremoteUrl=${SEL_URL} -Dtest=Senaryo3_RandevularEndpointTest test
        '''
      }
      post { always { junit allowEmptyResults: true, testResults: 'selenium-tests/target/surefire-reports/*.xml' } }
    }

    stage('Selenium Scenario 4') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          ../mvnw -q -DbaseUrl=${BASE_URL} -DremoteUrl=${SEL_URL} -Dtest=Senaryo4_UiSmokeTest test
        '''
      }
      post { always { junit allowEmptyResults: true, testResults: 'selenium-tests/target/surefire-reports/*.xml' } }
    }

    stage('Selenium Scenario 5') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          ../mvnw -q -DbaseUrl=${BASE_URL} -DremoteUrl=${SEL_URL} -Dtest=Senaryo5_HastalarEndpointTest test
        '''
      }
      post { always { junit allowEmptyResults: true, testResults: 'selenium-tests/target/surefire-reports/*.xml' } }
    }
  }

  post {
    always {
      sh '''
        set +e
        export DOCKER_CONFIG="$DOCKER_CONFIG"
        ${DOCKER} compose ps || true
        ${DOCKER} compose logs --no-color | tail -n 200 || true
        ${DOCKER} compose down -v --remove-orphans || true
      '''
    }
  }
}
