package be.api.controller;

import be.api.dto.request.RewardDTO;
import be.api.dto.response.ResponseData;
import be.api.model.Reward;
import be.api.services.impl.RewardServices;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reward")
@RequiredArgsConstructor
public class RewardController {
    private final RewardServices rewardServices;
    @GetMapping("/get-list-reward-by-paging")
    public ResponseEntity<Page<Reward>> getPaginateReward(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(rewardServices.getAllRewards(page, size));
    }
    @PostMapping("/create-reward")
    public ResponseEntity<Reward> createReward(@RequestBody RewardDTO reward) {
        return ResponseEntity.ok(rewardServices.createReward(reward));
    }
}
