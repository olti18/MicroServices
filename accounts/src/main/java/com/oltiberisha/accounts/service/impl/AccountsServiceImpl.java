package com.oltiberisha.accounts.service.impl;

import com.oltiberisha.accounts.constans.AccountConstans;
import com.oltiberisha.accounts.dto.AccountsDto;
import com.oltiberisha.accounts.dto.CustomerDto;
import com.oltiberisha.accounts.entity.Accounts;
import com.oltiberisha.accounts.entity.Customer;
import com.oltiberisha.accounts.exception.CustomerAlreadyExistsException;
import com.oltiberisha.accounts.exception.ResourceNotFoundException;
import com.oltiberisha.accounts.mapper.AccountsMapper;
import com.oltiberisha.accounts.mapper.CustomerMapper;
import com.oltiberisha.accounts.repository.AccountsRepository;
import com.oltiberisha.accounts.repository.CustomerRepository;
import com.oltiberisha.accounts.service.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
//@AllArgsConstructor
public class AccountsServiceImpl implements IAccountService {

    private final AccountsRepository accountsRepository;
    private final  CustomerRepository customerRepository;
    @Autowired
    public AccountsServiceImpl(AccountsRepository accountsRepository, CustomerRepository customerRepository) {
        this.accountsRepository = accountsRepository;
        this.customerRepository = customerRepository;
    }
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber()); // check if mobile number exists
        if(optionalCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("Customer already registerd with given mobile number" + customerDto.getMobileNumber());
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonymous");
        Customer savedCustomer= customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
    }

    @Override
    public CustomerDto fetchAccountDetails(String mobileNumber) {

        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

       CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
         return customerDto;

    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;

        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto != null){
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "accountNumber", accountsDto.getAccountNumber().toString()));
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "customerId", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            isUpdated = true;

        }

        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber.toString())
        );
                accountsRepository.deleteByCustomerId(customer.getCustomerId());
                customerRepository.deleteById(customer.getCustomerId());
        return false;
    }


    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
      

        long randomAccNumber = 1000000000L + Math.abs(new Random().nextLong() % 9000000000L);


        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountConstans.SAVINGS);
        newAccount.setBranchAddress(AccountConstans.ADDRESS);
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("Anonymous");

        return newAccount;
    }

}
