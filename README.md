# Matt's Online Aim Trainer (MOAT) Server

MOAT is an aim trainer game where the user has to click targets on the screen and accrue points to beat the online leaderboards.

This is the server software written in Java using Spring Boot.  This software is used to interact with the MOAT client by providing functionality such as leaderboards, score updating and administrative access.

© 2023 All rights reserved Matthew Dixon.

## Notes

### Requirements

This software requires `Java 17` to run.

To build this project, you will need `Maven v3.8.7`.

### Build

1. Clone the repository by running `git clone https://github.com/CoderMattGH/moat-be.git`.
2. Change to the `moat-be` repository directory.
3. Run `mvn clean package`. This will build the project after running any tests.
4. Change to the `target` directory, where you will find the `MOATserver.jar` file.

### Running

The software is compiled to the file `MOATserver.jar`, which is an executable `jar` file containing an embedded Apache TomCat server courtesy of Spring Boot.

You can run MOAT Server via the terminal using the command `java -jar MOATserver.jar --DB_USERNAME="admin" --DB_PASSWORD="password"`.

Replace `admin` and `password` with the correct database username and database password, respectively.

Environment variables for development are stored in `application-dev.properties` in the `./src/main/resources/` directory.

Production variables and global variables are stored in `application.properties` in the `./src/main/resources/` directory.

The active profile for development is `dev`.  This can be enabled by running the software with the JVM option `-Dspring.profiles.active=dev`.  For example, `java -jar -Dspring.profiles.active=dev MOATserver.jar --DB_USERNAME="admin" --DB_PASSWORD="password"`.

The default production H2 database file (specified in the `application.properties` file) is `./dbfile/moatdb.mv.db`.  This file and folder must be kept in the root directory (usually the directory where the `MOATserver.jar` file resides) when running the software. 

The default development H2 database file (specified in the `application-dev.properties` file) is `./dbfile/moatdb_dev.mv.db`. This file and folder must be kept in the root directory (usually the directory where the `MOATserver.jar` file resides) when running the software. 

The default port (specified in the `application.properties` file) is TCP port 5000 listening on
 localhost (127.0.0.1).

### Filters

This software uses optional profanity filters.  Filters can be enabled by specifying the property `moat.filters.load-profanity-filter=true` in `application.properties`.  The profanity filter is enabled by default.  Setting this value to false will disable the profanity filter.

If the profanity filter is enabled, then the software will look for a profanity filter file.  The software will look for a file named `./filters/prof_filter.txt` in the root directory.  The root directory, remember, is normally where the `MOATserver.jar` artifact is being run from.

The `./filters/prof_filter.txt` file should contain a list of banned words with a carriage return between each entry.

Currently the profanity filter simply filters out any vulgar nicknames from appearing on the leaderboard.

### Database

The database used by default by the application is a H2 database which is stored in the file `./dbfile/moatdb` relative to the classpath.

#### Rebuilding The Database

The database is not saved in the repository because of security concerns.  If you should need to recreate the database, then you should simply change the `spring.jpa.hibernate.ddl-auto=validate` to `spring.jpa.hibernate.ddl-auto=create` property in `application.properties` and load the application.  This will create a new database file with the correct schema.  After obtaining the file, you should change the property back to it's default value of `validate`.

Alternatively, instead of editing the `application.properties` file directly, you can specify the additional command line argument `--spring.jpa.hibernate.ddl-auto="create"` when running the application.

For example, `java -jar MOATserver.jar --DB_USERNAME="requested_username" --DB_PASSWORD="requested_password" --spring.jpa.hibernate.ddl-auto="create"`.  Replace `requested_username` and `requested_password` with the database username and password you would like to use.

If you wish to recreate the schema manually, then you should study the `com.moat.entity` package to identify the schema you will need.

If you wish to enable administrative options, you should also add an entry to the database "Administrator" table containing the `ID`, `username` and `password`.  The password needs to be stored as a hashed Bcrypt value of strength 10.  There are several sites on the Internet that will convert plain text to a hashed BCrypt value.

#### Database Administration

Administration of the database file can be achieved by visiting the H2 Console at `http://localhost:5000/h2-console` on the machine where the server is running (assuming that `localhost` on TCP port 5000 is running the software).  By default, the H2 Console is only accessible via `localhost`.  Remote access is disabled by default, but can be enabled by adding the property `spring.h2.console.settings.web-allow-others=true` in `application.properties`.  It is strongly recommended to not allow remote access.


