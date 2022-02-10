package com.saga.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saga.api.dto.OrderRequestDTO;
import com.saga.api.events.OrderEvent;
import com.saga.api.events.OrderStatus;

import reactor.core.publisher.Sinks;

@Service
public class OrderStatusPublisher {

	@Autowired
	private Sinks.Many<OrderEvent> orderSinks;

	public void publishOrderEvent(OrderRequestDTO orderRequestDto, OrderStatus orderStatus) {
		OrderEvent orderEvent = new OrderEvent(orderRequestDto, orderStatus);
		orderSinks.tryEmitNext(orderEvent);
	}
}
