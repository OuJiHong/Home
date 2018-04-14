-----创建用户表--------------
create table user(
	id int primary key auto_increment comment '主键',
	name varchar(50) not null unique comment '姓名',
	password varchar(50) not null comment '密码',
	phone varchar(50) comment '手机号',
	age int comment '年龄',
	type int comment '类型：0普通用户  1管理员',
	detail varchar(500) comment '附录',
	create_date datetime comment '创建时间',
	modify_date datetime comment '修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--添加用户名唯一约束----
alter table user add constraint uq_user_name  unique (name);
