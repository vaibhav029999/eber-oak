import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { api, send } from './services/api.js';
import { Header, Footer, Toast, Loading } from './components/Layout.jsx';
import HomePage from './pages/HomePage.jsx';
import RestaurantsPage from './pages/RestaurantsPage.jsx';
import DetailPage from './pages/DetailPage.jsx';
import AuthPage from './pages/AuthPage.jsx';
import CartPage from './pages/CartPage.jsx';
import CheckoutPage from './pages/CheckoutPage.jsx';
import OrdersPage from './pages/OrdersPage.jsx';
import AdminPage from './pages/AdminPage.jsx';

export default function App() {
  const [path, setPath] = useState(window.location.pathname);
  const [restaurants, setRestaurants] = useState([]); const [categories, setCategories] = useState([]);
  const [featuredFoods, setFeaturedFoods] = useState([]); const [cart, setCart] = useState(null);
  const [user, setUser] = useState(null); const [toast, setToast] = useState({ message: '', kind: 'success' });
  const [catalogLoading, setCatalogLoading] = useState(true); const [catalogError, setCatalogError] = useState('');
  const notify = useCallback((message, kind = 'success') => { setToast({ message, kind }); window.clearTimeout(window.__eberToast); window.__eberToast = window.setTimeout(()=>setToast({message:'',kind:'success'}), 3400); }, []);
  const navigate = useCallback((event) => {
    if (event) { if (event.metaKey || event.ctrlKey || event.button !== 0) return; event.preventDefault(); }
    const href = event?.currentTarget?.getAttribute('href'); if (!href) return;
    const url = new URL(href, window.location.href); if (url.origin !== window.location.origin) { window.location.href = href; return; }
    if (url.pathname === path && url.hash) { document.querySelector(url.hash)?.scrollIntoView({ behavior: 'smooth' }); return; }
    window.history.pushState({}, '', `${url.pathname}${url.search}${url.hash}`); setPath(url.pathname); window.scrollTo({ top: 0, behavior: 'smooth' });
    if(url.hash) window.setTimeout(()=>document.querySelector(url.hash)?.scrollIntoView({behavior:'smooth'}),80);
  }, [path]);
  const refreshCart = useCallback(async () => {
    if (!localStorage.getItem('eber-oak-token')) { setCart(null); return; }
    try { setCart(await api('/cart')); } catch (e) { if (e.status === 401) { localStorage.removeItem('eber-oak-token'); setUser(null); } else notify(e.message, 'error'); }
  }, [notify]);
  useEffect(() => {
    const pop = () => { setPath(window.location.pathname); window.scrollTo(0, 0); }; window.addEventListener('popstate', pop); return () => window.removeEventListener('popstate', pop);
  }, []);
  useEffect(() => {
    let alive = true;
    Promise.all([api('/restaurants'), api('/categories')]).then(([rs, cs]) => { if (alive) { setRestaurants(rs); setCategories(cs); setCatalogError(''); } }).catch(e => { if (alive) setCatalogError(e.message); }).finally(()=>{if(alive)setCatalogLoading(false)});
    const token = localStorage.getItem('eber-oak-token');
    if (token) api('/auth/me').then(value => { if (alive) setUser(value); return refreshCart(); }).catch(() => { localStorage.removeItem('eber-oak-token'); if(alive)setUser(null); });
    return () => { alive = false; };
  }, [refreshCart]);
  useEffect(() => {
    let alive = true;
    const loadFeatured = async () => {
      const results = await Promise.allSettled(restaurants.slice(0, 4).map(r => api(`/restaurants/${r.id}/menu`)));
      if (alive) setFeaturedFoods(results.flatMap(r => r.status === 'fulfilled' ? r.value.slice(0, 2) : []));
    };
    if (restaurants.length) loadFeatured(); return () => { alive = false; };
  }, [restaurants]);
  const cartCount = useMemo(()=>cart?.items?.reduce((sum,item)=>sum+item.quantity,0) || 0,[cart]);
  const addToCart = async (foodItemId, quantity = 1) => {
    if (!user) { window.history.pushState({}, '', '/login'); setPath('/login'); window.scrollTo(0,0); notify('Sign in to start your order.', 'error'); return; }
    try { setCart(await api('/cart/items', send('POST', { foodItemId, quantity }))); notify('Added to your bag.'); }
    catch(e) { notify(e.message, 'error'); }
  };
  const logout = async () => { try { await api('/auth/logout', send('POST', {})); } catch {} localStorage.removeItem('eber-oak-token'); setUser(null); setCart(null); notify('You’ve signed out.'); window.history.pushState({}, '', '/'); setPath('/'); };
  const onAuth = async (authData) => { localStorage.setItem('eber-oak-token', authData.token); setUser(authData.user); await refreshCart(); notify(`Welcome${authData.user.name ? `, ${authData.user.name.split(' ')[0]}` : ''}!`); window.history.pushState({}, '', '/'); setPath('/'); };
  const onCartChange = (next) => setCart(next);
  const match = path.match(/^\/restaurants\/(\d+)$/);
  let page;
  if (catalogLoading && (path === '/' || path === '/restaurants')) page = <Loading/>;
  else if (match) page = <DetailPage id={match[1]} restaurants={restaurants} categories={categories} addToCart={addToCart} notify={notify}/>;
  else if (path === '/restaurants') page = <RestaurantsPage restaurants={restaurants} categories={categories} initialError={catalogError} navigate={navigate}/>;
  else if (path === '/login') page = <AuthPage onAuth={onAuth} navigate={navigate} notify={notify}/>;
  else if (path === '/cart') page = <CartPage cart={cart} onCartChange={onCartChange} navigate={navigate} user={user} notify={notify}/>;
  else if (path === '/checkout') page = <CheckoutPage cart={cart} user={user} navigate={navigate} notify={notify} onOrderPlaced={()=>setCart(null)}/>;
  else if (path === '/orders') page = <OrdersPage user={user} navigate={navigate}/>;
  else if (path === '/admin') page = <AdminPage user={user} notify={notify} navigate={navigate}/>;
  else page = <HomePage restaurants={restaurants} categories={categories} foods={featuredFoods} error={catalogError} navigate={navigate} addToCart={addToCart}/>;
  return <><Header path={path} navigate={navigate} user={user} cartCount={cartCount} logout={logout}/><main>{page}</main><Footer navigate={navigate}/><Toast message={toast.message} kind={toast.kind} onClose={()=>setToast({message:'',kind:'success'})}/></>;
}
