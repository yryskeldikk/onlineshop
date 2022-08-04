package com.yrys.onlineshop.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.yrys.onlineshop.dto.PaymentInfo;
import com.yrys.onlineshop.dto.Purchase;
import com.yrys.onlineshop.dto.PurchaseResponse;


public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);

    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;

}
