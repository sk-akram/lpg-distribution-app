package com.akram.mylpg;


public class OrderModel{

    int image;
    String consumerNo, orderId;
    String consumerName, orderStatus;

    public OrderModel(String consumerNo, int image, String orderId){
        this.consumerNo = consumerNo;
        this.image = image;
        this.orderId = orderId;
    }
}