pipeline {
    agent { label "Slave06" }
    environment {
        development_server_ip = '20.127.146.197'
        production_server_ip = ''
        //Add envars here
        backend_docker_image = 'maven:latest'
        backend_docker_args = '-u root'
        backend_artifact_name = 'backend-0.0.1-SNAPSHOT.jar'
        foo = 'bar'
    }
    stages {
        stage('Backend') {
            agent {
                dockerfile {
                    filename 'Dockerfile'
                    dir './devops/docker/'
                    args backend_docker_args
                    reuseNode true
                }
            }
            when {
                changeset '**/backend/*.*'
            }
            stages {
                stage('Build') {
                    steps {
                        echo 'Building backend...'
                        sh '''
                            cd ./backend
                            mvn -B -DskipTests -ntp clean package'''
                    }
                }
                stage('Test') {
                    steps {
                        echo 'Testing backend...'
                        echo 'Skipping for now, no tests configured yet'
                    }
                }
                stage('Deploy to development environment') {
                    steps {
                        echo 'Deploying backend to development environment...'
                        sh '''pwd
                            ls
                        '''
                        ansiblePlaybook disableHostKeyChecking: true, credentialsId: '4366c78a-21a5-4351-828b-786d769290e9', inventory: './devops/ansible/dev-inventory.yml', playbook: './devops/ansible/deploy-backend.yml'
                    }
                }
                stage('Deploy to production environment') {
                    when {
                        branch 'main'
                    }
                    steps {
                        echo 'Deploying backend to production environment...'
                    }
                }
            }
        }
        stage('Frontend') {
            when {
                changeset '**/frontend/*.*'
            }
            stages {
                stage('Build') {
                    steps {
                        echo 'Building frontend...'
                    }
                }
                stage('Test') {
                    steps {
                        echo 'Testing frontend...'
                        echo 'Skipping for now, no tests configured yet'
                    }
                }
                stage('Deploy to development environment') {
                    steps {
                        echo 'Deploying frontend to development environment...'
                    }
                }
                stage('Deploy to production environment') {
                    when {
                        branch 'main'
                    }
                    steps {
                        echo 'Deploying frontend to production environment...'
                    }
                }
            }
        }
    }
}
