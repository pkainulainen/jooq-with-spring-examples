Introduction
==============

This is an example application of my blog posts:

* [Using jOOQ with Spring: CRUD](http://www.petrikainulainen.net/programming/jooq/using-jooq-with-spring-crud/)
* [Using jOOQ with Spring: Sorting and Pagination](http://www.petrikainulainen.net/programming/jooq/using-jooq-with-spring-sorting-and-pagination/)

This application has two parts:

* The first part is the REST API provided by the Spring powered web application.
* The second part is the single page application which uses AngularJS.

Prerequisites
===============

You need to install the following tools in order to run this application:

Backend
---------

* [JDK 7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)
* [Maven 3](http://maven.apache.org/)

Frontend
----------

* [Node.js](http://nodejs.org/)
* [NPM](https://www.npmjs.org/)
* [Bower](http://bower.io/)
* [Gulp](http://gulpjs.com/)

You can install these tools by following these steps:

1.  Install Node.js by using a [downloaded binary](http://nodejs.org/download/) or a [package manager](https://github.com/joyent/node/wiki/Installing-Node.js-via-package-manager).
    You can also read this blog post: [How to install Node.js and NPM](http://blog.nodeknockout.com/post/65463770933/how-to-install-node-js-and-npm)

2.  Install Bower by using the following command:

        npm install -g bower

3. Install Gulp by using the following command:

        npm install -g gulp

Running the Application
=========================

After you have installed the tools that are required the build the application, you can run the application by invoking
the following command on command prompt:

        mvn jetty:run -P dev

Running the Tests
===================

1.  You can run unit tests by using the following command:

        mvn clean test -P dev

2.  You can run integration tests by using the following command:

        mvn clean verify -P integration-test

Credits
=========

* Kyösti Herrala. The Gulp build script and its Maven integration are based on Kyösti's ideas.