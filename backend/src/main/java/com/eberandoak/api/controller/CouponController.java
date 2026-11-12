package com.eberandoak.api.controller;

import com.eberandoak.api.dto.*;
import com.eberandoak.api.service.CouponService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/coupons")
public class CouponController {
    private final CouponService coupons;
    public CouponController(CouponService coupons){this.coupons=coupons;}
    @GetMapping public List<CouponView> active(){return coupons.publicCoupons();}
    @PostMapping("/validate") public CouponResult validate(@Valid @RequestBody CouponValidationRequest body){return coupons.validate(body);}
}
