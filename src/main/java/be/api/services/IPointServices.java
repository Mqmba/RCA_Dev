package be.api.services;

import be.api.dto.request.PointRequestDTO;
import jakarta.annotation.Nullable;

public interface IPointServices {
    double getPoints();
    String updatePointByUser (PointRequestDTO pointRequest);
}
