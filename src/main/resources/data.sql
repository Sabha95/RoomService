INSERT INTO rooms
(room_number, user_id, bin_status, common_area_clean_status,bin_Count)
VALUES
    (1, 3, 'True', 'True',0),
    (1, 4, 'False', 'False',0),
    (2, 1, 'False', 'False',0),
    (2, 2, 'False', 'False',0),
    (3, 5, 'False', 'False',0),
    (3, 6, 'False', 'False',0);


INSERT INTO User_Management
(username, password, email)
VALUES
    ('Bhakti', 'Bhakti19', 'alice@example.com',''),
    ('Asmita', 'Asmita94', 'bob@example.com',''),
    ('Shreyas', 'Shreyas123', 'charlie@example.com',''),
    ('Atharva', 'Atharva123', 'charlie@example.com',''),
    ('Krupa', 'Krupa25', 'charlie@example.com',''),
    ('Varsha', 'Varsha22', 'charlie@example.com','');

List<UserManagement> users = new ArrayList<>();

        users.add(new UserManagement(null, "Bhakti", "Bhakti19", "alice@example.com", "918828173732"));
        users.add(new UserManagement(null, "Asmita", "Asmita94", "bob@example.com", "918898346696"));
        users.add(new UserManagement(null, "Shreyas", "Shreyas123", "charlie@example.com", "918655537642"));
        users.add(new UserManagement(null, "Atharva", "Atharva123", "charlie@example.com", "919503443228"));
        users.add(new UserManagement(null, "Krupa", "Krupa25", "charlie@example.com", "919742561999"));
        users.add(new UserManagement(null, "Varsha", "Varsha22", "charlie@example.com", "919880623282"));









