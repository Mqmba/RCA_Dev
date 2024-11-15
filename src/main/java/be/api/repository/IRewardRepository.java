package be.api.repository;

import be.api.model.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRewardRepository extends JpaRepository<Reward, Integer> {
}
