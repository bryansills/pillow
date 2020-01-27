# Pillow
## Setup
Remove the `.example` suffix from `src/main/java/ninja/bryansills/pillow/server/BuildConfig.kt.example` and fill out the object with valid values

## Running
### Locally
This app is packaged using [Docker](docker.com), so if you don't have that set up, [follow these instructions](https://medium.com/@yutafujii_59175/a-complete-one-by-one-guide-to-install-docker-on-your-mac-os-using-homebrew-e818eb4cfc3).
If your Docker virtual machine is stopped, execute these commands to get it back up and running:

```
$ docker-machine start default
$ docker-machine env default
$ eval $(docker-machine env default)
```

Once your Docker setup is up and running, execute the following commands to start the server:

```
$ docker-machine ip
$ docker-compose up --build
```

The server should now be up and running. You can access from the `8080` port at the IP address that was outputted as a result of the `$ docker-machine ip` command.
Once you are done, make sure to stop the Docker virtual machine by running the following commands:

```
$ docker-compose down
$ docker-machine stop default
```

### Debugging locally
TODO

### Pushing to Heroku
#### For the initial deployment
- (Not currently required) Set up the necessary config vars in Heroku (see `BuildConfig.kt`), follow [these instructions](https://devcenter.heroku.com/articles/build-docker-images-heroku-yml)
- Add the Heroku Postgres addon into the Heroku project
- Connect the repository to Heroku using the [Heroku CLI tool](https://devcenter.heroku.com/articles/heroku-command-line) by executing the following command:
```
$ heroku git:remote -a [RELACE-WITH-HEROKU-PROJECT-NAME]
```
- Set Heroku to build using Docker files by executing the following command:
```
$ heroku stack:set container
```
- Deploy the project by executing the following command:
```
$ git push heroku master
```
#### For subsequent deployments
Run this command:
```
$ git push heroku master
```