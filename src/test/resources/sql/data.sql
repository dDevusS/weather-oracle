-- inserting users
INSERT INTO users (id, login, password)
VALUES (1, 'user1', 'password1'),
       (2, 'user2', 'password2'),
       (3, 'user3', 'password3'),
       (4, 'user4', 'password4'),
       (5, 'user5', 'password5');
SELECT SETVAL('users_id_seq', (SELECT MAX(id) from users));

-- inserting locations
INSERT INTO locations (id, name, user_id, latitude, longitude)
VALUES (1, 'Location1_User1', 1, 40.7128, -74.0060),
       (2, 'Location2_User1', 1, 34.0522, -118.2437),
       (3, 'Location1_User2', 2, 51.5074, -0.1278),
       (4, 'Location2_User2', 2, 48.8566, 2.3522),
       (5, 'Location1_User3', 3, 35.6895, 139.6917),
       (6, 'Location2_User3', 3, 55.7558, 37.6173),
       (7, 'Location1_User4', 4, -33.8688, 151.2093),
       (8, 'Location2_User4', 4, -37.8136, 144.9631),
       (9, 'Location1_User5', 5, 52.5200, 13.4050),
       (10, 'Location2_User5', 5, 40.4168, -3.7038);
SELECT SETVAL('locations_id_seq', (SELECT MAX(id) from locations));
