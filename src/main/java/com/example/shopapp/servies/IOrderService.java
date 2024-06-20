package com.example.shopapp.servies;

import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.responses.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws Exception;
    OrderResponse getOrder(Long id);
    List<OrderResponse> findByUserId(Long userId);
    OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws Exception;
    void deleteOrder(Long id) throws Exception;
}
