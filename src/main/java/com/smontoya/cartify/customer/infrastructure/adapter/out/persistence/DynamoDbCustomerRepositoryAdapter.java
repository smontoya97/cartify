package com.smontoya.cartify.customer.infrastructure.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.smontoya.cartify.customer.application.port.out.CustomerRepositoryPort;
import com.smontoya.cartify.customer.domain.exception.CustomerNotFoundException;
import com.smontoya.cartify.customer.domain.exception.DuplicateEmailException;
import com.smontoya.cartify.customer.domain.model.Customer;
import com.smontoya.cartify.customer.domain.model.valueobjects.CustomerId;
import com.smontoya.cartify.customer.domain.model.valueobjects.Email;
import com.smontoya.cartify.customer.infrastructure.adapter.out.persistence.entity.CustomerEmailIndexItem;
import com.smontoya.cartify.customer.infrastructure.adapter.out.persistence.entity.CustomerItem;
import com.smontoya.cartify.customer.infrastructure.adapter.out.persistence.mapper.CustomerMapper;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactPutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.TransactionCanceledException;

@Repository
public class DynamoDbCustomerRepositoryAdapter implements CustomerRepositoryPort {

    private static final String TABLE_NAME = "customers";

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<CustomerItem> customerTable;
    private final DynamoDbTable<CustomerEmailIndexItem> emailIndexTable;

    public DynamoDbCustomerRepositoryAdapter(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
        this.customerTable = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(CustomerItem.class));
        this.emailIndexTable = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(CustomerEmailIndexItem.class));
    }

    @Override
    public void save(Customer customer) {
        CustomerItem customerItem = CustomerMapper.toItem(customer);
        CustomerEmailIndexItem emailIndexItem = CustomerMapper.toEmailIndexItem(customer);
        TransactPutItemEnhancedRequest<CustomerEmailIndexItem> emailPut = TransactPutItemEnhancedRequest
                .builder(CustomerEmailIndexItem.class)
                .item(emailIndexItem)
                .conditionExpression(Expression.builder()
                        .expression("attribute_not_exists(pk)")
                        .build())
                .build();

        TransactPutItemEnhancedRequest<CustomerItem> customerPut = TransactPutItemEnhancedRequest
                .builder(CustomerItem.class)
                .item(customerItem)
                .build();

        try {
            enhancedClient.transactWriteItems(TransactWriteItemsEnhancedRequest.builder()
                    .addPutItem(emailIndexTable, emailPut)
                    .addPutItem(customerTable, customerPut)
                    .build());
        } catch (TransactionCanceledException e) {
            throw new DuplicateEmailException(customer.getEmail().value(), e);
        }
    }

    @Override
    public Optional<Customer> findById(CustomerId id) {
        String pk = "CUSTOMER#" + id;
        Key key = Key.builder().partitionValue(pk).sortValue(pk).build();
        return Optional.ofNullable(customerTable.getItem(key)).map(CustomerMapper::toCustomer);
    }

    @Override
    public Optional<Customer> findByEmail(Email email) {
        String pk = "EMAIL#" + email.value();
        Key key = Key.builder().partitionValue(pk).sortValue(pk).build();
        CustomerEmailIndexItem emailIndexItem = emailIndexTable.getItem(key);
        if (emailIndexItem == null) {
            return Optional.empty();
        }

        return findById(CustomerId.of(emailIndexItem.getCustomerId()));
    }

    @Override
    public void update(Customer customer) {
        CustomerItem item = CustomerMapper.toItem(customer);
        try {
            customerTable.updateItem(UpdateItemEnhancedRequest.builder(CustomerItem.class)
                    .item(item)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_exists(pk)")
                            .build())
                    .build());
        } catch (Exception e) {
            throw new CustomerNotFoundException(customer.getId().toString());
        }
    }
}
