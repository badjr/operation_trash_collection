--mysql -u trash_collector -p 123 -h localhost operation_trash_collection 
create database operation_trash_collection;
use operation_trash_collection;


create table trash_reports(trash_report_id int unsigned not null auto_increment, latitude double, longitude double, station_name varchar(128), trash_can_full varchar(1), trash_on_ground varchar(1), report_date timestamp, num_reports int, primary key (trash_report_id));

grant all privileges on operation_trash_collection.* to 'trash_collector'@'localhost' identified by '123';
