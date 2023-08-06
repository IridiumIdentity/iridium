# Using Docker for development

Run the following command to set up MariaDB on your local machine.
```shell
$ docker compose -f tools/schedulers/compose/local-dev-compose.yml up [-d]
```

Now you'll be able to start and stop the MariaDb container with the following commands.
```shell
$ docker stop iridium-db
$ docker start iridium-db
```
