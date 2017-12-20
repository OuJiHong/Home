-----创建用户表--------------
create table user(
	id int primary key auto_increment,
	name varchar(50) not null unique,
	password varchar(50) not null,
	phone varchar(50),
	age int,
	type int,
	detail varchar(500),
	create_date datetime,
	modify_date datetime
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--添加用户名唯一约束----
alter table user add constraint uq_user_name  unique (name);
