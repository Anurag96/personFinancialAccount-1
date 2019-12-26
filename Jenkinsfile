@Library('ESI_Build')
import com.esrx.testing.ExternalTest

// - Variables
def _pcfOrganization = 'PersonalFinancialAccount'
def _pcfApplicationName = 'personFinancialAccounts-1'
def _hipChatApiToken = 'Gf5ZlOqAb1kQL36kzjambC2FwHgQmoigEPpNRRWw'
def _hipChatRoom = 'Team_Plutus_Jenkins'
def _jiraProject = 'PEPPFACT'
def _messagingDestinations = ['finacckafkaapi','TP.FINACC.SYNC','TP.FINACC.LATEST','TP.FINACC.CHANGE']
def _messagingDestinationsTemp = ['finacckafkaapi_tmp','TP.FINACC.SYNC.TMP','TP.FINACC.LATEST.TMP','TP.FINACC.CHANGE.TMP']

// Deploy to Dev and QA
//def _pcfProps = [dev: [:]]
def _pcfProps = [dev: [:], qa: [:], uat: [:]]
//def _pcfProps = [dev: [:], qa: [:], integration: [:], preProd: [:], prod: [:]]

// - Dev
_pcfProps.dev.space = 'development'
_pcfProps.dev.appName = _pcfApplicationName + '-dev'
_pcfProps.dev.appNameTemp = _pcfApplicationName + '-dev-candidate'
_pcfProps.dev.route = _pcfApplicationName + '-dev'
_pcfProps.dev.routeTemp = _pcfApplicationName + '-dev-candidate'
_pcfProps.dev.requireApprovalOnDeploy = false
_pcfProps.dev.requireApprovalOnActivate = false
_pcfProps.dev.removeCandidateInstances = true
_pcfProps.dev.notifyOnActivate = true
_pcfProps.dev.messagingDestinations = _messagingDestinations
_pcfProps.dev.messagingDestinationsTemp = _messagingDestinationsTemp
_pcfProps.dev.endpoint = 'https://api.sys.ch3pcf04.express-scripts.com'
_pcfProps.dev.credentials = 'CH3PCF04 - Jenkins Space Developer'
_pcfProps.dev.domain = 'apps.ch3pcf04.express-scripts.com'

// - QA
_pcfProps.qa.space = 'QA'
_pcfProps.qa.appName = _pcfApplicationName + '-qa'
_pcfProps.qa.appNameTemp = _pcfApplicationName + '-qa-candidate'
_pcfProps.qa.route = _pcfApplicationName + '-qa'
_pcfProps.qa.routeTemp = _pcfApplicationName + '-qa-candidate'
_pcfProps.qa.requireApprovalOnDeploy = false
_pcfProps.qa.requireApprovalOnActivate = false
_pcfProps.qa.removeCandidateInstances = true
_pcfProps.qa.notifyOnActivate = true
_pcfProps.qa.shouldBindToAutoscale = true
_pcfProps.qa.messagingDestinations = _messagingDestinations
_pcfProps.qa.messagingDestinationsTemp = _messagingDestinationsTemp

// - UAT
_pcfProps.uat.space = 'UAT'
_pcfProps.uat.appName = _pcfApplicationName + '-uat'
_pcfProps.uat.appNameTemp = _pcfApplicationName + '-uat-candidate'
_pcfProps.uat.route = _pcfApplicationName + '-uat'
_pcfProps.uat.routeTemp = _pcfApplicationName + '-uat-candidate'
_pcfProps.uat.requireApprovalOnDeploy = false
_pcfProps.uat.requireApprovalOnActivate = false
_pcfProps.uat.removeCandidateInstances = true
_pcfProps.uat.notifyOnActivate = true
_pcfProps.uat.shouldBindToAutoscale = true
_pcfProps.uat.messagingDestinations = _messagingDestinations
_pcfProps.uat.messagingDestinationsTemp = _messagingDestinationsTemp

// - Production
//_pcfProps.prod.space = 'Production'
//_pcfProps.prod.credentials = 'PS2PCF02 - Jenkins Space Developer'
//_pcfProps.prod.endpoint = 'https://api.sys.ps2pcf02.express-scripts.com'
//_pcfProps.prod.domain = 'apps.ps2pcf02.express-scripts.com'
//_pcfProps.prod.appName = _pcfApplicationName
//_pcfProps.prod.appNameTemp = _pcfApplicationName + '-candidate'
//_pcfProps.prod.route = _pcfApplicationName
//_pcfProps.prod.routeTemp = _pcfApplicationName + '-candidate'
//_pcfProps.prod.hipChatRoom = _hipChatRoom
//_pcfProps.prod.requireApprovalOnDeploy = true
//_pcfProps.prod.requireApprovalOnActivate = true
//_pcfProps.prod.notifyOnDeploy=true

// Smoke Tests
def _testMap = [DEPLOYDEV: [:], DEPLOYQA: [:], DEPLOYUAT: [:]] //, DEPLOYPROD: [:]]
_testMap.DEPLOYDEV.sequentialTests = [new ExternalTest("PersonalFinancialAccount/personFinancialAccounts-api-Cucumber/personFinancialAccounts-api-Cucumber-dev")]
_testMap.DEPLOYQA.sequentialTests = [new ExternalTest("PersonalFinancialAccount/personFinancialAccounts-api-Cucumber/personFinancialAccounts-api-Cucumber-qa")]
_testMap.DEPLOYUAT.parallelTests = [new ExternalTest("PersonalFinancialAccount/personFinancialAccounts-api-Cucumber/personFinancialAccounts-api-Cucumber-uat")]
//_testMap.DEPLOYPROD.parallelTests = [new ExternalTest("PersonalFinancialAccount/personFinancialAccounts-api-Cucumber/personFinancialAccounts-api-Cucumber-prod")]

esiBuildFlow {
    buildType = 'SpringPcf'
    pomDirectory = './persfinacct-springboot'
    pcfProperties = _pcfProps
    applicationName = _pcfApplicationName
    organization = _pcfOrganization
    hipChatRoom = _hipChatRoom
    hipChatAPI = _hipChatApiToken
    jiraProject = _jiraProject
    //preReleaseBranches = /^develop.*|^feature.*/
    postDeployTesting = _testMap

    // CheckMarx Call
    cxExecuteCheckmarx = true
    cxAvoidDuplicateProjectScans = false
    cxVulnerabilityThresholdEnabled = true
    cxFullScansScheduled = true
    cxFullScanCycle = 10
    cxIncremental = true
    cxGroupId = 'f11f5381-ff0a-4710-8cd0-06af178a5147'
    cxProjectName = 'personFinancialAccounts-1'
    cxPreset = '36'

    // Sonar
    buildGoals = 'clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:3.4.0.905:sonar -U'

}
