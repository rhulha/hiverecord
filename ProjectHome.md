# Introduction #
HiveRecord let you use the ActiveRecord feature in your Hibernate project. Here is a simple example to show the power of HiveRecord.

```
// Insert a message to Database
Message message = new Message("HiveRecord");
message.setNextMessage(new Message("Next"));
Long id = message.save();
...

// Select a message
Message message = Message.find(Message.class, id);

@Entity
public class Message extends HiveRecord<Message> {
	@Id
	@GeneratedValue
	private Long id;
	private String message;
	@ManyToOne(cascade = CascadeType.ALL)
	private Message nextMessage;
        ...
} 
```

Now, HiveRecord 0.1 version is available in Maven repository.

Similar projects
  1. [Generic Data Access Objects](https://www.hibernate.org/328.html) I found this after implementing HiveRecord. This approach is similar to HiveRecord.
  1. [ActiveJDBC](http://igorpolevoy.blogspot.com/2010/03/activejdbc-features-birds-view.html) ActiveJDBC seems like based on Map and JDBC. But I don`t know details because source is not opened yet.
# Dependency setting in Maven #
1. Setting HiveRecord Dependency
```
// Last stable version
<repository>
  <id>hiverecord-maven-repository-snapshot</id>
  <url>http://hiverecord.googlecode.com/svn/maven/snapshot-repository</url>
</repository>    		
<repository>
  <id>hiverecord-maven-repository</id>
  <url>http://hiverecord.googlecode.com/svn/maven/repository</url>
</repository>    		

<dependencies>
  <dependency>
    <groupId>com.googlecode</groupId>
    <artifactId>hiverecord</artifactId>
    <version>0.2-SNAPSHOT</version>
  </dependency>
</dependencies>
```
2. Setting Your Hibernate EntityManager version
```
<dependency>
  <groupId>org.hibernate</groupId>
  <artifactId>hibernate-entitymanager</artifactId>
  <!-- Version can be changed accroding to your preference. -->
  <version>3.4.0.GA</version>
  <scope>provided</scope>
</dependency>
```
# Getting Strated #
1. First, you should register your Hibernate SessionFactory or JPA EntityManagerFactory to EntitySessionFactory. HiveRecord uses EntitySessionFactory to obtain Hibernate Session or JPA EntityManager.
```
EntitySessionFactory.register(yourHibernateSessionFactory);

or

EntitySessionFactory.register(yourJPAEntityManagerFactory);
```

2. Your entity should extend HiveRecord to use the APIs of HiveRecord.
```
public class Message extends HiveRecord<Message> {
}
```

3. Write your code included fields and JPA annotations. Notice that you should follow JPA(JSR 317, http://jcp.org/en/jsr/detail?id=317).
```
@Entity
public class Message extends HiveRecord<Message> {
	@Id
	@GeneratedValue
	private Long id;
	private String message;
	@ManyToOne(cascade = CascadeType.ALL)
	private Message nextMessage;
} 
```

4. Use it!
```
// Persist
Message message = new Message("HiveRecord");
message.setNextMessage(new Message("Next"));
message.persist();

// Find
Message message = Message.find(Message.class, 1L);
```

If you want more examples, see our [test](http://code.google.com/p/hiverecord/source/browse/trunk/src/test/java/com/googlecode/hiverecord/JPAEntitySessionTest.java). That test includes more examples.

# Backlog #
## Done ##
  1. Version 0.1(Released)
    1. ~~JPA EntityManager support~~ [Done in Rev5](http://code.google.com/p/hiverecord/source/detail?r=5)
    1. ~~External User-Transaction support~~ [Done in Rev16](http://code.google.com/p/hiverecord/source/detail?r=16)
  1. Version 0.2(SNAPSHOT)
    1. ~~Count support on HiveRecord~~ [Done in Rev60](http://code.google.com/p/hiverecord/source/detail?r=60)
    1. ~~Top support on HiveRecord~~ [Done in Rev63](http://code.google.com/p/hiverecord/source/detail?r=63)
## Doing ##
  1. Version 0.2
    1. Paging support on HiveRecord
## Ready ##
  1. Unknown
    1. Interfaces support like the method\_missing of ROR
    1. Integration support with Spring
    1. JDBC support
    1. If an user wants to specify own finder?
# Got support from YourKit #
YourKit is kindly supporting open source projects with its full-featured Java Profiler.
YourKit, LLC is the creator of innovative and intelligent tools for profiling
Java and .NET applications. Take a look at YourKit's leading software products:
<a href='http://www.yourkit.com/java/profiler/index.jsp'>YourKit Java Profiler</a> and
<a href='http://www.yourkit.com/.net/profiler/index.jsp'>YourKit .NET Profiler</a>.
