package com.example.newliving;

import java.util.Date;

public class ItemModel {

    String name,textUmzugsplan,date,text,textName,address,nameCompany,type,priceKM,priceHour,fullPrice,deposit;
    int id;
    Boolean done,predetermined;



    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getTextName() {
        return textName;
    }

    public String getText() {
        return text;
    }

    public String getTextUmzugsplan() {
        return textUmzugsplan;
    }

    public void setTextUmzugsplan(String textUmzugsplan) {
        this.textUmzugsplan = textUmzugsplan;
    }

    public Boolean getPredetermined() {
        return predetermined;
    }

    public void setPredetermined(Boolean predetermined) {
        this.predetermined = predetermined;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNameCompany() {
        return nameCompany;
    }

    public void setNameCompany(String nameCompany) {
        this.nameCompany = nameCompany;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPriceKM() {
        return priceKM;
    }

    public void setPriceKM(String priceKM) {
        this.priceKM = priceKM;
    }

    public String getPriceHour() {
        return priceHour;
    }

    public void setPriceHour(String priceHour) {
        this.priceHour = priceHour;
    }

    public String getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(String fullPrice) {
        this.fullPrice = fullPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
