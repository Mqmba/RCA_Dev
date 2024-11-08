package be.api.controller;


import be.api.model.RecyclingDepot;
import be.api.services.impl.RecyclingDepotService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recycling-depot")
@RequiredArgsConstructor
public class RecyclingDepotController {
    private final RecyclingDepotService recyclingDepotService;
    @GetMapping("/get-list-recycling-depot-by-paging")
    public Page<RecyclingDepot> getRecyclingDepots(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return recyclingDepotService.getRecyclingDepots(pageable);
    }

    @GetMapping("/get-list-active-recycling-depot-by-paging")
    public Page<RecyclingDepot> getActiveRecyclingDepots(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return recyclingDepotService.getActiveRecyclingDepots(pageable);
    }

    @PostMapping("/create-recycling-depot")
    public RecyclingDepot createRecyclingDepot(@RequestBody RecyclingDepot recyclingDepot) {
        return recyclingDepotService.createRecyclingDepot(recyclingDepot);
    }

    @PostMapping("/change-is-working-status")
    public RecyclingDepot changeIsWorkingStatus(@RequestParam int id, @RequestParam boolean isWorking) {
        return recyclingDepotService.changeIsWorkingStatus(id, isWorking);
    }

    @PostMapping("/change-working-date")
    public RecyclingDepot updateWorkingDate(@RequestParam int id) {
        return recyclingDepotService.updateWorkingDate(id);
    }
}
