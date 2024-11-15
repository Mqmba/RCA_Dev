package be.api.services;

import be.api.dto.request.RewardDTO;
import be.api.model.Reward;
import org.springframework.data.domain.Page;

public interface IRewardServices {
    Page<Reward> getAllRewards(int page, int size);
    Reward updateReward(int rewardId, RewardDTO reward);
    Reward createReward(RewardDTO reward);
}
