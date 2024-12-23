package be.api.repository;

import be.api.model.DrawMoneyHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDrawMoneyHistoryRepository extends JpaRepository<DrawMoneyHistory, Integer> {
    List<DrawMoneyHistory> findByUser_UserId(int userId);
}
