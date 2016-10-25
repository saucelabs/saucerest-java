#!groovy

/* Only keep the 10 most recent builds. */
properties([[$class: 'BuildDiscarderProperty',
                strategy: [$class: 'LogRotator', numToKeepStr: '10']]])

node {
  // Mark the code checkout 'stage'....
  stage 'Checkout'
  // Get some code from a GitHub repository
  checkout scm

  stage 'Clean'
  // Clean any locally modified files and ensure we are actually on master
  // as a failed release could leave the local workspace ahead of master
  sh "git clean -f && git reset --hard origin/master"

  stage 'Build'
  mvn "clean install -B -V -U -e -Dsurefire.useFile=false -Dmaven.test.failure.ignore=true -Dgpg.skip=true"

  stage 'Archive Results'
  step([$class: 'ArtifactArchiver', artifacts: 'target/*.hpi,target/*.jpi'])

  stage 'Publish Results'
  step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])

  // stage 'Run codecov'
  // sh 'virtualenv .venv'
  // sh 'source .venv/bin/activate && pip install codecov && codecov'
}

/* Run maven from tool "mvn" */
void mvn(def args) {
  /* Get jdk tool. */
  String jdktool = tool name: "jdk7", type: 'hudson.model.JDK'

  /* Get the maven tool. */
  def mvnHome = tool name: 'mvn'

  /* Set JAVA_HOME, and special PATH variables. */
  List javaEnv = [
    "PATH+JDK=${jdktool}/bin", "JAVA_HOME=${jdktool}",
  ]

  /* Call maven tool with java envVars. */
  withEnv(javaEnv) {
    timeout(time: 60, unit: 'MINUTES') {
      if (isUnix()) {
        sh "${mvnHome}/bin/mvn ${args}"
      } else {
        bat "${mvnHome}\\bin\\mvn ${args}"
      }
    }
  }
}
