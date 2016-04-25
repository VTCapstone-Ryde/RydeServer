/* 
 * Created by Cameron Gibson on 2016.03.28  * 
 * Copyright © 2016 Cameron Gibson. All rights reserved. * 
 */
/**
 * Author:  cameron
 * Created: Mar 28, 2016
 */
INSERT INTO User_Table (first_name, last_name, fb_tok, fb_id, phone_number, driver_status, car_make, car_model, car_color) VALUES 
('John', 'Johnson', 'JohnFBTok', 'JohnFBID', '(703)827-0912', '0', 'mazda', '3', 'blue'),
('Austin', 'Adams', 'AustinFBTok', 'AustinFBID', '(571)931-1243', '0', 'nissan', 'ultima', 'black'),
('Mike', 'Mitchell', 'MikeFBTok', 'MikeFBID', '(804)319-0145', '1', 'honda', 'civic', 'black'),
('Randy', 'Randyson', 'RandyFBTok', 'RandyFBID', '(540)123-0168', '0', 'hyundai', 'accent', 'blue'),
('Larry', 'Larryson', 'LarryFBTok', 'LarryFBID', '(540)893-8901', '0', 'hyundai', 'elantra', 'silver');
INSERT INTO Group_Table (title, description, directory_path) VALUES 
('Beta Balci Beta', 'Balcis first group', 'none'),
('Alpha Osman Chi', 'Lame kids', 'none'),
('Capstone Sigma', 'Coolest kids', 'none'),
('Osman Kappa Theta', 'Cool kids', 'none'),
('CS 4704', 'Group 2 is the best', 'none'),
('Balci Kappa Theta', 'Another description', 'none'),
('No Balci Beta', 'No Balcis allowed', 'none');
INSERT INTO Timeslot_Table (passcode, start_time, end_time) VALUES 
('aaaaa', CAST(N'2016-05-15 15:00:00' AS DATETIME), CAST(N'2016-05-15 17:00:00' AS DATETIME)),
('bbbbb', CAST(N'2016-05-15 19:30:00' AS DATETIME), CAST(N'2016-05-15 19:00:00' AS DATETIME)),
('ccccc', CAST(N'2016-05-15 19:30:00' AS DATETIME), CAST(N'2016-05-15 19:00:00' AS DATETIME)),
('ddddd', CAST(N'2016-05-17 19:00:00' AS DATETIME), CAST(N'2016-05-18 02:00:00' AS DATETIME)),
('eeeee', CAST(N'2016-05-06 20:00:00' AS DATETIME), CAST(N'2016-05-06 23:59:59' AS DATETIME)),
('fffff', CAST(N'2016-04-06 20:00:00' AS DATETIME), CAST(N'2016-04-07 03:00:00' AS DATETIME));
INSERT INTO GroupUser (user_id, group_id, admin) VALUES 
('1', '1', '0'),
('4', '1', '1'),
('2', '2', '1'),
('3', '2', '1'),
('4', '2', '0'),
('4', '3', '1'),
('5', '3', '0'),
('1', '4', '1'),
('2', '4', '0'),
('3', '4', '0'),
('4', '5', '1'),
('5', '5', '1'),
('1', '5', '0'),
('2', '5', '0'),
('1', '6', '1'),
('2', '6', '0');
INSERT INTO GroupTimeslot (group_id, ts_id) VALUES 
('1', '1'),
('3', '2'),
('4', '3'),
('4', '4'),
('6', '5');
INSERT INTO TimeslotUser (user_id, ts_id, driver) VALUES 
('1', '1', '0'),
('4', '1', '1'),
('2', '1', '0'),
('3', '1', '0'),
('5', '1', '0'),
('4', '2', '0'),
('5', '2', '1'),
('1', '3', '0'),
('2', '3', '1'),
('3', '3', '0'),
('1', '4', '0'),
('2', '4', '0'),
('3', '4', '1'),
('1', '5', '0'),
('2', '5', '1');
INSERT INTO Ride (driver_user_id, rider_user_id, ts_id, start_lat, start_lon, end_lat, end_lon, active) VALUE
('4', '1', '6', '37.2297109', '-80.4185688', '37.2296314', '-80.4182590', '0'),
('4', '2', '6', '37.2297109', '-80.4185688', '37.2294114', '-80.4180194', '0'),
('5', '3', '6', '37.2297109', '-80.4185688', '37.2290174', '-80.4180564', '0'),
('3', '4', '6', '36.2297109', '-79.4185688', '36.2298752', '-79.4181234', '0'),
('1', '3', '1', '38.2127109', '-91.4185688', '38.2121597', '-91.4181678', '1'),
('2', '4', '2', '3912397109', '-78.4185688', '3912390987', '-78.4181234', '1');
INSERT INTO Event (driver_user_id, ts_id, datetime, event_type) VALUES 
('4', '6', CAST(N'2016-04-06 20:00:00' AS DATETIME), 'online'),
('4', '6', CAST(N'2016-04-06 23:00:00' AS DATETIME), 'offline'),
('4', '6', CAST(N'2016-04-06 20:30:00' AS DATETIME), 'rideCompleted'),
('4', '6', CAST(N'2016-04-06 20:53:00' AS DATETIME), 'rideCompleted'),
('3', '6', CAST(N'2016-04-06 20:58:00' AS DATETIME), 'rideCancelled'),
('4', '6', CAST(N'2016-04-06 21:00:00' AS DATETIME), 'rideCompleted'),
('4', '6', CAST(N'2016-04-06 22:17:00' AS DATETIME), 'rideCompleted');
INSERT INTO RequestUser (user_id, group_id, invite) VALUES
('2', '1'),
('3', '1'),
('3', '6'),
('4', '4'),
('1', '2');
