import React, { useState } from 'react';
import axios from 'axios';

const PaymentForm = () => {
    const [method, setMethod] = useState('paypal');
    const [amount, setAmount] = useState('10.0');
    const [currency, setCurrency] = useState('USD');
    const [description, setDescription] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/payment/create', {
                method,
                amount,
                currency,
                description
            });
            const approvalUrl = response.data.approvalUrl;
            window.location.href = approvalUrl;
        } catch (error) {
            console.error('Payment creation failed:', error);
        }
    };

    return (
        <div className="container">
            <h1 className="text-center mt-5">Paypal Payment Integration</h1>
            <form onSubmit={handleSubmit} className="mt-5 card p-3">
                <div className="mb-3">
                    <label htmlFor="method" className="form-label">Payment method</label>
                    <input id="method" type="text" className="form-control" name="method" value={method} readOnly />
                </div>
                <div className="mb-3">
                    <label htmlFor="amount" className="form-label">Amount</label>
                    <input id="amount" type="number" className="form-control" name="amount" value={amount} onChange={(e) => setAmount(e.target.value)} />
                </div>
                <div className="mb-3">
                    <label htmlFor="currency" className="form-label">Currency</label>
                    <select className="form-control" id="currency" name="currency" value={currency} onChange={(e) => setCurrency(e.target.value)}>
                        <option value="USD">USD</option>
                        <option value="EUR">EUR</option>
                        <option value="GBP">GBP</option>
                    </select>
                </div>
                <div className="mb-3">
                    <label htmlFor="description" className="form-label">Description</label>
                    <input id="description" type="text" className="form-control" name="description" value={description} onChange={(e) => setDescription(e.target.value)} />
                </div>
                <div className="d-flex justify-content-center">
                    <button type="submit" className="btn btn-primary">Pay with Paypal</button>
                </div>
            </form>
        </div>
    );
};

export default PaymentForm;
