
Welcome to the wonderful world of containerized installation!

Using Docker, all the fiddly installation details can be coded into scripts so that people like you looking to set up an instance of the software don't have to worry about the database, server, Java installation, and all the hokey prerequisites that go along with it.  Just copy the files, open docker, run the docker-compose, and your instance is ready to be accessed.  

The current state of the docker files is somewhere just north of "shambles".  There were a lot of quirks that had to be figured out, and so as a result the files here are very messy, and have a *ton* of commented-out versions of the code that didn't work.  Eventually this will all be cleaned up, but for now take it as a statement of how many ways there are /not/ to create a container.

As a result, however, there are a few files that currently need to be modified with respect to the host's machine to get these to work.  Nothing complicated, just pointing to the location of where some of the subfolders of gemp's source code are. 

So let's get right into it.  The entry point is the docker-compose.yml, which defines two containers and all of their interfaces that are exposed to the outside world and to each other.  These files call gemp_app.Dockerfile and gemp_db.Dockerfile which are concerned with actually constructing the environments on these two containers. 

gemp_db is straightforward: it's a bare-bones linux instance using the official MariaDB docker image (MariaDB is a variant of MySQL, if you weren't aware).  It hosts the gemp database and doesn't do anything else.  

gemp_app is slightly more complicated.  Gemp is a Java server, is built using Maven, and utilizes Apache to serve http.  All of those have to be installed and available.  Unfortunately, Gemp expects its environment to be slightly different from what the defaults of either the official Apache or Maven images create, so this image starts with the Alpine Linux image (which includes Apache) and frankensteins the rest together to get both Maven and Java installed properly.

That's probably enough background.  Here is the checklist to actually getting it all to run:

1- install Docker (Docker Desktop if on Windows).  If you are on Windows, make sure that when you install it you check all boxes that have Docker behave more like it does on Linux.  If you're installing this on Linux, I assume you know more than I do about how to set it up properly.

2- install your container manager of choice.  I would HIGHLY recommend portainer.io, which itself runs in a docker container and exposes itself as an interactable web page.  This will give you a graphical readout of all your currently running containers, registered images, networks, volumes, and just about anything else you might want, PLUS have interactive command lines for when the GUI just doesn't cut it.  The manager that comes with Docker Desktop by default is pretty much only just barely enough to run portainer with, so don't bother with it otherwise.

3- pull the git repository down to your host machine and put it where you want the code to go.  You will not be able to easily move this later, so choose a spot you want to work with permanently.  Git repo here: https://github.com/ketura/gemp-lotr

4- navigate to gemp-lotr/gemp-lotr/docker.  There are some modifications that will need to be made before you can actually boot up the containers.

5- open up docker-compose.yml.  Every location that you see an X:/Projects/... hard-coded absolute file path you will need to replace with your own hard-coded absolute file path to the corresponding locations.  It should be possible later to modify this to be a relative path and no longer require this step, but for now absolute paths are required.  There are 4 lines that need edited, three under gemp_app and one under gemp_db.  Don't forget to save.

6- open a command line and navigate to the location you placed gemp-lotr, and navigate to gemp-lotr/gemp-lotr/docker.  Run the following command:

	docker-compose up -d
	
You should see "Starting gemp_app....done" and "Starting gemp_db....done" at the very end.  This process will take a while the first time you do it, and will be near instantaneous every time after.

7- the database should have automatically created the gemp databases that are needed.  You can verify this by connecting to the database on your host machine with your DB manager of choice (I recommend DBeaver).  It is exposed on localhost:3307 and uses the user/pass of gempuser/gemppassword.  If you can see the gemp_db database with league_participation and other tables, you're golden.  

Otherwise, run the script at gemp-lotr/gemp-lotr/database_script.sql manually in your database manager.  This should not be necessary though and is an indication of a problem if it is.

8- Now we need to compile the gemp code.  To do this, open a command line session in the gemp_app container (if using portainer.io, then log in, select your 'Local' endpoint, click the Containers tab on the left, click the >_ icon next to gemp_app, then click the Connect button).

Navigate to the gemp codebase (which is bound to the path on your host machine that you edited in step 5):

	cd etc/gemp-lotr

Now tell Maven to compile the project:

	mvn install
	
This process will take upwards of 5-10 minutes.  You should see a green "BUILD SUCCESS" when it is successfully done.  In portainer.io or another rich command line context, you should see lots of red text if it failed.

9- At this point the gemp server needs to be ran.  You could run the jar file directly in your command line session, but I find that this is sensitive to you closing that session.  Instead, we'll shut the docker container down and bring it back up, as the docker-compose is set up to run the appropriate jar command on startup and leave it up in the background.

Navigate in a command line on your host machine to gemp-lotr/gemp-lotr/docker and run the following commands:

	docker-compose down
	docker-compose up -d
	
10- if all has gone as planned, you should now be able to navigate to your own personal instance of Gemp.  Open your browser of choice and navigate to http://localhost:1337/gemp-lotr/ .  (If you need a different port to be bound to, then repeat step 5 and edit the exposed port.)

If you're presented with the home page, register a new user and log in. It's possible for the login page to present but login itself to fail if configured incorrectly, so don't celebrate until you see the (empty) lobby.  If you get that far, then congrats, you now have a working local version of Gemp.


--------------


At this point, editing the code is a matter of changing the files on your local machine and re-running step 8.  As you have seen how long a process this is, choose carefully what you edit.



The rest of this document may be safely ignored and is only in place to preserve the original documentation I received on how to get gemp to run.  The information in this post is useful but has already been incorporated into the docker files and does not need to be performed manually.


~~~~~~~~~~~~~~


Yeah, I've set up a low-key instance to test bug fixes and some new features: http://[REDACTED-IP]/gemp-lotr/

This guide is to roughly set up one of your own, but you can also submit changes to me and I'll reset afterwards. Really depends on what you want to do, I guess. I typed this up a while back for someone with less experience than you and made some changes, so it's possible some sections are way over-explained and others got edited into confusion :P

Gemp's file structure expects Apache, so if you don't already have Linus available to you somewhere (could work on a Mac I guess) you'll want to do that. Easiest way IMO would be to install a distro on a flash drive, but you seem in-the-know enough to get dual boot going if you wanted. You can download Gemp from Github, although taking the day or two to learn Git and do it from the command line will pay off.

Once you've got that, you'll need to install the Java Development Kit and some IDE or build tool. MarcinS uses Maven (and so do I), you can read the "Download, Install, Run Maven" guides here: https://maven.apache.org/. You also need to install SQL for the database. There's not much that needs to be done with this, it's kind of "set up and forget" so I've forgotten many of the details haha. MariaDB came standard on the OS I'm using, which appears to be a version of MySQL. Once you set it all up, you'll need to run "database_script.sql" in gemp-lotr/gemp-lotr/ to create the database that will store decks, user information, etc.

Now that you've got everything ready, go to gemp-lotr/gemp-lotr/gemp-lotr-common/src/main/resources/gemp-lotr.properties and change it this way (my comments are in parenthesis):


## Application root, used for example for storing replay files
application.root=/etc/gemp-lotr (Should point to wherever you installed the source files)

## DB connection
db.connection.class=org.gjt.mm.mysql.Driver
db.connection.url=jdbc:mysql://localhost/gemp-lotr
db.connection.username=gemp-lotr (whatever you set the SQL username to)
db.connection.password=gemp-lotr (whatever you set the SQL password to)
db.connection.validateQuery=/* ping */ select 1

port=80
web.path=/env/gemp-lotr/web/ (change this to wherever the /web/ folder is on your computer. I kept mine in .../gemp-lotr/gemp-lotr/gemp-lotr-async/src/main/web/, but technically it's more secure to move the web files to its own location)
card.path= .../gemp-lotr/gemp-lotr/gemp-lotr-async/src/main/web/cards/ (this line you'll need to add, since MarcinS is currently moving all of the cards from one format to another)



The stage is set, now the server has to be compiled. Open up the command line and navigate to the folder gemp-lotr/gemp-lotr/. The command "sudo mvn install" will compile all of the code of the server. This should not fail, since you haven't made any changes to the java. You should be able to start the server now. I do it from the command line, "sudo java -jar .../gemp-lotr/gemp-lotr/gemp-lotr-async/target/web.jar" (Maven will create the folder "/target/" for you). If you get the message "No appenders could be found for logger," just ignore it -- as long as that's the only error, that means it is working.

If everything worked, you can open up a browser and go to "localhost/gemp-lotr" and see the Gemp server. I've gone through this process a few times trying to get everything to work, but not recently. Let me know if you hit any snags and I'll figure out what I forgot to mention. I've also been rooting around the code for a while now, if you need help on that front. I recommend using Git to keep track of it all, I did everything on Github for a while and you don't notice how much extra work you're doing until you stop doing it. Git makes it so much easier, if you're planning on sticking with this for any length of time I'd advise you (again) to figure that out. It's really not too hard, I just use a handful of commands and the world is a simpler place for it.

Cheers!☺☻♥
