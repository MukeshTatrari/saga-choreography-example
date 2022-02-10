package com.saga.order.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saga.api.dto.OrderRequestDTO;
import com.saga.order.entity.PurchaseOrder;
import com.saga.order.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("/create")
	public PurchaseOrder createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {

		return orderService.createOrder(orderRequestDTO);
	}

	@GetMapping("/")
	public List<PurchaseOrder> getAllOrders() {

		return orderService.getAllOrders();
	}

}
