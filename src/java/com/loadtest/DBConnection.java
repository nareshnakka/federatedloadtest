/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loadtest;

import java.sql.Connection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author NakkaNar
 */
public class DBConnection {

    Connection connection;
    String jenkinsBaseURL;

    public DBConnection() {
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/PERF_USERS");
            connection = ds.getConnection();
            jenkinsBaseURL = (String) ctx.lookup("java:comp/env/jenkinsBaseURL");
            System.out.println("DB Connection Established!");
        } catch (Exception e) {
            
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void closeConnection() {
        try {
            this.connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getJenkinsBaseURL() {
        return jenkinsBaseURL;
    }

    public void setJenkinsBaseURL(String jenkinsBaseURL) {
        this.jenkinsBaseURL = jenkinsBaseURL;
    }

}
