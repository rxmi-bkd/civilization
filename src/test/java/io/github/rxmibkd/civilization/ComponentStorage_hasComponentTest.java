package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComponentStorage_hasComponentTest {

  private ComponentStorage<HealthComponent> storage;

  @BeforeEach
  void setUp() {
    storage = new ComponentStorage<>();
  }

  @Test
  void shouldReturnTrueWhenEntityHasComponent() {
    // Arrange
    Entity entity = new Entity(0, 0);
    HealthComponent health = new HealthComponent(10, 10);
    storage.addComponent(entity, health);

    // Act
    boolean result = storage.hasComponent(entity);

    // Assert
    assertTrue(result);
  }

  @Test
  void shouldReturnFalseWhenEntityHasNoComponent() {
    // Arrange
    Entity entity = new Entity(0, 0);

    // Act
    boolean result = storage.hasComponent(entity);

    // Assert
    assertFalse(result);
  }

  @Test
  void shouldReturnFalseWhenHandleIsStale() {
    // Arrange
    Entity current = new Entity(0, 1);
    Entity stale = new Entity(0, 0);
    HealthComponent health = new HealthComponent(10, 10);
    storage.addComponent(current, health);

    // Act
    boolean result = storage.hasComponent(stale);

    // Assert
    assertFalse(result);
  }
}
