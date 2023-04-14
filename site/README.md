# Website build instructions

## Create virtual environment
The tested environment at this time is Python@3.9

```shell
$ python -m venv .venv
```

## Activate virtual environment
```shell
$ source .venv/bin/activate
```

## Deactivate virtual environment
```shell
$ deactivate
```

## Update requirements.txt
```shell
$ pip freeze > requirements.txt
```

## In case mkdocs isn't found

### OSX
Add the correct python version to your path
```shell
$ export PATH="/Users/<you>/Library/Python/3.9/bin:$PATH"
```

### Mkdocs info

For full documentation visit [mkdocs.org](https://www.mkdocs.org).

## Commands

* `mkdocs new [dir-name]` - Create a new project.
* `mkdocs serve` - Start the live-reloading docs server.
* `mkdocs build` - Build the documentation site.
* `mkdocs -h` - Print help message and exit.

## Project layout

    mkdocs.yml    # The configuration file.
    docs/
        index.md  # The documentation homepage.
        ...       # Other markdown pages, images and other files.
