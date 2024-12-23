package be.api.services;

import be.api.dto.request.DrawMoneyRequestDTO;
import be.api.model.DrawMoneyHistory;

import java.util.List;

public interface IDrawMoneyServices {
    DrawMoneyHistory createDrawMoneyRequest(DrawMoneyRequestDTO dto);
    DrawMoneyHistory setStatusDrawMoneyRequest(int drawMoneyId, DrawMoneyHistory.STATUS status);
    List<DrawMoneyHistory> getListDrawMoneyRequestByUser();

}

