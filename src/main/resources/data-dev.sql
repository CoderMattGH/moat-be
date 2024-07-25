INSERT INTO score (score, nickname)
VALUES (100, 'MATTD'),
       (200, 'MATTD'),
       (400, 'BOBBY'),
       (500, 'PAUL');

INSERT INTO administrator (username, password)
-- password: 'password' hashed bcrypt strength 10.
VALUES ('mattd', '$2a$10$iqRvPqnXnmpsMChjx9RFReFwm9RcnSZFQQvdlqWTToFrQtOYGreFO');