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

    stage('Build') {
      steps {
        sh '''
          chmod +x mvnw
          ./mvnw clean package -DskipTests
        '''
      }
    }

    stage('Unit Tests') {
      steps {
        sh '''
          ./mvnw test
        '''
      }
    }

    stage('Integration Tests') {
      steps {
        sh '''
          ./mvnw verify -DskipTests=false
        '''
      }
    }

    stage('Run System (Docker)') {
      steps {
        sh '''
          docker-compose up -d --build
          sleep 20
        '''
      }
    }

    stage('System Test - Senaryo 1') {
      steps {
        sh '''
          cd selenium-tests
          chmod +x ../mvnw
          ../mvnw -Dtest=com.hastane.selenium.Senaryo1_* test \
            -Dsurefire.failIfNoSpecifiedTests=false
        '''
      }
    }

    stage('System Test - Senaryo 2') {
      steps {
        sh '''
          cd selenium-tests
          ../mvnw -Dtest=com.hastane.selenium.Senaryo2_* test \
            -Dsurefire.failIfNoSpecifiedTests=false
        '''
      }
    }

    stage('System Test - Senaryo 3') {
      steps {
        sh '''
          cd selenium-tests
          ../mvnw -Dtest=com.hastane.selenium.Senaryo3_* test \
            -Dsurefire.failIfNoSpecifiedTests=false
        '''
      }
    }

    stage('System Test - Senaryo 4 (UI Smoke)') {
      steps {
        sh '''
          cd selenium-tests
          ../mvnw -Dtest=com.hastane.selenium.Senaryo4_* test \
            -Dsurefire.failIfNoSpecifiedTests=false
        '''
      }
    }

    stage('System Test - Senaryo 5 (Dashboard / API)') {
      steps {
        sh '''
          cd selenium-tests
          ../mvnw -Dtest=com.hastane.selenium.Senaryo5_* test \
            -Dsurefire.failIfNoSpecifiedTests=false
        '''
      }
    }
  }

  post {
    always {
      // ✅ Test Results sekmesi için JUnit raporlarını yayınla
      junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
      junit allowEmptyResults: true, testResults: '**/target/failsafe-reports/*.xml'
      junit allowEmptyResults: true, testResults: 'selenium-tests/target/surefire-reports/*.xml'

      // ✅ Cleanup: asla UNSTABLE olmasın
      sh '''
        set +e

        # 1) normal kapatmayı dene (başarısız olsa bile devam)
        docker-compose down -v --remove-orphans || true

        # 2) "resource is still in use" olan network'ü zorla temizle
        NET="hastane-yonetim_default"

        # network varsa, ona bağlı container'ları bul ve sil
        if docker network inspect "$NET" >/dev/null 2>&1; then
          IDS=$(docker network inspect "$NET" -f '{{range $id, $c := .Containers}}{{println $id}}{{end}}' 2>/dev/null || true)
          if [ -n "$IDS" ]; then
            echo "Network $NET hala kullanılıyor, containerlari siliyorum:"
            echo "$IDS"
            echo "$IDS" | xargs -r docker rm -f || true
          fi

          # network'ü zorla kaldır
          docker network rm "$NET" || true
        fi

        # 3) genel temizlik (opsiyonel ama iyi)
        docker network prune -f || true
      '''
    }
  }
}
