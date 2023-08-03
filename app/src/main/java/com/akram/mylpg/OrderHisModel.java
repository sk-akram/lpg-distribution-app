package com.akram.mylpg;

public class OrderHisModel {

    private String consumerNo, distributorNo, orderId, distLoc, orTime, orDate, deTime, deDate;

    public OrderHisModel(String consumerNo, String distributorNo, String orderId, String distLoc, String orTime, String orDate, String deTime, String deDate) {
        this.consumerNo = consumerNo;
        this.distributorNo = distributorNo;
        this.orderId = orderId;
        this.distLoc = distLoc;
        this.orTime = orTime;
        this.orDate = orDate;
        this.deTime = deTime;
        this.deDate = deDate;
    }

    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getDistributorNo() {
        return distributorNo;
    }

    public void setDistributorNo(String distributorNo) {
        this.distributorNo = distributorNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDistLoc() {
        return distLoc;
    }

    public void setDistLoc(String distLoc) {
        this.distLoc = distLoc;
    }

    public String getOrTime() {
        return orTime;
    }

    public void setOrTime(String orTime) {
        this.orTime = orTime;
    }

    public String getOrDate() {
        return orDate;
    }

    public void setOrDate(String orDate) {
        this.orDate = orDate;
    }

    public String getDeTime() {
        return deTime;
    }

    public void setDeTime(String deTime) {
        this.deTime = deTime;
    }

    public String getDeDate() {
        return deDate;
    }

    public void setDeDate(String deDate) {
        this.deDate = deDate;
    }
}
