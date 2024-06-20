package com.example.shopapp.servies;

import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Order;
import com.example.shopapp.models.OrderStatus;
import com.example.shopapp.models.User;
import com.example.shopapp.repository.OrderRepository;
import com.example.shopapp.repository.UserRepository;
import com.example.shopapp.responses.OrderResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderImpl implements IOrderService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) throws Exception {
        //tìm xem user_id có tồn tại không
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id:" + orderDTO.getUserId()));
        //convert orderDTO sang order -> insert DB
        //Dùng thư viện modelMapper
        //Tạo một luồng bảng ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        //Cập nhật các trường của đơn hàng  từ OrderDTO
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(new Date()); // lấy thời điểm hiện tại
        order.setStatus(OrderStatus.PENDING);
        //Kiểm tra shipping đến  phải >= ngày hôm nay
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Date must be at least today!");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
        //Để map được từ Order sang OrderResponse thì phải sửa lại luồng map
        modelMapper.typeMap(Order.class, OrderResponse.class);
        OrderResponse orderResponse = new OrderResponse();
        modelMapper.map(order, orderResponse);
        return orderResponse;
    }

    @Override
    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
        return orderResponse;
    }

    @Override
    public List<OrderResponse> findByUserId(Long userId) {
        List<Order> orderList = orderRepository.findByUserId(userId);
        List<OrderResponse> orderResponses = orderList.stream()
                .map(order -> modelMapper.map(order, OrderResponse.class)).collect(Collectors.toList());
        return orderResponses;
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws Exception {
        //tìm xem user_order có tồn tại không
        Order exitstingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id:" + id));
        User exitstinUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id" + orderDTO.getUserId()));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        //Cập nhật các trường của đơn hàng  từ OrderDTO
        modelMapper.map(orderDTO, exitstingOrder);
        //Kiểm tra shipping đến  phải >= ngày hôm nay
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Date must be at least today!");
        }
        exitstingOrder.setShippingDate(shippingDate);
        exitstingOrder.setUser(exitstinUser);
        orderRepository.save(exitstingOrder);
        //Để map được từ Order sang OrderResponse thì phải sửa lại luồng map
        modelMapper.typeMap(Order.class, OrderResponse.class);
        OrderResponse orderResponse = new OrderResponse();
        modelMapper.map(exitstingOrder, orderResponse);
        return orderResponse;
    }

    @Override
    public void deleteOrder(Long id) throws DataNotFoundException {
        //tìm xem user_order có tồn tại không
        Order exitstingOrder = orderRepository.findById(id).orElse(null);
        //no hard-delete => please soft-delete
        if(exitstingOrder != null){
            exitstingOrder.setActive(false);
            orderRepository.save(exitstingOrder);
        }
    }
}
