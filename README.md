# Matt's Online Aim Trainer (MOAT) Server

MOAT is an aim trainer game where the user has to click targets on the screen
and accrue points to beat the online leaderboards.

This is the server software written in Java using Spring Boot.

You can play MOAT here: https://aim.codermatt.com

You can view the client front-end repository here: https://github.com/CoderMattGH/moat-fe/

You can view the API endpoints here: https://aim-api.codermatt.com

© 2024 All rights reserved Matthew Dixon.

## Requirements

`Java` _Minimum version 17_

`Maven` _Minimum version 3.8.7_

`PostgreSQL` _Minimum version 16.3_

## Build

1. Clone the repository by running:

```
git clone https://github.com/CoderMattGH/moat-be.git
```

2. Change to the `moat-be` repository directory.


3. To build the project and perform any tests run:

```
 mvn clean package
``` 

4. Change to the `target` directory, where you will find the `*.jar`
   file.

## Setup the database

1. Run the script from the repository root directory:

```
./db_setup/setup.sh 
```

___DATA LOSS WARNING:__ This will drop all the MOAT database tables if they
exist!_

___NOTE:__ You should add a `.pgpass` file in your home directory to enable the
application to automatically log in to your database. If not, you will have
to provide additional arguments to run the application._

___NOTE:__ You can also set up a database manually with the name `moat_db` for
the
production environment or `moat_db_dev` for development. The program will
automatically create
the required tables._

## Running in the development environment

1. After building the `*.jar` file, run the
   command:

```
java -jar -Dspring.profiles.active=dev <JAR_FILENAME>
```

_Where:_

_`JAR_FILENAME` is the name of the `*.jar` file._

_For example:_

```
java -jar -Dspring.profiles.active=dev moat-1.1.0.jar
```

### Database authentication

If you have not set up a `.pgpass` file, then you will need to add the following
arguments to the run command:

```
-Dspring.datasource.username=<DB_USERNAME> -Dspring.datasource.password=<DB_PASSWORD>
```

_Where:_

_`DB_USERNAME` is the database username_.

_`DB_PASSWORD` is the database password_.

_For example:_

```
java -jar -Dspring.datasource.username=admin -Dspring.datasource.password=password moat-1.1.0.jar
```

### Administrator account

In development mode, an Administrator account has been set up automatically with
the username `ADMIN` and a password of `password`.

## Running in the production environment

1. After building the `*.jar` file, run the command:

```
java -jar -DJWT_SECRET_KEY=<MY_SECRET_KEY> <JAR_FILENAME>
```

_Where:_

_`JAR_FILENAME` is the name of the `*.jar` file._

_`MY_SECRET_KEY` should be secret string._

_For example:_

```
java -jar -DJWT_SECRET_KEY=keyboard-cat moat-1.1.0.jar
```

### Database information

By default, MOAT will attempt to connect to a PostgreSQL database running
at `localhost`
on port `5432`.

If you wish to specify a different database location please add the following
argument to the run command:

```
-Dspring.datasource.url=jdbc:postgresql://<HOST>:<PORT>/moat_db
```

_For example:_

```
java -jar -DJWT_SECRET_KEY=keyboard-cat -Dspring.datasource.url=jdbc:postgresql://myhost.com:1234/moat_db moat-1.1.0.jar
```

### Database authentication

If you have not set up a `.pgpass` file, then you will need to add the following
arguments to the run command:

```
-Dspring.datasource.username=<DB_USERNAME> -Dspring.datasource.password=<DB_PASSWORD>
```

_Where:_

_`DB_USERNAME` is the database username_.

_`DB_PASSWORD` is the database password_.

_For example:_

```
java -jar -DJWT_SECRET_KEY=keyboard-cat -Dspring.datasource.username=admin -Dspring.datasource.password=password moat-1.1.0.jar
```

## Testing

1. From the repository root directory, run the command:

```
 mvn test
``` 

