# Matt's Online Aim Trainer (MOAT) Server

MOAT is an aim trainer game where the user has to click targets on the screen
and accrue points to beat the online leaderboards.

This is the server software written in Java using Spring Boot. This software is
used to interact with the MOAT client by providing functionality such as
leaderboards, score updating and administrative access.

© 2023 All rights reserved Matthew Dixon.

## Notes

### Requirements

`Java` _Minimum version 17_

`Maven` _Minimum version 3.8.7_

`PostgreSQL` _Minimum version 16.3_

### Build

1. Clone the repository by
   running `git clone https://github.com/CoderMattGH/moat-be.git`.
2. Change to the `moat-be` repository directory.
3. Run `mvn clean package`. This will build the project after running any tests.
4. Change to the `target` directory, where you will find the `*.jar`
   file.

### Setup the database

1. Run the script

### Running in the development environment

1. After building the `*.jar` file, run the
   command `java -jar -Dspring.profiles.active=dev <JAR_FILENAME>`
   where `JAR_FILENAME` is the name of the `*.jar` file.

### Running in the production environment

1. Not yet implemented.

### Testing

1. Run the command `mvn test` from the repository root directory.

### Filters

This software uses optional profanity filters. Filters can be enabled by
specifying the property `moat.filters.load-profanity-filter=true`
in `application.properties`. The profanity filter is enabled by default. Setting
this value to false will disable the profanity filter.

If the profanity filter is enabled, then the software will look for a profanity
filter file. The software will look for a file named `./filters/prof_filter.txt`
in the root directory. The root directory, remember, is normally where
the `MOATserver.jar` artifact is being run from.

The `./filters/prof_filter.txt` file should contain a list of banned words with
a carriage return between each entry.

Currently, the profanity filter simply filters out any vulgar nicknames from
appearing on the leaderboard.

