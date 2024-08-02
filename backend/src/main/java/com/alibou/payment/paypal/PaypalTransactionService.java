package com.alibou.payment.paypal;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RelatedResources;
import com.paypal.api.payments.Sale;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PaypalTransactionService {

    private final APIContext apiContext;
    private final PaymentDetailsRepository paymentDetailsRepository;

    public Payment getPaymentDetails(String transactionId) throws PayPalRESTException {
        return Payment.get(apiContext, transactionId);
    }

public PaymentDetails savePaymentDetails(String transactionId) throws PayPalRESTException {
    // Fetch payment details from PayPal
    Payment payment = getPaymentDetails(transactionId);

    // Create a new PaymentDetails object
    PaymentDetails paymentDetails = new PaymentDetails();
    paymentDetails.setTransactionId(payment.getId());

    // Parse and set the payment date
    paymentDetails.setDate(LocalDateTime.parse(payment.getCreateTime(), DateTimeFormatter.ISO_DATE_TIME));

    // Set the description of the payment
    paymentDetails.setDescription(payment.getTransactions().get(0).getDescription());

    // Set the payer's name
    paymentDetails.setName(payment.getPayer().getPayerInfo().getFirstName() + " " + payment.getPayer().getPayerInfo().getLastName());

    // Set the status of the payment
    paymentDetails.setStatus(payment.getState());

    // Set the gross amount of the payment
    paymentDetails.setGross(Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal()));

    // Retrieve transaction fee and net amount
    // Fees are typically part of the Payment's transaction details
    double fee = 0.0;
    double net = paymentDetails.getGross(); // Default to gross if fee isn't available

    for (Transaction transaction : payment.getTransactions()) {
        if (transaction.getRelatedResources() != null) {
            for (RelatedResources relatedResource : transaction.getRelatedResources()) {
                if (relatedResource.getSale() != null) {
                    Sale sale = relatedResource.getSale();
                    // Extract fee from the sale object
                    fee = sale.getTransactionFee().getValue() != null
                        ? Double.parseDouble(sale.getTransactionFee().getValue())
                        : 0.0;
                    // Calculate net amount
                    net = paymentDetails.getGross() - fee;
                    break;
                }
            }
        }
    }

    paymentDetails.setFee(fee);
    paymentDetails.setNet(net);

    // Save the payment details to the repository
    return paymentDetailsRepository.save(paymentDetails);
}

}
