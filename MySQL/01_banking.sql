-- ============================================================
--  BANKING SCENARIO  (Questions 1 - 40)
--  Database : banking_db

DROP DATABASE IF EXISTS banking_db;
CREATE DATABASE banking_db;
USE banking_db;

CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(50),
    city        VARCHAR(50)
);

CREATE TABLE accounts (
    account_id   INT PRIMARY KEY AUTO_INCREMENT,
    customer_id  INT,
    account_type VARCHAR(20),
    balance      DECIMAL(10,2),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

INSERT INTO customers (name, city) VALUES
('Janani','Chennai'),
('Arun','Coimbatore'),
('Priya','Madurai'),
('Karthik','Salem');

INSERT INTO accounts (customer_id, account_type, balance) VALUES
(1,'Savings',50000),
(1,'Current',20000),
(2,'Savings',30000),
(3,'Savings',15000),
(4,'Current',40000);


-- ============================================================
--  BASIC + WHERE + OPERATORS (Q1 - Q15)
-- ============================================================

-- ------------------------------------------------------------
-- Q1. Retrieve all accounts with balance greater than 20,000.
-- ------------------------------------------------------------
SELECT * FROM accounts
WHERE balance > 20000;

-- ------------------------------------------------------------
-- Q2. Find customers who live in Chennai.
-- ------------------------------------------------------------
SELECT * FROM customers
WHERE city = 'Chennai';

-- ------------------------------------------------------------
-- Q3. Display accounts with balance between 20,000 and 50,000.
-- ------------------------------------------------------------
SELECT * FROM accounts
WHERE balance BETWEEN 20000 AND 50000;

-- ------------------------------------------------------------
-- Q4. Find customers whose names start with 'J'.
-- ------------------------------------------------------------
SELECT * FROM customers
WHERE name LIKE 'J%';

-- ------------------------------------------------------------
-- Q5. Retrieve accounts of type 'Savings' or 'Current'.
-- ------------------------------------------------------------
SELECT * FROM accounts
WHERE account_type IN ('Savings', 'Current');

-- ------------------------------------------------------------
-- Q6. Display accounts that are not 'Savings'.
-- ------------------------------------------------------------
SELECT * FROM accounts
WHERE account_type <> 'Savings';

-- ------------------------------------------------------------
-- Q7. Find customers whose names contain the letter 'a'.
-- ------------------------------------------------------------
SELECT * FROM customers
WHERE name LIKE '%a%';

-- ------------------------------------------------------------
-- Q8. Retrieve accounts with balance less than or equal to 30,000.
-- ------------------------------------------------------------
SELECT * FROM accounts
WHERE balance <= 30000;

-- ------------------------------------------------------------
-- Q9. Find customers who are not from Madurai.
-- ------------------------------------------------------------
SELECT * FROM customers
WHERE city <> 'Madurai';

-- ------------------------------------------------------------
-- Q10. Display accounts where balance is not between 10,000 and 40,000.
-- ------------------------------------------------------------
SELECT * FROM accounts
WHERE balance NOT BETWEEN 10000 AND 40000;

-- ------------------------------------------------------------
-- Q11. Retrieve customers whose names end with 'i'.
-- ------------------------------------------------------------
SELECT * FROM customers
WHERE name LIKE '%i';

-- ------------------------------------------------------------
-- Q12. Find accounts with balance equal to 50,000.
-- ------------------------------------------------------------
SELECT * FROM accounts
WHERE balance = 50000;

-- ------------------------------------------------------------
-- Q13. Display customers whose city is either Chennai or Salem.
-- ------------------------------------------------------------
SELECT * FROM customers
WHERE city IN ('Chennai', 'Salem');

-- ------------------------------------------------------------
-- Q14. Find accounts with balance greater than 10,000 and less than 40,000.
-- ------------------------------------------------------------
SELECT * FROM accounts
WHERE balance > 10000 AND balance < 40000;

-- ------------------------------------------------------------
-- Q15. Retrieve accounts where account type is not in ('Current').
-- ------------------------------------------------------------
SELECT * FROM accounts
WHERE account_type NOT IN ('Current');

-- ============================================================
--  ORDER BY (Q16 - Q18)
-- ============================================================

-- ------------------------------------------------------------
-- Q16. Display all accounts sorted by balance in descending order.
-- ------------------------------------------------------------
SELECT * FROM accounts
ORDER BY balance DESC;

-- ------------------------------------------------------------
-- Q17. List customers sorted alphabetically by name.
-- ------------------------------------------------------------
SELECT * FROM customers
ORDER BY name ASC;

-- ------------------------------------------------------------
-- Q18. Display accounts sorted by account type and then by balance (descending).
-- ------------------------------------------------------------
SELECT * FROM accounts
ORDER BY account_type ASC, balance DESC;

-- ============================================================
--  AGGREGATE FUNCTIONS (Q19 - Q23)
-- ============================================================

-- ------------------------------------------------------------
-- Q19. Find the total balance of all accounts.
-- ------------------------------------------------------------
SELECT SUM(balance) AS total_balance
FROM accounts;

-- ------------------------------------------------------------
-- Q20. Calculate the average balance of accounts.
-- ------------------------------------------------------------
SELECT ROUND(AVG(balance), 2) AS average_balance
FROM accounts;

-- ------------------------------------------------------------
-- Q21. Find the maximum account balance.
-- ------------------------------------------------------------
SELECT MAX(balance) AS maximum_balance
FROM accounts;

-- ------------------------------------------------------------
-- Q22. Find the minimum account balance.
-- ------------------------------------------------------------
SELECT MIN(balance) AS minimum_balance
FROM accounts;

-- ------------------------------------------------------------
-- Q23. Count the total number of customers.
-- ------------------------------------------------------------
SELECT COUNT(*) AS total_customers
FROM customers;

-- ============================================================
--  GROUP BY + HAVING (Q24 - Q28)
-- ============================================================

-- ------------------------------------------------------------
-- Q24. Find total balance grouped by account type.
-- ------------------------------------------------------------
SELECT account_type, SUM(balance) AS total_balance
FROM accounts
GROUP BY account_type;

-- ------------------------------------------------------------
-- Q25. Find average balance for each account type.
-- ------------------------------------------------------------
SELECT account_type, ROUND(AVG(balance), 2) AS average_balance
FROM accounts
GROUP BY account_type;

-- ------------------------------------------------------------
-- Q26. Display account types having average balance greater than 20,000.
-- ------------------------------------------------------------
SELECT account_type, ROUND(AVG(balance), 2) AS average_balance
FROM accounts
GROUP BY account_type
HAVING AVG(balance) > 20000;

-- ------------------------------------------------------------
-- Q27. Count number of accounts for each customer.
-- ------------------------------------------------------------
SELECT customer_id, COUNT(account_id) AS total_accounts
FROM accounts
GROUP BY customer_id;

-- ------------------------------------------------------------
-- Q28. Display customers having more than one account.
-- ------------------------------------------------------------
SELECT customer_id, COUNT(account_id) AS total_accounts
FROM accounts
GROUP BY customer_id
HAVING COUNT(account_id) > 1;

-- ============================================================
--  JOINS (Q29 - Q35)
-- ============================================================

-- ------------------------------------------------------------
-- Q29. Retrieve customer names along with their account balances.
-- ------------------------------------------------------------
SELECT c.name, a.balance
FROM customers c
INNER JOIN accounts a
    ON c.customer_id = a.customer_id;

-- ------------------------------------------------------------
-- Q30. Display all customers and their accounts (including customers without accounts).
-- ------------------------------------------------------------
SELECT c.customer_id, c.name, a.account_id, a.account_type, a.balance
FROM customers c
LEFT JOIN accounts a
    ON c.customer_id = a.customer_id;
-- ------------------------------------------------------------
-- Q31. Display all accounts and corresponding customer details.
-- ------------------------------------------------------------
SELECT c.customer_id, c.name, c.city, a.account_id, a.account_type, a.balance
FROM customers c
RIGHT JOIN accounts a
    ON c.customer_id = a.customer_id;
-- ------------------------------------------------------------
-- Q32. Retrieve customer names and account types where balance is greater than 20,000.
-- ------------------------------------------------------------
SELECT c.name, a.account_type
FROM customers c
INNER JOIN accounts a
    ON c.customer_id = a.customer_id
WHERE a.balance > 20000;

-- ------------------------------------------------------------
-- Q33. List customers with their total balance using JOIN.
-- ------------------------------------------------------------
SELECT c.name, SUM(a.balance) AS total_balance
FROM customers c
INNER JOIN accounts a
    ON c.customer_id = a.customer_id
GROUP BY c.customer_id, c.name;

-- ------------------------------------------------------------
-- Q34. Display customer names and balances sorted by balance.
-- ------------------------------------------------------------
SELECT c.name, a.balance
FROM customers c
INNER JOIN accounts a
    ON c.customer_id = a.customer_id
ORDER BY a.balance;

-- ------------------------------------------------------------
-- Q35. Count number of accounts for each city using JOIN.
-- ------------------------------------------------------------
SELECT c.city, COUNT(a.account_id) AS total_accounts
FROM customers c
INNER JOIN accounts a
    ON c.customer_id = a.customer_id
GROUP BY c.city;

-- ============================================================
--  SUBQUERIES (Q36 - Q40)
-- ============================================================

-- ------------------------------------------------------------
-- Q36. Find accounts with balance greater than average balance.
-- ------------------------------------------------------------
SELECT * FROM accounts
WHERE balance > (SELECT AVG(balance) FROM accounts);

-- ------------------------------------------------------------
-- Q37. Retrieve customers who have accounts.
-- ------------------------------------------------------------
SELECT * FROM customers
WHERE customer_id IN (SELECT customer_id FROM accounts);

-- ------------------------------------------------------------
-- Q38. Find customers who do not have any accounts.
-- ------------------------------------------------------------
SELECT * FROM customers c
WHERE NOT EXISTS (
    SELECT 1 FROM accounts a
    WHERE a.customer_id = c.customer_id
);

-- ------------------------------------------------------------
-- Q39. Display account(s) with the maximum balance.
-- ------------------------------------------------------------
SELECT * FROM accounts
WHERE balance = (SELECT MAX(balance) FROM accounts);

-- ------------------------------------------------------------
-- Q40. Find customers whose total balance is greater than 40,000.
-- ------------------------------------------------------------
SELECT * FROM customers
WHERE customer_id IN (
    SELECT customer_id
    FROM accounts
    GROUP BY customer_id
    HAVING SUM(balance) > 40000
);

-- ============================================================
--  END OF FILE
-- ============================================================
