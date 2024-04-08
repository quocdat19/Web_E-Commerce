package com.ra.service;

import com.ra.model.entity.*;

import java.util.List;

public interface OrderService {
    List<Orders> getAll(Long userId);
    Orders add(Users users, Double totalPrice);
    Orders getbySerial(Long userId, String serial);
    List<Orders> getByStatus(Long userId, EOrderStatus status);
    Orders save(Orders orders);
    Orders getByIdAndStatus(Long userId, Long orderId, EOrderStatus status);
}
