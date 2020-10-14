delete from message;

insert into message(id, text, tag, user_id) values
(1, 'first', 'my-tag', 1),
(2, 'second', 'more', 1),
(3, 'third', 'my-tag', 1),
(4, 'fourth', 'another', 2);

delete FROM hibernate_sequence;
insert into hibernate_sequence(next_val) values (7);
insert into hibernate_sequence(next_val) values (7);