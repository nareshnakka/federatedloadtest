/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loadtest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author NakkaNar
 */
@ManagedBean
@ViewScoped
public class OnboardingControllerBean implements Serializable {

    String newapplication = "";
    DBConnection DBC;
    Connection connection;

    ArrayList<String> applicationList;
    String selectedApplication;

    ArrayList<String> testTypesList;
    String selectedTestType;
    String newTestType = "";

    String scenarioName;
    String scenarioDescription;
    String baselinePage;
    String confluenceid;
    String pctestid;
    String pcinstanceid;
    String testAuthKey;
    String avgtestduration;
    String concurrentusers;
    String selectedscenarioupload;
    String shortdescriptionupload;
    int selectedAccordian = 0;

    UploadedFile summaryFile;
    File tempFile;
    final String tempFilePath = "C:/UploadedFiles/";
    String selectedFilename;
    String getConfluenceAuthKey;
    String getConfluencePageId;
    String selectedapplicationupload;
    ArrayList<String> applicationlistupload;
    ArrayList<String> testtypelistupload;
    ArrayList<String> scenariolistupload;
    String selectedtesttypeupload;
    String summaryFilePathOnDisk;
    final String payload = "{\"type\": \"page\",\"title\": \"${CONFPAGENAME}\",\"ancestors\": [{\"id\": ${CONFPARENT}}],\"space\": {\"key\": \"AVSDO\"},\"body\": {\"storage\": {\"value\": \"<p>CREATED BY DEVOPS-PERFORMANCE</p>\",\"representation\": \"storage\"}}}";
    static String jenkinsBaseURL;
    static String jenkinsAuthKey;
    final String jenkinsJobPayload = "name=${JOBNAME}&mode=org.jenkinsci.plugins.workflow.job.WorkflowJob&from=&json=%7B%22name%22%3A+%22${JOBNAME}%22%2C+%22mode%22%3A+%22org.jenkinsci.plugins.workflow.job.WorkflowJob%22%2C+%22from%22%3A+%22%22%7D";
    final String jenkinsJobPipePayload = "description=&stapler-class-bag=true&specified=on&_.daysToKeepStr=1&_.numToKeepStr=3&_.artifactDaysToKeepStr=&_.artifactNumToKeepStr=&stapler-class=hudson.tasks.LogRotator&%24class=hudson.tasks.LogRotator&specified=on&specified=on&_.projectUrlStr=&_.displayName=&hint=MAX_SURVIVABILITY&_.buildCount=1&specified=on&parameter.name=TRIGGER_BY&parameter.defaultValue=&parameter.description=&_.trim=on&stapler-class=hudson.model.StringParameterDefinition&%24class=hudson.model.StringParameterDefinition&parameter.name=SUBMITTED_TIME&parameter.defaultValue=&parameter.description=&_.trim=on&stapler-class=hudson.model.StringParameterDefinition&%24class=hudson.model.StringParameterDefinition&parameter.name=BASELINE_DATE&parameter.defaultValue=&parameter.description=&_.trim=on&stapler-class=hudson.model.StringParameterDefinition&%24class=hudson.model.StringParameterDefinition&_.count=1&_.durationName=hour&stapler-class-bag=true&_.upstreamProjects=&ReverseBuildTrigger.threshold=SUCCESS&_.spec=&_.scmpoll_spec=&quiet_period=5&pseudoRemoteTrigger=on&authToken=${AUTKEY}&_.displayNameOrNull=&_.script=%0D%0Adef+TESTID+%3D+${TESTID}%0D%0Adef+INSTANCEID+%3D+${TESTINSTANCEID}%0D%0Adef+PCSERVER+%3D+%27cdlpfpcsvr04.es.ad.adp.com%27%0D%0Adef+PCUSER+%3D+%27nakkanar%27%0D%0Adef+PCPASSWORD+%3D+%27nakkanar%27%0D%0Adef+TIMESLOTDURATION+%3D+%278%27+%2F%2Fin+HRS%0D%0Adef+scenarioName+%3D+%22+${JOBNAME}+%22%0D%0Adef+triggeredBy+%3D+TRIGGER_BY%0D%0Adef+testDuration+%3D+%27${AVGTESTDURATION}%27%0D%0Adef+confluenceID+%3D+%22${CONFLUENCEID}%22%0D%0Adef+isBaseline+%3D+%22+False+%22%0D%0Adef+buildNumber+%3D+BUILD_NUMBER%09%0D%0Adef+baseLineDate+%3D+%27+%5C%22%27%2BBASELINE_DATE%2B%27%5C%22+%27%0D%0Adef+confluenceLink+%3D+%27+%27%0D%0Adef+submittedTime+%3D+SUBMITTED_TIME%0D%0Adef+mailCC+%3D+%27AVS_DevOps_Performance%40ADP.com%27%0D%0A%0D%0Anode%28%27master%27%29%7B%0D%0A++++%0D%0A%09stage%28%27%231.+Trigger+Request+Confirmation+Mail%27%29%7B%0D%0A%09%09testStatus+%3D+%27In+Progress%27%0D%0A++++%09mailBody+%3D+%27%3Cbody+bgcolor%3D%5C%22%23708090%5C%22%3E%3Ccenter%3E%3Ch3%3ETest+Infomration%3C%2Fh3%3E%3C%2Fbr%3E%3Ctable+border%3D%5C%222%5C%22%3E%3C%2Fbr%3E%3Ctable%3E%3Ctr%3E%3Ctd%3ETest+Name+%3A+%3C%2Ftd%3E%3Ctd%3E%27%2BscenarioName%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3ETriggered+By+%3A+%3C%2Ftd%3E%3Ctd%3E%27%2BtriggeredBy%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3EConfluence+Link+%3A+%3C%2Ftd%3E%3Ctd%3EWe+will+update+you+soon%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3ETest+Status%3A%3C%2Ftd%3E%3Ctd%3E%27%2BtestStatus%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3EEstimated+Duration+%3A%3C%2Ftd%3E%3Ctd%3E%27%2BtestDuration%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3C%2Ftable%3E%3C%2Ftable%3E%3C%2Fcenter%3E%3C%2Fbody%3E%27%0D%0A++++++++emailext+attachLog%3A+false%2C+body%3A+mailBody%2C+replyTo%3A+mailCC%2C+subject%3A+%27Your+request+is+in+progress%27%2C+to%3A+TRIGGER_BY%2B%22%2C%22%2BmailCC%0D%0A%09%0D%0A%09%7D%0D%0A%09%0D%0A%0D%0A++++stage%28%27%232.+Run+PC+Scenario+-+SCP+Landing+Page++%27%29%7B%0D%0A++++++++try%7B%0D%0A+++++++++++%0D%0A++++++++++++pcBuild+HTTPSProtocol%3A+false%2C+addRunToTrendReport%3A+%27NO_TREND%27%2C+almDomain%3A+%27AVS%27%2C+almPassword%3A+%27nakkanar%27%2C+almProject%3A+%27CAPS%27%2C+almUserName%3A+%27nakkanar%27%2C+autoTestInstanceID%3A+%27AUTO%27%2C+description%3A+%27pcbuild%27%2C+pcServerName%3A+%27cdlpfpcsvr04.es.ad.adp.com%27%2C+postRunAction%3A+%27COLLATE_AND_ANALYZE%27%2C+proxyOutPassword%3A+%27%27%2C+proxyOutURL%3A+%27%27%2C+proxyOutUser%3A+%27%27%2C+serverAndPort%3A+%27http%3A%2F%2Fcdlavscicdwin1%3A8080%2Fjenkins%27%2C+statusBySLA%3A+false%2C+testId%3A+%27${TESTID}%27%2C+testInstanceId%3A+%27%27%2C+timeslotDurationHours%3A+%278%27%2C+timeslotDurationMinutes%3A+%2730%27%2C+trendReportId%3A+%27%27%2C+vudsMode%3A+false%0D%0A%09%09%09%0D%0A%09%09%09testStatus+%3D+%27Completed%27%0D%0A++++++++%7D%0D%0A++++++++catch%28Exception+e%29%7B%0D%0A%09%09%09testStatus+%3D+%27Failed%27%0D%0A++++++++%7D%0D%0A++++%7D%0D%0A++++%0D%0A++++stage%28%27%233.+Extract+%26+Update+Results%27%29%7B%0D%0A+++++++++++%0D%0A%09%09%09def+jarCommand+%3D+%22java+-jar+C%3A%2FCICD%2FHTMLExtractor%2FExtractResults.jar%22%2B%22+%5C%22%22%2BBASELINE_DATE%2B%22%5C%22+%22%2B%22+%5C%22%22%2BSUBMITTED_TIME%2B%22%5C%22+%22%2B%22+%5C%22FALSE%5C%22+%22%2B%22+%5C%22%22%2BbuildNumber%2B%22%5C%22+%22%2B%22+%5C%22%22%2BconfluenceID%2B%22%5C%22+%22%2B%22+%5C%22%22%2BtriggeredBy%2B%22%5C%22+%22%2B%22+%5C%22%22%2BJOB_NAME%2B%22%5C%22+%22%3B%0D%0A%09++++++++bat+jarCommand%0D%0A%09+%7D%0D%0A%09%0D%0A%09stage%28%27%234.+Send+Email+to+User%27%29%7B%0D%0A%09%09mailBody+%3D+%27%3Cbody+bgcolor%3D%5C%22%23708090%5C%22%3E%3C%2Fbr%3E%3Ccenter%3E%3Ch3%3ETest+Infomration%3C%2Fh3%3E%3Ctable+border%3D%5C%222%5C%22%3E%3C%2Fbr%3E%3C%2Fbr%3E%3Ctable%3E%3Ctr%3E%3Ctd%3ETest+Name+%3A+%3C%2Ftd%3E%3Ctd%3E%27%2BscenarioName%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3ETriggered+By+%3A+%3C%2Ftd%3E%3Ctd%3E%27%2BtriggeredBy%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3EConfluence+Link+%3A+%3C%2Ftd%3E%3Ctd%3E%27%2BconfluenceLink%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3ETest+Status%3A%3C%2Ftd%3E%3Ctd%3E%27%2BtestStatus%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3C%2Ftable%3E%3C%2Ftable%3E%3C%2Fcenter%3E%3C%2Fbody%3E%27%0D%0A++++%09emailext+attachLog%3A+false%2C+body%3A+mailBody%2C+replyTo%3A+mailCC%2C+subject%3A+%27Test+is+completed%27%2C+to%3A+TRIGGER_BY%2B%22%2C%22%2BmailCC%0D%0A%09%7D%0D%0A++++%0D%0A%7D&_.sandbox=on&stapler-class=org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition&%24class=org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition&stapler-class=org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition&%24class=org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition&core%3Aapply=true&json=%7B%22description%22%3A+%22%22%2C+%22properties%22%3A+%7B%22stapler-class-bag%22%3A+%22true%22%2C+%22jenkins-model-BuildDiscarderProperty%22%3A+%7B%22specified%22%3A+true%2C+%22%22%3A+%220%22%2C+%22strategy%22%3A+%7B%22daysToKeepStr%22%3A+%221%22%2C+%22numToKeepStr%22%3A+%223%22%2C+%22artifactDaysToKeepStr%22%3A+%22%22%2C+%22artifactNumToKeepStr%22%3A+%22%22%2C+%22stapler-class%22%3A+%22hudson.tasks.LogRotator%22%2C+%22%24class%22%3A+%22hudson.tasks.LogRotator%22%7D%7D%2C+%22org-jenkinsci-plugins-workflow-job-properties-DisableConcurrentBuildsJobProperty%22%3A+%7B%22specified%22%3A+true%7D%2C+%22org-jenkinsci-plugins-workflow-job-properties-DisableResumeJobProperty%22%3A+%7B%22specified%22%3A+true%7D%2C+%22com-coravy-hudson-plugins-github-GithubProjectProperty%22%3A+%7B%7D%2C+%22org-jenkinsci-plugins-workflow-job-properties-DurabilityHintJobProperty%22%3A+%7B%22specified%22%3A+false%2C+%22hint%22%3A+%22MAX_SURVIVABILITY%22%7D%2C+%22org-jenkinsci-plugins-pipeline-modeldefinition-properties-PreserveStashesJobProperty%22%3A+%7B%22specified%22%3A+false%2C+%22buildCount%22%3A+%221%22%7D%2C+%22hudson-model-ParametersDefinitionProperty%22%3A+%7B%22specified%22%3A+true%2C+%22parameterDefinitions%22%3A+%5B%7B%22name%22%3A+%22TRIGGER_BY%22%2C+%22defaultValue%22%3A+%22%22%2C+%22description%22%3A+%22%22%2C+%22trim%22%3A+true%2C+%22stapler-class%22%3A+%22hudson.model.StringParameterDefinition%22%2C+%22%24class%22%3A+%22hudson.model.StringParameterDefinition%22%7D%2C+%7B%22name%22%3A+%22SUBMITTED_TIME%22%2C+%22defaultValue%22%3A+%22%22%2C+%22description%22%3A+%22%22%2C+%22trim%22%3A+true%2C+%22stapler-class%22%3A+%22hudson.model.StringParameterDefinition%22%2C+%22%24class%22%3A+%22hudson.model.StringParameterDefinition%22%7D%2C+%7B%22name%22%3A+%22BASELINE_DATE%22%2C+%22defaultValue%22%3A+%22%22%2C+%22description%22%3A+%22%22%2C+%22trim%22%3A+true%2C+%22stapler-class%22%3A+%22hudson.model.StringParameterDefinition%22%2C+%22%24class%22%3A+%22hudson.model.StringParameterDefinition%22%7D%5D%7D%2C+%22jenkins-branch-RateLimitBranchProperty%24JobPropertyImpl%22%3A+%7B%7D%2C+%22org-jenkinsci-plugins-workflow-job-properties-PipelineTriggersJobProperty%22%3A+%7B%22triggers%22%3A+%7B%22stapler-class-bag%22%3A+%22true%22%7D%7D%7D%2C+%22disable%22%3A+false%2C+%22hasCustomQuietPeriod%22%3A+false%2C+%22quiet_period%22%3A+%225%22%2C+%22pseudoRemoteTrigger%22%3A+%7B%22authToken%22%3A+%22${AUTKEY}%22%7D%2C+%22displayNameOrNull%22%3A+%22%22%2C+%22%22%3A+%220%22%2C+%22definition%22%3A+%7B%22script%22%3A+%22%5Cndef+TESTID+%3D+${TESTID}%5Cndef+INSTANCEID+%3D+${TESTINSTANCEID}%5Cndef+PCSERVER+%3D+%27cdlpfpcsvr04.es.ad.adp.com%27%5Cndef+PCUSER+%3D+%27nakkanar%27%5Cndef+PCPASSWORD+%3D+%27nakkanar%27%5Cndef+TIMESLOTDURATION+%3D+%278%27+%2F%2Fin+HRS%5Cndef+scenarioName+%3D+%5C%22+${JOBNAME}+%5C%22%5Cndef+triggeredBy+%3D+TRIGGER_BY%5Cndef+testDuration+%3D+%27${AVGTESTDURATION}%27%5Cndef+confluenceID+%3D+%5C%22${CONFLUENCEID}%5C%22%5Cndef+isBaseline+%3D+%5C%22+False+%5C%22%5Cndef+buildNumber+%3D+BUILD_NUMBER%5Ct%5Cndef+baseLineDate+%3D+%27+%5C%5C%5C%22%27%2BBASELINE_DATE%2B%27%5C%5C%5C%22+%27%5Cndef+confluenceLink+%3D+%27+%27%5Cndef+submittedTime+%3D+SUBMITTED_TIME%5Cndef+mailCC+%3D+%27AVS_DevOps_Performance%40ADP.com%27%5Cn%5Cnnode%28%27master%27%29%7B%5Cn++++%5Cn%5Ctstage%28%27%231.+Trigger+Request+Confirmation+Mail%27%29%7B%5Cn%5Ct%5CttestStatus+%3D+%27In+Progress%27%5Cn++++%5CtmailBody+%3D+%27%3Cbody+bgcolor%3D%5C%5C%5C%22%23708090%5C%5C%5C%22%3E%3Ccenter%3E%3Ch3%3ETest+Infomration%3C%2Fh3%3E%3C%2Fbr%3E%3Ctable+border%3D%5C%5C%5C%222%5C%5C%5C%22%3E%3C%2Fbr%3E%3Ctable%3E%3Ctr%3E%3Ctd%3ETest+Name+%3A+%3C%2Ftd%3E%3Ctd%3E%27%2BscenarioName%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3ETriggered+By+%3A+%3C%2Ftd%3E%3Ctd%3E%27%2BtriggeredBy%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3EConfluence+Link+%3A+%3C%2Ftd%3E%3Ctd%3EWe+will+update+you+soon%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3ETest+Status%3A%3C%2Ftd%3E%3Ctd%3E%27%2BtestStatus%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3EEstimated+Duration+%3A%3C%2Ftd%3E%3Ctd%3E%27%2BtestDuration%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3C%2Ftable%3E%3C%2Ftable%3E%3C%2Fcenter%3E%3C%2Fbody%3E%27%5Cn++++++++emailext+attachLog%3A+false%2C+body%3A+mailBody%2C+replyTo%3A+mailCC%2C+subject%3A+%27Your+request+is+in+progress%27%2C+to%3A+TRIGGER_BY%2B%5C%22%2C%5C%22%2BmailCC%5Cn%5Ct%5Cn%5Ct%7D%5Cn%5Ct%5Cn%5Cn++++stage%28%27%232.+Run+PC+Scenario+-+SCP+Landing+Page++%27%29%7B%5Cn++++++++try%7B%5Cn+++++++++++%5Cn++++++++++++pcBuild+HTTPSProtocol%3A+false%2C+addRunToTrendReport%3A+%27NO_TREND%27%2C+almDomain%3A+%27AVS%27%2C+almPassword%3A+%27nakkanar%27%2C+almProject%3A+%27CAPS%27%2C+almUserName%3A+%27nakkanar%27%2C+autoTestInstanceID%3A+%27AUTO%27%2C+description%3A+%27pcbuild%27%2C+pcServerName%3A+%27cdlpfpcsvr04.es.ad.adp.com%27%2C+postRunAction%3A+%27COLLATE_AND_ANALYZE%27%2C+proxyOutPassword%3A+%27%27%2C+proxyOutURL%3A+%27%27%2C+proxyOutUser%3A+%27%27%2C+serverAndPort%3A+%27http%3A%2F%2Fcdlavscicdwin1%3A8080%2Fjenkins%27%2C+statusBySLA%3A+false%2C+testId%3A+%27${TESTID}%27%2C+testInstanceId%3A+%27%27%2C+timeslotDurationHours%3A+%278%27%2C+timeslotDurationMinutes%3A+%2730%27%2C+trendReportId%3A+%27%27%2C+vudsMode%3A+false%5Cn%5Ct%5Ct%5Ct%5Cn%5Ct%5Ct%5CttestStatus+%3D+%27Completed%27%5Cn++++++++%7D%5Cn++++++++catch%28Exception+e%29%7B%5Cn%5Ct%5Ct%5CttestStatus+%3D+%27Failed%27%5Cn++++++++%7D%5Cn++++%7D%5Cn++++%5Cn++++stage%28%27%233.+Extract+%26+Update+Results%27%29%7B%5Cn+++++++++++%5Cn%5Ct%5Ct%5Ctdef+jarCommand+%3D+%5C%22java+-jar+C%3A%2FCICD%2FHTMLExtractor%2FExtractResults.jar%5C%22%2B%5C%22+%5C%5C%5C%22%5C%22%2BBASELINE_DATE%2B%5C%22%5C%5C%5C%22+%5C%22%2B%5C%22+%5C%5C%5C%22%5C%22%2BSUBMITTED_TIME%2B%5C%22%5C%5C%5C%22+%5C%22%2B%5C%22+%5C%5C%5C%22FALSE%5C%5C%5C%22+%5C%22%2B%5C%22+%5C%5C%5C%22%5C%22%2BbuildNumber%2B%5C%22%5C%5C%5C%22+%5C%22%2B%5C%22+%5C%5C%5C%22%5C%22%2BconfluenceID%2B%5C%22%5C%5C%5C%22+%5C%22%2B%5C%22+%5C%5C%5C%22%5C%22%2BtriggeredBy%2B%5C%22%5C%5C%5C%22+%5C%22%2B%5C%22+%5C%5C%5C%22%5C%22%2BJOB_NAME%2B%5C%22%5C%5C%5C%22+%5C%22%3B%5Cn%5Ct++++++++bat+jarCommand%5Cn%5Ct+%7D%5Cn%5Ct%5Cn%5Ctstage%28%27%234.+Send+Email+to+User%27%29%7B%5Cn%5Ct%5CtmailBody+%3D+%27%3Cbody+bgcolor%3D%5C%5C%5C%22%23708090%5C%5C%5C%22%3E%3C%2Fbr%3E%3Ccenter%3E%3Ch3%3ETest+Infomration%3C%2Fh3%3E%3Ctable+border%3D%5C%5C%5C%222%5C%5C%5C%22%3E%3C%2Fbr%3E%3C%2Fbr%3E%3Ctable%3E%3Ctr%3E%3Ctd%3ETest+Name+%3A+%3C%2Ftd%3E%3Ctd%3E%27%2BscenarioName%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3ETriggered+By+%3A+%3C%2Ftd%3E%3Ctd%3E%27%2BtriggeredBy%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3EConfluence+Link+%3A+%3C%2Ftd%3E%3Ctd%3E%27%2BconfluenceLink%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3ETest+Status%3A%3C%2Ftd%3E%3Ctd%3E%27%2BtestStatus%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3C%2Ftable%3E%3C%2Ftable%3E%3C%2Fcenter%3E%3C%2Fbody%3E%27%5Cn++++%5Ctemailext+attachLog%3A+false%2C+body%3A+mailBody%2C+replyTo%3A+mailCC%2C+subject%3A+%27Test+is+completed%27%2C+to%3A+TRIGGER_BY%2B%5C%22%2C%5C%22%2BmailCC%5Cn%5Ct%7D%5Cn++++%5Cn%7D%22%2C+%22%22%3A+%22%5Cu0001%5Cu0001%22%2C+%22sandbox%22%3A+true%2C+%22stapler-class%22%3A+%22org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition%22%2C+%22%24class%22%3A+%22org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition%22%7D%2C+%22core%3Aapply%22%3A+%22true%22%7D";
    //final String jenkinsJobPipePayload = "description=&stapler-class-bag=true&specified=on&_.daysToKeepStr=1&_.numToKeepStr=3&_.artifactDaysToKeepStr=&_.artifactNumToKeepStr=&stapler-class=hudson.tasks.LogRotator&%24class=hudson.tasks.LogRotator&specified=on&specified=on&_.projectUrlStr=&_.displayName=&hint=MAX_SURVIVABILITY&_.buildCount=1&specified=on&parameter.name=TRIGGER_BY&parameter.defaultValue=&parameter.description=&_.trim=on&stapler-class=hudson.model.StringParameterDefinition&%24class=hudson.model.StringParameterDefinition&parameter.name=SUBMITTED_TIME&parameter.defaultValue=&parameter.description=&_.trim=on&stapler-class=hudson.model.StringParameterDefinition&%24class=hudson.model.StringParameterDefinition&parameter.name=BASELINE_DATE&parameter.defaultValue=&parameter.description=&_.trim=on&stapler-class=hudson.model.StringParameterDefinition&%24class=hudson.model.StringParameterDefinition&_.count=1&_.durationName=hour&stapler-class-bag=true&_.upstreamProjects=&ReverseBuildTrigger.threshold=SUCCESS&_.spec=&_.scmpoll_spec=&quiet_period=5&pseudoRemoteTrigger=on&authToken=${AUTKEY}&_.displayNameOrNull=&_.script=%0D%0Adef+TESTID+%3D+${TESTID}%0D%0Adef+INSTANCEID+%3D+${TESTINSTANCEID}%0D%0Adef+PCSERVER+%3D+%27cdlpfpcsvr04.es.ad.adp.com%27%0D%0Adef+PCUSER+%3D+%27nakkanar%27%0D%0Adef+PCPASSWORD+%3D+%27nakkanar%27%0D%0Adef+TIMESLOTDURATION+%3D+%278%27+%2F%2Fin+HRS%0D%0Adef+scenarioName+%3D+%22+${JOBNAME}+%22%0D%0Adef+triggeredBy+%3D+TRIGGER_BY%0D%0Adef+testDuration+%3D+%27${AVGTESTDURATION}%27%0D%0Adef+confluenceID+%3D+%22${CONFLUENCEID}%22%0D%0Adef+isBaseline+%3D+%22+False+%22%0D%0Adef+buildNumber+%3D+BUILD_NUMBER%09%0D%0Adef+baseLineDate+%3D+%27+%5C%22%27%2BBASELINE_DATE%2B%27%5C%22+%27%0D%0Adef+confluenceLink+%3D+%27+%27%0D%0Adef+submittedTime+%3D+SUBMITTED_TIME%0D%0Adef+mailCC+%3D+%27AVS_DevOps_Performance%40ADP.com%27%0D%0A%0D%0Anode%28%27master%27%29%7B%0D%0A++++%0D%0A%09stage%28%27%231.+Trigger+Request+Confirmation+Mail%27%29%7B%0D%0A%09%09testStatus+%3D+%27In+Progress%27%0D%0A++++%09mailBody+%3D+%27%3Cbody+bgcolor%3D%5C%22%23708090%5C%22%3E%3Ccenter%3E%3Ch3%3ETest+Infomration%3C%2Fh3%3E%3C%2Fbr%3E%3Ctable+border%3D%5C%222%5C%22%3E%3C%2Fbr%3E%3Ctable%3E%3Ctr%3E%3Ctd%3ETest+Name+%3A+%3C%2Ftd%3E%3Ctd%3E%27%2BscenarioName%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3ETriggered+By+%3A+%3C%2Ftd%3E%3Ctd%3E%27%2BtriggeredBy%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3EConfluence+Link+%3A+%3C%2Ftd%3E%3Ctd%3EWe+will+update+you+soon%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3ETest+Status%3A%3C%2Ftd%3E%3Ctd%3E%27%2BtestStatus%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3EEstimated+Duration+%3A%3C%2Ftd%3E%3Ctd%3E%27%2BtestDuration%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3C%2Ftable%3E%3C%2Ftable%3E%3C%2Fcenter%3E%3C%2Fbody%3E%27%0D%0A++++++++emailext+attachLog%3A+false%2C+body%3A+mailBody%2C+replyTo%3A+mailCC%2C+subject%3A+%27Your+request+is+in+progress%27%2C+to%3A+TRIGGER_BY%2B%22%2C%22%2BmailCC%0D%0A%09%0D%0A%09%7D%0D%0A%09%0D%0A%0D%0A++++stage%28%27%232.+Run+PC+Scenario+-+SCP+Landing+Page++%27%29%7B%0D%0A++++++++try%7B%0D%0A+++++++++++%0D%0A++++++++++++pcBuild+HTTPSProtocol%3A+false%2C+addRunToTrendReport%3A+%27NO_TREND%27%2C+almDomain%3A+%27AVS%27%2C+almPassword%3A+%27nakkanar%27%2C+almProject%3A+%27CAPS%27%2C+almUserName%3A+%27nakkanar%27%2C+autoTestInstanceID%3A+%27AUTO%27%2C+description%3A+%27pcbuild%27%2C+pcServerName%3A+%27cdlpfpcsvr04.es.ad.adp.com%27%2C+postRunAction%3A+%27COLLATE_AND_ANALYZE%27%2C+proxyOutPassword%3A+%27%27%2C+proxyOutURL%3A+%27%27%2C+proxyOutUser%3A+%27%27%2C+serverAndPort%3A+%27http%3A%2F%2Fcdlavscicdwin1%3A8080%2Fjenkins%27%2C+statusBySLA%3A+false%2C+testId%3A+%27${TESTID}%27%2C+testInstanceId%3A+%27%27%2C+timeslotDurationHours%3A+%278%27%2C+timeslotDurationMinutes%3A+%2730%27%2C+trendReportId%3A+%27%27%2C+vudsMode%3A+false%0D%0A%09%09%09%0D%0A%09%09%09testStatus+%3D+%27Completed%27%0D%0A++++++++%7D%0D%0A++++++++catch%28Exception+e%29%7B%0D%0A%09%09%09testStatus+%3D+%27Failed%27%0D%0A++++++++%7D%0D%0A++++%7D%0D%0A%09%0D%0A%09stage%28%27%234.+Send+Email+to+User%27%29%7B%0D%0A%09%09mailBody+%3D+%27%3Cbody+bgcolor%3D%5C%22%23708090%5C%22%3E%3C%2Fbr%3E%3Ccenter%3E%3Ch3%3ETest+Infomration%3C%2Fh3%3E%3Ctable+border%3D%5C%222%5C%22%3E%3C%2Fbr%3E%3C%2Fbr%3E%3Ctable%3E%3Ctr%3E%3Ctd%3ETest+Name+%3A+%3C%2Ftd%3E%3Ctd%3E%27%2BscenarioName%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3ETriggered+By+%3A+%3C%2Ftd%3E%3Ctd%3E%27%2BtriggeredBy%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3EConfluence+Link+%3A+%3C%2Ftd%3E%3Ctd%3E%27%2BconfluenceLink%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3ETest+Status%3A%3C%2Ftd%3E%3Ctd%3E%27%2BtestStatus%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3C%2Ftable%3E%3C%2Ftable%3E%3C%2Fcenter%3E%3C%2Fbody%3E%27%0D%0A++++%09emailext+attachLog%3A+false%2C+body%3A+mailBody%2C+replyTo%3A+mailCC%2C+subject%3A+%27Test+is+completed%27%2C+to%3A+TRIGGER_BY%2B%22%2C%22%2BmailCC%0D%0A%09%7D%0D%0A++++%0D%0A%7D&_.sandbox=on&stapler-class=org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition&%24class=org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition&stapler-class=org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition&%24class=org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition&core%3Aapply=true&json=%7B%22description%22%3A+%22%22%2C+%22properties%22%3A+%7B%22stapler-class-bag%22%3A+%22true%22%2C+%22jenkins-model-BuildDiscarderProperty%22%3A+%7B%22specified%22%3A+true%2C+%22%22%3A+%220%22%2C+%22strategy%22%3A+%7B%22daysToKeepStr%22%3A+%221%22%2C+%22numToKeepStr%22%3A+%223%22%2C+%22artifactDaysToKeepStr%22%3A+%22%22%2C+%22artifactNumToKeepStr%22%3A+%22%22%2C+%22stapler-class%22%3A+%22hudson.tasks.LogRotator%22%2C+%22%24class%22%3A+%22hudson.tasks.LogRotator%22%7D%7D%2C+%22org-jenkinsci-plugins-workflow-job-properties-DisableConcurrentBuildsJobProperty%22%3A+%7B%22specified%22%3A+true%7D%2C+%22org-jenkinsci-plugins-workflow-job-properties-DisableResumeJobProperty%22%3A+%7B%22specified%22%3A+true%7D%2C+%22com-coravy-hudson-plugins-github-GithubProjectProperty%22%3A+%7B%7D%2C+%22org-jenkinsci-plugins-workflow-job-properties-DurabilityHintJobProperty%22%3A+%7B%22specified%22%3A+false%2C+%22hint%22%3A+%22MAX_SURVIVABILITY%22%7D%2C+%22org-jenkinsci-plugins-pipeline-modeldefinition-properties-PreserveStashesJobProperty%22%3A+%7B%22specified%22%3A+false%2C+%22buildCount%22%3A+%221%22%7D%2C+%22hudson-model-ParametersDefinitionProperty%22%3A+%7B%22specified%22%3A+true%2C+%22parameterDefinitions%22%3A+%5B%7B%22name%22%3A+%22TRIGGER_BY%22%2C+%22defaultValue%22%3A+%22%22%2C+%22description%22%3A+%22%22%2C+%22trim%22%3A+true%2C+%22stapler-class%22%3A+%22hudson.model.StringParameterDefinition%22%2C+%22%24class%22%3A+%22hudson.model.StringParameterDefinition%22%7D%2C+%7B%22name%22%3A+%22SUBMITTED_TIME%22%2C+%22defaultValue%22%3A+%22%22%2C+%22description%22%3A+%22%22%2C+%22trim%22%3A+true%2C+%22stapler-class%22%3A+%22hudson.model.StringParameterDefinition%22%2C+%22%24class%22%3A+%22hudson.model.StringParameterDefinition%22%7D%2C+%7B%22name%22%3A+%22BASELINE_DATE%22%2C+%22defaultValue%22%3A+%22%22%2C+%22description%22%3A+%22%22%2C+%22trim%22%3A+true%2C+%22stapler-class%22%3A+%22hudson.model.StringParameterDefinition%22%2C+%22%24class%22%3A+%22hudson.model.StringParameterDefinition%22%7D%5D%7D%2C+%22jenkins-branch-RateLimitBranchProperty%24JobPropertyImpl%22%3A+%7B%7D%2C+%22org-jenkinsci-plugins-workflow-job-properties-PipelineTriggersJobProperty%22%3A+%7B%22triggers%22%3A+%7B%22stapler-class-bag%22%3A+%22true%22%7D%7D%7D%2C+%22disable%22%3A+false%2C+%22hasCustomQuietPeriod%22%3A+false%2C+%22quiet_period%22%3A+%225%22%2C+%22pseudoRemoteTrigger%22%3A+%7B%22authToken%22%3A+%22${AUTKEY}%22%7D%2C+%22displayNameOrNull%22%3A+%22%22%2C+%22%22%3A+%220%22%2C+%22definition%22%3A+%7B%22script%22%3A+%22%5Cndef+TESTID+%3D+${TESTID}%5Cndef+INSTANCEID+%3D+${TESTINSTANCEID}%5Cndef+PCSERVER+%3D+%27cdlpfpcsvr04.es.ad.adp.com%27%5Cndef+PCUSER+%3D+%27nakkanar%27%5Cndef+PCPASSWORD+%3D+%27nakkanar%27%5Cndef+TIMESLOTDURATION+%3D+%278%27+%2F%2Fin+HRS%5Cndef+scenarioName+%3D+%5C%22+${JOBNAME}+%5C%22%5Cndef+triggeredBy+%3D+TRIGGER_BY%5Cndef+testDuration+%3D+%27${AVGTESTDURATION}%27%5Cndef+confluenceID+%3D+%5C%22${CONFLUENCEID}%5C%22%5Cndef+isBaseline+%3D+%5C%22+False+%5C%22%5Cndef+buildNumber+%3D+BUILD_NUMBER%5Ct%5Cndef+baseLineDate+%3D+%27+%5C%5C%5C%22%27%2BBASELINE_DATE%2B%27%5C%5C%5C%22+%27%5Cndef+confluenceLink+%3D+%27+%27%5Cndef+submittedTime+%3D+SUBMITTED_TIME%5Cndef+mailCC+%3D+%27AVS_DevOps_Performance%40ADP.com%27%5Cn%5Cnnode%28%27master%27%29%7B%5Cn++++%5Cn%5Ctstage%28%27%231.+Trigger+Request+Confirmation+Mail%27%29%7B%5Cn%5Ct%5CttestStatus+%3D+%27In+Progress%27%5Cn++++%5CtmailBody+%3D+%27%3Cbody+bgcolor%3D%5C%5C%5C%22%23708090%5C%5C%5C%22%3E%3Ccenter%3E%3Ch3%3ETest+Infomration%3C%2Fh3%3E%3C%2Fbr%3E%3Ctable+border%3D%5C%5C%5C%222%5C%5C%5C%22%3E%3C%2Fbr%3E%3Ctable%3E%3Ctr%3E%3Ctd%3ETest+Name+%3A+%3C%2Ftd%3E%3Ctd%3E%27%2BscenarioName%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3ETriggered+By+%3A+%3C%2Ftd%3E%3Ctd%3E%27%2BtriggeredBy%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3EConfluence+Link+%3A+%3C%2Ftd%3E%3Ctd%3EWe+will+update+you+soon%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3ETest+Status%3A%3C%2Ftd%3E%3Ctd%3E%27%2BtestStatus%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3EEstimated+Duration+%3A%3C%2Ftd%3E%3Ctd%3E%27%2BtestDuration%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3C%2Ftable%3E%3C%2Ftable%3E%3C%2Fcenter%3E%3C%2Fbody%3E%27%5Cn++++++++emailext+attachLog%3A+false%2C+body%3A+mailBody%2C+replyTo%3A+mailCC%2C+subject%3A+%27Your+request+is+in+progress%27%2C+to%3A+TRIGGER_BY%2B%5C%22%2C%5C%22%2BmailCC%5Cn%5Ct%5Cn%5Ct%7D%5Cn%5Ct%5Cn%5Cn++++stage%28%27%232.+Run+PC+Scenario+-+SCP+Landing+Page++%27%29%7B%5Cn++++++++try%7B%5Cn+++++++++++%5Cn++++++++++++pcBuild+HTTPSProtocol%3A+false%2C+addRunToTrendReport%3A+%27NO_TREND%27%2C+almDomain%3A+%27AVS%27%2C+almPassword%3A+%27nakkanar%27%2C+almProject%3A+%27CAPS%27%2C+almUserName%3A+%27nakkanar%27%2C+autoTestInstanceID%3A+%27AUTO%27%2C+description%3A+%27pcbuild%27%2C+pcServerName%3A+%27cdlpfpcsvr04.es.ad.adp.com%27%2C+postRunAction%3A+%27COLLATE_AND_ANALYZE%27%2C+proxyOutPassword%3A+%27%27%2C+proxyOutURL%3A+%27%27%2C+proxyOutUser%3A+%27%27%2C+serverAndPort%3A+%27http%3A%2F%2Fcdlavscicdwin1%3A8080%2Fjenkins%27%2C+statusBySLA%3A+false%2C+testId%3A+%27${TESTID}%27%2C+testInstanceId%3A+%27%27%2C+timeslotDurationHours%3A+%278%27%2C+timeslotDurationMinutes%3A+%2730%27%2C+trendReportId%3A+%27%27%2C+vudsMode%3A+false%5Cn%5Ct%5Ct%5Ct%5Cn%5Ct%5Ct%5CttestStatus+%3D+%27Completed%27%5Cn++++++++%7D%5Cn++++++++catch%28Exception+e%29%7B%5Cn%5Ct%5Ct%5CttestStatus+%3D+%27Failed%27%5Cn++++++++%7D%5Cn++++%7D%5Cn%5Ct%5Cn%5Ctstage%28%27%234.+Send+Email+to+User%27%29%7B%5Cn%5Ct%5CtmailBody+%3D+%27%3Cbody+bgcolor%3D%5C%5C%5C%22%23708090%5C%5C%5C%22%3E%3C%2Fbr%3E%3Ccenter%3E%3Ch3%3ETest+Infomration%3C%2Fh3%3E%3Ctable+border%3D%5C%5C%5C%222%5C%5C%5C%22%3E%3C%2Fbr%3E%3C%2Fbr%3E%3Ctable%3E%3Ctr%3E%3Ctd%3ETest+Name+%3A+%3C%2Ftd%3E%3Ctd%3E%27%2BscenarioName%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3ETriggered+By+%3A+%3C%2Ftd%3E%3Ctd%3E%27%2BtriggeredBy%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3EConfluence+Link+%3A+%3C%2Ftd%3E%3Ctd%3E%27%2BconfluenceLink%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3Ctr%3E%3Ctd%3ETest+Status%3A%3C%2Ftd%3E%3Ctd%3E%27%2BtestStatus%2B%27%3C%2Ftd%3E%3C%2Ftr%3E%3C%2Ftable%3E%3C%2Ftable%3E%3C%2Fcenter%3E%3C%2Fbody%3E%27%5Cn++++%5Ctemailext+attachLog%3A+false%2C+body%3A+mailBody%2C+replyTo%3A+mailCC%2C+subject%3A+%27Test+is+completed%27%2C+to%3A+TRIGGER_BY%2B%5C%22%2C%5C%22%2BmailCC%5Cn%5Ct%7D%5Cn++++%5Cn%7D%22%2C+%22%22%3A+%22%5Cu0001%5Cu0001%22%2C+%22sandbox%22%3A+true%2C+%22stapler-class%22%3A+%22org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition%22%2C+%22%24class%22%3A+%22org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition%22%7D%2C+%22core%3Aapply%22%3A+%22true%22%7D";

    public String getSummaryFilePathOnDisk() {
        return summaryFilePathOnDisk;
    }

    public void setSummaryFilePathOnDisk(String summaryFilePathOnDisk) {
        this.summaryFilePathOnDisk = summaryFilePathOnDisk;
    }

    public String getSelectedapplicationupload() {
        return selectedapplicationupload;
    }

    public int getSelectedAccordian() {
        return selectedAccordian;
    }

    public void setSelectedAccordian(int selectedAccordian) {
        this.selectedAccordian = selectedAccordian;
    }

    public String getSelectedscenarioupload() {
        return selectedscenarioupload;
    }

    public ArrayList<String> getScenariolistupload() {
        return scenariolistupload;
    }

    public void setScenariolistupload(ArrayList<String> scenariolistupload) {
        this.scenariolistupload = scenariolistupload;
    }

    public String getShortdescriptionupload() {
        return shortdescriptionupload;
    }

    public void setShortdescriptionupload(String shortdescriptionupload) {
        this.shortdescriptionupload = shortdescriptionupload;
    }

    public void setSelectedscenarioupload(String selectedscenarioupload) {
        this.selectedscenarioupload = selectedscenarioupload;
    }

    public String getSelectedtesttypeupload() {
        return selectedtesttypeupload;
    }

    public ArrayList<String> getTesttypelistupload() {
        return testtypelistupload;
    }

    public void setTesttypelistupload(ArrayList<String> testtypelistupload) {
        this.testtypelistupload = testtypelistupload;
    }

    public void setSelectedtesttypeupload(String selectedtesttypeupload) {
        this.selectedtesttypeupload = selectedtesttypeupload;
    }

    public ArrayList<String> getApplicationlistupload() {
        readApplicationListForUpload();
        return applicationlistupload;
    }

    public void setApplicationlistupload(ArrayList<String> applicationlistupload) {
        this.applicationlistupload = applicationlistupload;
    }

    public void setSelectedapplicationupload(String selectedapplicationupload) {
        this.selectedapplicationupload = selectedapplicationupload;
    }

    public String getAvgtestduration() {
        return avgtestduration;
    }

    public String getConcurrentusers() {
        return concurrentusers;
    }

    public void setConcurrentusers(String concurrentusers) {
        this.concurrentusers = concurrentusers;
    }

    public void updateconcurrentusers() {
        System.out.println(this.getConcurrentusers());
    }

    public void setAvgtestduration(String avgtestduration) {
        this.avgtestduration = avgtestduration;
    }

    public void updateSelectedApplicationupid() {

    }

    /**
     * Creates a new instance of OnboardingControllerBean
     */
    public OnboardingControllerBean() {
    }

    public String getTestAuthKey() {
        return testAuthKey;
    }

    public void setTestAuthKey(String testAuthKey) {
        this.testAuthKey = testAuthKey;
    }

    public void updateAuthKey() {
        System.out.println(this.getTestAuthKey());
    }

    public String getSelectedFilename() {

        return selectedFilename;
    }

    public void setSelectedFilename(String selectedFilename) {
        this.selectedFilename = selectedFilename;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public String getScenarioDescription() {
        return scenarioDescription;
    }

    public String getBaselinePage() {
        return baselinePage;
    }

    public String getConfluenceid() {
        return confluenceid;
    }

    public String getPctestid() {
        return pctestid;
    }

    public void setPctestid(String pctestid) {
        this.pctestid = pctestid;
    }

    public String getPcinstanceid() {
        return pcinstanceid;
    }

    public void setPcinstanceid(String pcinstanceid) {
        this.pcinstanceid = pcinstanceid;
    }

    public void setConfluenceid(String confluenceid) {
        this.confluenceid = confluenceid;
    }

    public void setBaselinePage(String baselinePage) {
        this.baselinePage = baselinePage;
    }

    public void setScenarioDescription(String scenarioDescription) {
        this.scenarioDescription = scenarioDescription;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public String getNewapplication() {
        return newapplication;
    }

    public UploadedFile getSummaryFile() {
        return summaryFile;
    }

    public void setSummaryFile(UploadedFile summaryFile) {
        this.summaryFile = summaryFile;
    }

    public void setNewapplication(String newapplication) {
        this.newapplication = newapplication;
    }

    public ArrayList<String> getApplicationList() {
        this.getListOfApplications();
        return applicationList;
    }

    public ArrayList<String> getTestTypesList() {
        getListOfTestTypes();
        return testTypesList;
    }

    public String getNewTestType() {
        return newTestType;
    }

    public void setNewTestType(String newTestType) {
        this.newTestType = newTestType;
    }

    public void setTestTypesList(ArrayList<String> testTypesList) {
        this.testTypesList = testTypesList;
    }

    public String getSelectedTestType() {
        return selectedTestType;
    }

    public void setSelectedTestType(String selectedTestType) {
        this.selectedTestType = selectedTestType;
    }

    public void setApplicationList(ArrayList<String> applicationList) {
        this.applicationList = applicationList;
    }

    public String getSelectedApplication() {
        return selectedApplication;
    }

    public void setSelectedApplication(String selectedApplication) {
        this.selectedApplication = selectedApplication;
    }

    public void updateSelectedApplicationupload() {
        System.out.println("Selected : " + this.getSelectedapplicationupload());
        this.readSelectedTestTypesUpload();
    }

    public void updateSelectedtesttypeupload() {

        this.readSelectedScenariosUpload();
        System.out.println("Selected : " + this.getSelectedtesttypeupload());
    }

    public void updateSelectedscenarioupload() {
        System.out.println("Selected : " + this.getSelectedscenarioupload());
    }

    public void createNewApplication() {

        System.out.println(this.getNewapplication());
        if (this.newapplication != null && this.newapplication.length() > 1 && !this.verifyExisitngApplication(this.newapplication)) {
            this.insertNewApplication(this.newapplication);
            System.out.println(this.newapplication);
            FacesContext.getCurrentInstance().addMessage("confirmmessage", new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "\"" + this.getNewapplication().toUpperCase() + "\"" + " added to list"));

        } else {
            String meessage = "";
            if (this.newapplication.length() < 1) {
                meessage = "Application name should not be empty!";
            } else {
                meessage = "\"" + this.getNewapplication().toUpperCase() + "\" - Already Exists!";

            }
            System.out.println(meessage);
            FacesContext.getCurrentInstance().addMessage("confirmmessage", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", meessage));
        }
        this.newapplication = "";

    }

    public void createNewTestType() {
        System.out.println(this.newTestType);

        if (this.newTestType != null && this.newTestType.length() > 1 && !this.verifyExisitngTestType(this.newTestType)) {
            this.insertNewTestType();
            System.out.println(this.newTestType);
            FacesContext.getCurrentInstance().addMessage("confirmmessage", new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "\"" + this.getNewTestType().toUpperCase() + "\"" + " added to list"));

        } else {
            String meessage = "";
            if (this.newTestType.length() < 1) {
                meessage = "Test Type name should not be empty!";
            } else {
                meessage = "\"" + this.getNewTestType().toUpperCase() + "\" - Already Exists!";

            }
            System.out.println(meessage);
            FacesContext.getCurrentInstance().addMessage("confirmmessage", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", meessage));
        }
        this.newTestType = "";
    }

    public boolean verifyExisitngApplication(String applicationName) {
        try {
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            String query = "SELECT count(*) FROM PERF_USERS.APPLICATIONNAMES WHERE APPNAME = ?";
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, this.newapplication.toUpperCase());
            ResultSet rs = pst.executeQuery();
            int numberOfRows = 0;
            if (rs.next()) {
                numberOfRows = rs.getInt(1);
            }
            pst.close();
            connection.close();
            if (numberOfRows > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean verifyExisitngTestType(String testType) {
        try {
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            String query = "SELECT count(*) FROM PERF_USERS.TESTTYPES WHERE TESTTYPE = ?";
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, this.getNewTestType().toUpperCase());
            ResultSet rs = pst.executeQuery();
            int numberOfRows = 0;
            if (rs.next()) {
                numberOfRows = rs.getInt(1);
            }
            pst.close();
            connection.close();
            if (numberOfRows > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertNewApplication(String application) {
        try {
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            String query = "INSERT INTO PERF_USERS.APPLICATIONNAMES(APPNAME) VALUES(?)";
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, this.newapplication.toUpperCase());
            pst.executeUpdate();
            pst.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertNewTestType() {
        try {
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            String query = "INSERT INTO PERF_USERS.TESTTYPES(TESTTYPE) VALUES(?)";
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, this.getNewTestType().toUpperCase());
            pst.executeUpdate();
            pst.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getListOfApplications() {
        try {
            this.applicationList = new ArrayList<String>();
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            String query = "SELECT DISTINCT APPNAME FROM APPLICATIONNAMES";
            PreparedStatement pst = this.connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                this.applicationList.add(rs.getString("APPNAME"));
            }
            pst.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateNewApplicationText() {
        System.out.println(this.getNewapplication());
    }

    public void updateNewTestTypeText() {
        System.out.println(this.getNewTestType());
    }

    public void updateShortDescription() {
        System.out.println("Short Description : " + this.getShortdescriptionupload());
    }

    public void getListOfTestTypes() {
        try {
            this.testTypesList = new ArrayList<String>();
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            String query = "SELECT TESTTYPE FROM PERF_USERS.TESTTYPES";
            PreparedStatement pst = this.connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                this.testTypesList.add(rs.getString("TESTTYPE"));
            }
            pst.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateScenarioDescription() {
        System.out.println(this.getScenarioDescription());
    }

    public void updateConfluenceLink() {
        System.out.println(this.getBaselinePage());
    }

    public void updateAvgtestduration() {
        System.out.println(this.avgtestduration);
    }

    public void createNewConfluencePage() {

        if (this.getScenarioName() != null && this.getScenarioName().length() > 1) {
            System.out.println("Creating New Confluence Page!");
            this.getConfluenceAuthKey();
            this.getConfluenceParentID();
            this.createConfluencePage();

        } else {
            FacesContext.getCurrentInstance().addMessage("confirmmessage", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Valiadtion Error", "Please Input Scenario name first!"));
        }
    }

    public void createConfluencePage() {
        RestClientProvider rest = new RestClientProvider();
        final String JOB_NAME = this.getSelectedApplication().toUpperCase() + this.getSelectedTestType() + this.getScenarioName().toUpperCase();
        rest.setUrl("https://confluence.es.ad.adp.com/rest/api/content/");
        String currentPayload = this.payload.replace("${CONFPAGENAME}", JOB_NAME.toUpperCase()).replace("${CONFPARENT}", this.getConfluencePageId);
        //System.out.println("Pay Load : " + currentPayload);
        rest.setPayLoad(currentPayload);
        rest.firePostRequest("Authorization", this.getConfluenceAuthKey);
        //System.out.println(rest.getResponseBody());
        if (rest.getResponseCode().equals("200")) {
            try {
                Object obj = new JSONParser().parse(rest.getResponseBody());
                JSONObject jo = (JSONObject) obj;
                this.setConfluenceid((String) (Object) jo.get("id"));
                FacesContext.getCurrentInstance().addMessage("confirmmessage", new FacesMessage(FacesMessage.SEVERITY_INFO, "Page Created", "Confluence Page Successfully Created!"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (rest.getResponseCode().equals("400")) {
            FacesContext.getCurrentInstance().addMessage("confirmmessage", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Page Exists", "Confluence Page with the scenario name already exisits!"));
        } else {
            FacesContext.getCurrentInstance().addMessage("confirmmessage", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Confluence Error", "Confluence is down!"));
        }

    }

    public void getConfluenceAuthKey() {
        try {
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            String query = "SELECT VALUE FROM PERF_USERS.CONFIG WHERE NAME=?";
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, "CONFLUENCE_AUTHKEY");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                this.getConfluenceAuthKey = rs.getString("VALUE");
            }
            pst.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getConfluenceParentID() {
        try {
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            String query = "SELECT VALUE FROM PERF_USERS.CONFIG WHERE NAME=?";
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, "CONFLUENCE_PARENT_ID");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                this.getConfluencePageId = rs.getString("VALUE");
            }
            pst.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getJenkinsAuthKey() {
        try {
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            String query = "SELECT VALUE FROM PERF_USERS.CONFIG WHERE NAME=?";
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, "JENKINS_AUTHKEY");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                this.jenkinsAuthKey = rs.getString("VALUE");
            }
            pst.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updatepcTestId() {
        System.out.println(this.getPctestid());
    }

    public void updatepcInstanceId() {
        System.out.println(this.getPcinstanceid());
    }

    public void updateConfluenceID() {
        System.out.println(this.getConfluenceid());
    }

    public void updateScenarioName() {
        System.out.println(this.getScenarioName());
        if (this.validateScenarioName()) {
            FacesContext.getCurrentInstance().addMessage("confirmmessage", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Scenario Error", "Scenario Name already exists!"));
            this.setScenarioName(null);
        }
    }

    public void updateScenarioDesc() {
        System.out.println(this.getScenarioDescription());
    }

    public void updateSelectedApplication() {
        System.out.println(this.getSelectedApplication());
    }

    public void updateSelectedTestType() {
        System.out.println(this.getSelectedTestType());
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {

        this.summaryFile = event.getFile();
        this.setSelectedFilename(this.summaryFile.getFileName());
        try {
            String tempPath = this.tempFilePath + "/";
            this.tempFile = File.createTempFile("summary", ".html", new File(tempPath));
            FileOutputStream fos = new FileOutputStream(tempFile);
            InputStream is = this.getSummaryFile().getInputstream();
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];
            int a;
            while (true) {
                a = is.read(buffer);
                if (a < 0) {
                    break;
                }
                fos.write(buffer, 0, a);
                fos.flush();
            }
            fos.close();
            is.close();
            System.out.println("Temp File Name : " + tempFile.getAbsolutePath());
            this.setSummaryFilePathOnDisk(tempFile.getAbsolutePath().toString());
            FacesContext.getCurrentInstance().addMessage("confirmmessage", new FacesMessage(FacesMessage.SEVERITY_INFO, "File Uploaded", "You File has been uploaded"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void submitNewTest() {

        if (this.validateUIFields()) {
            System.out.println("UI Filedss Validation Error! Step1");
            RequestContext.getCurrentInstance().execute("PF('inputallfields').show()");
            //FacesContext.getCurrentInstance().addMessage("confirmmessage", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Valiadtion Error", "Please input all fields!"));
        } else if (this.validateScenarioNameAPPTT()) {
            this.setScenarioName(null);
            System.out.println("UI Filedss Validation Error! Step2");
            RequestContext.getCurrentInstance().execute("PF('exisitingscenarioerror').show()");
            // FacesContext.getCurrentInstance().addMessage("confirmmessage", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Scenario Error", "Scenario already exists for selected application & test type!"));
        } else {
            this.createJenkinsJob();
            this.upateJenkinsJob();
            this.insertNewApplicationDetailsIntoDB();
            this.resetFormData();

            // this.updateJenkinsJob();
            RequestContext.getCurrentInstance().execute("PF('onboardsuccessdialog').show()");

        }
    }

    public boolean updateJenkinsJob() {
        try {
            String jobURL = this.jenkinsBaseURL + "/createItem";
            RestClientProvider rest = new RestClientProvider();
            rest.setUrl(jobURL);
            String tempPayload = this.jenkinsJobPayload.replace("${JOBNAME}", URLEncoder.encode(this.getScenarioName(), "UTF-8"));
            this.getJenkinsAuthKey();
            String responseCode = rest.firePostHTTP(this.jenkinsAuthKey);
            if (responseCode.contains("403")) {
                return true;
            }

            rest.setPayLoad(tempPayload);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean createJenkinsJob() {
        try {
            String jobURL = this.jenkinsBaseURL + "/createItem";
            RestClientProvider rest = new RestClientProvider();
            rest.setUrl(jobURL);
            this.getJenkinsAuthKey();
            final String JOB_NAME = this.getSelectedApplication().toUpperCase() + this.getSelectedTestType() + this.getScenarioName().toUpperCase();
            System.out.println("Creating Jenkins Job for Scenario : " + this.getScenarioName() + ", With AuthKey : " + this.jenkinsAuthKey);
            String tempPayload = this.jenkinsJobPayload.replace("${JOBNAME}", URLEncoder.encode(JOB_NAME, "UTF-8"));
            //System.out.println("Payload : " + tempPayload);
            rest.setPayLoad(tempPayload);
            String responseCode = rest.firePostHTTP(this.jenkinsAuthKey);
            if (responseCode.contains("403")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void upateJenkinsJob() {
        try {
            final String JOB_NAME = URLEncoder.encode(this.getSelectedApplication() + this.getSelectedTestType() + this.getScenarioName(), "UTF-8").replace("+", "%20").toUpperCase();
            String jobURL = this.jenkinsBaseURL + "job/" + JOB_NAME + "/configSubmit";
            String tempPayload = this.jenkinsJobPipePayload.replace("${JOBNAME}", JOB_NAME).
                    replace("${AUTKEY}", this.getTestAuthKey()).
                    replace("${TESTID}", this.getPctestid()).
                    replace("${TESTINSTANCEID}", this.getPcinstanceid()).
                    replace("${AVGTESTDURATION}", this.getAvgtestduration()).
                    replace("${CONFLUENCEID}", this.getConfluenceid());
            //System.out.println("URL :" + jobURL + "\nPayload : " + tempPayload);
            RestClientProvider rest = new RestClientProvider();
            rest.setUrl(jobURL);
            rest.setPayLoad(tempPayload);
            String responseCode = rest.firePostHTTP(this.jenkinsAuthKey);
            System.out.println("Update job Date response Code : " + responseCode);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showUploadBaselineAccord() {
        //this.setSelectedAccordian(1);
    }

    public boolean validateScenarioName() {
        int numberOfRows = 0;
        if (this.getSelectedApplication() != null && this.getScenarioName() != null) {
            try {
                DBC = new DBConnection();
                this.connection = DBC.getConnection();
                String query = "SELECT COUNT(*) FROM PERF_USERS.APPLICATIONS WHERE SCENARIONAME= ? AND APPNAME = ? ";
                PreparedStatement pst = this.connection.prepareStatement(query);
                pst.setString(1, this.getScenarioName().toUpperCase());
                pst.setString(2, this.getSelectedApplication().toUpperCase());
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    numberOfRows = rs.getInt(1);
                }
                pst.close();
                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Number of row : " + numberOfRows);
            if (numberOfRows > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public boolean validateScenarioNameAPPTT() {
        int numberOfRows = 0;
        try {
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            String query = "SELECT COUNT(*) FROM PERF_USERS.APPLICATIONS WHERE SCENARIONAME= ? AND APPNAME = ? AND TESTTYPE=?";
            PreparedStatement pst = this.connection.prepareStatement(query);
            System.out.println(this.getScenarioName() + "," + this.getSelectedApplication() + "," + this.getSelectedTestType());
            pst.setString(1, this.getScenarioName().toUpperCase());
            pst.setString(2, this.getSelectedApplication().toUpperCase());
            pst.setString(3, this.getSelectedTestType().toUpperCase());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                numberOfRows = rs.getInt(1);
            }
            pst.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (numberOfRows > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void insertNewApplicationDetailsIntoDB() {
        try {
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            String query = "INSERT INTO PERF_USERS.APPLICATIONS("
                    + "APPNAME, SCENARIONAME, DESCRIPTION,"
                    + "TESTAUTHKEY, TESTTYPE, BASELINELINK, CONFLUENCEID, "
                    + "PCTESTID,PCINSTANCEID,AVGTESTDURATION,CONCURRENCY, APPLICATIONSTATUS)"
                    + "VALUES"
                    + "(?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, this.getSelectedApplication().toUpperCase());
            pst.setString(2, this.getScenarioName().toUpperCase());
            pst.setString(3, this.getScenarioDescription());
            pst.setString(4, this.getTestAuthKey());
            pst.setString(5, this.getSelectedTestType().toUpperCase());
            pst.setString(6, this.getBaselinePage());
            pst.setString(7, this.getConfluenceid());
            pst.setString(8, this.getPctestid());
            pst.setString(9, this.getPcinstanceid());
            pst.setString(10, this.getAvgtestduration());
            pst.setString(11, this.getConcurrentusers());
            pst.setString(12, "DEFINED");
            int ret = pst.executeUpdate();
            System.out.println("Records Updated : " + ret);
            pst.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetFormData() {
        this.setSelectedApplication(null);
        this.setSelectedTestType(null);
        this.setScenarioName(null);
        this.setScenarioDescription(null);
        this.setBaselinePage(null);
        this.setConfluenceid(null);
        this.setPctestid(null);
        this.setPcinstanceid(null);
        this.setTestAuthKey(null);
        this.setConcurrentusers(null);
        this.setAvgtestduration(null);
    }

    public boolean validateUIFields() {

        if (this.getSelectedApplication() == null || this.getSelectedTestType() == null || this.getScenarioName() == null
                || this.getScenarioDescription() == null || this.getBaselinePage() == null || this.getConfluenceid() == null
                || this.getPctestid() == null || this.getPcinstanceid() == null || this.getTestAuthKey() == null || this.getConcurrentusers() == null
                || this.getAvgtestduration() == null) {
            System.out.println("Validation failed in IF");
            return true;

        } else if (this.getSelectedApplication().length() < 2 || this.getSelectedTestType().length() < 2 || this.getScenarioName().length() < 2
                || this.getScenarioDescription().length() < 2 || this.getBaselinePage().length() < 2 || this.getConfluenceid().length() < 2
                || this.getPctestid().length() < 2 || this.getPcinstanceid().length() < 2 || this.getTestAuthKey().length() < 2
                || this.getConcurrentusers().length() < 1 || this.getAvgtestduration().length() < 2) {
            System.out.println("Validation failed in ELSE");
            return true;

        }
        return false;

    }

    public void createBasline() {
        System.out.println("User Clicked Submit baseline");
        if (this.getSelectedapplicationupload() == null || this.getSelectedtesttypeupload() == null || this.getSelectedscenarioupload() == null || this.summaryFile == null || this.shortdescriptionupload == null) {
            System.out.println("In 1st IF");
            RequestContext.getCurrentInstance().execute("PF('inputallfieldsupload').show()");
        } else if (this.getSelectedapplicationupload().length() < 2 || this.getSelectedtesttypeupload().length() < 2 || this.getSelectedscenarioupload().length() < 2 || this.summaryFile.getFileName().length() < 2 || this.getShortdescriptionupload().length() < 1) {
            System.out.println("In 2nd IF");
            RequestContext.getCurrentInstance().execute("PF('inputallfieldsupload').show()");
        } else {
            this.processBaselineResults();
        }
    }

    public void processBaselineResults() {
        ExtractResults extract = new ExtractResults();
        extract.startExtracting(this.getSelectedapplicationupload(), this.getSelectedtesttypeupload(), this.getSelectedscenarioupload(), this.getShortdescriptionupload(), this.getSummaryFilePathOnDisk());
        RequestContext.getCurrentInstance().execute("PF('uploadresultssuccess').show()");
    }

    public void resetBaselineUploadForm() {
        System.out.println("user reset form");
        this.selectedapplicationupload = null;
        this.selectedscenarioupload = null;
        this.selectedTestType = null;
        this.summaryFile = null;
    }

    public void readApplicationListForUpload() {
        this.applicationlistupload = new ArrayList<String>();
        System.out.println("Reading Application list for upload page");
        try {
            DBC = new DBConnection();
            this.jenkinsBaseURL = DBC.getJenkinsBaseURL();
            this.connection = DBC.getConnection();
            System.out.println("Jenkins Base URL : " + this.jenkinsBaseURL);
            String query = "SELECT DISTINCT APPNAME FROM PERF_USERS.APPLICATIONS";
            PreparedStatement pst = this.connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                this.applicationlistupload.add(rs.getString("APPNAME"));
            }
            pst.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readSelectedTestTypesUpload() {
        this.testtypelistupload = new ArrayList<String>();
        if (this.selectedapplicationupload != null) {
            if (this.selectedapplicationupload.length() > 1) {
                System.out.println("Reading Test types for Upload");

                try {
                    DBC = new DBConnection();
                    this.connection = DBC.getConnection();
                    String query = "SELECT DISTINCT TESTTYPE FROM PERF_USERS.APPLICATIONS WHERE APPNAME=?";
                    PreparedStatement pst = this.connection.prepareStatement(query);
                    pst.setString(1, this.getSelectedapplicationupload());
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        this.testtypelistupload.add(rs.getString("TESTTYPE"));
                    }
                    pst.close();
                    connection.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void readSelectedScenariosUpload() {
        System.out.println("Selected Application : " + this.getSelectedapplicationupload());
        System.out.println("Selected Test Type : " + this.getSelectedtesttypeupload());
        this.scenariolistupload = new ArrayList<String>();
        if (this.getSelectedapplicationupload() != null && this.getSelectedtesttypeupload() != null) {
            if (this.getSelectedapplicationupload().length() > 1 && this.getSelectedtesttypeupload().length() > 1) {
                System.out.println("Reading Scenario list for Upload");
                try {
                    DBC = new DBConnection();
                    this.connection = DBC.getConnection();
                    String query = "SELECT DISTINCT SCENARIONAME FROM PERF_USERS.APPLICATIONS WHERE APPNAME=? AND TESTTYPE=?";
                    PreparedStatement pst = this.connection.prepareStatement(query);
                    pst.setString(1, this.getSelectedapplicationupload().toUpperCase());
                    pst.setString(2, this.getSelectedtesttypeupload().toUpperCase());
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        this.scenariolistupload.add(rs.getString("SCENARIONAME"));
                    }
                    pst.close();
                    connection.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            this.scenariolistupload = new ArrayList<String>();
        }

    }
}
