-- Tárgyalók
INSERT INTO meeting_rooms (name, location, capacity) VALUES
                                                         ('Nagy tárgyaló', '2. emelet', 10),
                                                         ('Kis tárgyaló', '1. emelet', 6),
                                                         ('Online tárgyaló', 'Videó', 20),
                                                         ('VIP tárgyaló', 'Földszint', 15),
                                                         ('Átrium', 'Külső', 30);


-- Több foglalás
INSERT INTO bookings (room_id, "from", "to", title, created_at) VALUES
                                                                    (1, '2026-03-06T09:00:00', '2026-03-06T10:00:00', 'Standup', CURRENT_TIMESTAMP),
                                                                    (1, '2026-03-06T14:00:00', '2026-03-06T15:30:00', 'Sprint Review', CURRENT_TIMESTAMP),
                                                                    (2, '2026-03-06T10:30:00', '2026-03-06T11:30:00', '1:1', CURRENT_TIMESTAMP);