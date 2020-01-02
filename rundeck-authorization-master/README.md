# rundeck-authorization

### Usage :
./rundeck-acl-generator.pl -i inputfile.csv

#### outputs
* A aclpolicy file is created and populated with rundeck policy and its name is the unique combination of username,context and project. 
* Entire rundeck acl policy is printed on the screen for the inputfile

###Input file format
csv file with  ':' seperated fields
```
# <FIELD1>:<FIELD2>:<FIELD3>:<FIELD4>:<FIELD5>
```

(user|groups)-(input-1):context-(project|application):(project-(input-2)):resource-(input-3)(,regex,input-4):(input-5)

 input-1 : username|groupname
 input-2 : projectname
 input-3 : resourcename ex:job,host
 input-4 : attributes <key=value ...> ex:name=NAME1
 input-5 : permissions ex:read,run,execute,delete,etc.

#### Template1 : readonly access at project level
user level
```
user-<username>:context-application::resource-project,name=<projectname>:read`
```
group level
```
groups-<groupname>:context-application::resource-project,name=<projectname>:read
```

#### Template2 : configuration access at project level
user level
```
user-<username>:context-application::resource-project,name=<projectname>:'*'
```
group level
```
groups-<groupname>:context-application::resource-project,name=<projectname>:'*'
```

#### Template3 : Full access on existing jobs of the project
user level
```
user-<username>:context-project:project-<projectname>:resource-job,regex,name="(.*)":'*'
```
group level
```
groups-<groupname>:context-project:project-<projectname>:resource-job,regex,name="(.*)":'*'
```

#### Template4 : New job creation access at project level
user level
```
user-<username>:context-project:project-<projectname>:generic-job:'*'
```
group level
```
groups-<groupname>:context-project:project-<projectname>:generic-job:'*'
```

#### Template5 : only read and execute access on existing jobs of the project
user level
```
user-<username>:context-project:project-<projectname>:resource-job,regex,name="(.*)":read,run
```
group level
```
groups-<groupname>:context-project:project-<projectname>:resource-job,regex,name="(.*)":read,run
```
