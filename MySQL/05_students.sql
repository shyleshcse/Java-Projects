-- ============================================================
--  STUDENTS  (Questions 31 - 45)
--  Database : students_db

DROP DATABASE IF EXISTS students_db;
CREATE DATABASE students_db;
USE students_db;

CREATE TABLE Students (
    student_id   INT PRIMARY KEY,
    student_name VARCHAR(50),
    department   VARCHAR(30),
    marks        INT
);

INSERT INTO Students VALUES
(1,'Aarav','CSE',92),
(2,'Bhavna','CSE',85),
(3,'Charan','CSE',78),
(4,'Divya','CSE',88),
(5,'Eshwar','CSE',70),
(6,'Farah','CSE',95),
(7,'Gokul','CSE',81),
(8,'Harini','ECE',76),
(9,'Imran','ECE',82),
(10,'Jaya','ECE',68),
(11,'Kiran','ECE',90),
(12,'Lakshmi','ECE',74),
(13,'Manoj','ECE',79),
(14,'Naveen','MECH',65),
(15,'Oviya','MECH',72),
(16,'Pranav','MECH',58),
(17,'Rohit','MECH',80),
(18,'Sandhya','CIVIL',88),
(19,'Tarun','CIVIL',91),
(20,'Uma','CIVIL',77);


-- ============================================================
--  STUDENTS TABLE (Q31 - Q45)
-- ============================================================

-- ------------------------------------------------------------
-- Students Q31. Find the average marks scored by students in each department.
-- ------------------------------------------------------------
SELECT department, ROUND(AVG(marks), 2) AS average_marks
FROM Students
GROUP BY department;

-- ------------------------------------------------------------
-- Students Q32. Find departments whose average marks are above 75.
-- ------------------------------------------------------------
SELECT department, ROUND(AVG(marks), 2) AS average_marks
FROM Students
GROUP BY department
HAVING AVG(marks) > 75;

-- ------------------------------------------------------------
-- Students Q33. Find the highest mark scored in each department.
-- ------------------------------------------------------------
SELECT department, MAX(marks) AS highest_mark
FROM Students
GROUP BY department;

-- ------------------------------------------------------------
-- Students Q34. Find the total number of students in each department.
-- ------------------------------------------------------------
SELECT department, COUNT(*) AS total_students
FROM Students
GROUP BY department;

-- ------------------------------------------------------------
-- Students Q35. Find departments having more than 5 students.
-- ------------------------------------------------------------
SELECT department, COUNT(*) AS total_students
FROM Students
GROUP BY department
HAVING COUNT(*) > 5;

-- ------------------------------------------------------------
-- Students Q36. Display departments sorted by average marks in descending order.
-- ------------------------------------------------------------
SELECT department, ROUND(AVG(marks), 2) AS average_marks
FROM Students
GROUP BY department
ORDER BY average_marks DESC;

-- ------------------------------------------------------------
-- Students Q37. Find the top 3 departments based on average marks.
-- ------------------------------------------------------------
SELECT department, ROUND(AVG(marks), 2) AS average_marks
FROM Students
GROUP BY department
ORDER BY average_marks DESC
LIMIT 3;

-- ------------------------------------------------------------
-- Students Q38. Find departments whose average marks are between 70 and 90.
-- ------------------------------------------------------------
SELECT department, ROUND(AVG(marks), 2) AS average_marks
FROM Students
GROUP BY department
HAVING AVG(marks) BETWEEN 70 AND 90;

-- ------------------------------------------------------------
-- Students Q39. Find the total marks scored by students in each department.
-- ------------------------------------------------------------
SELECT department, SUM(marks) AS total_marks
FROM Students
GROUP BY department;

-- ------------------------------------------------------------
-- Students Q40. Display departments sorted by the total number of students.
-- ------------------------------------------------------------
SELECT department, COUNT(*) AS total_students
FROM Students
GROUP BY department
ORDER BY total_students DESC;

-- ------------------------------------------------------------
-- Students Q41. Find the lowest mark scored in each department.
-- ------------------------------------------------------------
SELECT department, MIN(marks) AS lowest_mark
FROM Students
GROUP BY department;

-- ------------------------------------------------------------
-- Students Q42. Find departments where the highest mark is greater than 90.
-- ------------------------------------------------------------
SELECT department, MAX(marks) AS highest_mark
FROM Students
GROUP BY department
HAVING MAX(marks) > 90;

-- ------------------------------------------------------------
-- Students Q43. Find the number of students scoring above 80 in each department.
-- ------------------------------------------------------------
SELECT department, COUNT(*) AS students_above_80
FROM Students
WHERE marks > 80
GROUP BY department;

-- ------------------------------------------------------------
-- Students Q44. Find departments where more than 3 students scored above 75.
-- ------------------------------------------------------------
SELECT department, COUNT(*) AS students_above_75
FROM Students
WHERE marks > 75
GROUP BY department
HAVING COUNT(*) > 3;

-- ------------------------------------------------------------
-- Students Q45. Display departments ordered by highest mark in descending order.
-- ------------------------------------------------------------
SELECT department, MAX(marks) AS highest_mark
FROM Students
GROUP BY department
ORDER BY highest_mark DESC;

-- ============================================================
--  END OF FILE
-- ============================================================
