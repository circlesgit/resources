# Lab: Getting started with docker

In this lab, you will learn

  * How to launch containers with docker
  * Common options used with docker container run command
  * Run web applications and access those with port mapping  and
  * Learn how to manage container lifecycle and to debug those.

###  Validating the setup

Begin by checking the  version of docker installed with,

```
docker version
```

you could further validate r by running a smoke test as,

```
docker run hello-world
```

This should launch a container successfully and show you hello world message. This smoke test validates the following,

  * you have docker client installed
  * docker daemon is up and running
  * docker client is been correctly configured and authorized to talk to docker daemon
  * you have non blocking internet connectivity
  * you are able to pull an image from docker registry and run a container with it


###  Launching your first container  

Before you begin launching your first container, open a new terminal and run the following command to analyze the events on the docker daemon side.

```
docker system events
```

When you launch the above command, you may not see any output. Keep that window open, and it would start streaming events as you proceed to use docker cli further.

Now launch your first  container with the following command,

```

docker container run centos ps

```

Where,

  * docker is the command line client
  * **container run**  is the command to launch a container. You could alternately use just **run** command here
  * centos is the image
  * ps is the actual command which is run inside this container.


Next go ahead and try creating a few more containers with the same imafge but with  different commands as,

```
docker container run centos uptime
docker container run centos uname -a
docker container run centos free
```

you could check the events in the window where you have started running  `docker system events` command earlier. It shows whats happening on docker daemon side.


###  Listing Containers


you can check your last run container using following commands,

```
docker ps -l
docker ps -n 2
docker ps -a
```

where
  * -l : last run container
  * -n xx : last xx number of containers
  * -a : all containers (even if they are in stopped state )


### Learning about images

Containers are launched using images, which are pulled from registry. Observe following command,

 ```
 docker container run centos ps
 ```
 Here the image name is **centos** which can actually expanded to

`registry.docker.io/docker/centos:latest`

where following are the fields,

  * registry: registry.docker.io
  * namespace: docker
  * repo: centos
  * tag: latest


You could use following commands to list your images from your local machine and pull the image from docker-hub. You could even examine the layers of an image  with *history* command.


```
docker image ls

docker image pull nginx

docker image history nginx

```


### Default run options


You have learnt how to launch a container. However, its ephemeral, and exits immediately after running the command. To make the container persist, and in order to be able to interact with it, lets try adding a couple of new options.

```
docker run -it centos bash
```

where,
  * -i : interactive. provides standard in to the process running inside the container
  * -t : provides a pseudo terminal in order to interact

You could further add *--rm* options to ensure the container is deleted when stopped.

```
 docker run --rm -it alpine sh
```


Another useful and important option is *-d*, which will launch the process in a contained environment, and detach from it. This allows the container to keep running in the background.

```
docker container run -idt --name redis redis:alpine
```



### Launching and connecting to Web Applications

So far, you have launched a few simple containers, with one off commands. Its time to now learn how to launch a web application, and learn how to connect to it.  

You could launch a container with nginx web server as follows. Observe the new *-p* option.

```
 docker container run -idt -p 8080:80 nginx
```

where,

  * -p allows you to define a port mapping
  * 8080 is host side port
  * 80 is the container side port, the one on which application is listening  


Once you configure  the port mapping, access that application on your browser by visiting

   `http://IPADDRESS:8080`

`replace IPADDRESS with the *actual IP* in case your docker host is remote, or with *localhost* if using Docker Desktop.`



With the command above using *-p* option you explitly defined the host side port. You could also have docker pick the port automatically with  `-P` option.

```
docker container run -idt -P nginx

```

  * where host port is automatically chosen and incremented starting with 32768
  * container port is automatically read from the image

As usual you could use `docker ps` commands to observe the port mapping.



### Troubleshooting Containers

To  debug issues with the  containers, which are nothing but processes running in an isolated environment,  following could be two important tasks ,

  * checking process logs
  * being able to establish a connecting inside container (simiar to ssh)

You would learn about both in this sub section.

To start examining logs find your container id or container name using

```

docker ps

```

You could use the following command to find out logs for a container whose name is *redis*

```
 docker logs redis

```

You have an option of replacing *redis* name with the actual container id ( the first column in the output of  docker ps command ).



you can follow the logs using `-f` option and `docker exec` allows you to run a command inside a container.


```
 docker logs -f redis
```


**exec** command allows you connect to the container and launch a shell inside it, similar to a ssh connection. It also allows running one off commands.


```
  docker exec redis ps
  docker exec -it redis sh

```

With  **logs** and **exec** commands which you should be able to get started with essential debugging.


## Summary

You learnt about the basics of container operations. This foundation would help you through out the course as we plan to use docker extensively as part of the Continuous Integration  and later with Continuous Delivery.


## Exercises

With the knowledge that you have gained so far, you have been tasked with setting up the following two apps with docker

  * [Nextcloud](https://nextcloud.com/)
  * [Portainer](https://www.portainer.io/)


Try launching it on your own before you proceed with the [solutions given here](../dockerintro/solutions.md).



### Stop, remove and cleanup


Here you will learn how to stop, remove and cleanup the containers which you have created.

Find your container id and stop the container before you are going to remove and you can pause and unpause the container as well.

```
docker ps
docker stop redis
docker stop b584 84e6 e4da
```

where  

  * ac69 b584 84e6 e4da are container ids
  * these  could vary on your system. Use the ones you see when you run `docker ps`


you can start a container thats is in stopped state with,

```
docker start redis
```


To remove a container(destructive process) that is been stopped use,  
```
docker stop redis
docker rm  redis
```

If the container is in running state, it can not be stopped unless a force optin is used.

```
docker container run -idtP --name web nginx  

docker rm web

docker rm -f web
```


Images are independent of containers which are its run time instance. You could further remove the images using commands such as,

```
docker image rm 4206
docker image rm 4f1 4d9 4c1 9f3
docker image  prune
```

where, 4206 4f1 4d9 4c1 9f3 are the beginning characters of the image ids on my host.

You could get all your docker system information by using `docker system info`.
