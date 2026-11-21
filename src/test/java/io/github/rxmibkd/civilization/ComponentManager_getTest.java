package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComponentManager_getTest {

  private record PositionComponent(int x, int y) implements Component {}

  private ComponentManager manager;

  @BeforeEach
  void setUp() {
    manager = new ComponentManager();
  }

  @Test
  void shouldReturnComponentWhenEntityHasOneOfThatType() {
    // Arrange
    Entity entity = new Entity(0, 0);
    HealthComponent health = new HealthComponent(10, 10);
    manager.add(entity, health);

    // Act
    Optional<HealthComponent> result = manager.get(entity, HealthComponent.class);

    // Assert
    Optional<HealthComponent> expected = Optional.of(health);
    assertEquals(expected, result);
  }

  @Test
  void shouldReturnEmptyWhenTypeWasNeverAdded() {
    // Arrange
    Entity entity = new Entity(0, 0);

    // Act
    Optional<HealthComponent> result = manager.get(entity, HealthComponent.class);

    // Assert
    boolean present = result.isPresent();
    assertFalse(present);
  }

  @Test
  void shouldReturnEmptyWhenEntityHasOnlyOtherTypes() {
    // Arrange
    Entity entity = new Entity(0, 0);
    PositionComponent position = new PositionComponent(1, 2);
    manager.add(entity, position);

    // Act
    Optional<HealthComponent> result = manager.get(entity, HealthComponent.class);

    // Assert
    boolean present = result.isPresent();
    assertFalse(present);
  }

  @Test
  void shouldReturnEmptyWhenHandleIsStale() {
    // Arrange
    Entity current = new Entity(0, 1);
    Entity stale = new Entity(0, 0);
    HealthComponent health = new HealthComponent(10, 10);
    manager.add(current, health);

    // Act
    Optional<HealthComponent> result = manager.get(stale, HealthComponent.class);

    // Assert
    boolean present = result.isPresent();
    assertFalse(present);
  }
}
