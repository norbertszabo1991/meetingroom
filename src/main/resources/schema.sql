CREATE TABLE IF NOT EXISTS meeting_rooms (
                                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             name VARCHAR(255) NOT NULL,
                                             location VARCHAR(255),
                                             capacity INTEGER NOT NULL
);


CREATE TABLE IF NOT EXISTS bookings (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          room_id BIGINT NOT NULL,
                          "from" TIMESTAMP NOT NULL,
                          "to" TIMESTAMP NOT NULL,
                          title VARCHAR(255),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (room_id) REFERENCES meeting_rooms(id)
);
