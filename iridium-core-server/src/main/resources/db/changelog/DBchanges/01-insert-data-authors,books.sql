
--liquibase formatted sql
--changeset jakeChrysler:1
INSERT INTO authors (id, name) VALUES (1, 'author 1');
INSERT INTO authors (id, name) VALUES (2, 'author 2');
INSERT INTO authors (id, name) VALUES (3, 'author 3');
INSERT INTO authors (id, name) VALUES (4, 'author 4');


INSERT INTO books (id, name) VALUES (1, 'book 1');
INSERT INTO books (id, name) VALUES (2, 'book 2');
INSERT INTO books (id, name) VALUES (3, 'book 3');
INSERT INTO books (id, name) VALUES (4, 'book 4');