package com.saga.order.config;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.saga.api.events.PaymentEvent;

@Configuration
public class EventConsumerConfig {

	@Autowired
	private OrderStatusUpdateHandler handler;

	@Bean
	public Consumer<PaymentEvent> paymentEventConsumer() {
		// listen payment-event-topic
		// will check payment status
		// if payment status completed -> complete the order
		// if payment status failed -> cancel the order
		return (payment) -> handler.updateOrder(payment.getPaymentRequestDTO().getOrderId(), po -> {
			po.setPaymentStatus(payment.getPaymentStatus());
		});
	}
}
