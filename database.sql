-- ============================================
--  Library Management System - Database Schema
--  Database: MySQL 8.0+
-- ============================================

CREATE DATABASE IF NOT EXISTS library_management;
USE library_management;

-- ─── BOOKS TABLE ───────────────────────────────────────────
CREATE TABLE IF NOT EXISTS books (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  title         VARCHAR(200)  NOT NULL,
  author        VARCHAR(150)  NOT NULL,
  isbn          VARCHAR(20)   NOT NULL UNIQUE,
  category      VARCHAR(100)  NOT NULL,
  publisher     VARCHAR(150),
  published_year YEAR,
  total_copies  INT           NOT NULL DEFAULT 1,
  available_copies INT        NOT NULL DEFAULT 1,
  description   TEXT,
  created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ─── MEMBERS TABLE ─────────────────────────────────────────
CREATE TABLE IF NOT EXISTS members (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  name          VARCHAR(100)  NOT NULL,
  email         VARCHAR(100)  NOT NULL UNIQUE,
  phone         VARCHAR(15),
  address       TEXT,
  membership_type ENUM('Student','Faculty','Public') NOT NULL DEFAULT 'Student',
  status        ENUM('Active','Inactive','Suspended') NOT NULL DEFAULT 'Active',
  joined_date   DATE          NOT NULL DEFAULT (CURDATE()),
  created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ─── BORROW RECORDS TABLE ──────────────────────────────────
CREATE TABLE IF NOT EXISTS borrow_records (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  book_id       INT           NOT NULL,
  member_id     INT           NOT NULL,
  borrow_date   DATE          NOT NULL DEFAULT (CURDATE()),
  due_date      DATE          NOT NULL,
  return_date   DATE,
  status        ENUM('Borrowed','Returned','Overdue') NOT NULL DEFAULT 'Borrowed',
  fine_amount   DECIMAL(8,2)  DEFAULT 0.00,
  created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (book_id)   REFERENCES books(id)   ON DELETE RESTRICT,
  FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE RESTRICT
);

-- ─── SAMPLE DATA: BOOKS ────────────────────────────────────
INSERT INTO books (title, author, isbn, category, publisher, published_year, total_copies, available_copies, description) VALUES
('The Pragmatic Programmer',         'Andrew Hunt',           '978-0135957059', 'Technology',  'Addison-Wesley', 2019, 3, 3, 'A guide for software developers on best practices.'),
('Clean Code',                        'Robert C. Martin',      '978-0132350884', 'Technology',  'Prentice Hall',  2008, 2, 2, 'A handbook of agile software craftsmanship.'),
('To Kill a Mockingbird',             'Harper Lee',            '978-0061935466', 'Fiction',     'HarperCollins',  1960, 4, 4, 'A classic novel about racial injustice.'),
('Sapiens: A Brief History',          'Yuval Noah Harari',     '978-0062316097', 'Non-Fiction', 'Harper Perennial',2015, 3, 3, 'A narrative history of humankind.'),
('Introduction to Algorithms',        'Thomas H. Cormen',      '978-0262033848', 'Technology',  'MIT Press',       2009, 2, 2, 'Comprehensive textbook on algorithms.'),
('The Alchemist',                     'Paulo Coelho',          '978-0062315007', 'Fiction',     'HarperOne',       1988, 5, 5, 'A philosophical novel about following your dreams.'),
('Atomic Habits',                     'James Clear',           '978-0735211292', 'Self-Help',   'Avery',           2018, 3, 3, 'An easy guide to building good habits.'),
('Design Patterns',                   'Gang of Four',          '978-0201633610', 'Technology',  'Addison-Wesley', 1994, 2, 2, 'Elements of reusable object-oriented software.'),
('1984',                              'George Orwell',         '978-0451524935', 'Fiction',     'Signet Classics', 1949, 4, 4, 'A dystopian social science fiction novel.'),
('The Great Gatsby',                  'F. Scott Fitzgerald',   '978-0743273565', 'Fiction',     'Scribner',        1925, 3, 3, 'A story of the American dream.');


-- ─── SAMPLE DATA: MEMBERS ──────────────────────────────────
INSERT INTO members (name, email, phone, address, membership_type, status, joined_date) VALUES
('Arjun Kumar',   'arjun@example.com',   '9876543210', '12 Anna Nagar, Chennai',   'Student',  'Active',   '2024-01-10'),
('Priya Sharma',  'priya@example.com',   '9123456780', '45 T Nagar, Chennai',      'Faculty',  'Active',   '2024-02-15'),
('Rahul Verma',   'rahul@example.com',   '9988776655', '7 Velachery, Chennai',     'Student',  'Active',   '2024-03-05'),
('Sneha Reddy',   'sneha@example.com',   '9001122334', '88 Adyar, Chennai',        'Public',   'Active',   '2024-04-20'),
('Karthik Nair',  'karthik@example.com', '9445566778', '3 Mylapore, Chennai',      'Student',  'Inactive', '2024-05-11');

-- ─── SAMPLE BORROW RECORDS ─────────────────────────────────
INSERT INTO borrow_records (book_id, member_id, borrow_date, due_date, return_date, status) VALUES
(1, 1, '2025-01-01', '2025-01-15', '2025-01-14', 'Returned'),
(2, 2, '2025-02-10', '2025-02-24', NULL,          'Borrowed'),
(3, 3, '2025-01-20', '2025-02-03', '2025-02-02', 'Returned'),
(5, 1, '2025-03-01', '2025-03-15', NULL,          'Borrowed');

USE library_management;

-- Drop foreign keys first
ALTER TABLE borrow_records DROP FOREIGN KEY borrow_records_ibfk_1;
ALTER TABLE borrow_records DROP FOREIGN KEY borrow_records_ibfk_2;

-- Change column types to BIGINT
ALTER TABLE books          MODIFY COLUMN id        BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE members        MODIFY COLUMN id        BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE borrow_records MODIFY COLUMN id        BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE borrow_records MODIFY COLUMN book_id   BIGINT NOT NULL;
ALTER TABLE borrow_records MODIFY COLUMN member_id BIGINT NOT NULL;

-- Re-add foreign keys
ALTER TABLE borrow_records ADD CONSTRAINT fk_book   FOREIGN KEY (book_id)   REFERENCES books(id);
ALTER TABLE borrow_records ADD CONSTRAINT fk_member FOREIGN KEY (member_id) REFERENCES members(id);


INSERT INTO books (title, author, isbn, category, publisher, published_year, total_copies, available_copies, description) VALUES
('Think and Grow Rich',         'Napolean Hill',      '978-158542433',  'Self-Help',    'Penguin',      1937,  10,  5,  'A timeless classic revealing 13 Principles of success.');


USE library_management;
SELECT * FROM books ORDER BY id DESC;