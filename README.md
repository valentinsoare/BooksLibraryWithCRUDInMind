# :books: LibraryWithCRUDInMind

## _with create, read, update, delete operations and several flavors of them_

<br>

:arrow_forward: Small library applications interacting with a MysQL application within a docker
container on port 3306 using only JDBC driver, no framework, from the Java standard point of view.

:arrow_forward: When developing these methods around CRUD principles I followed the Data Access Object Generic Interface (where I defined
the methods) and the template method pattern from the Gang of Four design patterns.
and SOLID design principles. Also there is a way in the code when I extend a generic class using anonymous classes
in order to be able to define an object which in the class is a generic one T.

:arrow_forward: In the source code you can see that there are two ways of writing a method,
the naive way without design patterns implemented and the one with all things necessary in order to be able to test the code
and function properly.

:arrow_forward: More information will be down bellow.

![personal_branding][1]


## Technologies and concepts:
1. [X] OOP Principles (composition, inheritance, polymorphism, abstraction);
2. [X] Data Structures - Array, Deque, ArrayList, LinkedList;
3. [X] Third Party Libraries - Lombok and JDBC;
4. [X] Generic, anonymous classes. An anonymous class was made to extend a generic one;
5. [X] Optional class and Comparable in order to be able to avoid null pointer and to compare objects, Book object;
6. [X] SOLID design principles and template method pattern;
7. [X] Docker and MySQL 8.0;
8. [X] Fedora 38;

<br>

> Project is :100: completed!

<br>

:point_right: Here are the CRUD methods defined in the Data Access Object generic interface.

![DataAccessObjectGenericInterfaceMethods][2]

<br>

:point_right: This is the generic abstract class QueryTemplate that extends the ConnectionToDb 
to easily access the open/close connection and getConnection methods.

![QueryTemplateClass][3]

<br>

:point_right: For the link with DB we have an abstract class that is extended by various classes
where we need the connection.

![ConnectionToDB][4]

<br>

:point_right: With BookDAO we implement those CRUD operations, naive way is the first version which it's difficult to make tests for and the second one where I followed design pattern and SOLID principles in order to have a better build. 
With this class we extend the generic QueryTemplate one with an anonymous class.

![BookDAO][5]

<br>

## :technologist: How To Set Up:

1. Clone the repo:
    `git clone https://github.com/valentinsoare/LibraryWithCRUDInMind.git`

2. Ensure that you have Docker and Docker-compose installed/

4. Make sure that you have the YML file called docker-compose.yml, and it is written to deploy the Mysql Server.
   * Docker file:
   ```yml
    version: '3.8'

    networks:
    default:

    services:
    db:
    image: mysql:8.0
    container_name: library
    ports:
    - 3306:3306
    volumes:
      - "./.data/db:/var/lib/mysql"
      environment:
      MYSQL_ROOT_PASSWORD: "XXXXX"
      MYSQL_DATABASE: "library_db"
   ```
   
4. Then please run `docker-compose up -d` in order to fetch the Mysql Server and deploy it. Then you can connect to it.

> [!NOTE]
> It is recommended you modify the docker-compose.yml file but only the user and password for DB.

### :computer: Dependencies and plugins for Maven

:white_check_mark: For the pom.xml file we only have two dependencies (Lombok and JDBC) and  
the necessary plugins for build (jar make) and download dependencies sources and documentation automatically.

1. Dependencies
```xml
<dependencies>
    <!-- LOMBOK -->
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.18.28</version>
      <scope>provided</scope>
    </dependency>


    <!-- JDBC Driver for MySQL -->
    <dependency>
      <groupId>com.mysql</groupId>
      <artifactId>mysql-connector-j</artifactId>
      <version>8.1.0</version>
    </dependency>

  </dependencies>
```

2. Plugins
```xml
<plugins>
        <!--_________________________________________________________________________________________________________-->
        <!--Used to build JAR with dependencies inside....with command # mvn compile assembly:single-->

        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-assembly-plugin</artifactId>
          <configuration>
            <archive>
              <manifest>
                <addClasspath>true</addClasspath>
                <mainClass>org.clibankinjava.App</mainClass>
              </manifest>
            </archive>
            <descriptorRefs>
              <descriptorRef>jar-with-dependencies</descriptorRef>
            </descriptorRefs>
          </configuration>
          <executions>
            <execution>
              <id>make-my-jar-with-dependencies</id>
              <phase>package</phase>
              <goals>
                <goal>single</goal>
              </goals>
            </execution>
          </executions>
        </plugin>

        <!--        To Download sources and documentation automatically-->
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-dependency-plugin</artifactId>
          <version>3.1.2</version>
          <executions>
            <execution>
              <goals>
                <goal>sources</goal>
                <goal>resolve</goal>
              </goals>
              <configuration>
                <classifier>javadoc</classifier>
              </configuration>
            </execution>
          </executions>
        </plugin>

        <!--_________________________________________________________________________________________________________-->
</plugins>
```

<br>

_Statistics_

[![HitCount](https://hits.dwyl.com/valentinsoare/LibraryWithCRUDInMind.svg?style=flat-square&show=unique)](http://hits.dwyl.com/valentinsoare/LibraryWithCRUDInMind)


_Social buttons_

[![valentinsoare - LibraryWithCRUDInMind](https://img.shields.io/static/v1?label=valentinsoare&message=LibraryWithCRUDInMind&color=blue&logo=github)](https://github.com/valentinsoare/LibraryWithCRUDInMind "Go to GitHub repo")
[![stars - LibraryWithCRUDInMind](https://img.shields.io/github/stars/valentinsoare/LibraryWithCRUDInMind?style=social)](https://github.com/valentinsoare/LibraryWithCRUDInMind)
[![forks - LibraryWithCRUDInMind](https://img.shields.io/github/forks/valentinsoare/LibraryWithCRUDInMind?style=social)](https://github.com/valentinsoare/LibraryWithCRUDInMind)


_Repo metadata_


[![GitHub tag](https://img.shields.io/github/tag/valentinsoare/LibraryWithCRUDInMind?include_prereleases=&sort=semver&color=blue)](https://github.com/valentinsoare/LibraryWithCRUDInMind/releases/)
[![License](https://img.shields.io/badge/License-MIT-blue)](#license)
[![issues - LibraryWithCRUDInMind](https://img.shields.io/github/issues/valentinsoare/LibraryWithCRUDInMind)](https://github.com/valentinsoare/LibraryWithCRUDInMind/issues)


## License

Released under [MIT](/LICENSE) by [@valentinsoare](https://github.com/valentinsoare)
:mailbox: [Contact me](soarevalentinn@gmail.com "Contact me at soarevalentinn@gmail.com")



[1]: <https://i.postimg.cc/DfLpz7ky/final-Small.png> (https://moviesondemand.io)
[2]: <https://i.postimg.cc/QNBM8Tcj/Screenshot-from-2023-12-19-11-04-48.png> (DataAccessObjectGenericIntefaceMethods)
[3]: <https://i.postimg.cc/FHL2K0ZX/Screenshot-from-2023-12-19-11-09-59.png> (QueryTemplate abstract class)
[4]: <https://i.postimg.cc/7Pm3C6PB/Screenshot-from-2023-12-19-11-19-36.png> (ConnectionToDB)
[5]: <https://i.postimg.cc/7Pm3C6PB/Screenshot-from-2023-12-19-11-19-36.png> (BookDAO)