package be.api.services;

import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

public interface IPaymentServices {
    public String payWithVNPAYOnline(HttpServletRequest request, int price) throws UnsupportedEncodingException;
    public Boolean paymentCallback(int paymentId);
}
