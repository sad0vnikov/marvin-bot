# Marvin the Skype bot

This is my project I started for practicing Java. Marvin is a simple Skype bot which as for now can:
- Notify about new git commits to the master branch. Only Bitbucket webhooks are supported for now.
- Send you new Xkcd comics each time they are published
- Remind you that Friday has came and it's time to party!

Marvin has plugin system built using [Pf4j framework](https://github.com/decebals/pf4j). 
Interacting with skype is made using [Skype4j library](https://github.com/samczsun/Skype4J). Some features like authorising contact doesn't work correctly because of that, so I'm planning to implement skype's bot api.

## Starting Marvin
The easiest way to start Marvin is to use Docker image.
You need to install [Docker](https://www.docker.com/products/overview) and [Docker Compose](https://docs.docker.com/compose/) and copy the following files from this repository:
- docker-compose.yml
- config.env.example

Rename the *config.env.example* file to *config.env* and set the properties MARVINBOT_LOGIN, MARVINBOT_PASSWORD, MARVINBOT_ADMINS and MARVINBOT_LOCALE

### Configuration options description
- MARVINBOT_LOGIN: Bot's skype login
- MARVINBOT_PASSWORD: Bot's skype password
- MARVINBOT_ADMINS: Skype logins of users who has admin's privileges for managing Marvin
- MARVINBOT_LOCALE: Bot's locale. English and Russian languages are supported for now. Set this param to *RU* if you want to set the language to Russian and *EN* if you prefer English. More languages can easily be added by extending locale bundles.

After finishing the configuration run `docker-compose up -d` command which will get and run MongoDB and Marvin containers
