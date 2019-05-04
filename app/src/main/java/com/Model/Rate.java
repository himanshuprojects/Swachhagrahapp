package com.Model;

public class Rate {
    public String ajmer;
    public String bikaner;
    public String bharatpur;
    public String jodhpur;
    public  String jaipur;
    public String kota;
    public String udaipur;



    public Rate()
{

}
    public Rate(String ajmer, String bikaner,String bharatpur,String jodhpur,String kota,String udaipur) {
        this.ajmer = ajmer;
        this.bikaner=bikaner;
        this.bharatpur=bharatpur;
        this.jodhpur=jodhpur;
        this.kota=kota;
        this.udaipur=udaipur;
    }

    public String getAjmer() {
        return ajmer;
    }

    public void setAjmer(String ajmer) {
        this.ajmer = ajmer;
    }

    public String getBikaner() {
        return bikaner;
    }

    public void setBikaner(String bikaner) {
        this.bikaner = bikaner;
    }

    public String getJaipur() {
        return jaipur;
    }

    public void setJaipur(String jaipur) {
        this.jaipur = jaipur;
    }

    public String getBharatpur() {
        return bharatpur;
    }

    public void setBharatpur(String bharatpur) {
        this.bharatpur = bharatpur;
    }

    public String getJodhpur() {
        return jodhpur;
    }

    public void setJodhpur(String jodhpur) {
        this.jodhpur = jodhpur;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getUdaipur() {
        return udaipur;
    }

    public void setUdaipur(String udaipur) {
        this.udaipur = udaipur;
    }

}
