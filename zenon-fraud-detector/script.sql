create table transactions (
	id bigint auto_increment primary key,
	step int not null,
	type varchar(20) not null,
	amount decimal(20,2) not null,
	name_origin varchar(50) not null,
	old_balance_origin decimal(20,2) not null,
	new_balance_origin decimal(20,2) not null,
	name_recipient varchar(50) not null,
	old_balance_recipient decimal(20,2) not null,
	new_balance_recipient decimal(20,2) not null,
	is_fraud tinyint(1) default 0,
	is_flagged_fraud tinyint(1) default 0
);