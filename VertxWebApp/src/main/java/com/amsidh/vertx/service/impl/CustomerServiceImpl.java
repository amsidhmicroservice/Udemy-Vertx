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
    public Customer saveCustomer(Customer customer) {
        if (customers.get(customer.getId()) == null) {
            customers.put(customer.getId(), customer);
            return customers.get(customer.getId());
        } else {
            throw new RuntimeException("Customer with id " + customer.getId() + "is already present");
        }
    }

    @Override
    public Customer getCustomerById(Integer id) {
        return Optional.ofNullable(customers.get(id)).orElseThrow(() -> new RuntimeException("Customer with id " + id + " not found"));
    }


    @Override
    public Customer updateCustomer(Integer id, Customer customer) {
        Customer oldCustomer = getCustomerById(id);
        Optional.ofNullable(customer.getId()).ifPresent(oldCustomer::setId);
        Optional.ofNullable(customer.getName()).ifPresent(oldCustomer::setName);
        return customers.put(id, oldCustomer);
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customers.remove(getCustomerById(id).getId());
    }

    @Override
    public List<Customer> getAllCustomer() {
        return customers.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
    }
}
