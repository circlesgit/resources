# Deploy to dev with Docker Compose

Here is what you are going to learn in this chapter,

  * You would begin by learning how  docker compose could help launch a monitoring stack.
  * Then you would understand the docker compose spec version 1
  * Keep adding services to docker compose
  * Refactor the compose file with version3 syntax
  * Create a consolidated jenkins pipeline
  * and start deploying to a integrated dev environment with docker compose  


`file: docker-compose.yaml`


```
version: "3.7"

volumes:
  db-data:

networks:
  instavote:
    driver: bridge

services:
  vote:
    image: initcron/vote:master
    ports:
      - 5000:80
    depends_on:
      - redis
    networks:
      - instavote

  redis:
    image: redis:alpine
    networks:
      - instavote

  db:
    image: postgres:9.4
    volumes:
      - "db-data:/var/lib/postgresql/data"
    networks:
      - instavote

  result:
    image: initcron/result:master
    ports:
      - 5001:4000
    depends_on:
      - db
    networks:
      - instavote

  worker:
    image: initcron/worker:master
    depends_on:
      - redis
      - db
    networks:
      - instavote
```


Add a deploy to dev stage to Jenkinsfile

```

      stage('deploy to dev'){
          agent any
          when{
            branch 'master'
          }
          steps{
            echo 'Deploy instavote app with docker compose'
            sh 'docker-compose up -d'
          }
      }

```
