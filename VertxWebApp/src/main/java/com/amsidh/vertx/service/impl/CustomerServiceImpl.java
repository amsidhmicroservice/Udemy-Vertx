package com.amsidh.vertx.service.impl;

import com.amsidh.vertx.model.Customer;
import com.amsidh.vertx.service.CustomerService;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private static final Map<Integer, Customer> customers = new HashMap<>();

    static {
        customers.put(1, Customer.builder().id(1).name("Amsidh").build());
        customers.put(2, Customer.builder().id(2).name("Anjali").build());
        customers.put(3, Customer.builder().id(3).name("Aditya").build());
        customers.put(4, Customer.builder().id(4).name("Adithi").build());
    }

    @Override
    public Optional<Customer> saveCustomer(Customer customer) {
        Optional<Customer> optionalCustomer = getCustomerById(customer.getId());
        if (optionalCustomer.isPresent()) {
            return optionalCustomer;
        } else {
            customers.put(customer.getId(), customer);
            return Optional.ofNullable(customer);
        }
    }

    @Override
    public Optional<Customer> getCustomerById(Integer id) {
        return Optional.ofNullable(customers.get(id));
    }


    @Override
    public Optional<Customer> updateCustomer(Integer id, Customer customer) {
        Optional<Customer> optionalCustomer = getCustomerById(id);
        if (optionalCustomer.isPresent()) {
            Customer oldCustomer = optionalCustomer.get();
            Optional.ofNullable(customer.getId()).ifPresent(oldCustomer::setId);
            Optional.ofNullable(customer.getName()).ifPresent(oldCustomer::setName);
            customers.put(id, oldCustomer);
            return optionalCustomer;
        } else {
            return optionalCustomer;
        }

    }

    @Override
    public Optional<Customer> deleteCustomerById(Integer id) {
        Optional<Customer> optionalCustomer = getCustomerById(id);
        if (optionalCustomer.isPresent()) {
            customers.remove(optionalCustomer.get().getId());
        }
        return optionalCustomer;
    }

    @Override
    public List<Customer> getAllCustomer() {
        return customers.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
    }
}
