package be.api.dto.request;

import jakarta.persistence.Column;
import lombok.Data;

import java.io.Serializable;

@Data
public class VoucherRequestDTO implements Serializable {
    public String name;

    public String description;

    public String voucherCode;

    public double pointToRedeem;
}
