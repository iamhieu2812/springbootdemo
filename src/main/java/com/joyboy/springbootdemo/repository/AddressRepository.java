package com.joyboy.springbootdemo.repository;

import com.joyboy.springbootdemo.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
