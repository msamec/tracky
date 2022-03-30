
# Tracky

Convert Toggl entries into Tempo worklogs. Using toggl api key fetch all entires (last 9 days) that do not have `synced` tag. Task description must consist of task id and decription separated by pipeline `|` character. On sync it will send request to tempo api and add time log.

![image](https://user-images.githubusercontent.com/4154034/152775907-6a85c1f3-d81f-429e-8243-9f60d4b14262.png)


## Requirements

* [Clojure](https://clojure.org/guides/getting_started)
* [Node](https://nodejs.org/en/)

## Installation

### Secrets

Create `.envrd` file in root folder with following keys (insert valid values)

```
export GOOGLE_CLIENT_ID=""
export GOOGLE_CLIENT_SECRET=""
export ISSUER_URL="https://accounts.google.com"
export AUDIENCE=""
export JWKS_URI="https://www.googleapis.com/oauth2/v3/certs"
export REDIRECT_URI="http://localhost:3000/"
```

## Usage

### Backend

```
$ lein run
```

### Frontend

```
$ npx shadow-cljs watch app
```

## Running tests

```
bin/kaocha
```
