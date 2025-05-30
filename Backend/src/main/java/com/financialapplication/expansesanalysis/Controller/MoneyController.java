package com.financialapplication.expansesanalysis.Controller;


import com.financialapplication.expansesanalysis.Exception.NotFoundException;
import com.financialapplication.expansesanalysis.Model.Response.CommonResponse;
import com.financialapplication.expansesanalysis.Service.MoneyService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/amount")
public class MoneyController {

    private final MoneyService  moneyService;

    public MoneyController(MoneyService moneyService){
        this.moneyService =moneyService;
    }



    @PostMapping("/income")
    public ResponseEntity<CommonResponse> createExpanseLimit(@AuthenticationPrincipal UserDetails userDetails , @RequestParam @NotNull(message = "Saving Is Not Present In Valid Form") double saving,
                                                     @RequestParam @NotNull(message = "Income is not in Valid Form") double income) throws NotFoundException {
        return moneyService.saveIncome(userDetails.getUsername(),saving,income);
    }


}
