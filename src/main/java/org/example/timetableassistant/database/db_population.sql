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

