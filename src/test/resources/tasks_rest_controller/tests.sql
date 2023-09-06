
insert into application_user(id, username, password)
values ('5c3f7277-ccda-4de9-a0f8-d92edcb70b0c', 'user', '{noop}password');

insert into task(id, details, completed, id_application_user)
values('5c3f7277-ccda-4de9-a0f8-d92edcb70b0c', 'first task', false, '5c3f7277-ccda-4de9-a0f8-d92edcb70b0c');

