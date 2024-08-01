-- Passwords Bcrypt Strength 10:
--     MATTD = 'passw'
--     ADMIN = 'password'

INSERT INTO moat_user (username, password, email, verified, role)
VALUES ('MATTD', '$2a$10$NRjtbbiy6Q6cjwk1Pltwn.RQQOdBYoPZhRCTgSL1t4.5bfB5eg27a',
        'matt@email.com', true, 'USER'),
       ('BOBBY', '$2a$10$iqRvPqnXnmpsMChjx9RFReFwm9RcnSZFQQvdlqWTToFrQtOYGreFO',
        'bobby@email.com', true, 'USER'),
       ('BEANIE',
        '$2a$10$iqRvPqnXnmpsMChjx9RFReFwm9RcnSZFQQvdlqWTToFrQtOYGreFO',
        'beanie@email.com', true, 'USER'),
       ('ADMIN', '$2a$10$iqRvPqnXnmpsMChjx9RFReFwm9RcnSZFQQvdlqWTToFrQtOYGreFO',
        'admin@email.com', true, 'ADMIN');

INSERT INTO score (score, moat_user_id)
VALUES (100, 1),
       (200, 1),
       (400, 2),
       (500, 1),
       (600, 1);