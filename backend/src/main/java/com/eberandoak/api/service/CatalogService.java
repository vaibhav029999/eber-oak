package com.eberandoak.api.service;

import com.eberandoak.api.dto.*;
import com.eberandoak.api.entity.*;
import com.eberandoak.api.exception.ApiException;
import com.eberandoak.api.repository.*;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CatalogService {
    private final RestaurantRepository restaurants; private final CategoryRepository categories; private final FoodItemRepository foods;
    public CatalogService(RestaurantRepository restaurants,CategoryRepository categories,FoodItemRepository foods){this.restaurants=restaurants;this.categories=categories;this.foods=foods;}
    @Transactional(readOnly=true) public List<RestaurantView> restaurants(String search,String category,Double minimumRating,String sort){
        var result=restaurants.findAllByActiveTrueOrderByFeaturedDescRatingDesc().stream().filter(r->search==null||search.isBlank()||(r.getName()+" "+r.getCuisine()).toLowerCase(Locale.ROOT).contains(search.trim().toLowerCase(Locale.ROOT)))
            .filter(r->category==null||category.isBlank()||r.getCuisine().toLowerCase(Locale.ROOT).contains(category.trim().toLowerCase(Locale.ROOT)))
            .filter(r->minimumRating==null||r.getRating().doubleValue()>=minimumRating).toList();
        Comparator<Restaurant> comparator=switch(sort==null?"":sort){case "price"->Comparator.comparingInt((Restaurant r)->r.getPriceLevel()==null?0:r.getPriceLevel().length()).thenComparing(Restaurant::getMinimumOrder);case "delivery"->Comparator.comparing(Restaurant::getDeliveryMinutes);case "rating"->Comparator.comparing(Restaurant::getRating).reversed();default->null;};
        return (comparator==null?result:result.stream().sorted(comparator).toList()).stream().map(ViewMapper::restaurant).toList();
    }
    @Transactional(readOnly=true) public RestaurantView restaurant(Long id){return restaurants.findById(id).filter(Restaurant::isActive).map(ViewMapper::restaurant).orElseThrow(()->ApiException.notFound("We couldn’t find that restaurant."));}
    @Transactional(readOnly=true) public List<CategoryView> categories(){return categories.findAllByOrderByNameAsc().stream().map(ViewMapper::category).toList();}
    @Transactional(readOnly=true) public List<FoodView> menu(Long id){if(!restaurants.findById(id).filter(Restaurant::isActive).isPresent())throw ApiException.notFound("We couldn’t find that restaurant.");return foods.findByRestaurant_IdAndAvailableTrueOrderByCategory_NameAscNameAsc(id).stream().map(ViewMapper::food).toList();}
    @Transactional(readOnly=true) public List<RestaurantView> adminRestaurants(){return restaurants.findAllByOrderByNameAsc().stream().map(ViewMapper::restaurant).toList();}
    @Transactional public RestaurantView createRestaurant(RestaurantRequest body){Restaurant r=new Restaurant();apply(r,body);r.setActive(true);return ViewMapper.restaurant(restaurants.save(r));}
    @Transactional public RestaurantView updateRestaurant(Long id,RestaurantRequest body){Restaurant r=restaurants.findById(id).orElseThrow(()->ApiException.notFound("Restaurant not found."));boolean restore=!r.isActive()&&body.active();apply(r,body);if(restore)foods.findByRestaurant_IdOrderByNameAsc(id).forEach(f->f.setAvailable(true));return ViewMapper.restaurant(restaurants.save(r));}
    @Transactional public void archiveRestaurant(Long id){Restaurant r=restaurants.findById(id).orElseThrow(()->ApiException.notFound("Restaurant not found."));r.setActive(false);restaurants.save(r);foods.findByRestaurant_IdOrderByNameAsc(id).forEach(f->f.setAvailable(false));}
    @Transactional(readOnly=true) public List<FoodView> adminFoods(Long restaurantId){if(restaurantId!=null&&!restaurants.existsById(restaurantId))throw ApiException.notFound("Restaurant not found.");return (restaurantId==null?foods.findAll():foods.findByRestaurant_IdOrderByNameAsc(restaurantId)).stream().map(ViewMapper::food).toList();}
    @Transactional public FoodView createFood(FoodRequest body){FoodItem f=new FoodItem();apply(f,body);return ViewMapper.food(foods.save(f));}
    @Transactional public FoodView updateFood(Long id,FoodRequest body){FoodItem f=foods.findById(id).orElseThrow(()->ApiException.notFound("Menu item not found."));apply(f,body);return ViewMapper.food(foods.save(f));}
    @Transactional public void archiveFood(Long id){FoodItem f=foods.findById(id).orElseThrow(()->ApiException.notFound("Menu item not found."));f.setAvailable(false);foods.save(f);}
    private void apply(Restaurant r,RestaurantRequest b){r.setName(b.name().trim());r.setCuisine(b.cuisine().trim());r.setDescription(b.description());r.setImageUrl(b.imageUrl());r.setRating(b.rating());r.setDeliveryMinutes(b.deliveryMinutes());r.setDeliveryFee(b.deliveryFee());r.setMinimumOrder(b.minimumOrder());r.setPriceLevel(b.priceLevel()==null?"₹₹":b.priceLevel());r.setOffer(b.offer());r.setFeatured(b.featured());r.setActive(b.active());}
    private void apply(FoodItem f,FoodRequest b){Restaurant r=restaurants.findById(b.restaurantId()).orElseThrow(()->ApiException.notFound("Restaurant not found."));Category c=categories.findById(b.categoryId()).orElseThrow(()->ApiException.notFound("Category not found."));f.setRestaurant(r);f.setCategory(c);f.setName(b.name().trim());f.setDescription(b.description());f.setImageUrl(b.imageUrl());f.setPrice(b.price());f.setVegetarian(b.vegetarian());f.setSpicy(b.spicy());f.setAvailable(b.available());}
}
