/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loadtest;

import java.io.Serializable;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.primefaces.context.RequestContext;
import java.util.Calendar;
import java.util.TimeZone;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author NakkaNar
 */
@ManagedBean
@ViewScoped

public class TestControllerBean implements Serializable {

//    @Resource(name = "jdbc/testondemand")
//    private DataSource ds;

    /*Drop Down Lists*/
    ArrayList<String> applicationList;
    ArrayList<String> testTypeList;
    ArrayList<String> scenarioList;
    HashMap<String, String> baselineDateList;
    private ArrayList<ShowTransactionsData> transDataList;

    String selectedApplication;
    String selectedTestType;
    String selectedScenario;
    String selectedBaselineDate;
    String runningScenarioName;
    String runningApplicationName;
    String runningTestType;
    int polllooper = 0;
    boolean onetimeShow = true;
    String runningfor;
    boolean disableSelectedBaselineDate = false;

    String selectedApplication_status;
    String selectedTestType_status;
    String selectedScenarioName_status;
    String testSubmittedTime_status = "Please Wait..";
    String testStarttime_status = "Please Wait..";
    String testEndTime_status = "Please Wait..";
    long testETA_status = 0;
    String testTriggerBy_status;
    String testConfluencepath_status;
    String testProgressBar_status;
    String selectedTestConfId;
    String recentresultspage;
    boolean isTestPollingStopped = true;
    boolean isStopBtnDisabled = true;
    boolean statusDialogStopStatus = false;

    DBConnection DBC;
    Connection connection;

    String scenarioname = "";
    String concurrentusers = "";
    String avgtestduration = "";
    String recentbaselinedate = "";
    String baselineconfluencepage = "";
    String testdescription = "";

    String teststartby = "";
    String teststatus = "";
    String concurrentusersdet = "";
    String testperiod = "";
    String testduration = "";
    String secretkeystatusddialog;
    String runningTestAuthKey;
    String userEmail;
    String userAuthKey;
    String secretkeystopddialog;
    String uniqueTestID;
    String jenkinsBaseURL;
    long buildNumber;
    String shortnameForTest;

    String seletedAccordIndex = "-1";
    boolean disableStartBtn = true;
    boolean disableStopBtn = true;

    public String getRunningfor() {
        return runningfor;
    }

    public boolean getDisableSelectedBaselineDate() {
        return disableSelectedBaselineDate;
    }

    public void setDisableSelectedBaselineDate(boolean disableSelectedBaselineDate) {
        this.disableSelectedBaselineDate = disableSelectedBaselineDate;
    }

    public void setRunningfor(String runningfor) {
        this.runningfor = runningfor;
    }

    public String getShortnameForTest() {
        return shortnameForTest;
    }

    public void setShortnameForTest(String shortnameForTest) {
        this.shortnameForTest = shortnameForTest;
    }

    public ArrayList<ShowTransactionsData> getTransDataList() {
        return transDataList;
    }

    public void setTransDataList(ArrayList<ShowTransactionsData> transDataList) {
        this.transDataList = transDataList;
    }

    public boolean getStatusDialogStopStatus() {
        return statusDialogStopStatus;
    }

    public void updateShortNameForTest() {
        System.out.println("Given Shortname : " + this.getShortnameForTest());
    }

    public void setStatusDialogStopStatus(boolean statusDialogStopStatus) {
        this.statusDialogStopStatus = statusDialogStopStatus;
    }

    public String getRecentresultspage() {
        return recentresultspage;
    }

    public void setRecentresultspage(String recentresultspage) {
        this.recentresultspage = recentresultspage;
    }

    public String getTestEndTime_status() {
        return testEndTime_status;
    }

    public void setTestEndTime_status(String testEndTime_status) {
        this.testEndTime_status = testEndTime_status;
    }

    public boolean getIsTestPollingStopped() {
        return isTestPollingStopped;
    }

    public boolean getIsStopBtnDisabled() {
        return isStopBtnDisabled;
    }

    public void setIsStopBtnDisabled(boolean isStopBtnDisabled) {
        this.isStopBtnDisabled = isStopBtnDisabled;
    }

    public void setIsTestPollingStopped(boolean isTestPollingStopped) {
        this.isTestPollingStopped = isTestPollingStopped;
    }

    public String getSelectedTestConfId() {
        return selectedTestConfId;
    }

    public void setSelectedTestConfId(String selectedTestConfId) {
        this.selectedTestConfId = selectedTestConfId;
    }

    public String getSelectedApplication_status() {
        return selectedApplication_status;
    }

    public void setSelectedApplication_status(String selectedApplication_status) {
        this.selectedApplication_status = selectedApplication_status;
    }

    public String getSelectedTestType_status() {
        return selectedTestType_status;
    }

    public void setSelectedTestType_status(String selectedTestType_status) {
        this.selectedTestType_status = selectedTestType_status;
    }

    public String getSelectedScenarioName_status() {
        return selectedScenarioName_status;
    }

    public void setSelectedScenarioName_status(String selectedScenarioName_status) {
        this.selectedScenarioName_status = selectedScenarioName_status;
    }

    public String getTestSubmittedTime_status() {
        return testSubmittedTime_status;
    }

    public void setTestSubmittedTime_status(String testSubmittedTime_status) {
        this.testSubmittedTime_status = testSubmittedTime_status;
    }

    public String getTestStarttime_status() {
        return testStarttime_status;
    }

    public void setTestStarttime_status(String testStarttime_status) {
        this.testStarttime_status = testStarttime_status;
    }

    public long getTestETA_status() {
        return testETA_status;
    }

    public void setTestETA_status(long testETA_status) {
        this.testETA_status = testETA_status;
    }

    public String getTestTriggerBy_status() {
        return testTriggerBy_status;
    }

    public void setTestTriggerBy_status(String testTriggerBy_status) {
        this.testTriggerBy_status = testTriggerBy_status;
    }

    public String getTestConfluencepath_status() {
        return testConfluencepath_status;
    }

    public void setTestConfluencepath_status(String testConfluencepath_status) {
        this.testConfluencepath_status = testConfluencepath_status;
    }

    public String getTestProgressBar_status() {
        return testProgressBar_status;
    }

    public void setTestProgressBar_status(String testProgressBar_status) {
        this.testProgressBar_status = testProgressBar_status;
    }

    public long getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(long buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getJenkinsBaseURL() {
        return jenkinsBaseURL;
    }

    public void setJenkinsBaseURL(String jenkinsBaseURL) {
        this.jenkinsBaseURL = jenkinsBaseURL;
    }

    public String getUniqueTestID() {
        return uniqueTestID;
    }

    public void setUniqueTestID(String uniqueTestID) {
        this.uniqueTestID = uniqueTestID;
    }

    public void updatesecretkeyforstop() {
        System.out.println("Secret Key for Stop :" + this.getSecretkeystopddialog());
    }

    public String getSecretkeystopddialog() {
        return secretkeystopddialog;
    }

    public void setSecretkeystopddialog(String secretkeystopddialog) {
        this.secretkeystopddialog = secretkeystopddialog;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAuthKey() {
        return userAuthKey;
    }

    public void setUserAuthKey(String userAuthKey) {
        this.userAuthKey = userAuthKey;
    }

    public void updatesecretkeyforstatus() {
        System.out.println("User typed Secret key : " + this.getSecretkeystatusddialog());
    }

    public String getRunningTestAuthKey() {
        return runningTestAuthKey;
    }

    public void setRunningTestAuthKey(String runningTestAuthKey) {
        this.runningTestAuthKey = runningTestAuthKey;
    }

    public String getSecretkeystatusddialog() {
        return secretkeystatusddialog;
    }

    public void setSecretkeystatusddialog(String secretkeystatusddialog) {
        this.secretkeystatusddialog = secretkeystatusddialog;
    }

    public String getBaselineconfluencepage() {
        return baselineconfluencepage;
    }

    public void setBaselineconfluencepage(String baselineconfluencepage) {
        this.baselineconfluencepage = baselineconfluencepage;
    }

    public String getConcurrentusers() {
        return concurrentusers;
    }

    public void setConcurrentusers(String concurrentusers) {
        this.concurrentusers = concurrentusers;
    }

    public String getScenarioname() {
        return scenarioname;
    }

    public void setScenarioname(String scenarioname) {
        this.scenarioname = scenarioname;
    }

    public boolean isDisableStartBtn() {
        return disableStartBtn;
    }

    public void setDisableStartBtn(boolean disableStartBtn) {
        this.disableStartBtn = disableStartBtn;
    }

    public boolean isDisableStopBtn() {
        return disableStopBtn;
    }

    public void setDisableStopBtn(boolean disableStopBtn) {
        this.disableStopBtn = disableStopBtn;
    }

    public String getAvgtestduration() {
        return avgtestduration;
    }

    public void setAvgtestduration(String avgtestduration) {
        this.avgtestduration = avgtestduration;
    }

    public String getRecentbaselinedate() {
        return recentbaselinedate;
    }

    public void setRecentbaselinedate(String recentbaselinedate) {
        this.recentbaselinedate = recentbaselinedate;
    }

    public String getTestdescription() {
        return testdescription;
    }

    public void setTestdescription(String testdescription) {
        this.testdescription = testdescription;
    }

    public String getTeststartby() {
        return teststartby;
    }

    public void setTeststartby(String teststartby) {
        this.teststartby = teststartby;
    }

    public String getTeststatus() {
        return teststatus;
    }

    public void setTeststatus(String teststatus) {
        this.teststatus = teststatus;
    }

    public String getConcurrentusersdet() {
        return concurrentusersdet;
    }

    public void setConcurrentusersdet(String concurrentusersdet) {
        this.concurrentusersdet = concurrentusersdet;
    }

    public String getTestperiod() {
        return testperiod;
    }

    public void setTestperiod(String testperiod) {
        this.testperiod = testperiod;
    }

    public String getSeletedAccordIndex() {
        return seletedAccordIndex;
    }

    public void setSeletedAccordIndex(String seletedAccordIndex) {
        this.seletedAccordIndex = seletedAccordIndex;
    }

    public String getTestduration() {
        return testduration;
    }

    public void setTestduration(String testduration) {
        this.testduration = testduration;
    }

    public TestControllerBean() {
        this.readApplicationList();
        this.setDefaultRunFor();
    }

    public void setDefaultRunFor() {
        this.setRunningfor("validation");
    }

    public String getSelectedApplication() {
        return selectedApplication;
    }

    public void setSelectedApplication(String selectedApplication) {
        this.selectedApplication = selectedApplication;
    }

    public String getSelectedTestType() {
        return selectedTestType;
    }

    public void setSelectedTestType(String selectedTestType) {
        this.selectedTestType = selectedTestType;
    }

    public String getSelectedScenario() {
        return selectedScenario;
    }

    public void setSelectedScenario(String selectedScenario) {
        this.selectedScenario = selectedScenario;
    }

    public ArrayList<String> getApplicationList() {
        return applicationList;
    }

    public void setApplicationList(ArrayList<String> applicationList) {
        this.applicationList = applicationList;
    }

    public ArrayList<String> getTestTypeList() {
        return testTypeList;
    }

    public void setTestTypeList(ArrayList<String> testTypeList) {
        this.testTypeList = testTypeList;
    }

    public ArrayList<String> getScenarioList() {
        return scenarioList;
    }

    public void setScenarioList(ArrayList<String> scenarioList) {
        this.scenarioList = scenarioList;
    }

    public HashMap<String, String> getBaselineDateList() {
        return baselineDateList;
    }

    public void setBaselineDateList(HashMap<String, String> baselineDateList) {
        this.baselineDateList = baselineDateList;
    }

    public String getSelectedBaselineDate() {
        return selectedBaselineDate;
    }

    public void setSelectedBaselineDate(String selectedBaselineDate) {
        this.selectedBaselineDate = selectedBaselineDate;
    }

    public String getRunningScenarioName() {
        return runningScenarioName;
    }

    public void setRunningScenarioName(String runningScenarioName) {
        this.runningScenarioName = runningScenarioName;
    }

    public String getRunningApplicationName() {
        return runningApplicationName;
    }

    public void setRunningApplicationName(String runningApplicationName) {
        this.runningApplicationName = runningApplicationName;
    }

    public String getRunningTestType() {
        return runningTestType;
    }

    public void setRunningTestType(String runningTestType) {
        this.runningTestType = runningTestType;
    }

    public void resetRunningStatus() {
        this.setRunningApplicationName(null);
        this.setRunningTestType(null);
        this.setRunningScenarioName(null);
    }

    public void updateRunngingFor() {
        System.out.println(this.getRunningfor());
        if (this.getRunningfor().equals("certification")) {
            this.setDisableSelectedBaselineDate(true);
        } else if (this.getRunningfor().equals("validation")) {
            this.setDisableSelectedBaselineDate(false);
        }

        this.testTypeList = new ArrayList<String>();
        this.scenarioList = new ArrayList<String>();
        this.baselineDateList = new HashMap<String, String>();
        this.setDisableStopBtn(true);
        this.setDisableStartBtn(true);
        this.setSelectedApplication("Select Application");
        this.setSelectedTestType("Select Test type");
        this.setSelectedScenario("Select Test/Scenario");
        this.setSelectedBaselineDate("Select Baseline");
        this.setShortnameForTest("");
    }

    public void updateTestProgressAuthentication() {

    }

    public void updateTestTypeDropDown() {
        this.resetRunningStatus();
        this.testTypeList = new ArrayList<String>();
        this.scenarioList = new ArrayList<String>();
        this.baselineDateList = new HashMap<String, String>();
        this.setShortnameForTest("");
        if (this.getSelectedApplication() == null) {
            this.setDisableStartBtn(true);
            this.setDisableStopBtn(true);
        } else if (this.getSelectedApplication().length() < 2) {
            this.setDisableStartBtn(true);
            this.setDisableStopBtn(true);
        } else if (this.isTestRunningForSelectedApplication()) {

            this.setDisableStopBtn(true);
            this.setDisableStartBtn(true);
            this.readTestTypeList();
            RequestContext.getCurrentInstance().execute("PF('testinprogressdialog').show()");
        } else {
            this.readTestTypeList();
            this.setDisableStopBtn(true);
            this.setDisableStartBtn(true);
        }
    }

    public void updateScenarioList() {
        this.scenarioList = new ArrayList<String>();
        if (this.getSelectedApplication() == null || this.getSelectedTestType() == null) {
            this.setDisableStopBtn(true);
            this.setDisableStartBtn(true);
        } else if (this.getSelectedApplication().length() < 2 || this.getSelectedTestType().length() < 2) {
            this.setDisableStopBtn(true);
            this.setDisableStartBtn(true);
        } else {
            this.readScenarioList();
            this.setDisableStopBtn(true);
            this.setDisableStartBtn(true);
        }
    }

    public void updatBaselineDates() {
        this.baselineDateList = new HashMap<String, String>();
        this.setShortnameForTest("");
        if (this.getSelectedApplication() == null || this.getSelectedTestType() == null || this.getSelectedScenario() == null) {
            this.setDisableStopBtn(true);
            this.setDisableStartBtn(true);

        } else if (this.getSelectedApplication().length() < 2 || this.getSelectedTestType().length() < 2 || this.getSelectedScenario().length() < 2) {
            this.setDisableStopBtn(true);
            this.setDisableStartBtn(true);

        } else {
            this.readBaselineDateList();
            this.readScenarioBasicInfo();
            this.setSeletedAccordIndex("0");
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            if (this.getRunningfor().equals("certification")) {
                this.setShortnameForTest("Certification Test - " + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DATE) + "-" + calendar.get(Calendar.YEAR));
                this.setSelectedBaselineDate("Most Recent Baseline");
                if (this.isTestRunningForSelectedApplication()) {
                    this.setDisableStartBtn(true);
                    this.setDisableStopBtn(false);
                } else {
                    this.setDisableStartBtn(false);
                    this.setDisableStopBtn(true);
                }
                this.readSelectedScenarioDetails();
                this.prepareTrasactionsData();

            } else if (this.getRunningfor().equals("validation")) {
                this.setShortnameForTest("Validation Test - " + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DATE) + "-" + calendar.get(Calendar.YEAR));
                this.setDisableStopBtn(true);
                this.setDisableStartBtn(false);
            }

        }
    }

    public void updateTestInformation() {
        transDataList = new ArrayList<ShowTransactionsData>();
        if (this.getSelectedBaselineDate() != null && this.getSelectedBaselineDate().length() > 1) {
            System.out.println("Selected Baseline Date : " + this.getSelectedBaselineDate());
            this.readSelectedScenarioDetails();
            this.setSeletedAccordIndex("1");
            this.prepareTrasactionsData();
        }
        if (this.isTestRunningForSelectedApplication()) {
            this.setDisableStartBtn(true);
            this.setDisableStopBtn(false);
        } else {
            this.setDisableStartBtn(false);
            this.setDisableStopBtn(true);
        }

    }

    /* To read all application list from Database*/
    public void readApplicationList() {
        this.applicationList = new ArrayList<String>();
        System.out.println("Reading Application List");
        try {
            String query = "SELECT DISTINCT APPNAME FROM PERF_USERS.APPLICATIONS";
            DBC = new DBConnection();
            this.setJenkinsBaseURL(DBC.getJenkinsBaseURL());
            System.out.println("Jenkins Base URL : " + this.getJenkinsBaseURL());
            this.connection = DBC.getConnection();
            PreparedStatement pst = this.connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                this.applicationList.add(rs.getString("APPNAME"));
            }
            rs.close();
            pst.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Server Down", "Database Server not reachable.."));
        }

    }

    /*TO read testtype list from Database*/
    public void readTestTypeList() {
        System.out.println("Reading Test Type List");
        try {
            String query = "SELECT DISTINCT TESTTYPE FROM PERF_USERS.APPLICATIONS WHERE APPNAME=?";
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, this.getSelectedApplication().toUpperCase());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                this.testTypeList.add(rs.getString("TESTTYPE"));
            }
            rs.close();
            pst.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /*To read Scenario List from Database*/
    public void readScenarioList() {
        System.out.println("Reading Scenario List");
        try {
            String query = "SELECT DISTINCT SCENARIONAME FROM PERF_USERS.APPLICATIONS WHERE APPNAME=? AND TESTTYPE=?";
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, this.getSelectedApplication().toUpperCase());
            pst.setString(2, this.getSelectedTestType().toUpperCase());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                this.scenarioList.add(rs.getString("SCENARIONAME"));
            }
            rs.close();
            pst.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateRecentResultsPage() {
        String scenarioFullName = URLEncoder.encode(this.getSelectedApplication() + this.getSelectedTestType() + this.getSelectedScenario()).toUpperCase();
        this.setRecentresultspage("https://confluence.es.ad.adp.com/display/AVSDO/" + scenarioFullName);
    }

    public void readBaselineDateList() {
        System.out.println("Reading Baseline List");
        {
            this.generateRecentResultsPage();
            try {

                String query = "SELECT TESTID,SHORT_NAME FROM PERF_USERS.JENKINS_TESTS WHERE APPNAME=? AND TESTTYPE=? AND SCENARIONAME = ? AND ISBASELINE=? ORDER BY TESTID DESC FETCH FIRST 4 ROWS ONLY";
                DBC = new DBConnection();
                this.connection = DBC.getConnection();
                PreparedStatement pst = this.connection.prepareStatement(query);
                pst.setString(1, this.getSelectedApplication().toUpperCase());
                pst.setString(2, this.getSelectedTestType().toUpperCase());
                pst.setString(3, this.getSelectedScenario().toUpperCase());
                pst.setString(4, "TRUE");
                ResultSet rs = pst.executeQuery();
                boolean flag = true;
                while (rs.next()) {
                    this.baselineDateList.put(rs.getString("TESTID"), rs.getString("SHORT_NAME"));
                    if (flag) {
                        this.setRecentbaselinedate(this.getDateByTimeStamp(rs.getString("TESTID")));

                    }
                }
                rs.close();
                pst.close();
                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.getRunningfor().equals("validation")) {
            try {
                String query = "SELECT TESTID,SHORT_NAME FROM PERF_USERS.JENKINS_TESTS WHERE APPNAME=? AND TESTTYPE=? AND SCENARIONAME = ? AND ISBASELINE=? ORDER BY TESTID DESC FETCH FIRST 4 ROWS ONLY";
                DBC = new DBConnection();
                this.connection = DBC.getConnection();
                PreparedStatement pst = this.connection.prepareStatement(query);
                pst.setString(1, this.getSelectedApplication().toUpperCase());
                pst.setString(2, this.getSelectedTestType().toUpperCase());
                pst.setString(3, this.getSelectedScenario().toUpperCase());
                pst.setString(4, "FALSE");
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    this.baselineDateList.put(rs.getString("TESTID"), rs.getString("SHORT_NAME"));
                }
                pst.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void readRunningTestAttributeFromDB() {
        try {
            String query = "SELECT TESTID,SHORT_NAME FROM PERF_USERS.JENKINS_TESTS WHERE APPNAME=? AND TESTTYPE=? AND SCENARIONAME = ? AND ISBASELINE=? ORDER BY TESTID DESC FETCH FIRST 1 ROWS ONLY";
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, this.getSelectedApplication().toUpperCase());
            pst.setString(2, this.getSelectedTestType().toUpperCase());
            pst.setString(3, this.getSelectedScenario().toUpperCase());
            pst.setString(4, "TRUE");
            ResultSet rs = pst.executeQuery();
            boolean flag = true;
            while (rs.next()) {
                this.baselineDateList.put(rs.getString("TESTID"), rs.getString("SHORT_NAME"));
                if (flag) {
                    this.setRecentbaselinedate(this.getDateByTimeStamp(rs.getString("TESTID")));
                }
            }
            rs.close();
            pst.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isTestRunningForSelectedApplication() {
        try {
            String query = "SELECT APPNAME, TESTTYPE, SCENARIONAME,TESTAUTHKEY,CONFLUENCEID FROM PERF_USERS.APPLICATIONS WHERE APPNAME=? AND APPLICATIONSTATUS=?";
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, this.getSelectedApplication().toUpperCase());
            pst.setString(2, "RUNNING");
            ResultSet rs = pst.executeQuery();
            boolean status = false;
            if (rs.next()) {
                this.setRunningApplicationName(rs.getString("APPNAME").toUpperCase());
                this.setSelectedApplication_status(rs.getString("APPNAME").toUpperCase());
                this.setRunningTestType(rs.getString("TESTTYPE").toUpperCase());
                this.setSelectedTestType_status(rs.getString("TESTTYPE").toUpperCase());
                this.setRunningScenarioName(rs.getString("SCENARIONAME").toUpperCase());
                this.setSelectedScenarioName_status(rs.getString("SCENARIONAME").toUpperCase());
                this.setSelectedTestConfId(rs.getString("CONFLUENCEID"));
                this.setRunningTestAuthKey(rs.getString("TESTAUTHKEY"));
                this.setTestConfluencepath_status(rs.getString("CONFLUENCEID"));
                status = true;
            }
            rs.close();
            pst.close();
            connection.close();
            return status;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getTimeStampByDate(String date) {
        //System.out.println("getTimeStampByDate : " + date);
        return Timestamp.valueOf(date).getTime() + "";
    }

    public String getDateByTimeStamp(String timeStamp) {
        //System.out.println("getDateByTimeStamp : " + timeStamp);
        Timestamp ts = new Timestamp(Long.parseLong(timeStamp));
        return ts.toString();
    }

    public void getConflueTinyURLByID(String conflueceId) {
        try {
            RestClientProvider rest = new RestClientProvider();
            rest.setUrl("https://confluence.es.ad.adp.com/rest/api/content/" + this.getTestConfluencepath_status());
            System.out.println("Confluence Path : " + "https://confluence.es.ad.adp.com/rest/api/content/" + this.getTestConfluencepath_status());
            rest.fireGetRequest();
            String response = rest.getResponseBody();
            System.out.println("Confluence Response : " + response);
            String confpath = this.filterTextByRegEx(response, "\"tinyui\":\"(.*?)\"");
            System.out.println("Tiny URL : " + confpath);
            this.setTestConfluencepath_status("https://confluence.es.ad.adp.com" + confpath);
            this.setIsTestPollingStopped(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String filterTextByRegEx(String text, String regExp) {
        Pattern r = Pattern.compile(regExp);
        Matcher m = r.matcher(text);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }

    public void readSelectedScenarioDetails() {
        try {
            this.setScenarioname(this.getSelectedScenario());
            String query = "SELECT TRIGGERBY, TESTSTATUS, CONCURRENCY, TESTSTARTTIME, TESTENDTIME ,TESTDURATION FROM PERF_USERS.JENKINS_TESTS WHERE TESTID = ?";
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            PreparedStatement pst = this.connection.prepareStatement(query);
            if (this.getRunningfor().equals("certification")) {
                String temp;
                if (this.getBaselineDateList().size() == 0) {
                    temp = "00000000";
                } else {
                    temp = this.getBaselineDateList().keySet().iterator().next();
                }

                System.out.println("Selected Test Id : " + temp);
                pst.setString(1, temp);
            } else {
                pst.setString(1, this.getSelectedBaselineDate());
            }

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                this.setTeststartby(rs.getString("TRIGGERBY"));
                this.setTeststatus(rs.getString("TESTSTATUS"));
                this.setConcurrentusersdet(rs.getString("CONCURRENCY"));
                this.setTestperiod(rs.getString("TESTSTARTTIME") + " - " + rs.getString("TESTENDTIME"));
                this.setTestduration(rs.getString("TESTDURATION"));

            }
            rs.close();
            pst.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readScenarioBasicInfo() {
        try {
            this.setScenarioname(this.getSelectedScenario());
            String query = "SELECT CONCURRENCY, AVGTESTDURATION, BASELINELINK, DESCRIPTION FROM PERF_USERS.APPLICATIONS WHERE APPNAME=? AND TESTTYPE =? AND SCENARIONAME = ?";
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, this.getSelectedApplication().toUpperCase());
            pst.setString(2, this.getSelectedTestType().toUpperCase());
            pst.setString(3, this.getSelectedScenario().toUpperCase());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                this.setConcurrentusers(rs.getString("CONCURRENCY"));
                this.setAvgtestduration(rs.getString("AVGTESTDURATION"));
                this.setBaselineconfluencepage(rs.getString("BASELINELINK"));
                this.setTestdescription(rs.getString("DESCRIPTION"));
            }
            rs.close();
            pst.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readRunningTestAttributes() {
        try {
            String query = "SELECT TESTID, TRIGGERBY FROM PERF_USERS.JENKINS_TESTS WHERE APPNAME=? AND  TESTTYPE=? AND SCENARIONAME=? AND TESTSTATUS=?";
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, this.getSelectedApplication_status().toUpperCase());
            pst.setString(2, this.getSelectedTestType_status().toUpperCase());
            pst.setString(3, this.getSelectedScenarioName_status().toUpperCase());
            pst.setString(4, "RUNNING");

            ResultSet rs = pst.executeQuery();
            boolean status = false;
            if (rs.next()) {
                this.setTestSubmittedTime_status(this.getDateByTimeStamp(rs.getString("TESTID").toUpperCase()) + " PST");
                this.setTestTriggerBy_status(rs.getString("TRIGGERBY"));

                status = true;
            }
            rs.close();
            pst.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pollTestStatus() {
        this.setIsStopBtnDisabled(false);
        this.readRunningTestAttributes();
        this.readTestStatusFromJenkins();
        this.setIsTestPollingStopped(false);
        RequestContext.getCurrentInstance().execute("PF('showteststatusdialog').show()");
    }

    public void validateShowTestStatus() {
        if (this.getSecretkeystatusddialog() != null && this.getSecretkeystatusddialog().length() > 1) {
            if (this.getSecretkeystatusddialog().equals(this.getRunningTestAuthKey())) {
                System.out.println("Show Status Progress here");
                this.getConflueTinyURLByID(this.getTestConfluencepath_status());
                RequestContext.getCurrentInstance().execute("PF('authenticatestatusdialog').hide()");
                this.pollTestStatus();

            } else {
                RequestContext.getCurrentInstance().execute("PF('invalidauthrizationkey').show()");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('invalidauthrizationkey').show()");

        }
    }

    public void startTest() {
        if (this.getShortnameForTest().length() < 3) {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Input missing", "Please provide a valid short description!"));
        } else {
            RequestContext.getCurrentInstance().execute("PF('starttestauthenticationdialog').show()");
        }

    }

    public String getNextTestId() {
        return System.currentTimeMillis() + "";
    }

    public void stopTest() {
        RequestContext.getCurrentInstance().execute("PF('authenticatestopdialog').show()");
    }

    public void submitStopTest() {
        if (this.getSecretkeystopddialog() != null && this.validateUserAuthKey(this.getSecretkeystopddialog())) {
            this.setStatusDialogStopStatus(true);
            this.updateTestStatusToDB("ABORTED");
            this.updateTestStatusTODB2("ABORTED");
            this.setDisableStartBtn(false);
            this.setDisableStopBtn(true);
            this.readTestStatusFromJenkins();
            this.stopJenkinsJob();
            this.setIsTestPollingStopped(true);
            RequestContext.getCurrentInstance().execute("PF('tesaborteddialog').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('invalidauthrizationkeystoptest').show()");
        }
    }

    public boolean stopJenkinsJob() {
        RestClientProvider rest = new RestClientProvider();
        String scenarioFullName = URLEncoder.encode(this.getSelectedApplication() + this.getSelectedTestType() + this.getSelectedScenario()).replace("+", "%20").toUpperCase();
        String scenarioURL = DBC.getJenkinsBaseURL() + "job/" + scenarioFullName + "/" + this.getBuildNumber() + "/stop";
        System.out.println("Stop URL : " + scenarioFullName);
        System.out.println("Firing request : " + scenarioURL);
        rest.setUrl(scenarioURL);
        rest.firePostHTTP("Basic amVua2luc2FkbWluOmplbmtpbnNhZG1pbg==");
        return false;
    }

    public void getUniqueTestIdForRunningTest() {
        try {
            this.setScenarioname(this.getSelectedScenario());
            String query = "SELECT TESTID FROM PERF_USERS.JENKINS_TESTS WHERE APPNAME=? AND TESTTYPE=? AND SCENARIONAME=? AND TESTSTATUS=?";
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, this.getSelectedApplication().toUpperCase());
            pst.setString(2, this.getSelectedTestType().toUpperCase());
            pst.setString(3, this.getSelectedScenario().toUpperCase());
            pst.setString(4, "RUNNING");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                this.setUniqueTestID(rs.getString("TESTID"));
            }
            rs.close();
            pst.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateTestStatusTODB2(String status) {
        try {
            DBC = new DBConnection();
            System.out.println("Current test Id : " + this.getUniqueTestID());
            this.connection = DBC.getConnection();
            String query = " UPDATE PERF_USERS.JENKINS_TESTS SET TESTSTATUS=? WHERE TESTID=?";
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, "ABORTED");
            pst.setString(2, this.getUniqueTestID());
            pst.executeUpdate();
            pst.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTestStatusToDB(String status) {
        try {
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            String query = " UPDATE PERF_USERS.APPLICATIONS SET APPLICATIONSTATUS=? WHERE APPNAME = ? AND TESTTYPE=? AND SCENARIONAME=?";
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, status);
            pst.setString(2, this.getSelectedApplication().toUpperCase());
            pst.setString(3, this.getSelectedTestType().toUpperCase());
            pst.setString(4, this.getSelectedScenario().toUpperCase());
            pst.executeUpdate();
            pst.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readLiveMetrics() {
        this.isTestRunningForSelectedApplication();
        this.readRunningTestAttributes();
        this.setIsTestPollingStopped(false);
        this.getConflueTinyURLByID(this.getTestConfluencepath_status());
        RequestContext.getCurrentInstance().execute("PF('authenticatestatusdialog').hide()");
        RequestContext.getCurrentInstance().execute("PF('showteststatusdialog').show()");
        this.setIsTestPollingStopped(false);
        this.getRunningScenarioStatus();
    }

    public void submitTest() {
        if (this.getUserAuthKey() != null && this.validateUserAuthKey(this.getUserAuthKey())) {
            if (!this.readTestStatusFromJenkins()) {
                System.out.println("User Entered : " + this.getUserAuthKey() + ", " + this.getUserEmail());
                this.setStatusDialogStopStatus(false);
                RequestContext.getCurrentInstance().execute("PF('starttestauthenticationdialog').hide()");
                this.updateTestStatusToDB("RUNNING");
                this.setIsTestPollingStopped(false);
                this.setUniqueTestID(this.getNextTestId());
                while (true) {
                    if (this.verifyTestID(this.getUniqueTestID())) {
                        this.setUniqueTestID(this.getNextTestId());
                    } else {
                        break;
                    }
                }
                this.triggetJenkinsJob();
                this.insertNewTestInfo(this.getUniqueTestID());
                this.setDisableStartBtn(true);
                this.setDisableStopBtn(false);
                this.readLiveMetrics();
                this.pollTestStatus();
                FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Test Scheduled", "Your test has been sceduled!"));
                RequestContext.getCurrentInstance().execute("PF('showteststatusdialog').show()");

            } else {
                FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Test Error", "Test Already running for selcted crieteria!"));
            }

        } else {
            System.out.println("In Else");
            RequestContext.getCurrentInstance().execute("PF('invalidauthrizationkeystarttest').show()");
        }

    }

    public boolean readTestStatusFromJenkins() {
        RestClientProvider rest = new RestClientProvider();
        String scenarioFullName = "";
        if (this.getSelectedTestType_status() != null) {
            System.out.println("In IF ");
            scenarioFullName = URLEncoder.encode(this.getSelectedApplication_status() + this.getSelectedTestType_status() + this.getSelectedScenarioName_status()).replace("+", "%20").toUpperCase();
        } else {
            System.out.println("In ELSE ");
            scenarioFullName = URLEncoder.encode(this.getSelectedApplication() + this.getSelectedTestType() + this.getSelectedScenario()).replace("+", "%20").toUpperCase();
        }
        String scenarioURL = DBC.getJenkinsBaseURL() + "job/" + scenarioFullName + "/lastBuild/api/json";
        System.out.println("Firing request : " + scenarioURL);
        rest.setUrl(scenarioURL);
        rest.fireGetRequest();
        boolean isTestRunning = this.getScenarioStatusByJSON(rest.getResponseBody());
        System.out.println("Reading Test Status : " + scenarioFullName + " : " + isTestRunning);
        return isTestRunning;

    }

    public boolean getScenarioStatusByJSON(String jsonBody) {
        try {
            //System.out.println("Received JSON Body : " + jsonBody);
            Object obj = new JSONParser().parse(jsonBody);
            JSONObject jo = (JSONObject) obj;
            boolean status = (Boolean) (Object) jo.get("building");
            if (status == true) {
                this.buildNumber = (Long) (Object) jo.get("number");
                System.out.println("Selected Build Number " + this.getBuildNumber());

            } else {

            }

            return status;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public void insertNewTestInfo(String testID) {
        try {
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            String query = "INSERT INTO PERF_USERS.JENKINS_TESTS(TESTID, APPNAME, TESTTYPE,SCENARIONAME, TESTSTATUS, TRIGGERBY, SHORT_NAME ) VALUES(?,?, ?, ?, ?, ?,?)";
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, testID);
            pst.setString(2, this.getSelectedApplication().toUpperCase());
            pst.setString(3, this.getSelectedTestType().toUpperCase());
            pst.setString(4, this.getSelectedScenario().toUpperCase());
            pst.setString(5, "RUNNING");
            pst.setString(6, this.getUserEmail());
            pst.setString(7, this.getShortnameForTest());
            pst.executeUpdate();
            pst.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean triggetJenkinsJob() {
        RestClientProvider rest = new RestClientProvider();
        String temp = "000000";
        if (this.getRunningfor().equals("certification")) {
            temp = this.getBaselineDateList().keySet().iterator().next();
        } else {
            temp = this.getTimeStampByDate(this.getSelectedBaselineDate());
        }

        String scenarioFullName = URLEncoder.encode(this.getSelectedApplication() + this.getSelectedTestType() + this.getSelectedScenario()).replace("+", "%20").toUpperCase();
        String scenarioURL = DBC.getJenkinsBaseURL() + "job/" + scenarioFullName + "/buildWithParameters?token=";
        scenarioURL = scenarioURL + this.getUserAuthKey();
        scenarioURL = scenarioURL + "&TRIGGER_BY=" + this.getTeststartby();
        scenarioURL = scenarioURL + "&SUBMITTED_TIME=" + this.getUniqueTestID();
        scenarioURL = scenarioURL + "&BASELINE_DATE=" + temp;

        System.out.println("Firing request : " + scenarioURL);
        rest.setUrl(scenarioURL);
        rest.fireGetRequest();
        String statusCode = rest.getResponseCode();
        if (statusCode.contains("200") || statusCode.contains("201")) {
            return true;
        } else {
            return false;
        }

    }

    public boolean verifyTestID(String testID) {
        try {
            this.setScenarioname(this.getSelectedScenario());
            String query = "SELECT COUNT(*) FROM PERF_USERS.JENKINS_TESTS WHERE TESTID=?";
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, this.getSelectedApplication().toUpperCase());
            ResultSet rs = pst.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            pst.close();
            connection.close();
            if (count > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean validateUserAuthKey(String key) {
        try {
            this.setScenarioname(this.getSelectedScenario());
            String query = "SELECT COUNT(*) FROM PERF_USERS.APPLICATIONS WHERE APPNAME=? AND TESTTYPE =? AND SCENARIONAME = ? AND TESTAUTHKEY = ?";
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            PreparedStatement pst = this.connection.prepareStatement(query);
            pst.setString(1, this.getSelectedApplication().toUpperCase());
            pst.setString(2, this.getSelectedTestType().toUpperCase());
            pst.setString(3, this.getSelectedScenario().toUpperCase());
            pst.setString(4, key);
            ResultSet rs = pst.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            pst.close();
            connection.close();
            if (count > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void getRunningScenarioStatus() {
        System.err.println("getRunningScenarioStatus : is called");
        if (this.getBuildNumber() == 0) {
            this.setIsTestPollingStopped(true);
        } else {
            System.out.println("Polling Progress Stopped : " + this.getIsTestPollingStopped());
            String scenarioFullName = URLEncoder.encode(this.getSelectedApplication_status() + this.getSelectedTestType_status() + this.getSelectedScenarioName_status()).replace("+", "%20").toUpperCase();
            RestClientProvider rest = new RestClientProvider();
            String scenarioURL = DBC.getJenkinsBaseURL() + "job/" + scenarioFullName + "/" + this.getBuildNumber() + "/api/json";
            System.out.println(scenarioURL);
            rest.setUrl(scenarioURL);
            rest.fireGetRequest();
            boolean status = this.getScenarioStatusByJSON(rest.getResponseBody());
            if (status) {
                System.out.println("In Status : True");
                try {
                    Object obj = new JSONParser().parse(rest.getResponseBody());
                    JSONObject jo = (JSONObject) obj;
                    long estimatedDuration = (Long) (Object) jo.get("estimatedDuration");
                    long startTimeStamp = (Long) (Object) jo.get("timestamp");
                    this.setTestStarttime_status(this.getDateByTimeStamp(startTimeStamp + "") + " PST");
                    this.setTestEndTime_status(this.getDateByTimeStamp((startTimeStamp + estimatedDuration) + "") + " PST");
                    this.calculateProgress(startTimeStamp, (startTimeStamp + estimatedDuration));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    System.out.println("In Status : False");
                    this.polllooper++;

                    if (this.polllooper > 4) {
                        this.setTestETA_status(100);
                        this.setDisableStopBtn(true);
                        this.setIsStopBtnDisabled(true);
                        this.setStatusDialogStopStatus(true);

                    }
                    if (this.polllooper > 5) {
                        this.setIsTestPollingStopped(true);
                        this.polllooper = 0;
                        FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Test Completed", "Click on Confluence link!"));
                    }

                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Server Down", "Jenkins Server not reachable.."));
                    e.printStackTrace();
                }
            }
        }
    }

    public void calculateProgress(long startTime, long endTime) {
        long currentTime = System.currentTimeMillis();
        if (currentTime >= endTime) {
            this.setTestETA_status(99);
            this.setIsStopBtnDisabled(true);
            this.setStatusDialogStopStatus(true);

        } else {
            long progressStatus = (int) (100 - ((currentTime - endTime) * 100) / (startTime - endTime));
            System.out.println("Progress : " + this.getTestETA_status());
            if (progressStatus < 99) {
                this.setTestETA_status(progressStatus);
            } else if (progressStatus == 99) {
                if (onetimeShow) {
                    onetimeShow = false;
                    FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Almost Completed", "Your test is almost completed!\nPlease wait...!"));
                }

            }

        }
    }

    public void stoppollingStatus() {
        this.setIsTestPollingStopped(true);
    }

    private void prepareTrasactionsData() {
        transDataList = new ArrayList<ShowTransactionsData>();
        try {
            this.setScenarioname(this.getSelectedScenario());
            String query = "SELECT TRANSACTIONNAME, RESPMIN, RESPAVG, RESPMAX, RESPSTD,"
                    + " RESPNINP, RESPPASS FROM PERF_USERS.JENKINS_RESPONSETIMES WHERE "
                    + "TESTID = ?";
            DBC = new DBConnection();
            this.connection = DBC.getConnection();
            PreparedStatement pst = this.connection.prepareStatement(query);
            if (this.getRunningfor().equals("certification")) {
                String temp;
                if (this.getBaselineDateList().size() == 0) {
                    temp = "000000";
                } else {
                    temp = this.getBaselineDateList().keySet().iterator().next();
                }

                System.out.println("Selected Test Id : " + temp);
                pst.setString(1, temp);
            } else {
                pst.setString(1, this.getSelectedBaselineDate());
            }

            ResultSet res = pst.executeQuery();
            while (res.next()) {
                transDataList.add(new ShowTransactionsData(
                        res.getString("TRANSACTIONNAME"),
                        res.getFloat("RESPMIN") + "",
                        res.getFloat("RESPAVG") + "",
                        res.getFloat("RESPMAX") + "",
                        res.getFloat("RESPSTD") + "",
                        res.getFloat("RESPNINP") + "",
                        res.getString("RESPPASS")
                ));
            }
            res.close();
            pst.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
