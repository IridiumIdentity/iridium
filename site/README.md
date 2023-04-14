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
Add the correct python version to your path
```shell
$ export PATH="/Users/<you>/Library/Python/3.9/bin:$PATH"
```
