insert into users (first_name, login, password) values ('Vasilii', 'vas', 'vasPass1');
insert into users (first_name, login, password) values ('Peter', 'pet', 'petPass2');
insert into users (first_name, login, password) values ('Victor', 'vic', 'vicPass3');

insert into authorities (id, role) values (1, 'Operator');
insert into authorities (id, role) values (2, 'Analytic');
insert into authorities (id, role) values (3, 'Admin');

insert into users_authorities (users_login, roles_id) values ('vas', 1);
insert into users_authorities (users_login, roles_id) values ('vas', 3);
insert into users_authorities (users_login, roles_id) values ('pet', 1);
insert into users_authorities (users_login, roles_id) values ('pet', 2);
insert into users_authorities (users_login, roles_id) values ('vic', 3);
