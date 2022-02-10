package com.saga.api.events;

import java.util.Date;
import java.util.UUID;

import com.saga.api.dto.OrderRequestDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderEvent implements Event {

	private UUID eventId = UUID.randomUUID();
	private Date eventDate = new Date();
	private OrderRequestDTO orderRequestDTO;
	private OrderStatus orderStatus;

	@Override
	public UUID getEventId() {
		return eventId;
	}

	@Override
	public Date getDate() {
		return eventDate;
	}

	public OrderEvent(OrderRequestDTO orderRequestDTO, OrderStatus orderStatus) {
		super();
		this.orderRequestDTO = orderRequestDTO;
		this.orderStatus = orderStatus;
	}

}
