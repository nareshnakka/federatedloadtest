/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loadtest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author NakkaNar
 */
public class ExtractResults {

    DBConnection DBC;
    String summaryFilePath;
    static HashMap<String, String> currentTestTransactions;
    static HashMap<String, String> baselineTestTransactions;
    String testStartTime;
    String currentTestId;
    String testEndTime;
    String concurrency;
    String isBaseline;
    String baselineTestId;
    String jenkinsBuildNumber;
    String pcRunId;
    String testDuration;
    String confluencePageId;
    String confluenceBaseURL;
    Connection conn;
    String confluenceAuth;
    String jenkinsBasePath;
    String jenkinsJobName;
    String ConfluencePageTitle;
    String testTriggerBy;
    String applicatioName;
    String testType;
    String scenarioName;
    long confluencePageNextVersion;
    String shortdescription;

    public void startExtracting(String args0, String args1, String args2, String args3, String args4) {

        this.initTemp(args0, args1, args2, args3, args4);
        this.connectToDB();
        this.readExtractSummaryFile();
        this.generateTestID();
        this.commitBasicDetailstoDB();
        this.commitCurrentTestResultstoDB();
        this.updateTestStausToDB();
        this.cleanTempFile();
        this.printOutPuts();

    }

    private void initTemp(String arg0, String arg1, String arg2, String arg3, String arg4) {
        //C:\CICD\HTMLExtractor>java -jar ExtractResults.jar APPLICATION TESTTYPE SCENARIO FILEPATH\
        this.setApplicatioName(arg0);
        this.setTestType(arg1);
        this.setScenarioName(arg2);
        this.setShortdescription(arg3);
        this.setSummaryFilePath(arg4);
//        this.setApplicatioName("TAX MANAGER");
//        this.setTestType("LOAD TEST");
//        this.setScenarioName("DEMO USER");
//        this.setSummaryFilePath("C:\\UploadedFiles\\summary5984340271054394928.html");

    }

    public String getApplicatioName() {
        return applicatioName;
    }

    public void setApplicatioName(String applicatioName) {
        this.applicatioName = applicatioName;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public String getShortdescription() {
        return shortdescription;
    }

    public void setShortdescription(String shortdescription) {
        this.shortdescription = shortdescription;
    }

    public String getTestTriggerBy() {
        return testTriggerBy;
    }

    public void setTestTriggerBy(String testTriggerBy) {
        this.testTriggerBy = testTriggerBy;
    }

    public String getConfluencePageTitle() {
        return ConfluencePageTitle;
    }

    public void setConfluencePageTitle(String ConfluencePageTitle) {
        this.ConfluencePageTitle = ConfluencePageTitle;
    }

    public long getConfluencePageNextVersion() {
        return confluencePageNextVersion;
    }

    public void setConfluencePageNextVersion(long confluencePageNextVersion) {
        this.confluencePageNextVersion = confluencePageNextVersion;
    }

    public String getConfluenceBaseURL() {
        return confluenceBaseURL;
    }

    public void setConfluenceBaseURL(String confluenceBaseURL) {
        this.confluenceBaseURL = confluenceBaseURL;
    }

    public String getJenkinsJobName() {
        return jenkinsJobName;
    }

    public void setJenkinsJobName(String jenkinsJobName) {
        this.jenkinsJobName = jenkinsJobName;
    }

    public String getJenkinsBasePath() {
        return jenkinsBasePath;
    }

    public void setJenkinsBasePath(String jenkinsBasePath) {
        this.jenkinsBasePath = jenkinsBasePath;
    }

    public String getCurrentTestId() {
        return currentTestId;
    }

    public void setCurrentTestId(String currentTestId) {
        this.currentTestId = currentTestId;
    }

    public String getSummaryFilePath() {
        return summaryFilePath;
    }

    public void setSummaryFilePath(String summaryFilePath) {
        this.summaryFilePath = summaryFilePath;
    }

    public String getConfluencePageId() {
        return confluencePageId;
    }

    public void setConfluencePageId(String confluencePageId) {
        this.confluencePageId = confluencePageId;
    }

    public String getBaselineTestId() {
        return baselineTestId;
    }

    public void setBaselineTestId(String baselineTestId) {
        this.baselineTestId = baselineTestId;
    }

    public String getTestDuration() {
        return testDuration;
    }

    public void setTestDuration(String testDuration) {
        this.testDuration = testDuration;
    }

    public String getTestStartTime() {
        return testStartTime;
    }

    public void setTestStartTime(String testStartTime) {
        this.testStartTime = testStartTime;
    }

    public String getConfluenceAuth() {
        return confluenceAuth;
    }

    public void setConfluenceAuth(String confluenceAuth) {
        this.confluenceAuth = confluenceAuth;
    }

    public String getTestEndTime() {
        return testEndTime;
    }

    public void setTestEndTime(String testEndTime) {
        this.testEndTime = testEndTime;
    }

    public String getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(String concurrency) {
        this.concurrency = concurrency;
    }

    public String getIsBaseline() {
        return isBaseline;
    }

    public void setIsBaseline(String isBaseline) {
        this.isBaseline = isBaseline;
    }

    public String getJenkinsBuildNumber() {
        return jenkinsBuildNumber;
    }

    public void setJenkinsBuildNumber(String jenkinsBuildNumber) {
        this.jenkinsBuildNumber = jenkinsBuildNumber;
    }

    public String getPcRunId() {
        return pcRunId;
    }

    public void setPcRunId(String pcRunId) {
        this.pcRunId = pcRunId;
    }

    private String getMillesSecconds() {
        Random random = new Random();
        int x = random.nextInt(900) + 100;
        return x + "";
    }

    private String convertStringToData(String stringData) {
        System.out.println(stringData);
        if (stringData.contains("AM") || stringData.contains("PM")) {
            try {
                String formatedDate = "";
                String temp[] = stringData.split(" ");
                String date[] = temp[0].split("/");
                String time[] = temp[1].split(":");
                formatedDate = formatedDate + date[2] + "-" + date[0] + "-" + date[1] + " ";
                if (temp[2].equals("AM")) {
                    formatedDate = formatedDate + time[0] + ":" + time[1] + ":" + time[2] + "." + this.getMillesSecconds();
                    System.out.println(formatedDate);
                } else {
                    formatedDate = formatedDate + (12 + Integer.parseInt(time[0])) + ":" + time[1] + ":" + time[2] + "." + this.getMillesSecconds();
                }

                return formatedDate;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                String formatedDate = "";
                String temp[] = stringData.split("   ");
                String date[] = temp[0].split("/");
                String time[] = temp[1].split(":");
                formatedDate = formatedDate + date[2] + "-" + date[0] + "-" + date[1] + " ";
                formatedDate = formatedDate + time[0] + ":" + time[1] + ":" + time[2] + "." + this.getMillesSecconds();

                return formatedDate;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private void readExtractSummaryFile() {

        //this.setSummaryFilePath(this.getJenkinsBasePath() + this.getJenkinsJobName() + "/builds/" + this.getJenkinsBuildNumber() + "/archive/performanceTestsReports/pcRun" + this.getPcRunId() + "/Report/summary.html");
        String transactionLine = "";
        boolean isTransactionStarted = false;
        this.currentTestTransactions = new HashMap<String, String>();
        try {
            FileReader summaryReader = new FileReader(new File(summaryFilePath));
            BufferedReader summaryBuffer = new BufferedReader(summaryReader);
            String line;
            while ((line = summaryBuffer.readLine()) != null) {
                if (line.contains("class=\"header_timerange\">Period: ")) {
                    this.extractTestPeriod(line);
                }
                if (line.contains("<td headers=\"LraDuration\" class=\"VerBl8\">")) {
                    this.extractTestDuration(line);
                }
                if (line.contains("LraMaximumRunningVusers")) {
                    this.extractConcurrency(line);
                }
                if (line.contains("<td headers=\"LraTransaction Name&nbsp;\" class=\"styling_2\">")) {
                    isTransactionStarted = true;
                    //System.out.println("Set  to True");
                }
                /*old version of Summary*/
                if (line.contains("headers=\"LraTransaction Name\" style=\"white-space: nowrap;\">")) {
                    isTransactionStarted = true;
                    //System.out.println("Set  to True");
                }
                if (isTransactionStarted && line.contains("LraStop")) {
                    isTransactionStarted = false;
                    // System.out.println(transactionLine);
                    this.extractTransactionProfile(transactionLine.replace("        ", ""));
                    transactionLine = "";
                    //System.out.println("Set to False");
                }
                if (isTransactionStarted) {
                    transactionLine = transactionLine + line;
                }
            }
            summaryBuffer.close();
            summaryReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void extractTestPeriod(String line) {
        Pattern pattern;
        if (line.contains("\\(")) {
            pattern = Pattern.compile("class=\"header_timerange\">Period: (.*?) - (.*?) \\((.*?)");
        } else {
            pattern = Pattern.compile("class=\"header_timerange\">Period: (.*?) - (.*?)</td>");
        }

        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            this.setTestStartTime(matcher.group(1));
            this.setTestEndTime(matcher.group(2));
        }

    }

    private void extractTestDuration(String line) {
        Pattern pattern = Pattern.compile("<td headers=\"LraDuration\" class=\"VerBl8\">(.*?)</td>");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            this.setTestDuration(matcher.group(1));
        }
    }

    private void extractConcurrency(String line) {
        Pattern pattern = Pattern.compile("<td headers=\"LraMaximumRunningVusers\" class=\"90WidthClass\"><span class=\"Verbl8\">(.*?)</span></td>");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            this.setConcurrency(matcher.group(1));
        }
    }

    private void printOutPuts() {
        System.out.println("Test Start Time : " + this.getTestStartTime());
        System.out.println("Test End Time : " + this.getTestEndTime());
        System.out.println("Test Duration : " + this.getTestDuration());
        System.out.println("Concurrency : " + this.getConcurrency());
        System.out.println("Current Test Data : " + this.currentTestTransactions);
        System.out.println("Current Test ID : " + this.getCurrentTestId());

    }

    private void extractTransactionProfile(String line) {
        if (line.contains("href=\"ResponseTime")) {
            Pattern pattern = Pattern.compile("<td headers=\"LraTransaction Name&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td><td><img src=\"led_no_data.gif\" alt=\"Show SLA Results\" border=0></a></td><td headers=\"LraMinimum&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td><td headers=\"LraAverage&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td><td headers=\"LraMaximum&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td><td headers=\"LraStd. Deviation&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td><td headers=\"Lra90 Percent&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td><td headers=\"LraPass&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td><td headers=\"LraFail&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td>");
            if (line.contains("white-space: nowrap;")) {
                System.out.println("Matching with 1st Pattern");
                pattern = Pattern.compile("<td headers=\"LraTransaction Name\" style=\"white-space: nowrap;\"><a href=\"(.*?)\" span class=\"VerBluUnd7\">(.*?)</td><td><a href=\"slarules:(.*?)\"><img src=\"led_no_data.gif\" alt=\"Show SLA Results\" border=0></a></td><td headers=\"LraMinimum\" style=\"white-space: nowrap;\"><span class=\"VerBl8\">(.*?)</td><td headers=\"LraAverage\" style=\"white-space: nowrap;\"><span class=\"VerBl8\">(.*?)</td><td headers=\"LraMaximum\" style=\"white-space: nowrap;\"><span class=\"VerBl8\">(.*?)</td><td headers=\"LraStd. Deviation\" style=\"white-space: nowrap;\"><span class=\"VerBl8\">(.*?)</td><td headers=\"Lra90 Percent\" style=\"white-space: nowrap;\"><span class=\"VerBl8\">(.*?)</td><td headers=\"LraPass\" style=\"white-space: nowrap;\"><span class=\"VerBl8\">(.*?)</td><td headers=\"LraFail\" style=\"white-space: nowrap;\"><span class=\"VerBl8\">(.*?)</td>");
            } else if (line.contains("class=\"styling_2\"")) {
                System.out.println("Matching with 2 Pattern");
                pattern = Pattern.compile("<td headers=\"LraTransaction Name&nbsp;\" class=\"styling_2\"><a href=\"(.*?)\" span class=\"VerBluUnd7\">(.*?)</td><td><a href=\"slarules:(.*?)\"><img src=\"led_no_data.gif\" alt=\"Show SLA Results\" border=0></a></td><td headers=\"LraMinimum&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td><td headers=\"LraAverage&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td><td headers=\"LraMaximum&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td><td headers=\"LraStd. Deviation&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td><td headers=\"Lra90 Percent&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td><td headers=\"LraPass&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td><td headers=\"LraFail&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td>");
            }
            System.out.println(">>>\n" + line);
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String tName = matcher.group(2);
                String respMetrics = "";
                for (int loop = 4; loop <= 10; loop++) {
                    if (loop != 4) {
                        respMetrics = respMetrics + ",";
                    }
                    respMetrics = respMetrics + matcher.group(loop);
                }
                this.currentTestTransactions.put(tName, respMetrics);
            }
        } else {
            //System.out.println("Matching with 3rd Pattern");
            Pattern pattern = Pattern.compile("<td headers=\"LraTransaction Name&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td><td><img src=\"led_no_data.gif\" alt=\"Show SLA Results\" border=0></a></td><td headers=\"LraMinimum&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td><td headers=\"LraAverage&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td><td headers=\"LraMaximum&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td><td headers=\"LraStd. Deviation&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td><td headers=\"Lra90 Percent&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td><td headers=\"LraPass&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td><td headers=\"LraFail&nbsp;\" class=\"styling_2\"><span class=\"VerBl8\">(.*?)</td>");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {

                String tName = matcher.group(1);
                String respMetrics = "";
                for (int loop = 2; loop <= 8; loop++) {
                    if (loop != 2) {
                        respMetrics = respMetrics + ",";
                    }
                    respMetrics = respMetrics + matcher.group(loop);
                }
                this.currentTestTransactions.put(tName, respMetrics);
            }
        }

    }

    public String getTimeStampByDate(String date) {
        //System.out.println("getTimeStampByDate : " + date);
        return Timestamp.valueOf(date).getTime() + "";
    }

    private void readConfigsFromDB() {
        {
            //reading Conflucen auth key
            try {
                final String query = "SELECT VALUE FROM PERF_USERS.CONFIG WHERE NAME=?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, "CONFLUENCE_AUTHKEY");
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    this.setConfluenceAuth(rs.getString("VALUE"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        {
            //Read Jenkins base path
            try {
                final String query = "SELECT VALUE FROM PERF_USERS.CONFIG WHERE NAME=?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, "JENKINS_DIRECTORY_PATH");
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    this.setJenkinsBasePath(rs.getString("VALUE"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        {
            //Read Confluence base path
            try {
                final String query = "SELECT VALUE FROM PERF_USERS.CONFIG WHERE NAME=?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, "CONFLUENCE_PAGE_BASE_URL");
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    this.setConfluenceBaseURL(rs.getString("VALUE"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void updateTestStausToDB() {
        try {
            final String query = "UPDATE PERF_USERS.APPLICATIONS SET APPLICATIONSTATUS=? WHERE SCENARIONAME=? AND APPNAME=? AND TESTTYPE=? AND APPLICATIONSTATUS=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "COMPLETED");
            stmt.setString(2, this.getScenarioName());
            stmt.setString(3, this.getApplicatioName());
            stmt.setString(4, this.getTestType());
            stmt.setString(5, "RUNNING");
            int updateCount = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void commitBasicDetailstoDB() {
        try {
            String query = "INSERT INTO PERF_USERS.JENKINS_TESTS("
                    + "TESTID,"
                    + "SCENARIONAME,"
                    + "TESTSTATUS,"
                    + "APPNAME,"
                    + "ISBASELINE,"
                    + "TESTDURATION,"
                    + "CONCURRENCY,"
                    + "TESTSTARTTIME,"
                    + "TESTENDTIME,"
                    + "TESTTYPE,"
                    + "TRIGGERBY,"
                    + "SHORT_NAME"
                    + ") VALUES "
                    + "(?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, this.getCurrentTestId());
            stmt.setString(2, this.getScenarioName());
            stmt.setString(3, "COMPLETED");
            stmt.setString(4, this.getApplicatioName());
            stmt.setString(5, "TRUE");
            stmt.setString(6, this.getTestDuration());
            stmt.setString(7, this.getConcurrency());
            stmt.setString(8, this.getTestStartTime());
            stmt.setString(9, this.getTestEndTime());
            stmt.setString(10, this.getTestType());
            stmt.setString(11, "AVS_DevOps_Performance@ADP.com");
            stmt.setString(12, this.getShortdescription());
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void commitCurrentTestResultstoDB() {
        for (String key : currentTestTransactions.keySet()) {
            try {
                String numbers[] = currentTestTransactions.get(key).split(",");
                String query = "INSERT INTO PERF_USERS.JENKINS_RESPONSETIMES VALUES(?,?,?,?,?,?,?,?,?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, this.getCurrentTestId());
                stmt.setString(2, key);
                stmt.setDouble(3, Double.parseDouble(numbers[0]));
                stmt.setDouble(4, Double.parseDouble(numbers[1]));
                stmt.setDouble(5, Double.parseDouble(numbers[2]));
                stmt.setDouble(6, Double.parseDouble(numbers[3]));
                stmt.setDouble(7, Double.parseDouble(numbers[4]));
                stmt.setDouble(8, Double.parseDouble(numbers[5]));
                stmt.setDouble(9, Double.parseDouble(numbers[6]));
                stmt.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void connectToDB() {
        try {
            DBC = new DBConnection();
            conn = DBC.getConnection();
            System.out.println("Connected DB...!");
        } catch (Exception except) {
            System.out.println("Unable to Connect to DB...!");
            except.printStackTrace();
            System.exit(0);
        }
    }

    private String filterTextWithRegEx(String regEx, String text) {
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {

            return matcher.group(1);
        }
        return null;
    }

    private String getDateByTimeStamp(String timeStamp) {
        //System.out.println("getDateByTimeStamp : " + timeStamp);
        Timestamp ts = new Timestamp(Long.parseLong(timeStamp));
        return ts.toString();
    }

    private boolean verifyDBForTestID() {
        try {
            final String query = "SELECT TESTID FROM PERF_USERS.JENKINS_TESTS WHERE TESTID=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, this.getCurrentTestId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void generateTestID() {

        this.setCurrentTestId(this.getTimeStampByDate(this.convertStringToData(this.getTestStartTime())));
        while (this.verifyDBForTestID()) {
            this.setCurrentTestId(this.getTimeStampByDate(this.convertStringToData(this.getTestStartTime())));
        }
    }

    private void cleanTempFile() {
        File tempFile = new File(this.getSummaryFilePath());
        if (tempFile.delete()) {
            System.out.println("Summary File Deleted");
        }
    }

}
