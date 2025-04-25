package com.incon.backend.service;

import com.incon.backend.dto.request.OrderRequest;
import com.incon.backend.dto.response.OrderResponse;
import com.incon.backend.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(Integer buyerId, OrderRequest request);
    OrderResponse getOrderById(Integer orderId);
    List<OrderResponse> getOrdersByBuyerId(Integer buyerId);
    List<OrderResponse> getOrdersByStatus(OrderStatus orderStatus);
    OrderResponse updateOrderStatus(Integer orderId, OrderStatus orderStatus);
    OrderResponse cancelOrder(Integer orderId);
}