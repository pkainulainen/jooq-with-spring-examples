# jOOQ Tips: Implementing a Read-Only One-to-Many Relationship

This is a sample application of a blog post that describes how we can implement one-to-many relationship when we are 
reading information from the database with jOOQ. After we have finished this blog post, we:

* Understand how we can build a query that fetches the nested collection by using a multiset value constructor.
* Can map the query results into returned objects without using extra dependencies.

## The Database of the Sample Application

The database of this sample application has two tables:

**1.** The `students` table contains the information of students. This table has two columns:

* The `id` column contains the id of the student.
* The `name` column contains the full name of the student.

**2.** The `books` table contains the information of books which are owned by the students found from the `students` 
table. This database table has three columns:

* The `id` column contains the id of the book.
* The `name` column contains the name of the book.
* the `student_id` column contains the id of the student
  who owns the book.

## The Sample Application

The `net.petrikainulainen.jooqtips.student` package contains the `StudentRepository` class which has two methods:

* The `findAll()` method returns a list which contains all students found from the database.
* The `findById()` method returns an `Optional` object that contains the information of the requested student. If the
  requested student isn't found from the database, this method returns an empty `Optional` object.

## Running Tests

You can run integration tests by running the following command at command prompt:

        mvn clean test

## Credits

The Maven build of this example project is based on this excellent blog post by Lukas Eder:
[Using Testcontainers to Generate jOOQ Code](https://blog.jooq.org/using-testcontainers-to-generate-jooq-code/).