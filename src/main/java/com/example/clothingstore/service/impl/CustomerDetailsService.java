package com.example.clothingstore.service.impl;

import com.example.clothingstore.entity.Customer;
import com.example.clothingstore.repository.CustomerRepository;
import com.example.clothingstore_contracts.input.CustomerInput;
import com.example.clothingstore.dto.CustomerDTO;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public CustomerDetailsService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<String> roles = new HashSet<>();
        if (customer.getEmail().contains("store")) {
            roles.add("ROLE_ADMIN");
        } else {
            roles.add("ROLE_USER");
        }

        return new User(
                customer.getEmail(),
                customer.getPassword(),
                roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
    }

    public void registerCustomer(CustomerInput customerInput) {
        Customer customer = modelMapper.map(customerInput, Customer.class);

        customer.setPassword(passwordEncoder.encode(customerInput.getPassword()));
        customer.setRoles(new HashSet<>(Collections.singleton("ROLE_USER")));

        customerRepository.save(customer);
    }

    public CustomerDTO getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + email));

        return modelMapper.map(customer, CustomerDTO.class);
    }

    public Long getCustomerIdByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return customer.getId();
    }
}
