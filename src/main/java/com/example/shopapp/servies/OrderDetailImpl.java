package com.example.shopapp.servies;

import com.example.shopapp.dtos.OrderDetailDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Order;
import com.example.shopapp.models.OrderDetail;
import com.example.shopapp.models.Product;
import com.example.shopapp.repository.OrderDetailRepository;
import com.example.shopapp.repository.OrderRepository;
import com.example.shopapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailImpl implements IOrderDetailService{
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        //Kiem tra xem orderId va productId co trong db khong
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id:" + orderDetailDTO.getOrderId()));
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id" + orderDetailDTO.getProductId()));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .price(product.getPrice())
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .totalMoney((int) (product.getPrice() * orderDetailDTO.getNumberOfProducts()))
                .color(orderDetailDTO.getColor())
                .build();
        orderDetailRepository.save(orderDetail);
        return orderDetail;
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws Exception {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Can not find orderDetail id" + id));
        return orderDetail;
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception {
        //Kiem tra xem orderId va productId co trong db khong
        OrderDetail exitstingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Can not find order detail with id" + id));
            Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                    .orElseThrow(() -> new DataNotFoundException("Cannot find order with id:" + orderDetailDTO.getOrderId()));
            Product product = productRepository.findById(orderDetailDTO.getProductId())
                    .orElseThrow(() -> new DataNotFoundException("Cannot find user with id" + orderDetailDTO.getProductId()));
            exitstingOrderDetail.setOrder(order);
            exitstingOrderDetail.setProduct(product);
            exitstingOrderDetail.setPrice(product.getPrice());
            exitstingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
            exitstingOrderDetail.setTotalMoney((int) (product.getPrice() * orderDetailDTO.getNumberOfProducts()));
            exitstingOrderDetail.setColor(orderDetailDTO.getColor());
            orderDetailRepository.save(exitstingOrderDetail);
            return exitstingOrderDetail;
    }

    @Override
    public void deleteOrderDetail(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findAllByOrderId(Long orderId)  {
        List<OrderDetail> orderDetailList = orderDetailRepository.findAllByOrderId(orderId);
        return orderDetailList;
    }
}
