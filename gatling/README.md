# Relevant Articles:
- [Intro to Gatling](http://www.baeldung.com/introduction-to-gatling)

# Load testing

## Install Scala

 ``` 
brew install scala
 ```

## Install plugin for IntelliJ 

 Follow normal process to install the Scala plugin
 
## Configure proxy for browser 

![proxy](./docs/gatling-recording-proxy1.png)

![proxy](./docs/gatling-recording-proxy2.png)

## Add host to /etc/hosts

echo $(ipconfig getifaddr en0)" parts" | sudo tee -a /etc/hosts

## Record tests

 Start the application
 
 ``` 
java -Dserver.address=parts -Dspring.profiles.active=h2 -jar backend/target/parts-backend-*.*.*-SNAPSHOT.jar
 ```

 Start the recorder with the following settings

![gatling](./docs/gatling-recording.png)

 Open browser to [here](http://parts:8080) and start using the application
 
## Run tests

 ``` 
cd gatling
../mvnw -Psimulation test
 ```
 
 You can view the reports after it's finished by opening the index.html file output after the run
 
 ``` 
open $(find target/gatling -regex '.*usersimulation.*index.html' | sort -n | tail -1)
 ```

 or all in one command
 
 ```
../mvnw -Psimulation clean test && open $(find target/gatling -regex '.*usersimulation.*index.html' | sort -n | tail -1)
 ```

 Here is a sample during the run
 
 ``` 
Simulation us.thirdbase.simulations.UserSimulation started...

================================================================================
2019-10-15 10:59:55                                           5s elapsed
---- Requests ------------------------------------------------------------------
> Global                                                   (OK=24     KO=0     )
> Login Request                                            (OK=12     KO=0     )
> Next page                                                (OK=12     KO=0     )

---- User Scenario -------------------------------------------------------------
[-                                                                         ]  0%
          waiting: 10646  / active: 12     / done: 0
================================================================================


================================================================================
2019-10-15 11:00:00                                          10s elapsed
---- Requests ------------------------------------------------------------------
> Global                                                   (OK=158    KO=0     )
> Login Request                                            (OK=39     KO=0     )
> Next page                                                (OK=119    KO=0     )

---- User Scenario -------------------------------------------------------------
[-                                                                         ]  0%
          waiting: 10618  / active: 40     / done: 0
================================================================================


================================================================================
2019-10-15 11:00:05                                          15s elapsed
---- Requests ------------------------------------------------------------------
> Global                                                   (OK=720    KO=0     )
> Login Request                                            (OK=138    KO=0     )
> Next page                                                (OK=566    KO=0     )
> Create part                                              (OK=16     KO=0     )

---- User Scenario -------------------------------------------------------------
[-                                                                         ]  0%
          waiting: 10518  / active: 126    / done: 14
================================================================================


================================================================================
2019-10-15 11:00:10                                          20s elapsed
---- Requests ------------------------------------------------------------------
> Global                                                   (OK=1662   KO=0     )
> Login Request                                            (OK=239    KO=0     )
> Next page                                                (OK=1345   KO=0     )
> Create part                                              (OK=78     KO=0     )

---- User Scenario -------------------------------------------------------------
[--                                                                        ]  0%
          waiting: 10418  / active: 182    / done: 58
================================================================================
 ```

# Troubleshooting

 The latest version as of this writing is below and it can run up to jdk 11.   
 
  ``` 
<gatling.version>3.2.1</gatling.version>
<scala-maven-plugin.version>4.2.0</scala-maven-plugin.version>
<gatling-maven-plugin.version>3.0.3</gatling-maven-plugin.version>

  ```

