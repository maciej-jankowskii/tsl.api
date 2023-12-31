package com.tsl.service;

import com.tsl.dtos.CustomerDTO;
import com.tsl.enums.PaymentRating;
import com.tsl.exceptions.AddressNotFoundException;
import com.tsl.exceptions.ContactPersonNotFoundException;
import com.tsl.exceptions.CustomerNotFoundException;
import com.tsl.mapper.CustomerMapper;
import com.tsl.model.address.Address;
import com.tsl.model.contractor.ContactPerson;
import com.tsl.model.contractor.Customer;
import com.tsl.repository.AddressRepository;
import com.tsl.repository.ContactPersonRepository;
import com.tsl.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    private final AddressRepository addressRepository;
    private final ContactPersonRepository contactPersonRepository;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, AddressRepository addressRepository, ContactPersonRepository contactPersonRepository) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.addressRepository = addressRepository;
        this.contactPersonRepository = contactPersonRepository;
    }

    /**
     * Finding methods
     */

    public List<CustomerDTO> findAllCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::mapToDTO).collect(Collectors.toList());
    }

    public List<CustomerDTO> findAllCustomersSortedBy(String sortBy) {
        return customerRepository.findAllCustomersBy(sortBy).stream().map(customerMapper::mapToDTO).collect(Collectors.toList());
    }

    public CustomerDTO findCustomerById(Long id) {
        return customerRepository.findById(id).map(customerMapper::mapToDTO).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }

    /**
     * Create, update methods
     */

    @Transactional
    public CustomerDTO addCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.mapToEntity(customerDTO);

        addAddressAndContactPersonsForCustomer(customerDTO, customer);
        addAdditionalDataForCustomer(customer);
        addAdditionalDataForContactPerson(customer);

        Customer saved = customerRepository.save(customer);
        return customerMapper.mapToDTO(saved);
    }

    @Transactional
    public void updateCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.mapToEntity(customerDTO);

        customerRepository.save(customer);
    }

    /**
     * Helper methods
     */

    private void addAddressAndContactPersonsForCustomer(CustomerDTO customerDTO, Customer customer) {
        Address address = addressRepository.findById(customerDTO.getAddressId()).orElseThrow(() -> new AddressNotFoundException("Address not found"));
        customer.setAddress(address);
        List<ContactPerson> contact = customerDTO.getContactPersonIds().stream()
                .map(contactPersonIds -> contactPersonRepository.findById(contactPersonIds)
                        .orElseThrow(() -> new ContactPersonNotFoundException("Contact Person not found with this ID " + contactPersonIds)))
                .collect(Collectors.toList());
        customer.setContactPersons(contact);
    }

    private static void addAdditionalDataForContactPerson(Customer customer) {
        List<ContactPerson> contactPersons = customer.getContactPersons();
        if (!contactPersons.isEmpty()) {
            contactPersons.forEach(person -> person.setContractor(customer));
        }
    }

    private static void addAdditionalDataForCustomer(Customer customer) {
        customer.setBalance(BigDecimal.ZERO);
        customer.setPaymentRating(PaymentRating.NONE);
    }
}
