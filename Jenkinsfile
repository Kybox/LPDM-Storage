pipeline {
    agent any
    tools {
        maven 'Apache Maven 3.5.2'
    }
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/vyjorg/LPDM-Storage'
            }
        }
        stage('Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                }
                failure {
                    error 'The tests failed'
                }
            }
        }
        stage('Deploy') {
            steps {
                sh 'docker stop LPDM-StorageMS'
                sh 'docker rm LPDM-StorageMS'
                sh 'docker-compose -f /var/lib/jenkins/workspace/LPDM_LPDM-Storage_master/docker/dc-lpdm-storage-ms.yml build --no-cache'
                step([$class: 'DockerComposeBuilder', dockerComposeFile: 'docker/dc-lpdm-storage-ms.yml', option: [$class: 'StartAllServices'], useCustomDockerComposeFile: true])
            }
        }
    }
}