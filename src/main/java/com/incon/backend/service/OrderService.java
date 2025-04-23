package com.incon.backend.service;

import com.incon.backend.dto.request.OrderRequest;
import com.incon.backend.dto.response.OrderResponse;
import com.incon.backend.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(Integer userId, OrderRequest request);
    OrderResponse getOrderById(Integer orderId);
    List<OrderResponse> getOrdersByUserId(Integer userId);
    List<OrderResponse> getOrdersByStatus(OrderStatus status);
    OrderResponse updateOrderStatus(Integer orderId, OrderStatus status);
    OrderResponse cancelOrder(Integer orderId);
}