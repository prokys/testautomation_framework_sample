# Test automation framework sample

Sample of test automation framework for Angular petclinic app with Spring backend and Postgresql database

Currently, there are only CRUD tests for each UI, API and database testing

### Used technologies:
* Java 17
* TestNG
* Selenium WebDriver
* REST Assured
* Docker

### How to run project
##### Clone project from GitHub
```
$ git clone https://github.com/prokys/testautomation_framework_sample.git
```
##### Run Petclinic app with db in Docker
```
$ docker compose up
```
##### Run tests
```
$ mvn clean test
```

