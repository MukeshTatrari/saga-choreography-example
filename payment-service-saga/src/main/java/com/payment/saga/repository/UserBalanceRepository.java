package com.payment.saga.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payment.saga.entity.UserBalance;

public interface UserBalanceRepository extends JpaRepository<UserBalance, Integer> {

}
