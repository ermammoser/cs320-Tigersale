package com.tigersale.model;

/**
 * Created by ermam on 3/20/2017 for the tigersale.com application.
 *
 * An application representation of a Payment Method
 */
public class PaymentMethod {
    public String paymentMethodName;
    public String customerUsername;
    public String nameOnCard;
    public String creditCardNumber;
    public String cvcCode;
    public String expirationDate;

    public PaymentMethod(String paymentMethodName, String customerUsername, String nameOnCard, String creditCardNumber,
                         String cvcCode, String expirationDate)
    {
        this.paymentMethodName = paymentMethodName;
        this.customerUsername = customerUsername;
        this.nameOnCard = nameOnCard;
        this.creditCardNumber = creditCardNumber;
        this.cvcCode = cvcCode;
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString()
    {
        return paymentMethodName + ": " + customerUsername + ", " + nameOnCard + ", " + creditCardNumber + ", " + cvcCode + ", " + expirationDate;
    }
}
