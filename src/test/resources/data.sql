INSERT INTO moat_user (username, password, email)
VALUES ('MATTD', 'pass', 'matt@email.com'),
       ('BOBBY', 'pass', 'bobby@email.com'),
       ('BEANIE', 'pass', 'beanie@email.com');

INSERT INTO score (score, moat_user_id)
VALUES (100, 1),
       (200, 1),
       (400, 2),
       (500, 1),
       (600, 1);

INSERT INTO moat_admin (username, password, email, verified)
-- password: 'password' hashed bcrypt strength 10.
VALUES ('ADMIN', '$2a$10$iqRvPqnXnmpsMChjx9RFReFwm9RcnSZFQQvdlqWTToFrQtOYGreFO',
        'admin@email.com', true);