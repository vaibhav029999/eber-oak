import React, { useEffect, useState } from 'react';
import { api, money } from '../services/api.js';
import { Image } from '../components/Layout.jsx';

function RestaurantCard({ restaurant, navigate }) {
  return <a className="restaurant-card" href={`/restaurants/${restaurant.id}`} onClick={navigate}>
    <div className="restaurant-image-wrap"><Image src={restaurant.imageUrl} alt={restaurant.name} className="restaurant-image"/>{restaurant.offer && <span className="offer-tag">{restaurant.offer}</span>}<span className="delivery-badge">{restaurant.deliveryMinutes} min</span></div>
    <div className="restaurant-card-info"><div><h3>{restaurant.name}</h3><p>{restaurant.cuisine}</p></div><span className="rating">★ {Number(restaurant.rating).toFixed(1)}</span></div>
    <div className="restaurant-meta"><span><i className="meta-dot"/>{restaurant.priceLevel || '₹₹'} for two</span><span>·</span><span>{money(restaurant.deliveryFee)} delivery</span></div>
  </a>;
}

function FoodCard({ food, addToCart }) {
  return <article className="food-card"><div className="food-image-box"><Image src={food.imageUrl} alt={food.name} className="food-image"/><span className={food.vegetarian ? 'diet-icon veg' : 'diet-icon nonveg'}><i/></span></div><div className="food-card-body"><div className="food-card-title"><h3>{food.name}</h3><strong>{money(food.price)}</strong></div><p>{food.description}</p><button className="add-button" onClick={()=>addToCart(food.id)}>Add <span>+</span></button></div></article>;
}

export default function HomePage({ restaurants, categories, foods, error, navigate, addToCart }) {
  const [offers, setOffers] = useState([]); const [query, setQuery] = useState('');
  useEffect(()=>{api('/coupons').then(setOffers).catch(()=>setOffers([]))},[]);
  const search = (e) => { e.preventDefault(); const term = query.trim(); const target = `/restaurants${term ? `?search=${encodeURIComponent(term)}` : ''}`; window.history.pushState({}, '', target); window.dispatchEvent(new PopStateEvent('popstate')); window.scrollTo(0,0); };
  return <>
    <section className="hero"><div className="hero-photo"/><div className="hero-content"><div className="eyebrow"><span/> THE NEIGHBORHOOD TABLE</div><h1>Good food.<br/><em>Good mood.</em><br/>Right on time.</h1><p>Find a little something lovely, made by the kitchens around you.</p><form className="search-bar" onSubmit={search}><span className="search-icon">⌕</span><input value={query} onChange={e=>setQuery(e.target.value)} placeholder="Restaurant, dish, or craving" aria-label="Search restaurants or food"/><button aria-label="Search">Find food <span>↗</span></button></form><div className="hero-proof"><span>✳&nbsp; Made with care</span><i/><span>Delivered to your door</span></div></div><div className="hero-stamp"><span>GOOD<br/>FOOD</span><i>✳</i><span>GOOD<br/>NEIGHBORS</span></div><div className="hero-caption">A little joy is on its way.</div></section>
    <section className="home-section category-section"><div className="section-heading"><div><div className="eyebrow small">FOLLOW YOUR APPETITE</div><h2>What sounds <em>good?</em></h2></div><a className="text-link" href="/restaurants" onClick={navigate}>See all restaurants <span>↗</span></a></div><div className="category-row">{categories.slice(0, 6).map((category,i)=><a className="category-card" href={`/restaurants?category=${encodeURIComponent(category.name)}`} key={category.id} onClick={navigate}><div className={`category-visual category-${i%6}`}><Image src={category.imageUrl} alt=""/></div><span>{category.name}</span></a>)}</div></section>
    <section className="offer-band" id="offers"><div className="offer-art">✳</div><div className="offer-band-copy"><div className="eyebrow light">A LITTLE EXTRA FOR YOU</div><h2>Good things<br/>come <em>to the table.</em></h2><p>Freshly picked offers from the places you love.</p></div><div className="coupon-cards">{offers.slice(0,2).map(o=><div className="coupon-card" key={o.code}><span className="coupon-top">A TREAT ON US</span><strong>{o.code}</strong><span>{o.discountPercent}% off {o.minimumAmount > 0 ? `orders above ${money(o.minimumAmount)}` : 'your order'}</span></div>)}{!offers.length&&<div className="offer-note">A delicious offer is always around the corner.</div>}</div></section>
    <section className="home-section restaurant-section"><div className="section-heading"><div><div className="eyebrow small">GOOD PEOPLE, GREAT FOOD</div><h2>Neighborhood <em>favorites</em></h2></div><a className="text-link" href="/restaurants" onClick={navigate}>Explore all <span>↗</span></a></div>{error && <div className="inline-alert">We couldn’t reach the menu service. Start the Spring Boot backend and refresh to load restaurant data.</div>}<div className="restaurant-grid">{restaurants.slice(0,3).map(r=><RestaurantCard restaurant={r} navigate={navigate} key={r.id}/>)}</div></section>
    <section className="home-section food-section"><div className="section-heading"><div><div className="eyebrow small">SOMETHING TO SAVOR</div><h2>Little bites, <em>big love</em></h2></div><span className="section-aside">A few favorites, picked just for you</span></div><div className="food-grid">{foods.slice(0,4).map(f=><FoodCard key={f.id} food={f} addToCart={addToCart}/>)}</div></section>
    <section className="delivery-note"><div className="delivery-spark">✳</div><div><div className="eyebrow small">FROM THEIR KITCHEN TO YOURS</div><h2>A good meal makes <em>the day.</em></h2></div><p>We bring your neighborhood’s best cooking straight to your door, with all the small details that make it feel special.</p><a className="button button-dark" href="/restaurants" onClick={navigate}>Find your next favorite <span>↗</span></a></section>
  </>;
}
