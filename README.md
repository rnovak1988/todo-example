# todo-example
This is an example of a todo application, written in Java with Spring-WebMVC, Spring-Security, AngularJS, and Bootstrap

Installation of this application requires the following software:
* NGINX
* Apache Tomcat v8
* Java 8

Building of this application requires:
* Java 8 SDK
* Apache Maven

To install this onto a server which you control you must first build the application .WAR file:

     git clone git@github.com:rnovak1988/todo-example.git && cd todo-example
     mvn clean install


The file for install is located at <pre>./target/todo.war</pre>

After deploying this in a tomcat container, you can link/copy the nginx-conf/production.conf file to the appropriate directory, customize according to your own setup, and then reload nginx

After that, it should be viewable at the domain specified in the ngix server configuration block
