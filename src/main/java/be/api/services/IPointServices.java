package be.api.services;

import be.api.dto.request.PointRequestDTO;
import jakarta.annotation.Nullable;

public interface IPointServices {
    int getPoints();
    String updatePointByUser (PointRequestDTO pointRequest);
}
