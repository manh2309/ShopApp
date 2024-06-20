package com.example.shopapp.controller;

import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.responses.OrderResponse;
import com.example.shopapp.servies.IOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    @Autowired
    private IOrderService iOrderService;
    @GetMapping("/user/{user_id}")
    //orders/user/{user_id}
    public  ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId){
        try{
            List<OrderResponse> orderResponses = iOrderService.findByUserId(userId);
            return ResponseEntity.ok(orderResponses);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public  ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long orderId){
        try{
            OrderResponse exitstingOrder = iOrderService.getOrder(orderId);
            return ResponseEntity.ok(exitstingOrder);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDTO orderDTO,
                                         BindingResult result){
        try{
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            OrderResponse orderResponse = iOrderService.createOrder(orderDTO);
            return ResponseEntity.ok(orderResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    //Cong viec cua admin
    public ResponseEntity<?> updateOrder(@Valid @PathVariable long id,
                                         @Valid @RequestBody OrderDTO orderDTO,
                                         BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            OrderResponse orderResponse = iOrderService.updateOrder(id,orderDTO);
            return ResponseEntity.ok(orderResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@Valid @PathVariable Long id){
        // Xoa mem thay doi status trong order thanh false
        try {
            iOrderService.deleteOrder(id);
            return ResponseEntity.ok("Xoa thanh cong");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
