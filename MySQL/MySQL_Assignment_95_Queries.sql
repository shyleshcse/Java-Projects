-- ============================================================
-- MYSQL ASSIGNMENT 1 - COMPLETE 95 QUERY SOLUTION
-- Database: MySQL_Assignment

-- DROP DATABASE IF EXISTS MySQL_Assignment;
CREATE DATABASE IF NOT EXISTS MySQL_Assignment;
USE MySQL_Assignment;

-- ============================================================
-- BANKING SCENARIO
-- ============================================================

CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50),
    city VARCHAR(50)
);

CREATE TABLE accounts (
    account_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    account_type VARCHAR(20),
    balance DECIMAL(10,2),
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
-- BANKING QUESTIONS 1-40
-- ============================================================

-- Q1. Retrieve all accounts with balance greater than 20,000.
SELECT * FROM accounts WHERE balance > 20000;

-- Q2. Find customers who live in Chennai.
SELECT * FROM customers WHERE city = 'Chennai';

-- Q3. Display accounts with balance between 20,000 and 50,000.
SELECT * FROM accounts WHERE balance BETWEEN 20000 AND 50000;

-- Q4. Find customers whose names start with 'J'.
SELECT * FROM customers WHERE name LIKE 'J%';

-- Q5. Retrieve accounts of type 'Savings' or 'Current'.
SELECT * FROM accounts WHERE account_type IN ('Savings', 'Current');

-- Q6. Display accounts that are not 'Savings'.
SELECT * FROM accounts WHERE account_type <> 'Savings';

-- Q7. Find customers whose names contain the letter 'a'.
SELECT * FROM customers WHERE name LIKE '%a%';

-- Q8. Retrieve accounts with balance less than or equal to 30,000.
SELECT * FROM accounts WHERE balance <= 30000;

-- Q9. Find customers who are not from Madurai.
SELECT * FROM customers WHERE city <> 'Madurai';

-- Q10. Display accounts where balance is not between 10,000 and 40,000.
SELECT * FROM accounts WHERE balance NOT BETWEEN 10000 AND 40000;

-- Q11. Retrieve customers whose names end with 'i'.
SELECT * FROM customers WHERE name LIKE '%i';

-- Q12. Find accounts with balance equal to 50,000.
SELECT * FROM accounts WHERE balance = 50000;

-- Q13. Display customers whose city is either Chennai or Salem.
SELECT * FROM customers WHERE city IN ('Chennai', 'Salem');

-- Q14. Find accounts with balance greater than 10,000 and less than 40,000.
SELECT * FROM accounts WHERE balance > 10000 AND balance < 40000;

-- Q15. Retrieve accounts where account type is not in ('Current').
SELECT * FROM accounts WHERE account_type NOT IN ('Current');

-- Q16. Display all accounts sorted by balance in descending order.
SELECT * FROM accounts ORDER BY balance DESC;

-- Q17. List customers sorted alphabetically by name.
SELECT * FROM customers ORDER BY name ASC;

-- Q18. Display accounts sorted by account type and then by balance (descending).
SELECT * FROM accounts ORDER BY account_type ASC, balance DESC;

-- Q19. Find the total balance of all accounts.
SELECT SUM(balance) AS total_balance FROM accounts;

-- Q20. Calculate the average balance of accounts.
SELECT AVG(balance) AS average_balance FROM accounts;

-- Q21. Find the maximum account balance.
SELECT MAX(balance) AS maximum_balance FROM accounts;

-- Q22. Find the minimum account balance.
SELECT MIN(balance) AS minimum_balance FROM accounts;

-- Q23. Count the total number of customers.
SELECT COUNT(*) AS total_customers FROM customers;

-- Q24. Find total balance grouped by account type.
SELECT account_type, SUM(balance) AS total_balance
FROM accounts
GROUP BY account_type;

-- Q25. Find average balance for each account type.
SELECT account_type, AVG(balance) AS average_balance
FROM accounts
GROUP BY account_type;

-- Q26. Display account types having average balance greater than 20,000.
SELECT account_type, AVG(balance) AS average_balance
FROM accounts
GROUP BY account_type
HAVING AVG(balance) > 20000;

-- Q27. Count number of accounts for each customer.
SELECT customer_id, COUNT(*) AS account_count
FROM accounts
GROUP BY customer_id;

-- Q28. Display customers having more than one account.
SELECT c.customer_id, c.name, COUNT(a.account_id) AS account_count
FROM customers c
JOIN accounts a ON c.customer_id = a.customer_id
GROUP BY c.customer_id, c.name
HAVING COUNT(a.account_id) > 1;

-- Q29. Retrieve customer names along with their account balances.
SELECT c.name, a.balance
FROM customers c
JOIN accounts a ON c.customer_id = a.customer_id;

-- Q30. Display all customers and their accounts (including customers without accounts).
SELECT c.customer_id, c.name, c.city,
       a.account_id, a.account_type, a.balance
FROM customers c
LEFT JOIN accounts a ON c.customer_id = a.customer_id;

-- Q31. Display all accounts and corresponding customer details.
SELECT a.account_id, a.account_type, a.balance,
       c.customer_id, c.name, c.city
FROM accounts a
JOIN customers c ON a.customer_id = c.customer_id;

-- Q32. Retrieve customer names and account types where balance is greater than 20,000.
SELECT c.name, a.account_type
FROM customers c
JOIN accounts a ON c.customer_id = a.customer_id
WHERE a.balance > 20000;

-- Q33. List customers with their total balance using JOIN.
SELECT c.customer_id, c.name,
       COALESCE(SUM(a.balance), 0) AS total_balance
FROM customers c
LEFT JOIN accounts a ON c.customer_id = a.customer_id
GROUP BY c.customer_id, c.name;

-- Q34. Display customer names and balances sorted by balance.
SELECT c.name, a.balance
FROM customers c
JOIN accounts a ON c.customer_id = a.customer_id
ORDER BY a.balance ASC;

-- Q35. Count number of accounts for each city using JOIN.
SELECT c.city, COUNT(a.account_id) AS account_count
FROM customers c
LEFT JOIN accounts a ON c.customer_id = a.customer_id
GROUP BY c.city;

-- Q36. Find accounts with balance greater than average balance.
SELECT *
FROM accounts
WHERE balance > (SELECT AVG(balance) FROM accounts);

-- Q37. Retrieve customers who have accounts.
SELECT *
FROM customers
WHERE customer_id IN (SELECT customer_id FROM accounts);

-- Q38. Find customers who do not have any accounts.
SELECT *
FROM customers
WHERE customer_id NOT IN (SELECT customer_id FROM accounts);

-- Q39. Display account(s) with the maximum balance.
SELECT *
FROM accounts
WHERE balance = (SELECT MAX(balance) FROM accounts);

-- Q40. Find customers whose total balance is greater than 40,000.
SELECT c.customer_id, c.name, SUM(a.balance) AS total_balance
FROM customers c
JOIN accounts a ON c.customer_id = a.customer_id
GROUP BY c.customer_id, c.name
HAVING SUM(a.balance) > 40000;

-- ============================================================
-- RAILWAY RESERVATION SCENARIO
-- ============================================================

CREATE TABLE trains (
    train_id INT PRIMARY KEY AUTO_INCREMENT,
    train_name VARCHAR(50),
    source VARCHAR(50),
    destination VARCHAR(50)
);

CREATE TABLE bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    train_id INT,
    passenger_name VARCHAR(50),
    fare DECIMAL(10,2),
    status VARCHAR(20),
    FOREIGN KEY (train_id) REFERENCES trains(train_id)
);

INSERT INTO trains (train_name, source, destination) VALUES
('Express1','Chennai','Madurai'),
('Express2','Coimbatore','Salem'),
('Express3','Madurai','Chennai');

INSERT INTO bookings (train_id, passenger_name, fare, status) VALUES
(1,'Janani',500,'Confirmed'),
(1,'Arun',500,'Waiting'),
(2,'Priya',300,'Confirmed'),
(3,'Karthik',450,'Cancelled'),
(2,'Meena',300,'Confirmed');

-- ============================================================
-- RAILWAY QUESTIONS 41-50
-- ============================================================

-- Q41. Retrieve all bookings with fare greater than 400.
SELECT * FROM bookings WHERE fare > 400;

-- Q42. Find bookings where status is not 'Confirmed'.
SELECT * FROM bookings WHERE status <> 'Confirmed';

-- Q43. Display trains starting from Chennai.
SELECT * FROM trains WHERE source = 'Chennai';

-- Q44. Retrieve bookings with fare between 300 and 500.
SELECT * FROM bookings WHERE fare BETWEEN 300 AND 500;

-- Q45. Find passengers whose names start with 'A'.
SELECT * FROM bookings WHERE passenger_name LIKE 'A%';

-- Q46. Retrieve train names along with passenger names.
SELECT t.train_name, b.passenger_name
FROM trains t
JOIN bookings b ON t.train_id = b.train_id;

-- Q47. Count number of bookings for each train.
SELECT t.train_id, t.train_name, COUNT(b.booking_id) AS booking_count
FROM trains t
LEFT JOIN bookings b ON t.train_id = b.train_id
GROUP BY t.train_id, t.train_name;

-- Q48. Display train names and total fare collected for each train.
SELECT t.train_id, t.train_name,
       COALESCE(SUM(b.fare), 0) AS total_fare
FROM trains t
LEFT JOIN bookings b ON t.train_id = b.train_id
GROUP BY t.train_id, t.train_name;

-- Q49. Find bookings with fare equal to the highest fare.
SELECT *
FROM bookings
WHERE fare = (SELECT MAX(fare) FROM bookings);

-- Q50. Retrieve trains that have more than one booking.
SELECT t.train_id, t.train_name, COUNT(b.booking_id) AS booking_count
FROM trains t
JOIN bookings b ON t.train_id = b.train_id
GROUP BY t.train_id, t.train_name
HAVING COUNT(b.booking_id) > 1;

-- ============================================================
-- EMPLOYEE MANAGEMENT
-- ============================================================

CREATE TABLE Employee (
    emp_id INT PRIMARY KEY,
    emp_name VARCHAR(50),
    department VARCHAR(30),
    salary DECIMAL(10,2),
    city VARCHAR(30),
    joining_date DATE
);

INSERT INTO Employee VALUES
(101,'John','IT',60000,'Chennai','2022-01-15'),
(102,'David','HR',45000,'Bangalore','2021-03-10'),
(103,'Smith','IT',70000,'Chennai','2020-07-12'),
(104,'Mary','Finance',55000,'Mumbai','2023-01-20'),
(105,'James','HR',48000,'Delhi','2022-05-05'),
(106,'Linda','Finance',65000,'Mumbai','2021-08-18');

-- ============================================================
-- EMPLOYEE QUESTIONS 51-65
-- ============================================================

-- Q51. Find the total number of employees in each department.
SELECT department, COUNT(*) AS employee_count
FROM Employee
GROUP BY department;

-- Q52. Find the average salary of employees in each department.
SELECT department, AVG(salary) AS average_salary
FROM Employee
GROUP BY department;

-- Q53. Display departments having more than one employee.
SELECT department, COUNT(*) AS employee_count
FROM Employee
GROUP BY department
HAVING COUNT(*) > 1;

-- Q54. Find the highest salary in each department.
SELECT department, MAX(salary) AS highest_salary
FROM Employee
GROUP BY department;

-- Q55. Find the lowest salary in each department.
SELECT department, MIN(salary) AS lowest_salary
FROM Employee
GROUP BY department;

-- Q56. Find departments whose average salary is greater than 50,000.
SELECT department, AVG(salary) AS average_salary
FROM Employee
GROUP BY department
HAVING AVG(salary) > 50000;

-- Q57. Calculate the total salary expenditure for each department.
SELECT department, SUM(salary) AS total_salary
FROM Employee
GROUP BY department;

-- Q58. Display all employees sorted by salary in descending order.
SELECT * FROM Employee ORDER BY salary DESC;

-- Q59. Display employees sorted first by department and then by salary in descending order.
SELECT * FROM Employee
ORDER BY department ASC, salary DESC;

-- Q60. Find cities that have more than one employee.
SELECT city, COUNT(*) AS employee_count
FROM Employee
GROUP BY city
HAVING COUNT(*) > 1;

-- Q61. Find the total salary paid in each city.
SELECT city, SUM(salary) AS total_salary
FROM Employee
GROUP BY city;

-- Q62. Display departments ordered by total salary expenditure from highest to lowest.
SELECT department, SUM(salary) AS total_salary
FROM Employee
GROUP BY department
ORDER BY total_salary DESC;

-- Q63. Find the number of employees in each department whose salary is greater than 50,000.
SELECT department, COUNT(*) AS employee_count
FROM Employee
WHERE salary > 50000
GROUP BY department;

-- Q64. Find the difference between the highest and lowest salary in each department.
SELECT department,
       MAX(salary) - MIN(salary) AS salary_difference
FROM Employee
GROUP BY department;

-- Q65. Display the top 3 highest-paid employees.
SELECT *
FROM Employee
ORDER BY salary DESC
LIMIT 3;

-- ============================================================
-- CUSTOMERS & ORDERS
-- ============================================================

CREATE TABLE order_customers (
    customer_id INT PRIMARY KEY,
    customer_name VARCHAR(50),
    city VARCHAR(30)
);

CREATE TABLE Orders (
    order_id INT PRIMARY KEY,
    customer_id INT,
    amount DECIMAL(10,2),
    order_date DATE,
    FOREIGN KEY(customer_id) REFERENCES order_customers(customer_id)
);

INSERT INTO order_customers (customer_id, customer_name, city) VALUES
(1,'Ravi','Chennai'),
(2,'Priya','Coimbatore'),
(3,'Arun','Madurai'),
(4,'Divya','Salem'),
(5,'Karthik','Trichy'),
(6,'Meena','Erode'),
(7,'Suresh','Chennai');

INSERT INTO Orders (order_id, customer_id, amount, order_date) VALUES
(1001,1,2500,'2026-01-05'),
(1002,1,3200,'2026-01-15'),
(1003,1,1800,'2026-02-10'),
(1004,1,3000,'2026-03-01'),
(1005,2,4500,'2026-01-08'),
(1006,2,2200,'2026-02-12'),
(1007,2,1800,'2026-03-05'),
(1008,3,6000,'2026-01-20'),
(1009,3,2500,'2026-02-18'),
(1010,4,1200,'2026-01-11'),
(1011,4,1500,'2026-02-21'),
(1012,4,2000,'2026-03-12'),
(1013,4,1800,'2026-04-02'),
(1014,5,7000,'2026-01-25'),
(1015,5,5000,'2026-02-25'),
(1016,6,900,'2026-01-30'),
(1017,6,1100,'2026-03-10'),
(1018,7,15000,'2026-02-05');

-- ============================================================
-- CUSTOMERS & ORDERS QUESTIONS 66-80
-- ============================================================

-- Q66. Find the total order amount for each customer.
SELECT c.customer_id, c.customer_name,
       COALESCE(SUM(o.amount), 0) AS total_order_amount
FROM order_customers c
LEFT JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;

-- Q67. Find customers who have placed more than 3 orders.
SELECT c.customer_id, c.customer_name,
       COUNT(o.order_id) AS order_count
FROM order_customers c
JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING COUNT(o.order_id) > 3;

-- Q68. Find the average order amount for each customer.
SELECT c.customer_id, c.customer_name,
       AVG(o.amount) AS average_order_amount
FROM order_customers c
LEFT JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;

-- Q69. Find the highest order amount placed by each customer.
SELECT c.customer_id, c.customer_name,
       MAX(o.amount) AS highest_order_amount
FROM order_customers c
LEFT JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;

-- Q70. Display customers sorted by their total purchase amount.
SELECT c.customer_id, c.customer_name,
       COALESCE(SUM(o.amount), 0) AS total_purchase_amount
FROM order_customers c
LEFT JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
ORDER BY total_purchase_amount DESC;

-- Q71. Find customers whose total purchase amount exceeds 10,000.
SELECT c.customer_id, c.customer_name,
       SUM(o.amount) AS total_purchase_amount
FROM order_customers c
JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING SUM(o.amount) > 10000;

-- Q72. Display customer names along with the total number of orders placed.
SELECT c.customer_name, COUNT(o.order_id) AS total_orders
FROM order_customers c
LEFT JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;

-- Q73. Find the customer who spent the highest amount.
SELECT c.customer_id, c.customer_name,
       SUM(o.amount) AS total_purchase_amount
FROM order_customers c
JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
ORDER BY total_purchase_amount DESC
LIMIT 1;

-- Q74. Find the customer who placed the maximum number of orders.
SELECT c.customer_id, c.customer_name,
       COUNT(o.order_id) AS total_orders
FROM order_customers c
JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
ORDER BY total_orders DESC
LIMIT 1;

-- Q75. Find customers whose average order amount is greater than 2,000.
SELECT c.customer_id, c.customer_name,
       AVG(o.amount) AS average_order_amount
FROM order_customers c
JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING AVG(o.amount) > 2000;

-- Q76. Display the top 5 customers based on total purchase amount.
SELECT c.customer_id, c.customer_name,
       SUM(o.amount) AS total_purchase_amount
FROM order_customers c
JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
ORDER BY total_purchase_amount DESC
LIMIT 5;

-- Q77. Find the minimum order amount for each customer.
SELECT c.customer_id, c.customer_name,
       MIN(o.amount) AS minimum_order_amount
FROM order_customers c
LEFT JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;

-- Q78. Find customers who have placed orders worth more than 5,000 in total.
SELECT c.customer_id, c.customer_name,
       SUM(o.amount) AS total_purchase_amount
FROM order_customers c
JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING SUM(o.amount) > 5000;

-- Q79. Display customer-wise total orders and total purchase amount.
SELECT c.customer_id, c.customer_name,
       COUNT(o.order_id) AS total_orders,
       COALESCE(SUM(o.amount), 0) AS total_purchase_amount
FROM order_customers c
LEFT JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;

-- Q80. Find customers who placed more than 2 orders and spent more than 8,000.
SELECT c.customer_id, c.customer_name,
       COUNT(o.order_id) AS total_orders,
       SUM(o.amount) AS total_purchase_amount
FROM order_customers c
JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING COUNT(o.order_id) > 2
   AND SUM(o.amount) > 8000;

-- ============================================================
-- STUDENTS
-- ============================================================

CREATE TABLE Students (
    student_id INT PRIMARY KEY,
    student_name VARCHAR(50),
    department VARCHAR(30),
    marks INT
);

INSERT INTO Students (student_id, student_name, department, marks) VALUES
(1,'Aarav','CSE',92),
(2,'Bhavya','CSE',88),
(3,'Charan','CSE',76),
(4,'Deepa','CSE',81),
(5,'Ezhil','CSE',69),
(6,'Farhan','CSE',95),
(7,'Gokul','ECE',78),
(8,'Harini','ECE',84),
(9,'Ishaan','ECE',72),
(10,'Jaya','ECE',91),
(11,'Kavin','ECE',86),
(12,'Lavanya','ECE',79),
(13,'Manoj','IT',65),
(14,'Nisha','IT',74),
(15,'Omkar','IT',82),
(16,'Pooja','IT',88),
(17,'Rahul','IT',93),
(18,'Sneha','IT',77),
(19,'Tarun','EEE',55),
(20,'Uma','EEE',68),
(21,'Vijay','EEE',73),
(22,'Waseem','EEE',61),
(23,'Yamini','EEE',80),
(24,'Zara','EEE',58),
(25,'Adithya','MECH',71),
(26,'Bala','MECH',75),
(27,'Chetan','MECH',67),
(28,'Divya','MECH',89),
(29,'Eswar','MECH',94),
(30,'Fathima','MECH',63);

-- ============================================================
-- STUDENTS QUESTIONS 81-95
-- ============================================================

-- Q81. Find the average marks scored by students in each department.
SELECT department, AVG(marks) AS average_marks
FROM Students
GROUP BY department;

-- Q82. Find departments whose average marks are above 75.
SELECT department, AVG(marks) AS average_marks
FROM Students
GROUP BY department
HAVING AVG(marks) > 75;

-- Q83. Find the highest mark scored in each department.
SELECT department, MAX(marks) AS highest_mark
FROM Students
GROUP BY department;

-- Q84. Find the total number of students in each department.
SELECT department, COUNT(*) AS student_count
FROM Students
GROUP BY department;

-- Q85. Find departments having more than 5 students.
SELECT department, COUNT(*) AS student_count
FROM Students
GROUP BY department
HAVING COUNT(*) > 5;

-- Q86. Display departments sorted by average marks in descending order.
SELECT department, AVG(marks) AS average_marks
FROM Students
GROUP BY department
ORDER BY average_marks DESC;

-- Q87. Find the top 3 departments based on average marks.
SELECT department, AVG(marks) AS average_marks
FROM Students
GROUP BY department
ORDER BY average_marks DESC
LIMIT 3;

-- Q88. Find departments whose average marks are between 70 and 90.
SELECT department, AVG(marks) AS average_marks
FROM Students
GROUP BY department
HAVING AVG(marks) BETWEEN 70 AND 90;

-- Q89. Find the total marks scored by students in each department.
SELECT department, SUM(marks) AS total_marks
FROM Students
GROUP BY department;

-- Q90. Display departments sorted by the total number of students.
SELECT department, COUNT(*) AS student_count
FROM Students
GROUP BY department
ORDER BY student_count DESC;

-- Q91. Find the lowest mark scored in each department.
SELECT department, MIN(marks) AS lowest_mark
FROM Students
GROUP BY department;

-- Q92. Find departments where the highest mark is greater than 90.
SELECT department, MAX(marks) AS highest_mark
FROM Students
GROUP BY department
HAVING MAX(marks) > 90;

-- Q93. Find the number of students scoring above 80 in each department.
SELECT department, COUNT(*) AS students_above_80
FROM Students
WHERE marks > 80
GROUP BY department;

-- Q94. Find departments where more than 3 students scored above 75.
SELECT department, COUNT(*) AS students_above_75
FROM Students
WHERE marks > 75
GROUP BY department
HAVING COUNT(*) > 3;

-- Q95. Display departments ordered by highest mark in descending order.
SELECT department, MAX(marks) AS highest_mark
FROM Students
GROUP BY department
ORDER BY highest_mark DESC;

-- ============================================================
-- END OF MYSQL ASSIGNMENT 1
-- ============================================================
