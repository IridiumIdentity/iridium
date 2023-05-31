# Using Docker for development

If you don't want to use compose you can read the following.

Run the following command to set up MariaDB on your local machine.
```shell
$ docker run  --name iridium-db \
    -p 3306:3306 \
    -e MYSQL_ROOT_PASSWORD=iridium \
    -e MYSQL_DATABASE=identities \
    -e MYSQL_USER=identity-api \
    -e MYSQL_PASSWORD=iridium \
    -d mariadb:latest
```

Now you'll be able to start and stop the MariaDb container with the following commands.
```shell
$ docker stop iridium-db
$ docker start iridium-db
```
