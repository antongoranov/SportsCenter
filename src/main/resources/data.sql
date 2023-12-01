INSERT INTO roles(role)
VALUES ('ADMIN'),
       ('USER');

INSERT INTO sports(name, description)
VALUES ('Crossfit', 'CrossFit is a high-intensity fitness program that combines elements of weightlifting, cardiovascular exercise, and gymnastics. Participants engage in constantly varied and functional movements, performed at a high intensity. Workouts often include a mix of exercises like weightlifting, running, rowing, and bodyweight movements, promoting overall fitness and strength.'),

       ('Kangoo Jumps', 'Kangoo Jumps is a unique and energetic fitness class that involves wearing rebound shoes with springs. Participants perform various aerobic and dance movements while enjoying the benefits of reduced impact on joints due to the shock-absorbing technology in the shoes. It is a fun and effectives way to improve cardiovascular fitness, balance, and coordination.'),

       ('Tabata', 'Tabata training is a high-intensity interval training (HIIT) method characterized by short, intense bursts of exercise followed by brief periods of rest. Typically lasting four minutes, a Tabata workout consists of eight rounds of 20 seconds of intense exercise followed by 10 seconds of rest. This format, scientifically proven to boost cardiovascular fitness and metabolic rate, can involve various exercises, providing a quick yet effective workout.');

INSERT INTO instructors(first_name, last_name, email, phone_number, picture_url, bio, sport_id)
VALUES ('Ivan', 'Petrov', 'ivan.petrov@email.com', '+359876543210',
        'C:\\Users\\Anton\\Desktop\\Projects\\sportscenter\\sportscenter\\src\\main\\resources\\static\\images\\instructors\\team-1.jpg',
        'Bio Ivan', 1),

       ('Mariya', 'Ivanova', 'mariya.ivanova@email.com', '+359888765432',
        'C:\\Users\\Anton\\Desktop\\Projects\\sportscenter\\sportscenter\\src\\main\\resources\\static\\images\\instructors\\team-2.jpg',
        'Bio Mariya', 2),

       ('Georgi', 'Dimitrov', 'georgi.dimitrov@email.com', '+359897654321',
        'C:\\Users\\Anton\\Desktop\\Projects\\sportscenter\\sportscenter\\src\\main\\resources\\static\\images\\instructors\\team-3.jpg',
        'Bio Georgi', 3),

       ('Stanimira', 'Ilieva', 'stani.ilieva@email.com', '+359895784321',
        'C:\\Users\\Anton\\Desktop\\Projects\\sportscenter\\sportscenter\\src\\main\\resources\\static\\images\\instructors\\team-4.jpg',
        'Bio Stanimira', 1);


INSERT INTO sport_classes(sport_id, instructor_id, max_capacity, current_capacity, day_of_week, start_time, end_time)
VALUES (1, 1, 30, 0, 'MONDAY', '19:00:00', '20:00:00'),
       (2, 2, 20, 0, 'TUESDAY', '18:00:00', '19:00:00'),
       (3, 3, 20, 0, 'TUESDAY', '16:00:00', '17:00:00'),
       (1, 4, 20, 0, 'WEDNESDAY', '14:00:00', '15:00:00');