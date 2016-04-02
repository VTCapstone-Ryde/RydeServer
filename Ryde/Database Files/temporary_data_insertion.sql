/* 
 * Created by Cameron Gibson on 2016.03.28  * 
 * Copyright © 2016 Cameron Gibson. All rights reserved. * 
 */
/**
 * Author:  cameron
 * Created: Mar 28, 2016
 */

INSERT INTO User_Table (first_name, last_name, fb_tok, phone_number, driver_status, car_make, car_model, car_color) VALUES 
('John', 'Johnson', 'JohnFBTok', 'JohnPhone', '0', 'mazda', '3', 'blue'),
('Jim', 'Jackson', 'JimFBTok', 'JimPhone', '0', 'nissan', 'ultima', 'black'),
('Jake', 'Jundson', 'JakeFBTok', 'JakePhone', '1', 'honda', 'civic', 'black'),
('James', 'Jordan', 'JamesFBTok', 'JamesPhone', '0', 'honda', 'civic', 'blue');

INSERT INTO Group_Table (title, description) VALUES 
('Cool', 'Coolest kids'),
('Not Cool', 'Lame kids');

INSERT INTO Timeslot_Table (passcode, start_time, end_time) VALUES 
('123', '2016-03-15 15:00:00', '2016-03-15 17:00:00'),
('456', CAST(N'2016-03-15 13:00:00' AS DATETIME), CAST(N'2016-03-15 19:00:00' AS DATETIME));

INSERT INTO GroupUser (user_id, group_id) VALUES 
('1', '1'),
('2', '2');

INSERT INTO TimeslotUser (user_id, ts_id, driver) VALUES 
('1', '1', '1'),
('4', '1', '0'),
('2', '2', '1'),
('3', '2', '1'),
('4', '2', '0');

INSERT INTO GroupUser (user_id, group_id, admin) VALUES 
('1', '1', '0'),
('4', '1', '1'),
('2', '2', '1'),
('3', '2', '0'),
('4', '2', '0');