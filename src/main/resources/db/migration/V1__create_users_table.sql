create table users (
   active bit not null,
   id bigint not null auto_increment,
   email varchar(100) not null,
   password varchar(255) not null,
   primary key (id)
) engine=InnoDB;

alter table users
    add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);