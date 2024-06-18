package com.example.shopapp.controller;

import com.example.shopapp.dtos.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    @GetMapping("/{user_id}")
    public  ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId){
        try{
            return ResponseEntity.ok("Danh sach Order theo user_id");
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
            return ResponseEntity.ok("Create Oreder Successfully!");
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
            return ResponseEntity.ok("Update order thanh cong");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@Valid @PathVariable Long id){
        // Xoa mem thay doi status trong order thanh false
        return ResponseEntity.ok("Xoa thanh cong");
    }
}
