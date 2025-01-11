package be.api.controller;


import be.api.dto.request.CDPaymentRequestDTO;
import be.api.dto.request.CreateDepotRequestDTO;
import be.api.dto.response.ResponseData;
import be.api.model.RecyclingDepot;
import be.api.security.anotation.RecyclingDepotOnly;
import be.api.services.impl.CDPaymentServices;
import be.api.services.impl.RecyclingDepotService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recycling-depot")
@RequiredArgsConstructor
public class RecyclingDepotController {
    private final RecyclingDepotService recyclingDepotService;
    private final CDPaymentServices cdPaymentServices;

    @GetMapping("/get-list-active-recycling-depot-by-paging")
    public ResponseData<?> getActiveRecyclingDepots() {
        try{
            List<RecyclingDepot> recyclingDepots = recyclingDepotService.getActiveRecyclingDepots();
            return new ResponseData<>(200, "List active recycling depot found", recyclingDepots);
        }
        catch (Exception e){
            return new ResponseData<>(500, "Internal server error while retrieving list active recycling depot with message: " + e.getMessage(), null);
        }
    }


    @PostMapping("/create-recycling-depot")
    public ResponseData<?> createRecyclingDepot(@RequestBody CreateDepotRequestDTO dto) {
        try{
            return new ResponseData<>(200, "Recycling depot created successfully", recyclingDepotService.createRecyclingDepot(dto));
        }
        catch (Exception e){
            return new ResponseData<>(500, "Internal server error while creating recycling depot with message: " + e.getMessage(), null);
        }

    }

    @PostMapping("/create-recycling-depot-payment")
    @RecyclingDepotOnly
    public ResponseData<?> createRecyclingDepotPayment(@RequestBody CDPaymentRequestDTO dto) {
        try{
            return new ResponseData<>(200, "Recycling depot payment created successfully", cdPaymentServices.createCDPayment(dto));
        }
        catch (Exception e){
            return new ResponseData<>(500, "Internal server error while creating recycling depot payment with message: " + e.getMessage(), null);
        }
    }

    @PostMapping("/update-success-payment-by-id")
    public ResponseData<?> updateSuccessPaymentById(@RequestParam int id) {
        try{
            return new ResponseData<>(200, "Recycling depot payment updated successfully", cdPaymentServices.updateSuccessCDPayment(id));
        }
        catch (Exception e){
            return new ResponseData<>(500, e.getMessage(), null);
        }
    }

    @PostMapping("/change-is-working-status")
    public RecyclingDepot changeIsWorkingStatus(@RequestParam int id, @RequestParam boolean isWorking) {
        return recyclingDepotService.changeIsWorkingStatus(id, isWorking);
    }

    @PostMapping("/change-working-date")
    public RecyclingDepot updateWorkingDate(@RequestParam int id) {
        return recyclingDepotService.updateWorkingDate(id);
    }

    @PostMapping("/get-list-payment")
    public ResponseData<?> getListPayment() {
        try{
            return new ResponseData<>(200, "List payment found", cdPaymentServices.getListPayment());
        }
        catch (Exception e){
            return new ResponseData<>(500, "Internal server error while retrieving list payment with message: " + e.getMessage(), null);
        }
    }

    @GetMapping("/get-list-recycling-depot")
    public ResponseData<?> getListRecyclingDepot() {
        try{
            return new ResponseData<>(200, "List recycling depot found", recyclingDepotService.getListRecyclingDepots());
        }
        catch (Exception e){
            return new ResponseData<>(500, "Internal server error while retrieving list recycling depot with message: " + e.getMessage(), null);
        }
    }

    @GetMapping("/get-recycling-depot-by-id")
    public ResponseData<?> getRecyclingDepotById(@RequestParam int id) {
        try{
            return new ResponseData<>(200, "Recycling depot found", recyclingDepotService.getRecyclingDepotById(id));
        }
        catch (Exception e){
            return new ResponseData<>(500, e.getMessage(), null);
        }
    }

    @GetMapping("/check-missing-material")
    public ResponseData<?> checkMissingMaterial() {
        try{
            return new ResponseData<>(200, "List recycling depot found", recyclingDepotService.checkIsMissingMaterial());
        }
        catch (Exception e){
            return new ResponseData<>(500, "Internal server error while retrieving list recycling depot with message: " + e.getMessage(), null);
        }
    }

    @GetMapping("/get-analytic-material-depot")
    public ResponseData<?> getAnalyticMaterialDepot() {
        try{
            return new ResponseData<>(200, "List recycling depot found", recyclingDepotService.analysisMaterial()  );
        }
        catch (Exception e){
            return new ResponseData<>(500, "Internal server error while retrieving list recycling depot with message: " + e.getMessage(), null);
        }
    }


}
