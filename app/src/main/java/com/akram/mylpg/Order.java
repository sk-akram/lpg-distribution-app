package com.akram.mylpg;

public class Order {

    private String consumerNo, DistributorNo, status;

    public Order(){

    }
    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getDistributorNo() {
        return DistributorNo;
    }

    public void setDistributorNo(String distributorNo) {
        DistributorNo = distributorNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
