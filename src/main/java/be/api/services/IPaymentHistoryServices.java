package be.api.services;

import be.api.model.Payment_History;
import be.api.model.User;

import java.util.List;

public interface IPaymentHistoryServices {
    Boolean createPaymentHistory(String orderCode,  int paymentStatus, int NumberPoint);
    Boolean updateSuccessPayment(String orderCode);
    Payment_History getPaymentHistoryByOrderCode(String orderCode);
    List<Payment_History> getPaymentHistoryByUser();
    Boolean updateSuccessPaymentCollector(String orderCode);
    Boolean changePointFromDepotToCollector(Long point, int collectorId);
    List<Payment_History> getListPaymentHistoryByPaging();
}
