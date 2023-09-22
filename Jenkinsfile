pipeline {
    agent { label "Slave08" }
    environment {
        //Add envars here
        backend_docker_args = '-u root'
        frontend_docker_args = '-u root'
    }
    stages {
        stage('Backend') {
            when {
                beforeAgent true
                changeset '**/backend/**'
            }
            agent {
                dockerfile {
                    filename 'Dockerfile-BE'
                    dir './devops/docker/'
                    args backend_docker_args
                    reuseNode true
                }
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
                changeset '**/frontend/**'
            }
            agent {
                dockerfile {
                    filename 'Dockerfile-FE'
                    dir './devops/docker/'
                    args frontend_docker_args
                    reuseNode true
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
                        sh '''
                            echo Building frontend...
                            cd ./frontend
                            rm -rf ./dist
                            npm install
                            npm run build
                        '''
                        echo 'Zipping resulting artifact'
                        sh 'zip -r dist.zip ./frontend/dist'
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
                        echo 'Building frontend...'
                        sh '''
                            cd ./frontend
                            npm install
                            npm run build
                        '''
                        echo 'Zipping resulting artifact'
                        sh 'zip -r dist.zip ./frontend/dist'
                    }
                }
                stage('Test') {
                    steps {
                        echo 'Testing frontend...'
                        echo 'Skipping for now, no tests configured yet'
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
