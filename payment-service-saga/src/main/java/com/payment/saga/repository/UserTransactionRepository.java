package com.payment.saga.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payment.saga.entity.UserTransaction;

public interface UserTransactionRepository extends JpaRepository<UserTransaction, Integer> {

}
