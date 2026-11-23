package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComponentStorage_getComponentTest {

  private ComponentStorage<HealthComponent> storage;

  @BeforeEach
  void setUp() {
    storage = new ComponentStorage<>();
  }

  @Test
  void shouldReturnComponentWhenEntityHasOne() {
    // Arrange
    Entity entity = new Entity(3, 0);
    HealthComponent health = new HealthComponent(10, 10);
    storage.addComponent(entity, health);

    // Act
    Optional<HealthComponent> result = storage.getComponent(entity);

    // Assert
    Optional<HealthComponent> expected = Optional.of(health);
    assertEquals(expected, result);
  }

  @Test
  void shouldReturnEmptyWhenEntityHasNone() {
    // Arrange
    Entity entity = new Entity(3, 0);

    // Act
    Optional<HealthComponent> result = storage.getComponent(entity);

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
    storage.addComponent(current, health);

    // Act
    Optional<HealthComponent> result = storage.getComponent(stale);

    // Assert
    boolean present = result.isPresent();
    assertFalse(present);
  }
}
