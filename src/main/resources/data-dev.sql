INSERT INTO moat_user (username, password, email)
VALUES ('MATTD', 'pass', 'matt@email.com'),
       ('BOBBY', 'pass', 'bobby@email.com');

INSERT INTO score (score, moat_user_id)
VALUES (100, 1),
       (200, 1),
       (400, 2),
       (500, 1);

INSERT INTO administrator (username, password)
-- password: 'password' hashed bcrypt strength 10.
VALUES ('mattd', '$2a$10$iqRvPqnXnmpsMChjx9RFReFwm9RcnSZFQQvdlqWTToFrQtOYGreFO');