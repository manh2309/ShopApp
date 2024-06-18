package com.example.shopapp.controller;

import com.example.shopapp.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_detail")
public class OrderDetailController {

    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO){
        return ResponseEntity.ok("Create Order Detail Successfully!");
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrdeDetail(
            @Valid @PathVariable("id") Long id){
        return ResponseEntity.ok("Get order detail with id" + id);
    }
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrdeDetails(
            @Valid @PathVariable("orderId") Long orderId){
        return ResponseEntity.ok("get order details with orderId =" + orderId);
    }
    @PutMapping("/{id}")
    public  ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable("id") Long id,
            @RequestBody OrderDetailDTO orderDetailDTO){
        return ResponseEntity.ok("update order detail with id" + id +
                ", detail update = " + orderDetailDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id") Long id){
        return ResponseEntity.noContent().build();
    }
}
