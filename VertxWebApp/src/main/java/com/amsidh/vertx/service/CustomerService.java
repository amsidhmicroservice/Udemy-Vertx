package com.amsidh.vertx.service;

import com.amsidh.vertx.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Optional<Customer> saveCustomer(Customer customer);

    Optional<Customer> getCustomerById(Integer id);

    Optional<Customer> updateCustomer(Integer id, Customer customer);

    Optional<Customer> deleteCustomerById(Integer id);

    List<Customer> getAllCustomer();

}
