package be.api.services.impl;

import be.api.dto.request.RewardDTO;
import be.api.model.Reward;
import be.api.repository.IRewardRepository;
import be.api.services.IRewardServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RewardServices implements IRewardServices {

    private final IRewardRepository rewardRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<Reward> getAllRewards(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return rewardRepository.findAll(pageable);
    }

    @Override
    public Reward updateReward(int rewardId, RewardDTO reward) {
        return null;
    }

    @Override
    public Reward createReward(RewardDTO reward) {
        Reward rewardData = modelMapper.map(reward, Reward.class);
        return rewardRepository.save(rewardData);
    }
}
