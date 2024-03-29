package com.saga.api.dto;

import com.saga.api.events.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {

	private Integer userId;
	private Integer productId;
	private Integer amount;
	private Integer orderId;
	private OrderStatus orderStatus;

}
