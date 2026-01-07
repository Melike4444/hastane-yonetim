pipeline {
  agent any

  environment {
    DOCKER = "/usr/bin/docker"
    PATH = "/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/opt/homebrew/bin"
  }

  options { timestamps() }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Sanity: Tools') {
      steps {
        sh '''
          set -e
          echo "PATH=$PATH"
          which java || true
          java -version || true
          ./mvnw -v
          ${DOCKER} --version
          ${DOCKER} compose version
        '''
      }
    }

    stage('Fix Docker Credentials (Jenkins)') {
      steps {
        sh '''
          set -e
          # Jenkins içinde docker build sırasında "docker-credential-desktop" hatasını engelle
          mkdir -p "$WORKSPACE/.docker"
          cat > "$WORKSPACE/.docker/config.json" <<'JSON'
          { "auths": {} }
JSON
          echo "Created $WORKSPACE/.docker/config.json"
          cat "$WORKSPACE/.docker/config.json"
        '''
      }
    }

    stage('Backend: Unit Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw
          ./mvnw -Dtest='!**/integration/**' test
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
          ./mvnw -Dtest='**/integration/**' test
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
          ./mvnw -DskipTests package
        '''
      }
    }

    stage('Docker Up') {
      steps {
        sh '''
          set -e
          echo "Docker compose ile sistem ayağa kaldırılıyor..."
          export DOCKER_CONFIG="$WORKSPACE/.docker"
          ${DOCKER} compose -f docker-compose.yml up -d --build
          ${DOCKER} ps
        '''
      }
    }

    stage('Selenium Tests') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          chmod +x ../mvnw
          ../mvnw -q -DskipTests=false test
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'selenium-tests/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Docker Down') {
      steps {
        sh '''
          set +e
          export DOCKER_CONFIG="$WORKSPACE/.docker"
          ${DOCKER} compose -f docker-compose.yml down -v --remove-orphans
        '''
      }
    }

  } // stages

  post {
    always {
      sh '''
        set +e
        echo "Post cleanup: docker down"
        export DOCKER_CONFIG="$WORKSPACE/.docker"
        ${DOCKER} compose -f docker-compose.yml down -v --remove-orphans
      '''
    }
  }
}
