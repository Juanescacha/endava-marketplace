pipeline {
    agent {
        kubernetes {
            yamlFile 'pod.yml'
            defaultContainer 'python'
        }
    }

    stages {
       stage('Install Ansible') {
           when {
              anyOf {
                   branch 'development'
                   branch 'main'
              }
           }
           steps {
               sh 'python3 -m pip install ansible'
           }
       }
       stage('Backend') {
            when {
                beforeAgent true
                anyOf {
                      changeset '**/backend/**'
                      changeset '**/Jenkinsfile'
                      changeset '**/pod.yml'
                }
            }
            stages {
                stage('Build') {
                    steps {
                        container('mvn') {
                            echo 'Building backend...'
                            sh '''
                                cd ./backend
                                mvn -B -DskipTests -ntp clean package
                            '''
                        }
                    }
                }
                stage('Test') {
                    steps {
                        container('mvn') {
                            echo 'Testing backend...'
                            echo 'Skipping for now, no tests configured yet'
                        }
                    }
                }
                stage('Deploy to development environment') {
                    when {
                        branch 'development'
                    }
                    steps {
                        echo 'Deploying backend to development environment...'
                        ansiblePlaybook disableHostKeyChecking: true, credentialsId: 'marketplace-servers-key', inventory: './devops/ansible/dev-inventory.yml', playbook: './devops/ansible/deploy-backend.yml'
                    }
                }
                stage('Deploy to production environment') {
                    when {
                        branch 'main'
                    }
                    steps {
                        echo 'Deploying backend to production environment...'
                        ansiblePlaybook disableHostKeyChecking: true, credentialsId: 'marketplace-servers-key', inventory: './devops/ansible/prod-inventory.yml', playbook: './devops/ansible/deploy-backend.yml'
                    }
                }
            }
        }
        stage('Frontend') {
            when {
                beforeAgent true
                anyOf {
                    changeset '**/frontend/**'
                    changeset '**/Jenkinsfile'
                    changeset '**/pod.yml'
                }
            }
            stages {
                stage('Build for development') {
                    environment {
                        VITE_API_URL                    = credentials('marketplace-dev-api-url')
                        VITE_MICROSOFT_LOGIN_URL        = credentials('marketplace-microsoft-login-url')
                        VITE_TENANT_ID                  = credentials('marketplace-tenant-id')
                        VITE_MICROSOFT_LOGIN_URL_END    = credentials('marketplace-microsoft-login-url-end')
                        VITE_CLIENT_ID                  = credentials('marketplace-client-id')
                        VITE_URL_REDIRECT_URI           = credentials('marketplace-dev-url-redirect-uri')
                        VITE_MICROSOFT_LOGIN_PARAMS     = credentials('marketplace-microsoft-login-params')
                        VITE_CLIENT_SECRET              = credentials('marketplace-client-secret')
                    }
                    steps {
                        container('node') {
                            sh '''
                                echo Building frontend...
                                cd ./frontend
                                rm -rf ./dist
                                npm install
                                npm run build
                            '''
                        }
                    }
                }
                stage('Build for production') {
                    when {
                        branch 'main'
                    }
                    environment {
                        //TODO: Same as env, need to update with production server credentials
                        VITE_API_URL                    = credentials('marketplace-dev-api-url')
                        VITE_MICROSOFT_LOGIN_URL        = credentials('marketplace-microsoft-login-url')
                        VITE_TENANT_ID                  = credentials('marketplace-tenant-id')
                        VITE_MICROSOFT_LOGIN_URL_END    = credentials('marketplace-microsoft-login-url-end')
                        VITE_CLIENT_ID                  = credentials('marketplace-client-id')
                        VITE_URL_REDIRECT_URI           = credentials('marketplace-dev-url-redirect-uri')
                        VITE_MICROSOFT_LOGIN_PARAMS     = credentials('marketplace-microsoft-login-params')
                        VITE_CLIENT_SECRET              = credentials('marketplace-client-secret')
                    }
                    steps {
                        container('node') {
                            echo 'Building frontend...'
                            sh '''
                                cd ./frontend
                                npm install
                                npm run build
                            '''
                        }
                    }
                }
                stage('Test') {
                    steps {
                        echo 'Testing frontend...'
                        echo 'Skipping for now, no tests configured yet'
                    }
                }
                stage('Zip Artifact') {
                    steps {
                            sh 'apt update && apt install -y zip'
                            echo 'Zipping resulting artifact'
                            sh 'zip -r dist.zip ./frontend/dist'
                    }
                }
                stage('Deploy to development environment') {
                    when {
                        branch 'development'
                    }
                    steps {
                        echo 'Deploying frontend to development environment...'
                        ansiblePlaybook disableHostKeyChecking: true, credentialsId: 'marketplace-servers-key', inventory: './devops/ansible/dev-inventory.yml', playbook: './devops/ansible/deploy-frontend.yml'
                    }
                }
                stage('Deploy to production environment') {
                    when {
                        branch 'main'
                    }
                    steps {
                        echo 'Deploying frontend to production environment...'
                        ansiblePlaybook disableHostKeyChecking: true, credentialsId: 'marketplace-servers-key', inventory: './devops/ansible/prod-inventory.yml', playbook: './devops/ansible/deploy-frontend.yml'
                    }
                }
            }
        }
    }
}
