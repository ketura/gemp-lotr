# gemp-lotr
GEMP is a platform for running a rules-enforced CCG in a web-hosted fashion, so players can play in their browser.  This instance of GEMP is for the Lord of the Rings TCG, which has a public instance [hosted here](https://play.lotrtcgpc.net) by the [LOTR-TCG Player's Council](https://lotrtcgpc.net).

The engine is adaptable to other games, but essentially requires forking and ripping out all of the LOTR-based stuff and replacing it with context specific to the game of your choice.  The [Star Wars CCG Players Committee](https://www.starwarsccg.org/) has done so with [their SWCCG GEMP](https://github.com/PlayersCommittee/gemp-swccg-public), which is [hosted publicly here](https://www.starwarsccg.org/).  If forking for a new game, we recommend using this one as the base, as it contains several advancements not in the SW GEMP, including JSON-based card definitions and an updated Java version.

# Getting Help

If the below instructions do not tell you what you need, please contact ketura in [the #gemp-dev channel of the PC Discord](https://lotrtcgpc.net/discord) and ask whatever questions you may have there.


# Overview

GEMP is a server written for Java 18, using MariaDB for a MySQL database instance, and serving raw Javascript and barebones HTML/CSS for the client front-end (although a client rewrite is in progress to modernize the JS to something more palatable to modern developers).  

GEMP is divided into several modules which each handle a different aspect of the game, albeit for now they cannot be hosted separately (tho this is a future improvement we would like to make)

## gemp-lotr-async
This module manages the interface for the Javascript clients.  Each JS operation that contacts the server will eventually terminate in a call to [communication.js](communication.js), which is ultimately just a wrapper for calls to [RootUriRequestHandler](RootUriRequestHandler.java), which routes each endpoint to the appropriate handler within gemp-lotr-async.  From there the action could go anywhere depending on context.

gemp-lotr-async is also the home for the web portion of the project; everything in [/src/main/web/](gemp-lotr/gemp-lotr-async/src/main/web) is served as-is to the browser client, and the bulk of the Javascript can be found in [/js/gemp-022](gemp-lotr/gemp-lotr-async/src/main/web/js/gemp-022).  /images contains icons, booster pack images, and other such resources.

## gemp-lotr-cards

This module contains the card definitions of both styles.  Originally, cards were each written in Java, one class per card, and the majority of LOTR-TCG cards are still implemented in that format here, in [/src/main/java/com/gempukku/lotro/cards](gemp-lotr/gemp-lotr-cards/src/main/java/com/gempukku/lotro/cards).  

However, in August 2019 work was underway to implement a "Second Edition" fan set, and requiring a server restart for every balance update and addition was proving to be too obnoxious, so the engine was extended to permit definitions of cards in JSON, which, as data, could simply be reloaded with an admin command rather than requiring a recompile.  To date, all of the following card sets are implemented in JSON and not Java:

* 0 (Promotional)
* 1 (The Fellowship of the Ring)
* 9 (Reflections)
* 14 (Expanded Middle-earth)
* 16 (The Wraith Collection)
* 19 (Age's End)
* 30-33 (The Hobbit Draft Game)
* V1 (PC, Shadow of the Past)
* All PC Errata

Bug fixes nowadays will also take the opportunity to convert a card to JSON, so there are a smattering of cards from various other sets that have also been implemented.

In 2022, all JSON definition files were [converted to HJSON](https://hjson.github.io/), which is a mutually-convertable dialect of JSON that includes support for comments and does not require redundant quotes in strings or commas in field definitions.  Consult [this list of editor extensions](https://hjson.github.io/users.html) to get syntax highlighting support for your editor of choice.

When developing JSON cards, refer to the [JSON Card Specification documentation](https://docs.google.com/document/d/1s26gfFIx6olaXD8ZY1se6dymssvF-Ty6xXpJsFy_dvc/edit#heading=h.nwylf3kc6aef).

Other data definitions besides cards are also stored in this module, including booster pack definitions, formats, sets, and sealed definitions.


## gemp-lotr-common

This module contains definitions used throughout the project, including enums for card types, cultures, zones, and the like.  It also contains certain low-level classes for HTTP responses.


## gemp-lotr-images

A currently unused package intended to be used to generate new card images.  (The PC independently developed its own spreadsheet-based generator using nanDECK, which [can be found here](https://github.com/PlayersCouncil/LotR-TCG_card_generator)).


## gemp-lotr-logic

The home for classes used in executing game rules and also in generating card objects from JSON card definitions.


## gemp-lotr-server

The entry point for the server and the home for all database interaction, hall management, game running, chat, tournaments, and other services.  

This is also the home for the unit tests for the server, which will fail compilation if they do not pass: [/src/test/java/com/gempukku/lotro](gemp-lotr/gemp-lotr-server/src/test/java/com/gempukku/lotro).  Besides various service tests, there is a cards subfolder which contains a place to put unit tests for new cards (including errata).  All PC V-cards and errata have a class for holding tests for that card, which are initially generated from the same spreadsheet that is used for generating the images and manually expanded as needed (see ketura for help on that). 

Old card unit tests were scattershot and stored in the /at subfolder as needed.  Do not add to this subfolder; instead use the cards subfolder mentioned above.


# Hosting

GEMP includes tools for hosting within [Docker](https://docker-curriculum.com/), which makes setting up a local host a breeze (assuming the maintainers have kept the database scripts up-to-date).  See the readme in the [/docker](/gemp-lotr/docker) subfolder for more advanced details and instructions on the organization, but the following are basic instructions on how to start a local copy of GEMP.  Make sure to follow the advanced instructions if hosting a production instance, as it will give steps for updating the default ports and credentials for db accounts and such.

1. Install Docker (Docker Desktop if on Windows).  If you are on Windows, make sure that when you install it you check all boxes that have Docker behave like it's on Linux. 

2. Install your container manager of choice.  [portainer.io](https://www.portainer.io/) is highly recommended.

3. Pull the git repository down to your host machine
	* Open a command line window and navigate to the folder that you want to put GEMP in
	* Run the following command: `git clone https://github.com/PlayersCouncil/gemp-lotr.git`

4. Open a command line and navigate to gemp-lotr/gemp-lotr/docker.  
	* Run the command `docker-compose up -d`
	* You should see `Starting gemp_app....done` and `Starting gemp_db....done` at the end.  
	* This process will take a while the first time you do it, and will be near instantaneous every time after.

5. The database should have automatically created the gemp databases that are needed.  
	* You can verify this by connecting to the database on your host machine with your DB manager of choice ([DBeaver](https://dbeaver.io/) is highly recommended).  
	* It is exposed on localhost:35001 (unless you changed this) and uses the user/pass of `gempuser`/`gemppassword` (unless you changed this).  
	* If you can see the `gemp_db` database with `league_participation` and other tables, you're golden.  

6. Now we need to compile the gemp code.  
	1. Open a terminal inside the `gemp_app` container
		* If using portainer.io, 
			* log in
			* select your 'Local' endpoint
			* click the Containers tab on the left
			* click the `>_` icon next to gemp_app and click the Connect button
		* If using Docker Desktop
			* Open Docker Desktop
			* Select the "Container" option in the left navbar
			* expand the `gemp_1` container
			* click the actiosn button and select `Open in Terminal`

	2. Navigate to the gemp codebase: `cd etc/gemp-lotr`

	3. Now tell Maven to compile the project: `mvn install`
		* This process will take upwards of 5-10 minutes.  You should see a green "BUILD SUCCESS" when it is successfully done.  In portainer.io or another rich command line context, you should see lots of red text if it failed.

7. Open [gemp-lotr/gemp-lotr/docker/docker-compose.yml](gemp-lotr/docker/docker-compose.yml) in your editor of choice, and uncomment [this line](https://github.com/PlayersCouncil/gemp-lotr/blob/master/gemp-lotr/docker/docker-compose.yml#L52).  This will ensure that the container runs the GEMP server every time it is started automatically.

8. On your host machine cycle your docker container
	* In a terminal navigate to `gemp-lotr/docker`
	* Run `docker-compose down`
	* After that completes run `docker-compose up -d`	
	* This is how you restart GEMP any time that you need to incorporate changes to the Docker container.  If you just need to reload freshly-compiled code, then restart the container directly via Docker or portainer.io.
	
9. If all has gone as planned, you should now be able to navigate to your own personal instance of Gemp.  Open your browser of choice and navigate to http://localhost:17001/gemp-lotr/ . 

13. If you're presented with the home page, register a new user and log in. It's possible for the login page to present but login itself to fail if configured incorrectly, so don't celebrate until you see the (empty) lobby.  If you get that far, then congrats, you now have a working local version of Gemp.
  
  
By default, the above instructions will create 3 admin accounts: `asdf`, `qwer`, and `Librarian`, all with a password of `asdf`.  Decks on the Librarian user will be automatically included in the Deck Library for all users, and the other accounts can be used for personal testing.  Be sure to delete and/or change the credentials of these accounts if deploying to a production environment.


# Editing and Contributing

We recommend editing GEMP server code in [IntelliJ IDEA](https://www.jetbrains.com/idea/), but theoretically you can use your Java IDE of choice.  

Follow the instructions [in this document](https://docs.google.com/document/d/1mKAm9jCAttcU_M6yWCsjU33Hg98FmhWPchEa5fCG-fs/edit) to enable "remote" debugging into a local active instance of GEMP for debugging tricky issues.  Because of the way Maven is configured, you cannot have the remote debugging flag active *and* be able to compile GEMP within the Docker container, so this flag is mostly only useful for local debugging (where you can recompile through your IDE).  If you are in a production environment, be sure to undo the changes mentioned before you need to recompile remotely.

(If anyone has insight into how this could be changed, please let us know.)



