INSERT INTO roles(role)
VALUES ('ADMIN'),
       ('USER');

INSERT INTO sports(name, description, sport_image_url)
VALUES ('Crossfit', 'CrossFit is a high-intensity fitness program that combines elements of weightlifting, cardiovascular exercise, and gymnastics. Participants engage in constantly varied and functional movements, performed at a high intensity. Workouts often include a mix of exercises like weightlifting, running, rowing, and bodyweight movements, promoting overall fitness and strength.',
        '/images/sports/crossfit.jpg'),

       ('Kangoo Jumps', 'Kangoo Jumps is a unique and energetic fitness class that involves wearing rebound shoes with springs. Participants perform various aerobic and dance movements while enjoying the benefits of reduced impact on joints due to the shock-absorbing technology in the shoes. It is a fun and effectives way to improve cardiovascular fitness, balance, and coordination.',
        '/images/sports/kangoo.jpg'),

       ('Tabata', 'Tabata training is a high-intensity interval training (HIIT) method characterized by short, intense bursts of exercise followed by brief periods of rest. Typically lasting four minutes, a Tabata workout consists of eight rounds of 20 seconds of intense exercise followed by 10 seconds of rest. This format, scientifically proven to boost cardiovascular fitness and metabolic rate, can involve various exercises, providing a quick yet effective workout.',
        '/images/sports/tabata.jpg');

INSERT INTO instructors(first_name, last_name, email, phone_number, picture_url, bio, sport_id)
VALUES ('Ivan', 'Petrov', 'ivan.petrov@email.com', '+359876543210',
        '/images/instructors/team-1.jpg',
        'Bio Ivan', 1),

       ('Mariya', 'Ivanova', 'mariya.ivanova@email.com', '+359888765432',
        '/images/instructors/team-2.jpg',
        'Bio Mariya', 2),

       ('Georgi', 'Dimitrov', 'georgi.dimitrov@email.com', '+359897654321',
        '/images/instructors/team-3.jpg',
        'Bio Georgi', 3),

       ('Stanimira', 'Ilieva', 'stani.ilieva@email.com', '+359895784321',
        '/images/instructors/team-4.jpg',
        'Bio Stanimira', 1);


INSERT INTO sport_classes(sport_id, instructor_id, max_capacity, current_capacity, day_of_week, start_time, end_time)
VALUES (1, 1, 2, 1, 'MONDAY', '19:00:00', '20:00:00'),
       (1, 1, 2, 0, 'TUESDAY', '19:00:00', '20:00:00'),
       (2, 2, 20, 1, 'TUESDAY', '18:00:00', '19:00:00'),
       (2, 2, 20, 0, 'FRIDAY', '18:00:00', '19:00:00'),
       (2, 2, 20, 0, 'SUNDAY', '10:00:00', '11:00:00'),
       (3, 3, 20, 0, 'TUESDAY', '16:00:00', '17:00:00'),
       (1, 4, 20, 0, 'MONDAY', '14:00:00', '15:00:00'),
       (1, 4, 2, 0, 'WEDNESDAY', '17:00:00', '18:00:00'),
       (1, 4, 20, 0, 'FRIDAY', '14:00:00', '15:00:00');

INSERT INTO users(email, first_name, last_name, password, username, profile_picture_url)
VALUES ('anton@anton.com', 'Anton', 'Goranov',
        '$2a$10$IV/H.EI3PdfYpIxicMMbAOLe1MedkDwEUytHvFwEUoVbcV1.CoGw2', 'agoranov', '/images/users/admin.jpg'),

       ('user@gmail.com', 'User', 'Userov',
        '$2a$10$seonkCywjMOsOcnvHpdFv.0xfXAHkGwOhTxrYrXwQGbh0uCttWcyC', 'user', '/images/users/user.jpeg'),

       ('stela@gmail.com', 'Stela', 'Ivanova',
        '$2a$10$seonkCywjMOsOcnvHpdFv.0xfXAHkGwOhTxrYrXwQGbh0uCttWcyC', 'stela', NULL);

INSERT INTO users_roles(user_id, role_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (3, 2);

INSERT INTO bookings(user_id, sport_class_id, status)
VALUES (2, 1, 'ACTIVE'),
       (3, 3, 'ACTIVE');

INSERT INTO qr_codes(booking_id, qr_img)
VALUES (1, 0x89504E470D0A1A0A0000000D494844520000015E0000015E0100000000E5504AB7000001BA49444154785EEDD3516EC4200C04506EE2FBDF8A83544A3D33C0BAFB5155C5FDEA380918FB391F1BED787E1E1FE3BDF24D18D730AE615CC3B886710DE31AFF05CFC188CC62864A315178758C1B300F80CC72C1BE9FBD1ADF637C81D3C0CD23C74FC7B80B73CD27EDCA8CFF023FAC4E653B376EC47BE43595F99E587DE36B3C1881E6DBB53AC61DF8C4441D0BBE0BD712C6B5F91B9C6D80BD2892D2E3326EC18F4AA7C754EFC0B6B4F13D46598C8733F4A2C62D38F2568EFEBE31C651E30E3CF3781AF97CB1E71DC6B738CBA89E06ADDE1068AA6E7C8D579F435C6B059B710FCE1F1E1F05EBA3EF4172FE0DC65D5845208DA0C4A3C68C1B30280B5C27E6CAF0D6C61D985DF4D7E864CA5DD6B803A3BE773695AF77501BDFE20705D69400280F0E2A8C6FF10C5CD875C2B44ABCF360DC85D91F344308640F19B761ECE8732516E41FC1B8074388020DF6E990E94DC6D77887387B92B15F84AEF12D461DAD140116681333E5B47103E6892B7A4766A2CCB8094FF416C38127261CA036EEC368A9C48135A892711BCE8B4754E24C2C6D7C8FF708187330965834EEC18391F5812E768E6970CD1B5FE39F85710DE31AC6358C6B18D730AE615CE3E313246CC36D446893B30000000049454E44AE426082),
       (2, 0x89504E470D0A1A0A0000000D494844520000015E0000015E0100000000E5504AB7000002A849444154785EED99B175C3300C44A197226546D0281A4D1ACDA37004972EFCCCE00EB40C5B64E2972AC5A1302DE0CBC5050440C6EAFB76B557CF0F26389BE06C82B309CE26389BE06C82B3FD05BE18EC03CB6AF6799E9B73A937468212DC83BFFCE3F6E11F2703EC1F13E18DCEBA091EC3C5CCFDE7F9B41ADF599BEA372C8BE05FE15A3CCC14DD98A92713FC3E6CD39594EBFC5505BF0157EEEE96A2288C4D6EFCC261770B4EB0C15CD98252F8B4301294E00EDCEC624DD9823072334705E7E84367D640E4E60659E768CE25B6FC2145053FC1E1284BA3560FC17CF14C2524B80FDBB2C5E08C143DFBD3040AEFB0C3081EC3EE6078D99B89E169A3DC0BFF06827B309312ED63AEB5CDCF80EB3E0B0A1EC3EEF76CE4D2CC55AFA038E534CFFD9BE007FC3CFDF1DC8BA7989F5F75169C602F8C866FA888FB2CC808F2F6500A0467D8B5C446C63BC8D45DE0E8308404F7E08A5258A1B37199919438F0C6551F638207B0F7618B066C14789D3804FAF2A896823B30259D42670C34E1B4996BA82EB80FA399A014221B71977C4F514C32164F82FB30BA484552FA3BD37D5B1B8E1EE10C13DC83E160FB8814453BB64F1FAA0D637439B463C129456DC101CD05366CF29921C81DBF1094E03E6C2B9A89E1361EB90981D15ADAF157F008E67EAE68C0DE95637EC62C5820F085B72D824730248D11A6E9BC3FF1878212DC837707E47681A330A21D47B514FC13CC8DEC29CA6D8D10AFA69A091EC1CD581893B2D8E4702E1116DC819FA82BAF0C2C52B4FD0352F010C6398D43CB821308AF5998B0D4793E1446C1092E3EBBA08BA014B24C7270A6DE700AFE154661749D0B66414C32ECCAF65A45051FE04851633B6E034D8D53B0E0315CB1BBEF3AE3B6056EEA4CD5050F61A421B675C1F528CE697875BFA17AD1597082DF33C1D90467139C4D7036C1D90467139CEDBFC0DF68A9432FFD8306A70000000049454E44AE426082);


