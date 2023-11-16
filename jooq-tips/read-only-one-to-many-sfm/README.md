# jOOQ Tips: Implementing a Read-Only One-to-Many Relationship

This example demonstrates how we can implement one-to-many relationship
when we are reading information from the database with jOOQ.

The database of our example has two tables:

The `students` table contains the information of students. This table has two 
columns:

* The `id` column contains the id of the student.
* The `name` column contains the full name of the student.

The `books` table contains the information of books which are owned by the students
found from the `students` table. This database table has three columns:

* The `id` column contains the id of the book.
* The `name` column contains the name of the book.
* the `student_id` column contains the id of the student
  who owns the book.
  
The `net.petrikainulainen.jooqtips.student` package contains the `StudentRepository` 
class which has two methods:

* The `findAll()` method returns the information of all students found from
  the database.
* The `findById()` method returns the information of the specified student.  
  

You can run integration tests by running the following command at command prompt:

        mvn clean verify
        
