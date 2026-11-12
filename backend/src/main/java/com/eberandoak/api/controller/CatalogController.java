package com.eberandoak.api.controller;

import com.eberandoak.api.dto.*;
import com.eberandoak.api.service.CatalogService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api")
public class CatalogController {
    private final CatalogService catalog;
    public CatalogController(CatalogService catalog){this.catalog=catalog;}
    @GetMapping("/restaurants") public List<RestaurantView> restaurants(@RequestParam(required=false) String search,@RequestParam(required=false) String category,@RequestParam(required=false) Double rating,@RequestParam(required=false) String sort){return catalog.restaurants(search,category,rating,sort);}
    @GetMapping("/restaurants/{id}") public RestaurantView restaurant(@PathVariable Long id){return catalog.restaurant(id);}
    @GetMapping("/restaurants/{id}/menu") public List<FoodView> menu(@PathVariable Long id){return catalog.menu(id);}
    @GetMapping("/categories") public List<CategoryView> categories(){return catalog.categories();}
}
