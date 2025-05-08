INSERT INTO class_types (id, name) VALUES (1, 'COURSE') ON CONFLICT DO NOTHING;
INSERT INTO class_types (id, name) VALUES (2, 'LABORATORY') ON CONFLICT DO NOTHING;
INSERT INTO class_types (id, name) VALUES (3, 'SEMINAR') ON CONFLICT DO NOTHING;

INSERT INTO teachers (name)
VALUES
    ('Alboaie Lenuța'),
    ('Arusoaie Andrei'),
    ('Arusoaie Andreea-Valentina'),
    ('Balan Gheorghe'),
    ('Breabăn Mihaela'),
    ('Buraga Sabin Corneliu'),
    ('Captarencu Oana'),
    ('Ciobanu Sebastian'),
    ('Ciortuz Liviu'),
    ('Forăscu Corina'),
    ('Frăsinaru Cristian')
;

INSERT INTO disciplines (name)
VALUES
    ('Programare concurentă şi distribuită'),
    ('Programare Funcțională'),
    ('Probabilitati si statistica'),
    ('Calcul Numeric'),
    ('Programare orientata-obiect'),
    ('Sisteme de Operare'),
    ('Data Mining'),
    ('Tehnologii Web'),
    ('Modelarea Sistemelor Distribuite'),
    ('Retele Petri si Aplicatii'),
    ('Capitole Speciale de Invatare Automata'),
    ('Proiectarea algoritmilor'),
    ('Antreprenoriat şi inovare în IT'),
    ('Programare avansată')
;

INSERT INTO discipline_allocations (discipline_id, teacher_id, class_type_id, hours_per_week)
VALUES
    (1, 1, 1, 4),
    (2, 2, 2, 2),
    (3, 3, 3, 3),
    (4, 1, 1, 5),
    (5, 2, 2, 3)
;

INSERT INTO groups (number, semiyear)
VALUES
    (1, '1A'), (2, '1A'),
    (1, '1B'), (2, '1B'),
    (1, '2A'), (2, '2A'),
    (1, '2B'), (2, '2B'),
    (1, '3A'), (2, '3A'),
    (1, '3B'), (2, '3B')
;

INSERT INTO rooms (name, capacity, room_type_id)
VALUES
    ('C112', 70, 1), ('C2', 150, 1), ('C308', 40, 1), ('C309', 70, 1),
    ('C901', 25, 2), ('C903', 25, 2), ('C905', 25, 2), ('C909', 25, 2),
    ('C401', 20, 3), ('C403', 20, 3), ('C405', 20, 3), ('C409', 20, 3), ('C411', 20, 3), ('C412', 20, 3), ('C413', 20, 3)
;

INSERT INTO time_slots (day_of_week, start_time, end_time)
VALUES
    ('Monday', '08:00:00', '10:00:00'), ('Monday', '10:00:00', '12:00:00'), ('Monday', '12:00:00', '14:00:00'), ('Monday', '14:00:00', '16:00:00'), ('Monday', '16:00:00', '18:00:00'), ('Monday', '18:00:00', '20:00:00'),
    ('Tuesday', '08:00:00', '10:00:00'), ('Tuesday', '10:00:00', '12:00:00'), ('Tuesday', '12:00:00', '14:00:00'), ('Tuesday', '14:00:00', '16:00:00'), ('Tuesday', '16:00:00', '18:00:00'), ('Tuesday', '18:00:00', '20:00:00'),
    ('Wednesday', '08:00:00', '10:00:00'), ('Wednesday', '10:00:00', '12:00:00'), ('Wednesday', '12:00:00', '14:00:00'), ('Wednesday', '14:00:00', '16:00:00'), ('Wednesday', '16:00:00', '18:00:00'), ('Wednesday', '18:00:00', '20:00:00'),
    ('Thursday', '08:00:00', '10:00:00'), ('Thursday', '10:00:00', '12:00:00'), ('Thursday', '12:00:00', '14:00:00'), ('Thursday', '14:00:00', '16:00:00'), ('Thursday', '16:00:00', '18:00:00'), ('Thursday', '18:00:00', '20:00:00'),
    ('Friday', '08:00:00', '10:00:00'), ('Friday', '10:00:00', '12:00:00'), ('Friday', '12:00:00', '14:00:00'), ('Friday', '14:00:00', '16:00:00'), ('Friday', '16:00:00', '18:00:00'), ('Friday', '18:00:00', '20:00:00')
;