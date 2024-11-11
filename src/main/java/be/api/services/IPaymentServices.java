package be.api.services;

import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

public interface IPaymentServices {
    public String payWithVNPAYOnline(HttpServletRequest request, float price) throws UnsupportedEncodingException;
    
}
