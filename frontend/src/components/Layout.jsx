import React from 'react';

export function Brand({ onNavigate }) {
  return <a href="/" className="brand" onClick={onNavigate}><span className="brand-mark">e<span>·</span>o</span><span className="brand-name">EBER <i>&</i> OAK</span></a>;
}

export function Header({ path, navigate, user, cartCount, logout }) {
  const link = (href, label) => <a className={path === href ? 'nav-link active' : 'nav-link'} href={href} onClick={navigate}>{label}</a>;
  return <header className="site-header"><div className="header-inner"><Brand onNavigate={navigate} />
    <nav className="desktop-nav" aria-label="Main navigation">{link('/', 'Home')}{link('/restaurants', 'Restaurants')}<a className="nav-link" href="/#offers" onClick={(e)=>{navigate(e); if(path === '/') setTimeout(()=>document.getElementById('offers')?.scrollIntoView({behavior:'smooth'}),0)}}>Offers</a></nav>
    <div className="header-actions">{link('/cart', <span className="cart-nav">Cart <span className="cart-count">{cartCount}</span></span>)}
      {user ? <><a className="user-pill" href={user.role === 'ADMIN' ? '/admin' : '/orders'} onClick={navigate}><span className="avatar">{(user.name || 'E')[0].toUpperCase()}</span><span>{user.name?.split(' ')[0]}</span></a><button className="signout-link" onClick={logout}>Sign out</button></> : <a className="login-link" href="/login" onClick={navigate}>Log in <span>↗</span></a>}
      <button className="mobile-menu" aria-label="Open restaurants" onClick={()=>window.location.assign('/restaurants')}>☰</button></div>
  </div></header>;
}

export function Footer({ navigate }) {
  return <footer className="site-footer"><div className="footer-main"><div><Brand onNavigate={navigate}/><p className="footer-copy">Good food, made thoughtfully.<br/>Delivered with a little more care.</p></div><div><span className="footer-label">EXPLORE</span><a href="/restaurants" onClick={navigate}>Restaurants</a><a href="/#offers" onClick={navigate}>Offers</a><a href="/orders" onClick={navigate}>Your orders</a></div><div><span className="footer-label">SAY HELLO</span><a href="mailto:hello@eberandoak.example">hello@eberandoak.example</a><span className="footer-small">Open daily · 11am – 11pm</span></div><div className="footer-note">A neighborhood table,<br/><em>wherever you are.</em></div></div><div className="footer-bottom"><span>© 2026 Eber & Oak</span><span>Made for the love of good food&nbsp; ✳</span></div></footer>;
}

export function Toast({ message, kind = 'success', onClose }) {
  if (!message) return null;
  return <div role="status" className={`toast ${kind}`}><span>{kind === 'error' ? '!' : '✓'}</span>{message}<button aria-label="Dismiss" onClick={onClose}>×</button></div>;
}

export function Loading({ label = 'Bringing up the good things…' }) { return <div className="loading"><span className="loader"/><p>{label}</p></div>; }
export function Image({ src, alt, className = '', ...props }) { return <img src={src || 'https://images.unsplash.com/photo-1546549032-9571cd6b27df?auto=format&fit=crop&w=900&q=85'} alt={alt || ''} className={className} loading="lazy" onError={(e)=>{e.currentTarget.src='https://images.unsplash.com/photo-1546549032-9571cd6b27df?auto=format&fit=crop&w=900&q=85'}} {...props}/>; }
