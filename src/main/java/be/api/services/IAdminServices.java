package be.api.services;

import be.api.dto.response.AdminActivityResponseDTO;
import be.api.dto.response.AdminDashboardResponseDTO;
import be.api.dto.response.AdminTransactionResponseDTO;
import be.api.model.User;

import java.util.List;

public interface IAdminServices {
    AdminDashboardResponseDTO getAdminDashBoard();
    List<User> getAllUser();

    List<User> getAllDepot();

    AdminActivityResponseDTO getAdminActivity();
    AdminTransactionResponseDTO getAdminTransaction();

}
