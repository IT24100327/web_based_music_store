package model;

import model.enums.OrderStatus;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private List<Integer> trackIds;
    private double totalAmount;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private String paymentMethod;
    private String transactionId;
    private double discountAmount;
    private double finalAmount;
    private String promotionCode;

    public Order() {
    }

    ;

    public Order(int userId, List<Integer> trackIds, double totalAmount, OrderStatus status, LocalDateTime orderDate, String paymentMethod) {
        this.userId = userId;
        this.trackIds = trackIds;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
        this.paymentMethod = paymentMethod;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Integer> getTrackIds() {
        return trackIds;
    }

    public void setTrackIds(List<Integer> trackIds) {
        this.trackIds = trackIds;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public Date getOrderDateAsUtilDate() {
        if (this.orderDate != null) {
            return Timestamp.valueOf(this.orderDate);
        }
        return null;
    }
}