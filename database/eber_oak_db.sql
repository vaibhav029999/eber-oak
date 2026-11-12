-- EBER & OAK local MySQL database and sample menu
CREATE DATABASE IF NOT EXISTS eber_oak_db CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE eber_oak_db;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(190) NOT NULL,
  password_hash VARCHAR(100) NOT NULL,
  phone VARCHAR(30),
  role VARCHAR(15) NOT NULL DEFAULT 'USER',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id), UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS restaurants (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  cuisine VARCHAR(100) NOT NULL,
  description VARCHAR(500),
  image_url VARCHAR(1000),
  rating DECIMAL(2,1) DEFAULT 4.5,
  delivery_minutes INT NOT NULL DEFAULT 35,
  delivery_fee DECIMAL(10,2) NOT NULL DEFAULT 0,
  minimum_order DECIMAL(10,2) NOT NULL DEFAULT 0,
  price_level VARCHAR(10) DEFAULT '₹₹',
  offer VARCHAR(100),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  featured BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (id), KEY idx_restaurants_active_rating (active, rating)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS categories (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(70) NOT NULL,
  slug VARCHAR(90) NOT NULL,
  image_url VARCHAR(1000),
  PRIMARY KEY (id), UNIQUE KEY uk_categories_name (name), UNIQUE KEY uk_categories_slug (slug)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS food_items (
  id BIGINT NOT NULL AUTO_INCREMENT,
  restaurant_id BIGINT NOT NULL,
  category_id BIGINT NOT NULL,
  name VARCHAR(120) NOT NULL,
  description VARCHAR(500),
  image_url VARCHAR(1000),
  price DECIMAL(10,2) NOT NULL,
  vegetarian BOOLEAN NOT NULL DEFAULT TRUE,
  spicy BOOLEAN NOT NULL DEFAULT FALSE,
  available BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (id), KEY idx_food_restaurant_available (restaurant_id, available),
  CONSTRAINT fk_food_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id),
  CONSTRAINT fk_food_category FOREIGN KEY (category_id) REFERENCES categories(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS carts (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id), UNIQUE KEY uk_carts_user (user_id),
  CONSTRAINT fk_carts_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS cart_items (
  id BIGINT NOT NULL AUTO_INCREMENT,
  cart_id BIGINT NOT NULL,
  food_item_id BIGINT NOT NULL,
  quantity INT NOT NULL DEFAULT 1,
  PRIMARY KEY (id), UNIQUE KEY uk_cart_food (cart_id, food_item_id),
  CONSTRAINT fk_cart_items_cart FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
  CONSTRAINT fk_cart_items_food FOREIGN KEY (food_item_id) REFERENCES food_items(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS addresses (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  customer_name VARCHAR(100) NOT NULL,
  phone VARCHAR(30) NOT NULL,
  address_line VARCHAR(255) NOT NULL,
  city VARCHAR(100) NOT NULL,
  pincode VARCHAR(15) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id), KEY idx_addresses_user (user_id),
  CONSTRAINT fk_addresses_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS orders (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  restaurant_id BIGINT NOT NULL,
  address_id BIGINT NOT NULL,
  customer_name VARCHAR(100) NOT NULL,
  phone VARCHAR(30) NOT NULL,
  delivery_address VARCHAR(420) NOT NULL,
  subtotal DECIMAL(10,2) NOT NULL,
  delivery_fee DECIMAL(10,2) NOT NULL,
  discount_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
  total_amount DECIMAL(10,2) NOT NULL,
  payment_method VARCHAR(30) NOT NULL,
  status VARCHAR(30) NOT NULL DEFAULT 'PLACED',
  coupon_code VARCHAR(40),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id), KEY idx_orders_user_created (user_id, created_at), KEY idx_orders_status_created (status, created_at),
  CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_orders_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id),
  CONSTRAINT fk_orders_address FOREIGN KEY (address_id) REFERENCES addresses(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS order_items (
  id BIGINT NOT NULL AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  food_item_id BIGINT NULL,
  food_name VARCHAR(120) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  quantity INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
  CONSTRAINT fk_order_items_food FOREIGN KEY (food_item_id) REFERENCES food_items(id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS coupons (
  id BIGINT NOT NULL AUTO_INCREMENT,
  code VARCHAR(40) NOT NULL,
  discount_percent INT NOT NULL,
  minimum_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (id), UNIQUE KEY uk_coupons_code (code)
) ENGINE=InnoDB;

INSERT INTO restaurants (id,name,cuisine,description,image_url,rating,delivery_minutes,delivery_fee,minimum_order,price_level,offer,active,featured) VALUES
(1,'House of Thali','Indian','Slow-cooked comfort, bright pickles and the kind of rice you remember.','https://images.unsplash.com/photo-1585937421612-70a008356fbe?auto=format&fit=crop&w=1200&q=85',4.8,28,25,149,'₹₹','Free dessert above ₹499',TRUE,TRUE),
(2,'Olive & Ember','Italian','Wood-fired favorites, handmade pasta and long Sunday lunch energy.','https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=1200&q=85',4.7,35,35,199,'₹₹₹','15% off this week',TRUE,TRUE),
(3,'Common Ground','Cafe & Brunch','Good coffee, sunny sandwiches and a little something for every hour.','https://images.unsplash.com/photo-1554118811-1e0d58224f24?auto=format&fit=crop&w=1200&q=85',4.6,24,20,129,'₹₹','Free coffee with brunch',TRUE,TRUE),
(4,'Grain & Greens','Healthy','Colorful bowls, market greens and ingredients that do the talking.','https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=1200&q=85',4.9,22,20,149,'₹₹','A fresh start: 10% off',TRUE,TRUE),
(5,'Little Sizzle','Burgers','Smash-griddled, saucy and properly stacked. Napkins very much included.','https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=1200&q=85',4.5,32,30,179,'₹₹','Combo for two from ₹399',TRUE,FALSE),
(6,'Honeycomb Bakehouse','Sweet things','Small-batch bakes, warm pastries and a little something for the road.','https://images.unsplash.com/photo-1509440159596-0249088772ff?auto=format&fit=crop&w=1200&q=85',4.7,26,20,99,'₹','Fresh from the oven',TRUE,FALSE)
ON DUPLICATE KEY UPDATE name=VALUES(name),cuisine=VALUES(cuisine),description=VALUES(description),image_url=VALUES(image_url),rating=VALUES(rating),delivery_minutes=VALUES(delivery_minutes),delivery_fee=VALUES(delivery_fee),minimum_order=VALUES(minimum_order),price_level=VALUES(price_level),offer=VALUES(offer),active=VALUES(active),featured=VALUES(featured);

INSERT INTO categories (id,name,slug,image_url) VALUES
(1,'Indian','indian','https://images.unsplash.com/photo-1547592180-85f173990554?auto=format&fit=crop&w=600&q=80'),
(2,'Italian','italian','https://images.unsplash.com/photo-1473093295043-cdd812d0e601?auto=format&fit=crop&w=600&q=80'),
(3,'Cafe & Brunch','cafe-brunch','https://images.unsplash.com/photo-1482049016688-2d3e1b311543?auto=format&fit=crop&w=600&q=80'),
(4,'Healthy','healthy','https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=600&q=80'),
(5,'Burgers','burgers','https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=600&q=80'),
(6,'Sweet things','sweet-things','https://images.unsplash.com/photo-1486427944299-d1955d23e34d?auto=format&fit=crop&w=600&q=80')
ON DUPLICATE KEY UPDATE name=VALUES(name),slug=VALUES(slug),image_url=VALUES(image_url);

INSERT INTO food_items (id,restaurant_id,category_id,name,description,image_url,price,vegetarian,spicy,available) VALUES
(1,1,1,'Slow Sunday Thali','Dal makhani, seasonal veg, jeera rice, roti and a little pickle.','https://images.unsplash.com/photo-1547592180-85f173990554?auto=format&fit=crop&w=900&q=85',349,TRUE,FALSE,TRUE),
(2,1,1,'Paneer Tikka Bowl','Smoky paneer, herbed rice, mint chutney and crunchy onions.','https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?auto=format&fit=crop&w=900&q=85',269,TRUE,TRUE,TRUE),
(3,1,1,'Butter Chicken & Rice','Tender chicken in a velvety tomato sauce, served with fragrant rice.','https://images.unsplash.com/photo-1603894584373-5ac82b2ae398?auto=format&fit=crop&w=900&q=85',329,FALSE,FALSE,TRUE),
(4,2,2,'Burrata Margherita','Wood-fired dough, sweet tomato, basil and a generous cloud of burrata.','https://images.unsplash.com/photo-1574071318508-1cdbab80d002?auto=format&fit=crop&w=900&q=85',429,TRUE,FALSE,TRUE),
(5,2,2,'Silky Lemon Tagliatelle','Fresh ribbons, lemon butter, parmesan and just enough black pepper.','https://images.unsplash.com/photo-1473093295043-cdd812d0e601?auto=format&fit=crop&w=900&q=85',379,TRUE,FALSE,TRUE),
(6,2,2,'Sunday Ragu Pappardelle','Slow-braised tomato ragu, wide pasta and a little parmesan snow.','https://images.unsplash.com/photo-1551183053-bf91a1d81141?auto=format&fit=crop&w=900&q=85',449,FALSE,FALSE,TRUE),
(7,3,3,'Avocado & Egg Toast','Sourdough, smashed avocado, chilli oil and an easy-going egg.','https://images.unsplash.com/photo-1525351484163-7529414344d8?auto=format&fit=crop&w=900&q=85',259,TRUE,TRUE,TRUE),
(8,3,3,'Brown Butter Pancakes','Three soft pancakes, warm berries and maple butter.','https://images.unsplash.com/photo-1528207776546-365bb710ee93?auto=format&fit=crop&w=900&q=85',289,TRUE,FALSE,TRUE),
(9,3,3,'Roasted Tomato Sandwich','Slow-roasted tomatoes, whipped feta and garden herbs on toasted bread.','https://images.unsplash.com/photo-1528735602780-2552fd46c7af?auto=format&fit=crop&w=900&q=85',239,TRUE,FALSE,TRUE),
(10,4,4,'Green Goddess Bowl','Market greens, avocado, quinoa, toasted seeds and tangy herb dressing.','https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=900&q=85',299,TRUE,FALSE,TRUE),
(11,4,4,'Miso Sweet Potato Bowl','Crisp-edged sweet potato, brown rice, greens and miso tahini.','https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=900&q=85',279,TRUE,FALSE,TRUE),
(12,5,5,'Oakhouse Smash Burger','Two griddled patties, sharp cheddar, house sauce and a soft potato bun.','https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=900&q=85',329,FALSE,FALSE,TRUE),
(13,5,5,'Crispy Chickpea Burger','Spiced chickpea patty, crunchy greens and lemony yogurt.','https://images.unsplash.com/photo-1520072959219-c595dc870360?auto=format&fit=crop&w=900&q=85',269,TRUE,TRUE,TRUE),
(14,6,6,'Honey & Sea Salt Cookie','Golden-edged, soft in the middle and baked in small batches.','https://images.unsplash.com/photo-1499636136210-6f4ee915583e?auto=format&fit=crop&w=900&q=85',99,TRUE,FALSE,TRUE),
(15,6,6,'Brown Butter Cinnamon Roll','Soft dough, warm spice and a glossy brown-butter glaze.','https://images.unsplash.com/photo-1509365465985-25d11c17e812?auto=format&fit=crop&w=900&q=85',149,TRUE,FALSE,TRUE)
ON DUPLICATE KEY UPDATE restaurant_id=VALUES(restaurant_id),category_id=VALUES(category_id),name=VALUES(name),description=VALUES(description),image_url=VALUES(image_url),price=VALUES(price),vegetarian=VALUES(vegetarian),spicy=VALUES(spicy),available=VALUES(available);

INSERT INTO coupons (id,code,discount_percent,minimum_amount,active) VALUES
(1,'OAKFIRST',15,0,TRUE),(2,'GOODTABLE',10,499,TRUE),(3,'LUNCHLOVE',12,299,TRUE)
ON DUPLICATE KEY UPDATE discount_percent=VALUES(discount_percent),minimum_amount=VALUES(minimum_amount),active=VALUES(active);
