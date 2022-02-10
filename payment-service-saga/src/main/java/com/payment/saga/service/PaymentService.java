package com.payment.saga.service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payment.saga.entity.UserBalance;
import com.payment.saga.entity.UserTransaction;
import com.payment.saga.repository.UserBalanceRepository;
import com.payment.saga.repository.UserTransactionRepository;
import com.saga.api.dto.OrderRequestDTO;
import com.saga.api.dto.PaymentRequestDTO;
import com.saga.api.events.OrderEvent;
import com.saga.api.events.PaymentEvent;
import com.saga.api.events.PaymentStatus;

@Service
public class PaymentService {

	@Autowired
	private UserBalanceRepository userBalanceRepository;

	@Autowired
	private UserTransactionRepository userTransactionRepository;

	/**
	 * 
	 * @param orderEvent
	 * @return
	 * 
	 *         // get the userId
	 * 
	 *         // check the balance availability
	 * 
	 *         // if Balance Sufficient -->PAYMENT_COMPLETED and deduct amount from
	 *         DB
	 * 
	 *         // if Balance Insufficient --> PAYMENT_FAILED cancelled the order
	 *         event and // update the amount in DB.
	 * 
	 */
	@Transactional
	public PaymentEvent newOrderEvent(OrderEvent orderEvent) {
		OrderRequestDTO orderRequestDto = orderEvent.getOrderRequestDTO();

		PaymentRequestDTO paymentRequestDto = new PaymentRequestDTO(orderRequestDto.getOrderId(),
				orderRequestDto.getUserId(), orderRequestDto.getAmount());

		return userBalanceRepository.findById(orderRequestDto.getUserId())
				.filter(ub -> ub.getPrice() > orderRequestDto.getAmount()).map(ub -> {
					ub.setPrice(ub.getPrice() - orderRequestDto.getAmount());
					userTransactionRepository.save(new UserTransaction(orderRequestDto.getOrderId(),
							orderRequestDto.getUserId(), orderRequestDto.getAmount()));
					return new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_COMPLETED);
				}).orElse(new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_FAILED));

	}

	@Transactional
	public void cancelOrderEvent(OrderEvent orderEvent) {

		userTransactionRepository.findById(orderEvent.getOrderRequestDTO().getOrderId())
		.ifPresent(ut -> {
			userTransactionRepository.delete(ut);
			userTransactionRepository.findById(ut.getUserId())
					.ifPresent(ub -> ub.setAmount(ub.getAmount() + ut.getAmount()));
		});
	}

	@PostConstruct
	public void initUserBalanceInDB() {
		userBalanceRepository
				.saveAll(Stream.of(
						new UserBalance(101, 5000), 
						new UserBalance(102, 3000), 
						new UserBalance(103, 4200),
						new UserBalance(104, 20000), 
						new UserBalance(105, 999)).collect(Collectors.toList()));
	}

}
