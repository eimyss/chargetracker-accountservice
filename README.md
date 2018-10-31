# micro-expenses

# account && project service
 
This service used for account and project management.   
Basically it is a glorified CRUD with some messaging. 

This service is a part of ${insert.name.here} conglomerate. 
There are currently 5 Services (account - this, expenses, processing, gateway, frontend).  
In Addition it is backed by ELK, MariaDB, RabbitMQ and Keycloak. 
It is monitored by Prometheus (not public Yet).

Trello cards: https://trello.com/b/qmE6t9eQ/expenses-microservice  

It is intended to use as a docker container (later Kubernetes / Openshift)


## Communication

 * HTTP REST (json) 
 * Messaging (Rabbit) - async

## functionality

* projects (for android app bookings and webapp)
* actuator (monitoring / info)
* accounts for expenses (web app)

## Technology used
 
+ Spring (quite a lot)
+ eureka / ribbon / feign
+ messaging (rabbitmq)
+ jackson (for parsing json, mapping dtos, etc)
+ keycloack (for auth)
+ mvn docker plugin
+ mysql / h2 (for saving stuff)
+ prometheus and Zipkin clients (for tracing and monitoring)
+ swagger (for endpoint desc) 
+ logstash (for elk logging)


## TODO: 
* Pending diagrams and architecture
* multi tenancy 


# LICENCE 

* MIT
