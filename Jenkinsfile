#!groovy

def workerNode = "devel12"

pipeline {
	agent {label workerNode}
	triggers {
		pollSCM("H/03 * * * *")
	}
	options {
		timestamps()
	}
	stages {
		stage("clear workspace") {
			steps {
				deleteDir()
				checkout scm
			}
		}
		stage("verify") {
			steps {
				sh "mvn verify"
				junit "target/surefire-reports/TEST-*.xml"
			}
		}
		stage("sonarqube") {
            steps {
                withSonarQubeEnv(installationName: 'sonarqube.dbc.dk') {
                    script {
                        def status = 0

                        def sonarOptions = "-Dsonar.branch.name=${BRANCH_NAME}"
                        if (env.BRANCH_NAME != 'master') {
                            sonarOptions += " -Dsonar.newCode.referenceBranch=master"
                        }

                        // Do sonar via maven
                        status += sh returnStatus: true, script: """
                            mvn -B $sonarOptions sonar:sonar
                        """

                        if (status != 0) {
                            error("build failed")
                        }
                    }
                }
            }
        }
        stage("quality gate") {
            steps {
                // wait for analysis results
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
		stage("deploy to mavenrepo") {
			when {
				branch "master"
			}
			steps {
				sh "mvn jar:jar deploy:deploy"
			}
		}
	}
}
