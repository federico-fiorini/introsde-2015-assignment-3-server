# IntroSDE Assignment #3: SOAP Web Services - Server
**Introduction to Service Design and Engineering - University of Trento**

--------------

A SOAP web service in Java to manage people and their health status.

Using JAX-WS to implement CRUD services.

Project based the following [requirements](https://sites.google.com/a/unitn.it/introsde_2015-16/lab-sessions/assignments/assignment-3).


####IMPLEMENTATION

Endpoint publisher:

- `introsde.assignment.soap.endpoint`: contains the main class to be run to start the people service and publish the endpoint

Main packages:

- `introsde.assignment.soap.dao`: contains the enum class that manages the connection to the database
- `introsde.assignment.soap.model`: contains the model classes that map to the database tables
- `introsde.assignment.soap.ws`: contains the classes that implement the CRUD services


####HOW TO RUN IT
	# Clone the code from this repo
	git clone https://github.com/federico-fiorini/introsde-2015-assignment-3-server.git
	cd introsde-2015-assignment-3-server
	
	# To run the server
	ant run


######Deployed on heroku server:
	[https://warm-brook-6204.herokuapp.com/ws/people](https://warm-brook-6204.herokuapp.com/ws/people)

######Client part on github:
	[https://github.com/federico-fiorini/introsde-2015-assignment-3-client](https://github.com/federico-fiorini/introsde-2015-assignment-3-client)
