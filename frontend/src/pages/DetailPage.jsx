import React, { useEffect, useMemo, useState } from 'react';
import { api, money } from '../services/api.js';
import { Image, Loading } from '../components/Layout.jsx';

export default function DetailPage({ id, restaurants, categories, addToCart, notify }) {
  const [restaurant,setRestaurant]=useState(restaurants.find(r=>String(r.id)===String(id))||null); const [menu,setMenu]=useState([]);const [loading,setLoading]=useState(true);const [error,setError]=useState('');const [selected,setSelected]=useState('All');
  useEffect(()=>{let alive=true;Promise.all([api(`/restaurants/${id}`),api(`/restaurants/${id}/menu`)]).then(([r,m])=>{if(alive){setRestaurant(r);setMenu(m)}}).catch(e=>{if(alive)setError(e.message)}).finally(()=>{if(alive)setLoading(false)});return()=>{alive=false}},[id]);
  const groups=useMemo(()=>[...new Set(menu.map(f=>f.categoryName).filter(Boolean))],[menu]); const shown=menu.filter(f=>selected==='All'||f.categoryName===selected);
  if(loading)return <Loading label="Setting the table…"/>;
  if(error||!restaurant)return <div className="page-wrap empty-state"><span>✳</span><h2>We couldn’t find that table</h2><p>{error||'This restaurant may have moved on.'}</p><a className="button button-dark" href="/restaurants">Back to restaurants</a></div>;
  return <div className="detail-page"><div className="detail-cover"><Image src={restaurant.imageUrl} alt={restaurant.name}/><div className="detail-cover-shade"/><div className="detail-cover-content"><div className="page-kicker light">COOKED WITH CARE</div><h1>{restaurant.name}</h1><p>{restaurant.description || restaurant.cuisine}</p><div className="detail-pills"><span>★ {Number(restaurant.rating).toFixed(1)}</span><span>{restaurant.deliveryMinutes} min</span><span>{restaurant.priceLevel} for two</span><span>{money(restaurant.deliveryFee)} delivery</span></div></div></div>
    <div className="page-wrap menu-wrap"><div className="menu-intro"><div><div className="eyebrow small">A MENU TO MAKE YOUR DAY</div><h2>From our kitchen <em>to yours.</em></h2></div><span>{menu.length} things made with love</span></div><div className="menu-tabs"><button className={selected==='All'?'selected':''} onClick={()=>setSelected('All')}>Everything</button>{groups.map(g=><button key={g} className={selected===g?'selected':''} onClick={()=>setSelected(g)}>{g}</button>)}</div>
    {shown.length?<div className="menu-grid">{shown.map(food=><article className="menu-item" key={food.id}><div className="menu-item-copy"><span className={food.vegetarian?'diet-icon veg':'diet-icon nonveg'}><i/></span><h3>{food.name}</h3><strong>{money(food.price)}</strong><p>{food.description}</p>{food.spicy&&<small>✳ A little heat</small>}</div><div className="menu-item-image"><Image src={food.imageUrl} alt={food.name}/><button aria-label={`Add ${food.name} to cart`} onClick={()=>addToCart(food.id)}>+</button></div></article>)}</div>:<div className="empty-state compact"><p>This menu is being refreshed. Check back soon.</p></div>}</div>
  </div>;
}
