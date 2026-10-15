-- ============================================================
--  RAILWAY RESERVATION SCENARIO  (Questions 41 - 50)
--  Database : railway_db

DROP DATABASE IF EXISTS railway_db;
CREATE DATABASE railway_db;
USE railway_db;

CREATE TABLE trains (
    train_id    INT PRIMARY KEY AUTO_INCREMENT,
    train_name  VARCHAR(50),
    source      VARCHAR(50),
    destination VARCHAR(50)
);

CREATE TABLE bookings (
    booking_id     INT PRIMARY KEY AUTO_INCREMENT,
    train_id       INT,
    passenger_name VARCHAR(50),
    fare           DECIMAL(10,2),
    status         VARCHAR(20),
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
--  BASIC + WHERE + OPERATORS (Q41 - Q45)
-- ============================================================

-- ------------------------------------------------------------
-- Q41. Retrieve all bookings with fare greater than 400.
-- ------------------------------------------------------------
SELECT * FROM bookings
WHERE fare > 400;

-- ------------------------------------------------------------
-- Q42. Find bookings where status is not 'Confirmed'.
-- ------------------------------------------------------------
SELECT * FROM bookings
WHERE status <> 'Confirmed';

-- ------------------------------------------------------------
-- Q43. Display trains starting from Chennai.
-- ------------------------------------------------------------
SELECT * FROM trains
WHERE source = 'Chennai';

-- ------------------------------------------------------------
-- Q44. Retrieve bookings with fare between 300 and 500.
-- ------------------------------------------------------------
SELECT * FROM bookings
WHERE fare BETWEEN 300 AND 500;

-- ------------------------------------------------------------
-- Q45. Find passengers whose names start with 'A'.
-- ------------------------------------------------------------
SELECT * FROM bookings
WHERE passenger_name LIKE 'A%';

-- ============================================================
--  JOINS + GROUP BY + ORDER BY (Q46 - Q48)
-- ============================================================

-- ------------------------------------------------------------
-- Q46. Retrieve train names along with passenger names.
-- ------------------------------------------------------------
SELECT t.train_name, b.passenger_name
FROM trains t
INNER JOIN bookings b
    ON t.train_id = b.train_id
ORDER BY t.train_name, b.passenger_name;

-- ------------------------------------------------------------
-- Q47. Count number of bookings for each train.
-- ------------------------------------------------------------
SELECT t.train_name, COUNT(b.booking_id) AS total_bookings
FROM trains t
LEFT JOIN bookings b
    ON t.train_id = b.train_id
GROUP BY t.train_id, t.train_name
ORDER BY total_bookings DESC;

-- ------------------------------------------------------------
-- Q48. Display train names and total fare collected for each train.
-- ------------------------------------------------------------
SELECT t.train_name, SUM(b.fare) AS total_fare
FROM trains t
INNER JOIN bookings b
    ON t.train_id = b.train_id
GROUP BY t.train_id, t.train_name
ORDER BY total_fare DESC;

-- ============================================================
--  SUBQUERIES (Q49 - Q50)
-- ============================================================

-- ------------------------------------------------------------
-- Q49. Find bookings with fare equal to the highest fare.
-- ------------------------------------------------------------
SELECT * FROM bookings
WHERE fare = (SELECT MAX(fare) FROM bookings);


-- ------------------------------------------------------------
-- Q50. Retrieve trains that have more than one booking.
-- ------------------------------------------------------------
SELECT * FROM trains
WHERE train_id IN (
    SELECT train_id
    FROM bookings
    GROUP BY train_id
    HAVING COUNT(*) > 1
);

-- ============================================================
--  END OF FILE
-- ============================================================
