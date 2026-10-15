-- ============================================================
--  CUSTOMERS & ORDERS  (Questions 16 - 30)
--  Database : customer_orders_db

DROP DATABASE IF EXISTS customer_orders_db;
CREATE DATABASE customer_orders_db;
USE customer_orders_db;

CREATE TABLE Customers (
    customer_id   INT PRIMARY KEY,
    customer_name VARCHAR(50),
    city          VARCHAR(30)
);

CREATE TABLE Orders (
    order_id    INT PRIMARY KEY,
    customer_id INT,
    amount      DECIMAL(10,2),
    order_date  DATE,
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id)
);

INSERT INTO Customers VALUES
(1,'Anu','Chennai'),
(2,'Bala','Coimbatore'),
(3,'Chitra','Madurai'),
(4,'Dinesh','Salem'),
(5,'Esha','Trichy'),
(6,'Farhan','Chennai'),
(7,'Gita','Erode');

INSERT INTO Orders VALUES
(1001,1,3000,'2026-01-10'),
(1002,1,2500,'2026-02-14'),
(1003,1,4000,'2026-03-05'),
(1004,1,1500,'2026-04-20'),
(1005,2,2000,'2026-01-22'),
(1006,2,1800,'2026-03-18'),
(1007,2,3500,'2026-05-09'),
(1008,3,6000,'2026-02-02'),
(1009,3,1200,'2026-06-15'),
(1010,4,800,'2026-04-11'),
(1011,5,1000,'2026-01-05'),
(1012,5,900,'2026-02-27'),
(1013,5,1500,'2026-03-30'),
(1014,5,700,'2026-05-21'),
(1015,5,2200,'2026-07-08'),
(1016,6,4500,'2026-03-12'),
(1017,6,3800,'2026-06-02'),
(1018,6,1200,'2026-08-19');


-- ============================================================
--  CUSTOMERS & ORDERS TABLES (Q16 - Q30)
-- ============================================================

-- ------------------------------------------------------------
-- Customers-Orders Q16. Find the total order amount for each customer.
-- ------------------------------------------------------------
SELECT c.customer_name, SUM(o.amount) AS total_amount
FROM Customers c
INNER JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;

-- ------------------------------------------------------------
-- Customers-Orders Q17. Find customers who have placed more than 3 orders.
-- ------------------------------------------------------------
SELECT c.customer_name, COUNT(o.order_id) AS total_orders
FROM Customers c
INNER JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING COUNT(o.order_id) > 3;

-- ------------------------------------------------------------
-- Customers-Orders Q18. Find the average order amount for each customer.
-- ------------------------------------------------------------
SELECT c.customer_name, ROUND(AVG(o.amount), 2) AS average_amount
FROM Customers c
INNER JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;

-- ------------------------------------------------------------
-- Customers-Orders Q19. Find the highest order amount placed by each customer.
-- ------------------------------------------------------------
SELECT c.customer_name, MAX(o.amount) AS highest_order
FROM Customers c
INNER JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;

-- ------------------------------------------------------------
-- Customers-Orders Q20. Display customers sorted by their total purchase amount.
-- ------------------------------------------------------------
SELECT c.customer_name, SUM(o.amount) AS total_purchase
FROM Customers c
INNER JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
ORDER BY total_purchase DESC;

-- ------------------------------------------------------------
-- Customers-Orders Q21. Find customers whose total purchase amount exceeds 10,000.
-- ------------------------------------------------------------
SELECT c.customer_name, SUM(o.amount) AS total_purchase
FROM Customers c
INNER JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING SUM(o.amount) > 10000;

-- ------------------------------------------------------------
-- Customers-Orders Q22. Display customer names along with the total number of orders placed.
-- ------------------------------------------------------------
SELECT c.customer_name, COUNT(o.order_id) AS total_orders
FROM Customers c
LEFT JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;

-- ------------------------------------------------------------
-- Customers-Orders Q23. Find the customer who spent the highest amount.
-- ------------------------------------------------------------
SELECT c.customer_name, SUM(o.amount) AS total_purchase
FROM Customers c
INNER JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
ORDER BY total_purchase DESC
LIMIT 1;

-- ------------------------------------------------------------
-- Customers-Orders Q24. Find the customer who placed the maximum number of orders.
-- ------------------------------------------------------------
SELECT c.customer_name, COUNT(o.order_id) AS total_orders
FROM Customers c
INNER JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
ORDER BY total_orders DESC
LIMIT 1;

-- ------------------------------------------------------------
-- Customers-Orders Q25. Find customers whose average order amount is greater than 2,000.
-- ------------------------------------------------------------
SELECT c.customer_name, ROUND(AVG(o.amount), 2) AS average_amount
FROM Customers c
INNER JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING AVG(o.amount) > 2000;

-- ------------------------------------------------------------
-- Customers-Orders Q26. Display the top 5 customers based on total purchase amount.
-- ------------------------------------------------------------
SELECT c.customer_name, SUM(o.amount) AS total_purchase
FROM Customers c
INNER JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
ORDER BY total_purchase DESC
LIMIT 5;

-- ------------------------------------------------------------
-- Customers-Orders Q27. Find the minimum order amount for each customer.
-- ------------------------------------------------------------
SELECT c.customer_name, MIN(o.amount) AS lowest_order
FROM Customers c
INNER JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;

-- ------------------------------------------------------------
-- Customers-Orders Q28. Find customers who have placed orders worth more than 5,000 in total.
-- ------------------------------------------------------------
SELECT c.customer_name, SUM(o.amount) AS total_purchase
FROM Customers c
INNER JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING SUM(o.amount) > 5000;

-- ------------------------------------------------------------
-- Customers-Orders Q29. Display customer-wise total orders and total purchase amount.
-- ------------------------------------------------------------
SELECT c.customer_name,
       COUNT(o.order_id)         AS total_orders,
       IFNULL(SUM(o.amount), 0)  AS total_purchase
FROM Customers c
LEFT JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;

-- ------------------------------------------------------------
-- Customers-Orders Q30. Find customers who placed more than 2 orders and spent more than 8,000.
-- ------------------------------------------------------------
SELECT c.customer_name,
       COUNT(o.order_id) AS total_orders,
       SUM(o.amount)     AS total_purchase
FROM Customers c
INNER JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING COUNT(o.order_id) > 2
   AND SUM(o.amount) > 8000;

-- ============================================================
--  END OF FILE
-- ============================================================
