package be.api.services;

import be.api.dto.request.CDPaymentRequestDTO;
import be.api.dto.request.DepotMaterialRequestDTO;

public interface IDepotMaterialServices {
    boolean createDepotMaterial(DepotMaterialRequestDTO dto);
}
