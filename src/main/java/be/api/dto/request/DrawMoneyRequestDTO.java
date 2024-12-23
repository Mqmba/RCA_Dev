package be.api.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class DrawMoneyRequestDTO implements Serializable {
    public double numberPoint;
    public String bankName;
    public String bankAccountName;
    public String bankAccountNumber;
}

