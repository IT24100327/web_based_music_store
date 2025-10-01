CREATE TABLE orders (
                        order_id INT PRIMARY KEY AUTO_INCREMENT,
                        user_id INT NOT NULL,
                        total_amount DECIMAL(10,2) NOT NULL,
                        status VARCHAR(20) DEFAULT 'PENDING',
                        order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        payment_method VARCHAR(50),
                        transaction_id VARCHAR(100),
                        FOREIGN KEY (user_id) REFERENCES users(user_id)
);
