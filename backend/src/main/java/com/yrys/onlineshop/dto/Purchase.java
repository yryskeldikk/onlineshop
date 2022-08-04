package com.yrys.onlineshop.dto;

import com.yrys.onlineshop.domain.Address;
import com.yrys.onlineshop.domain.Customer;
import com.yrys.onlineshop.domain.Order;
import com.yrys.onlineshop.domain.OrderItem;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {
    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;
}
