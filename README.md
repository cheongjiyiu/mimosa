# Mimosa

![build](https://img.shields.io/badge/build-passing-brightgreen) ![dependencies](https://img.shields.io/badge/dependencies-stable-brightgreen)

## Description

Systems rebooting, database dumping, used to be called SecureCoding, now given to juniors, and finally completely rewritten.

This is an opensource core; it excludes administrative controls.

## Features

- Lots of challenges
- References to modern pop culture

## Getting Started

Download the latest the Mimosa release [here](https://github.com/securecoding-mimosa/mimosa-core/releases/latest).
Place mimosa in a directory of your preference before running the following commands in a terminal.

```console
user@mimosa:~$ java -jar mimosa-core-<version>.jar -g sql
Package(s) created!
user@mimosa:~$ java -jar mimosa-core-<version>.jar -g properties
Package(s) created!
```

This will generate two packages, a `mimosa-dump.sql` database dump to run in a MySQL server of your choice,
and an `application.properties` file that you can use to configure your Mimosa settings.

Once configuration is complete, you can start Mimosa by running the following command. Enjoy!

```console
user@mimosa:~$ java -jar mimosa-core.jar
            _
           (_)
  _ __ ___  _ _ __ ___   ___  ___  __ _
 | '_ ` _ \| | '_ ` _ \ / _ \/ __|/ _` |
 | | | | | | | | | | | | (_) \__ \ (_| |
 |_| |_| |_|_|_| |_| |_|\___/|___/\__,_|

 :: Spring Boot (v2.6.1) ::
 :: App Version (v3.3.1) ::

2022-05-20 17:26:37.947  INFO 18316 --- [           main] securecoding.Applicat...
```

## Requirements

- MySQL Server 8.x+
- Java JRE 17.x+
- NodeJS 14.x+ (Optional, for nodejs challenges)
