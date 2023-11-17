CREATE TABLE students(
    id bigserial NOT NULL,
    name varchar(200) NOT NULL,
    CONSTRAINT students_pk PRIMARY KEY (id)
);