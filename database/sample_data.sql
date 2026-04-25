INSERT INTO users (username, role) VALUES
('admin', 'ADMIN'),
('zahra', 'USER'),
('mariam', 'USER');

INSERT INTO books (title, author, quantity, is_deleted) VALUES
('Clean Code', 'Robert Martin', 5, 0),
('Design Patterns', 'GoF', 3, 0),
('Java Basics', 'Oracle', 10, 0);

INSERT INTO transactions (user_id, book_id, type) VALUES
(2, 1, 'BORROW'),
(2, 2, 'BORROW'),
(3, 3, 'BORROW');