/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loadtest;

import java.io.Serializable;

/**
 *
 * @author NakkaNar
 */
public class ShowTransactionsData implements Serializable {

    private String transactionName;
    private String respmin;
    private String respavg;
    private String respmax;
    private String respstd;
    private String respninp;
    private String resppass;

    /**
     * Creates a new instance of ShowTransactionsData
     */
    public ShowTransactionsData(String trans, String respmin, String respavg,
            String respmax, String respstd, String respninp, String resppass) {
        this.transactionName = trans;
        this.respmin = respmin;
        this.respmax = respmax;
        this.respavg = respavg;
        this.respstd = respstd;
        this.respninp = respninp;
        this.resppass = resppass;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public String getRespmin() {
        return respmin;
    }

    public void setRespmin(String respmin) {
        this.respmin = respmin;
    }

    public String getRespavg() {
        return respavg;
    }

    public void setRespavg(String respavg) {
        this.respavg = respavg;
    }

    public String getRespmax() {
        return respmax;
    }

    public void setRespmax(String respmax) {
        this.respmax = respmax;
    }

    public String getRespstd() {
        return respstd;
    }

    public void setRespstd(String respstd) {
        this.respstd = respstd;
    }

    public String getRespninp() {
        return respninp;
    }

    public void setRespninp(String respninp) {
        this.respninp = respninp;
    }

    public String getResppass() {
        return resppass;
    }

    public void setResppass(String resppass) {
        this.resppass = resppass;
    }

}
