def failMessage = 'Pipeline failed on step: '
def student = 'rrohau'

node(){
    stage('1 - Preparation(checking out SCM)'){
        try {
            checkout([$class: 'GitSCM', branches: [[name: "${student}"]], userRemoteConfigs: [[credentialsId: '4628a122-e41c-4549-b37d-6cdafc49a264', url: 'git@github.com:MNT-Lab/build-t00ls.git']]])
        } catch(Exception e){
            failMessage += "${STAGE_NAME}"
            currentBuild.result = 'FAILURE'
        }
    }
    stage('2 - Building code'){
        try {
            def mvn_install = 'Maven3.6.3'
            withEnv( ["PATH+MAVEN=${tool mvn_install}/bin"] ){
                sh 'mvn -f helloworld-project/helloworld-ws/pom.xml package'
            }
        } catch(Exception e){
            failMessage += "${STAGE_NAME}"
            currentBuild.result = 'FAILURE'
        }
    }
    stage('3 - Sonar scan'){
        try {
            def scanner_install = 'SonarQubeScanner'
            withSonarQubeEnv('SonarQubeServer') {
                    sh """${tool scanner_install}/bin/sonar-scanner -Dsonar.projectName='MNT project' \
                    -Dsonar.java.binaries=helloworld-project/helloworld-ws/target/classes \
                    -Dsonar.projectKey=MNT:project \
                    -Dsonar.projectVersion=2.0 \
                    -Dsonar.sources=."""
            } 
        } catch(Exception e){
            failMessage += "${STAGE_NAME}"
            currentBuild.result = 'FAILURE'
        }
    }
    stage('4 - Testing'){
        try{
            parallel(
                'Stage 1': {
                    stage('Pre-integration-test'){
                        echo "mvn pre-integration-test"
                    }
                },
                'Stage 2': {
                    stage('Integration-test'){
                        echo "mvn integration-test"
                    }
                },
                'Stage 3': {
                    stage('Post-integration-test'){
                        echo "mvn post-integration-test"
                    }
                }
            )
        } catch(Exception e){
            failMessage += "${STAGE_NAME}"
            currentBuild.result = 'FAILURE'
        }
    }
    stage('5 - Triggering job and fetching artefact after finishing'){
        try{
            def triggerJob = "MNTLAB-${student}-child1-build-job"
            build job: "${triggerJob}", parameters: [string(name: 'BRANCH_NAME', value: "${student}")]
            copyArtifacts(projectName: "${triggerJob}", filter: 'output.txt');
        } catch(Exception e){
            failMessage += "${STAGE_NAME}"
            currentBuild.result = 'FAILURE'
        }
    }
}