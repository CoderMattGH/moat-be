INSERT INTO moat_user (username, password, email, verified, role)
VALUES ('MATTD', '$2a$10$iqRvPqnXnmpsMChjx9RFReFwm9RcnSZFQQvdlqWTToFrQtOYGreFO',
        'matt@email.com', true, 'USER'),
       ('BOBBY', '$2a$10$iqRvPqnXnmpsMChjx9RFReFwm9RcnSZFQQvdlqWTToFrQtOYGreFO',
        'bobby@email.com', true, 'USER'),
       ('ADMIN', '$2a$10$iqRvPqnXnmpsMChjx9RFReFwm9RcnSZFQQvdlqWTToFrQtOYGreFO',
        'admin@email.com', true, 'ADMIN');

INSERT INTO score (score, moat_user_id, hits, not_hits, misses)
VALUES (100, 1, 40, 10, 3),
       (200, 1, 30, 20, 2),
       (400, 2, 20, 30, 4),
       (500, 1, 25, 25, 7);