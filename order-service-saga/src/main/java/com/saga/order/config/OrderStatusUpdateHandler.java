package com.saga.order.config;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.saga.api.dto.OrderRequestDTO;
import com.saga.api.events.OrderStatus;
import com.saga.api.events.PaymentStatus;
import com.saga.order.entity.PurchaseOrder;
import com.saga.order.repository.OrderRepository;
import com.saga.order.service.OrderStatusPublisher;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class OrderStatusUpdateHandler {

	@Autowired
	private OrderRepository repository;

	@Autowired
	private OrderStatusPublisher publisher;

	@Transactional
	public void updateOrder(Integer orderId, Consumer<PurchaseOrder> consumer) {
		repository.findById(orderId).ifPresent(consumer.andThen(this::updateOrderStatus));
	}

	private void updateOrderStatus(PurchaseOrder purchaseOrder) {
		log.info("Inside updateOrderStatus ::::: "+ purchaseOrder);
		boolean isPaymentComplete = PaymentStatus.PAYMENT_COMPLETED.equals(purchaseOrder.getPaymentStatus());
		OrderStatus orderStatus = isPaymentComplete ? OrderStatus.ORDER_COMPLETED : OrderStatus.ORDER_CANCELLED;
		purchaseOrder.setOrderStatus(orderStatus);
		if (!isPaymentComplete) {
			publisher.publishOrderEvent(convertEntityToDto(purchaseOrder), orderStatus);
		}
		log.info("Ending updateOrderStatus ::::: "+ purchaseOrder);
	}

	public OrderRequestDTO convertEntityToDto(PurchaseOrder purchaseOrder) {
		OrderRequestDTO orderRequestDto = new OrderRequestDTO();
		orderRequestDto.setOrderId(purchaseOrder.getId());
		orderRequestDto.setUserId(purchaseOrder.getUserId());
		orderRequestDto.setAmount(purchaseOrder.getPrice());
		orderRequestDto.setProductId(purchaseOrder.getProductId());
		return orderRequestDto;
	}
}
