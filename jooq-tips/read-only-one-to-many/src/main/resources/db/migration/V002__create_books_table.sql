CREATE TABLE books(
    id bigserial NOT NULL,
    name varchar(200) NOT NULL,
    student_id bigint NOT NULL,
    CONSTRAINT books_pk PRIMARY KEY (id),
    CONSTRAINT books_students_fk FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);