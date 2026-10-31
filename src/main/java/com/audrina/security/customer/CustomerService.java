package com.audrina.security.customer;

import com.audrina.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private  final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public CustomerResponse createCustomer(CreateCustomerRequest request) {

        if (customerRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("Email already exists");
        }

        Customer createCustomer = mapper.toCustomer(request);
        createCustomer.setPassword(passwordEncoder.encode(request.getPassword()));
        createCustomer.setRole(UserRole.USER);

        Customer savedCustomer = customerRepository.save(createCustomer);
        return mapper.toCustomerResponse(savedCustomer);
    }

public  LoginResponse CustomerLogin(CustomerLoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                       request.getEmail(), request.getPassword()
               )

        );

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    String token= jwtService.generateToken(userDetails);

    return  new LoginResponse(userDetails.getUsername()
            ,token);
}

}
