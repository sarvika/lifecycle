# Lifecycle

This project is a clone of [Apache](http://apache.org)'s Lifecycle API that they use in many projects like Tomcat and Maven.

This project aims to allow developers by allowing them to write *containers* for a service with ease and give them an 
actual *lifecycle* by providing them a mechanism to easily *initialize*, *start* and *stop* them.

When implemented in a software, this project also allows developers to write *plugins* for their own containers using an
event listener mechanism.

## Lifecycle States

This is the list of valid states for components that implement Lifecycle.

- NEW
- INITIALIZING
- INITIALIZED
- STARTING_PREP
- STARTING
- STARTED
- STOPPING_PREP
- STOPPING
- STOPPED
- DESTROYING
- DESTROYED
- FAILED

### State transition

(From `Lifecycle.java`)

```
start()
   -----------------------------
   |                           |
   | init()                    |
  NEW ->-- INITIALIZING        |
  | |           |              |     ------------------<-----------------------
  | |           |auto          |     |                                        |
  | |          \|/    start() \|/   \|/     auto          auto         stop() |
  | |      INITIALIZED -->-- STARTING_PREP -->- STARTING -->- STARTED -->---  |
  | |         |                                                            |  |
  | |destroy()|                                                            |  |
  | -->-----<--    ------------------------<--------------------------------  ^
  |     |          |                                                          |
  |     |         \|/          auto                 auto              start() |
  |     |     STOPPING_PREP ---->---- STOPPING ------>----- STOPPED ----->-----
  |    \|/                               ^                     |  ^
  |     |               stop()           |                     |  |
  |     |       --------------------------                     |  |
  |     |       |                                              |  |
  |     |       |    destroy()                       destroy() |  |
  |     |    FAILED ---->------ DESTROYING ---<-----------------  |
  |     |                        ^     |                          |
  |     |     destroy()          |     |auto                      |
  |     -------->-----------------    \|/                         |
  |                                 DESTROYED                     |
  |                                                               |
  |                            stop()                             |
  --->------------------------------>------------------------------
```

 * Any state can transition to `FAILED`. 
 * Calling `start()` while a component is in states `STARTING_PREP`, `STARTING` or `STARTED` has no effect. 
 * Calling `start()` while a component is in state `NEW` will cause `init()` to be called immediately after the `start()` method is entered. 
 * Calling `stop()` while a component is in states `STOPPING_PREP`, `STOPPING` or `STOPPED` has no effect. 
 * Calling `stop()` while a component is in state `NEW` transitions the component to `STOPPED`. This is typically encountered when a component fails to start and does not start all its sub-components. When the component is stopped, it will try to stop all sub-components - even those it didn't start.
 * Attempting any other transition will throw `LifecycleException`.

## Including Lifecycle in your Maven project

Add the following in your `pom.xml` so that your project is aware of [our artifact repository](https://opensource.sarvika.com/maven-repo/).

```xml
<repositories>
    <repository>
        <id>sarvika-maven-repo</id>
        <url>https://raw.githubusercontent.com/sarvika/maven-repo/master</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

Then you can add the following dependency:

```xml
<dependency>
    <groupId>io.sarvika</groupId>
    <artifactId>lifecycle</artifactId>
    <version>1.0</version>
</dependency>
```

## Using Lifecycle

To create any Lifecycle components, you should extend the classes with `LifecycleBase`. This will let you implement the
`initInternal()`, `startInternal()`, `stopInternal()` and `destroyInternal()` methods. The class `LifecycleBase` already
implements all of the `Lifecycle` flow that's documented above. `LifecycleBase` also expects the container to set it's
state to `STARTING` after it hits `STARTING_PREP`. This could be achieved in two ways:

1. Calling `setState(LifecycleState.STARTED)` in `startInternal()`.
2. Calling `lifecycleEvent.getLifecycle().setState(LifecycleState.STARTED)` from a `LifecycleListener`.

### Example

#### Container example

```java
public class Container extends LifecycleBase {

    private static final Log log = LogFactory.getLog(Container.class);

    @Override
    protected void initInternal() throws LifecycleException {
        log.info("Lifecycle initialized");
    }
    
    @Override
    protected void startInternal() throws LifecycleException {
    	log.info("Lifecycle started");
        setState(LifecycleState.STARTED);
    }
	
    @Override
    protected void stopInternal() throws LifecycleException {
        log.info("Lifecycle stopped");
    }
    
    @Override
    protected void destroyInternal() throws LifecycleException {
    	log.info("Lifecycle destroyed");
    }
	
}
```

Then you can just use `container.init()`, `container.start()`, `container.stop()` and `container.destroy()` to control
it's lifecycle.

#### Lifecycle listener example

```java
public class ContainerListener implements LifecycleListener {
	
    private static final Log log = LogFactory.getLog(ContainerListener.class);
	
    @Override
    public void lifecycleEvent(LifecycleEvent event) {
    	
    	// This will contain one of the event codes defined in the Lifecycle interface
    	String eventCode = event.getType();
    	log.info(eventCode);
    }
	
}
```

Then to register this with the container, you can write `container.addLifecycleListener(containerListener)`.

---

You can also have a look at the [javadoc](https://sarvika.github.io/lifecycle/docs/index.html) of this project.

We always encourage the users of our projects to contribute to them by improving source code or providing more
documentation and examples.

---

[Sarvika Opensource](https://opensource.sarvika.com) Team
