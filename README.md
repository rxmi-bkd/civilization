# Civilization

## Installation

The library is published to GitHub Packages. Add the repository and the dependency to your `pom.xml`:

```xml
<repositories>
  <repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/rxmi-bkd/civilization</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>io.github.rxmibkd</groupId>
    <artifactId>civilization</artifactId>
    <version>VERSION</version>
  </dependency>
</dependencies>
```

GitHub Packages requires authentication, even for public packages: add a `github` server with a personal access token (`read:packages`) to your `~/.m2/settings.xml`.

## Quick start

```java
import io.github.rxmibkd.civilization.Civilization;
import io.github.rxmibkd.civilization.Component;
import io.github.rxmibkd.civilization.Entity;

record Position(double x, double y) implements Component {}
record Velocity(double dx, double dy) implements Component {}

Civilization civilization = new Civilization();

// Create an entity and attach components
Entity entity = civilization.createEntity();
civilization.addComponent(entity, new Position(0, 0));
civilization.addComponent(entity, new Velocity(1, 2));

// A system queries the entities it cares about and replaces their components
civilization.addSystem((c, deltaTime) -> {
  for (Entity e : c.query(Position.class, Velocity.class)) {
    Position p = c.getComponent(e, Position.class).orElseThrow();
    Velocity v = c.getComponent(e, Velocity.class).orElseThrow();
    c.addComponent(e, new Position(p.x() + v.dx() * deltaTime, p.y() + v.dy() * deltaTime));
  }
});

// Run all systems, in registration order
civilization.update(0.016);
```

## License

[MIT](LICENSE)
