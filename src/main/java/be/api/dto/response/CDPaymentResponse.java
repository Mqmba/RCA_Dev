package be.api.dto.response;

import be.api.model.CDPayment_Detail;
import be.api.model.Collector;
import be.api.model.CollectorDepotPayment;
import be.api.model.RecyclingDepot;
import lombok.Data;

@Data
public class CDPaymentResponse {
    public CDPayment_Detail cdPaymentDetail;

    public CollectorDepotPayment collectorDepotPayment;
}
