package com.amsidh.vertx.service;

import com.amsidh.vertx.model.Customer;

import java.util.List;

public interface CustomerService {
    Customer saveCustomer(Customer customer);

    Customer getCustomerById(Integer id);

    Customer updateCustomer(Integer id, Customer customer);

    void deleteCustomerById(Integer id);

    List<Customer> getAllCustomer();

}
