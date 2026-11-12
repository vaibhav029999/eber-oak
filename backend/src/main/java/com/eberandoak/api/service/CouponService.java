package com.eberandoak.api.service;

import com.eberandoak.api.dto.*;
import com.eberandoak.api.entity.Coupon;
import com.eberandoak.api.exception.ApiException;
import com.eberandoak.api.repository.CouponRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CouponService {
    private final CouponRepository coupons;
    public CouponService(CouponRepository coupons){this.coupons=coupons;}
    @Transactional(readOnly=true) public List<CouponView> publicCoupons(){return coupons.findAllByActiveTrueOrderByCodeAsc().stream().map(ViewMapper::coupon).toList();}
    @Transactional(readOnly=true) public List<CouponView> allCoupons(){return coupons.findAllByOrderByCodeAsc().stream().map(ViewMapper::coupon).toList();}
    @Transactional(readOnly=true) public CouponResult validate(CouponValidationRequest request){
        Coupon c=coupons.findByCodeIgnoreCaseAndActiveTrue(request.code().trim()).orElseThrow(()->ApiException.badRequest("That offer code isn’t active. Check the code and try again."));
        if(request.subtotal().compareTo(c.getMinimumAmount())<0)throw ApiException.badRequest("Add a little more to your bag to use that offer.");
        BigDecimal discount=request.subtotal().multiply(BigDecimal.valueOf(c.getDiscountPercent())).divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP);
        return new CouponResult(true,c.getCode(),c.getDiscountPercent(),discount,"Lovely choice — "+c.getDiscountPercent()+"% off your order.");
    }
    @Transactional public CouponView create(CouponRequest request){String code=request.code().trim().toUpperCase();if(coupons.findByCodeIgnoreCase(code).isPresent())throw ApiException.conflict("That offer code already exists.");Coupon c=new Coupon();apply(c,request,code);return ViewMapper.coupon(coupons.save(c));}
    @Transactional public CouponView update(Long id,CouponRequest request){Coupon c=coupons.findById(id).orElseThrow(()->ApiException.notFound("Offer not found."));String code=request.code().trim().toUpperCase();coupons.findByCodeIgnoreCase(code).filter(existing->!existing.getId().equals(id)).ifPresent(existing->{throw ApiException.conflict("That offer code already exists.");});apply(c,request,code);return ViewMapper.coupon(coupons.save(c));}
    @Transactional public void delete(Long id){if(!coupons.existsById(id))throw ApiException.notFound("Offer not found.");coupons.deleteById(id);}
    private void apply(Coupon c,CouponRequest r,String code){c.setCode(code);c.setDiscountPercent(r.discountPercent());c.setMinimumAmount(r.minimumAmount());c.setActive(r.active());}
}
