
Welcome to the wonderful world of containerized installation!

Using Docker, all the fiddly setup and installation details can be coded into scripts so that people like you looking to set up an instance of the software don't have to worry about the database, server, Java installation, and all the hokey prerequisites that go along with it.  Just copy the files, open docker, run the docker-compose, and your instance is ready to be accessed.  



The entry point is the docker-compose.yml YAML file, which defines two containers and all of their interfaces that are exposed to the outside world and to each other.  These files call gemp_app.Dockerfile and gemp_db.Dockerfile which are concerned with actually constructing the environments on these two containers. 

gemp_db is straightforward: it's a bare-bones linux instance using the official MariaDB docker image (MariaDB is a variant of MySQL).  It hosts the gemp database and doesn't do anything else.  

gemp_app is slightly more complicated.  Gemp is a Java server, is built using Maven, and utilizes Apache to serve http.  All of those have to be installed and available in the arrangement that Gemp expects, so this image starts with the Alpine Linux image (which includes Apache) and frankensteins the rest together to get both Maven and Java installed properly.





1- install Docker (Docker Desktop if on Windows).  If you are on Windows, make sure that when you install it you check all boxes that have Docker behave like it's on Linux.  If you're installing this on Linux, I assume you know more than I do about how to set it up properly.

2- install your container manager of choice.  I would HIGHLY recommend portainer.io, which itself runs in a docker container and exposes itself as an interactable web page.  This will give you a graphical readout of all your currently running containers, registered images, networks, volumes, and just about anything else you might want, PLUS have interactive command lines for when the GUI just doesn't cut it.  The manager that comes with Docker Desktop by default is pretty much only just barely enough to run portainer with, so don't bother with it otherwise.

3- pull the git repository down to your host machine; you may have already done this.

4- navigate to gemp-lotr/gemp-lotr/docker.  Open up docker-compose.yml and change the defaults to suit your needs:

	4A- Note all the relative paths under each volume/source: these are all paths on your host system.  If you want e.g. the database to be in a different location than what's listed, alter these relative paths to something else on your host system.

	4B- under db/environment, note all of the username/password fields.  If you are hosting this for something other than personal development, be sure to change all of these to something else.
	
	4C- note the two "published" ports: 1337 for the app, and 3307 for the db.  These are the ports that you will be accessing the site with (and the db if you connect with a database manager). If you are hosting this for something other than personal development, consider changing these to something else.  DO NOT change the "target" ports.
	
5- If you changed SQL credentials in step 4, navigate to gemp-lotr/gemp-lotr/gemp-lotr-commond/src/main/resources/ and open gemp-lotr.properties:
	
	5A- DO NOT CHANGE the ports here.  These ports listed are the "target" ports in step 4C, which you didn't edit because you followed the big "DO NOT" imperative.
	
	5B- edit the db.connection.username and db.connection.password items to match the credentials you set in step 4B.
	
	5C- note the origin.allowed.pattern.  It is set to allow all connections, but if you are hosting this for something other than personal development, consider changing this to match your DNS hostname exactly.

6- open a command line and navigate to gemp-lotr/gemp-lotr/docker.  Run the following command:

	docker-compose up -d
	
You should see "Starting gemp_app....done" and "Starting gemp_db....done" at the very end.  This process will take a while the first time you do it, and will be near instantaneous every time after.

7- the database should have automatically created the gemp databases that are needed.  You can verify this by connecting to the database on your host machine with your DB manager of choice (I recommend DBeaver).  It is exposed on localhost:3307 (unless you changed this port in step 4C) and uses the user/pass of gempuser/gemppassword (unless you changed this in step 4B).  If you can see the gemp_db database with league_participation and other tables, you're golden.  

8- Now we need to compile the gemp code.  To do this, open a command line session in the gemp_app container (if using portainer.io, then log in, select your 'Local' endpoint, click the Containers tab on the left, click the >_ icon next to gemp_app, then click the Connect button).

	Navigate to the gemp codebase (which is bound to the path on your host machine that you edited in step 5):

		cd etc/gemp-lotr

	Now tell Maven to compile the project:

		mvn install
		
	This process will take upwards of 5-10 minutes.  You should see a green "BUILD SUCCESS" when it is successfully done.  In portainer.io or another rich command line context, you should see lots of red text if it failed.

9- open gemp-lotr/gemp-lotr/docker/docker-compose.yml again, and uncomment line 35 (the "command" line).  This will ensure that the container runs the gemp server every time it is started automatically.

10- Navigate in a command line on your host machine to gemp-lotr/gemp-lotr/docker and run the following commands:

		docker-compose down
		docker-compose up -d
	
11- if all has gone as planned, you should now be able to navigate to your own personal instance of Gemp.  Open your browser of choice and navigate to http://localhost:1337/gemp-lotr/ .  (If you need a different port to be bound to, then repeat step 5 and edit the exposed port.)

If you're presented with the home page, register a new user and log in. It's possible for the login page to present but login itself to fail if configured incorrectly, so don't celebrate until you see the (empty) lobby.  If you get that far, then congrats, you now have a working local version of Gemp.


--------------


At this point, editing the code is a matter of changing the files on your local machine and re-running step 8.  As you have seen how long a process this is, choose carefully what you edit.


