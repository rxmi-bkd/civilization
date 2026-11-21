package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComponentManager_hasTest {

  private record PositionComponent(int x, int y) implements Component {
  }

  private ComponentManager manager;

  @BeforeEach
  void setUp() {
    manager = new ComponentManager();
  }

  @Test
  void shouldReturnTrueWhenEntityHasComponentOfThatType() {
    // Arrange
    Entity entity = new Entity(0, 0);
    HealthComponent health = new HealthComponent(10, 10);
    manager.add(entity, health);

    // Act
    boolean result = manager.has(entity, HealthComponent.class);

    // Assert
    assertTrue(result);
  }

  @Test
  void shouldReturnFalseWhenTypeWasNeverAdded() {
    // Arrange
    Entity entity = new Entity(0, 0);

    // Act
    boolean result = manager.has(entity, HealthComponent.class);

    // Assert
    assertFalse(result);
  }

  @Test
  void shouldReturnFalseWhenEntityHasOnlyOtherTypes() {
    // Arrange
    Entity entity = new Entity(0, 0);
    PositionComponent position = new PositionComponent(1, 2);
    manager.add(entity, position);

    // Act
    boolean result = manager.has(entity, HealthComponent.class);

    // Assert
    assertFalse(result);
  }
}
