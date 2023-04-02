# COMP 361 Project

> See also [my video instuctions](https://www.cs.mcgill.ca/~mschie3/COMP361/Repository-Best-Practices.mp4) in the
> screencasts section on MyCourses.

## The Rules

* Feel free to edit/replace this file.
* Do not delete or rename the [reports](reports), [client](client), [server](server) or [docs](docs) directories.  
  See [Static Content](#static-content)
* Don't clutter your repo, update your [```.gitignore```](.gitignore) file, depending on your client language /
  technology.
    * Don't commit binaries. (Images, jar files, class files, etc...)
    * Don't commit buffer files. (Vim buffer files, IDE meta files etc...)
* Place your documentation in [```docs```](docs) on [master](branch).
* Commit frequently, commit fine grained.
* Use branches
* **Don't push on master!**
    * Create a new branch for your feature.
    * Work until stable / tested.
    * Merge / rebase your temporary branch back to master.
    * Delete your temporary branch.

## Static content

* [```.gitignore```](.gitignore): Preliminary git exclusion instructions.
  Visit [Toptal's generator](https://www.toptal.com/developers/gitignore) to update.
* [```reports```](reports): Base directory for automatic report collection. Your weekly reports go here. Must be
  uploaded every monday noon **to master** and follow correct date string ```YYYY-MM-DD.md```.
  Use [template](reports/YYYY-MM-DD.md) for your own reports. Do not resubmit same report / copy paste.
* [```docs```](docs): source directory for your [enabled GitHub Pages](https://comp361.github.io/f2022-hexanome-14/). (
  Update number in link). Configure IDE to generate API docs into this directory or build your own webpage (optional).
* [```client```](client): Place your client sources into this directory. Do not use a separate repository for your work.
* [```server```](server): Place your Spring Boot Game Server sources in this directory. Do not use a separate repository
  for your work.

## Useful Links

### Code Style and Tools

* [Chrome MarkDown Plugin](https://chrome.google.com/webstore/detail/markdown-viewer/ckkdlimhmcjmikdlpkmbgfkaikojcbjk?hl=en).
    * Don't forget to enable ```file://``` in ```advanced settings```.
* [IntelliJ Checkstyle Plugin](https://plugins.jetbrains.com/plugin/1065-checkstyle-idea).
    * Don't forget to
      enable [Google's Checkstyle Configuration](https://raw.githubusercontent.com/checkstyle/checkstyle/master/src/main/resources/google_checks.xml).
* [Git CheatSheet](git-cheatsheet.md).
* [Advanced Rest Client (Rest Call Code Generator)](https://docs.advancedrestclient.com/installation).

### Requirements

* [Lobby Service](https://github.com/kartoffelquadrat/LobbyService)
    * [Install Guide](https://github.com/kartoffelquadrat/LobbyService/blob/master/markdown/build-deploy.md)  
      Recommended: Startup in ```dev``` profile (default).
    * [API Doc and ARC Configurations](https://github.com/kartoffelquadrat/LobbyService/blob/master/markdown/api.md)
    * [Game Developer Instructions](https://github.com/kartoffelquadrat/LobbyService/blob/master/markdown/game-dev.md)
* [BGP sample deployment configuration](https://github.com/kartoffelquadrat/BoardGamePlatform) (This one is meant for
  testing / understanding the interaction between LS, UI and sample game)  
  Board Game Platform (BGP) = Lobby Service + Lobby Service Web UI + Sample Game, all as docker containers.
    * Sample [Lobby Service Web UI](https://github.com/kartoffelquadrat/LobbyServiceWebInterface)
    * Sample Lobby Service
      compatible [Game (Tic Tac Toe, backend + frontend)](https://github.com/kartoffelquadrat/BgpXox)

> Be careful not to confuse *Lobby Service* and *Board Game Platform*.

## Authors

* [Miguel Arrieta Torrecilla](https://github.com/Chuset21)
* [Eamonn Lye](https://github.com/eamonn-lye)
* [Kai Turanski](https://github.com/kailaidescope)
* [Fernando Borrell](https://github.com/fjborrell)
* [Matan Atlas](https://github.com/Matanatlas)
* [Krishi Madipelly](https://github.com/krishidub)

## How to run

### Native deployment

Run the services in the following order:

- Run the LobbyService and its database, following its [instructions](LobbyService/README.md).
- Run the server and its database, following its [instructions](server/README.md).
- Run the client, following its [instructions](client/README.md).

### Docker Compose

The service interplay configuration for this Splendor implementation uses Docker-Compose.

- Docker is a virtualization technology that allows a convenient deployment (installation) of services, independent of
  the host OS. Docker deploys individual services (the modules listed above) as containers.
- The Docker-Compose configuration of this repository orchestrates module containers (correct startup sequence, network
  wiring).

> Note: You can use this docker-compose configuration on any system, no matter if a personal laptop or a headless
> server.

The following files are used for deployment:

* [```docker-compose.yml```](docker-compose.yml): Docker-Compose file to orchestrate the startup sequence and network
  wiring of containerised platform modules:
    * [```Dockerfile-ls-db```](Dockerfile-ls-db): Container build instructions for the Lobby Service Database
      module.
    * [```Dockerfile-ls-api```](Dockerfile-ls-api): Container build instructions for
      the [Lobby-Service](https://github.com/kartoffelquadrat/LobbyService) module.
    * [```Dockerfile-sp-db```](Dockerfile-sp-db): Container build instructions for the Splendor server Database
      module.
    * [```Dockerfile-sp-api```](Dockerfile-sp-api): Container build instructions for
      the [Splendor](https://github.com/COMP361/f2022-hexanome-14) server module.

### Prerequisites

#### Docker-Compose

Depending on your host-os, install docker-compose with one of the links below:

* Mac OS / Windows: [Docker Desktop (docker-compose and GUI-tools)](https://www.docker.com/products/docker-desktop)
    * Windows: Additionally follow
      the [official instructions to install the linux kernel](https://docs.microsoft.com/en-us/windows/wsl/install-win10#manual-installation-steps).
* Prod Systems / Linux: [Docker Compose (just docker-compose)](https://docs.docker.com/compose/install/)

#### Code

* Clone this repository:  
  ```git clone https://github.com/COMP361/f2022-hexanome-14```

* *Recursively* pull, and re-attach all sub repositories:

    * Mac / Linux:  
      ```./updatesubmodules.sh```
    * Windows:  
      ```.\updatesubmodules.ps1```

#### Java and maven

Install [java](https://www.oracle.com/java/technologies/downloads/) version 19
and [maven](https://maven.apache.org/install.html) to run the client.  
Here are some good instructions by Baeldung on how to install
maven: https://www.baeldung.com/install-maven-on-windows-linux-mac  
This is also necessary if the server is being deployed natively.

### Deployment

* If you're on a Mac with M1 chip, set these environment variables:

```
export DOCKER_BUILDKIT=0
export COMPOSE_DOCKER_CLI_BUILD=0
```

* Power up the platform:  
  ```docker-compose up```  
  (use ```docker-compose up --build``` to force rebuild)

* Test the platform:
    * Run the client with the these [instructions](client/README.md)
    * In the running client login with a user, such as ```maex```, ```linus```, ```khabiir``` or ```marianick```, with
      the
      password: ```abc123_ABC123```
    * Create a session
    * Launch another instance of the client
    * Login as a different user to client 1
    * On client 2, join the session created by client 1
    * Launch the game on client 1
    * Join the game on client 2
    * *Play!* on both clients.
