package com.yrys.onlineshop.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.yrys.onlineshop.domain.Customer;
import com.yrys.onlineshop.domain.Order;
import com.yrys.onlineshop.domain.OrderItem;
import com.yrys.onlineshop.dto.PaymentInfo;
import com.yrys.onlineshop.dto.Purchase;
import com.yrys.onlineshop.dto.PurchaseResponse;
import com.yrys.onlineshop.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class CheckoutServiceImpl implements CheckoutService{

    private CustomerRepository customerRepository;

    public CheckoutServiceImpl(CustomerRepository customerRepository,
                               @Value("${stripe.key.secret}") String secretKey) {
        this.customerRepository = customerRepository;
        Stripe.apiKey = secretKey;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {
        Order order = purchase.getOrder();
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(item -> order.add(item));

        order.setShippingAddress(purchase.getShippingAddress());
        order.setBillingAddress(purchase.getBillingAddress());

        Customer customer = purchase.getCustomer();
        String email = customer.getEmail();
        Customer customerFromDB = customerRepository.findByEmail(email);
        if (customerFromDB != null) {
            customer = customerFromDB;
        }
        customer.add(order);

        customerRepository.save(customer);

        return new PurchaseResponse(orderTrackingNumber);
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {

        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);
        params.put("description", "YKShop purchase");
        params.put("receipt_email", paymentInfo.getReceiptEmail());

        return PaymentIntent.create(params);
    }

    private String generateOrderTrackingNumber() {
        // UUID
        return UUID.randomUUID().toString();
    }
}
