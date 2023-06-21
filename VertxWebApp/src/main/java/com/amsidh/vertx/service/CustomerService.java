package com.amsidh.vertx.service;

import com.amsidh.vertx.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Optional<Customer> saveCustomer(Customer customer);

    Optional<Customer> getCustomerById(Long id);

    Optional<Customer> updateCustomer(Long id, Customer customer);

    Optional<Customer> deleteCustomerById(Long id);

    List<Customer> getAllCustomer();

}
