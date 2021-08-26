
# MongoDB Connector 

### Install MongoDB

- Prerequisites and installation 

```
xcode-select --install
brew tap mongodb/brew
brew install mongodb-community@5.0
```

- Installed [mongo compas](https://www.mongodb.com/products/compass) which is a GUI tool

- Start / Stop / List mongodb service

```
brew services start mongodb-community@5.0
brew services stop mongodb-community@5.0
brew services list
brew --prefix # prefix/etc/mongod.conf
```

# Resources 

- [MongoDB Connector](https://www.confluent.io/hub/mongodb/kafka-connect-mongodb)
- [MongoDB Connector Github](https://github.com/mongodb/mongo-kafka)
- [Install Mongo](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-os-x/)
- [MongoDB cheat sheet](https://gist.github.com/bradtraversy/f407d642bdc3b31681bc7e56d95485b6)
- [Mongo as Docker container](https://hub.docker.com/_/mongo/)