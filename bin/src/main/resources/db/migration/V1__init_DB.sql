create table hibernate_sequence (next_val bigint) engine=InnoDB;
insert into hibernate_sequence values ( 1 );
insert into hibernate_sequence values ( 1 );

create table message (
	id bigint not null,
	filename varchar(255),
	tag varchar(255),
	text varchar(2048) not null,
	user_id bigint,
	primary key (id)
) engine=InnoDB;

create table user_role (
	user_id bigint not null,
	roles varchar(255)
) engine=InnoDB;

create table usr (
	id bigint not null,
	activation_code varchar(255),
	active bit not null,
	email varchar(255),
	password varchar(255) not null,
	username varchar(255) not null,
	primary key (id)
) engine=InnoDB;

ALTER TABLE message 
ADD INDEX user_id_idx (user_id ASC);

ALTER TABLE message 
ADD CONSTRAINT message_user
  FOREIGN KEY (user_id)
  REFERENCES usr (id)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;	
	
	
ALTER TABLE user_role 
ADD INDEX role_user_idx (user_id ASC);

ALTER TABLE user_role 
ADD CONSTRAINT role_user
  FOREIGN KEY (user_id)
  REFERENCES usr (id)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
