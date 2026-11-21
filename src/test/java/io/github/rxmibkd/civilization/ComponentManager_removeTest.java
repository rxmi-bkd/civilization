package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComponentManager_removeTest {

  private record PositionComponent(int x, int y) implements Component {
  }

  private ComponentManager manager;

  @BeforeEach
  void setUp() {
    manager = new ComponentManager();
  }

  @Test
  void shouldDetachComponentWhenEntityHasOneOfThatType() {
    // Arrange
    Entity entity = new Entity(0, 0);
    HealthComponent health = new HealthComponent(10, 10);
    manager.add(entity, health);

    // Act
    manager.remove(entity, HealthComponent.class);

    // Assert
    boolean present = manager.has(entity, HealthComponent.class);
    assertFalse(present);
  }

  @Test
  void shouldKeepOtherTypesWhenOneTypeIsRemoved() {
    // Arrange
    Entity entity = new Entity(0, 0);
    HealthComponent health = new HealthComponent(10, 10);
    PositionComponent position = new PositionComponent(1, 2);
    manager.add(entity, health);
    manager.add(entity, position);

    // Act
    manager.remove(entity, HealthComponent.class);

    // Assert
    boolean positionPresent = manager.has(entity, PositionComponent.class);
    assertTrue(positionPresent);
  }

  @Test
  void shouldDoNothingWhenTypeWasNeverAdded() {
    // Arrange
    Entity entity = new Entity(0, 0);

    // Act
    manager.remove(entity, HealthComponent.class);

    // Assert
    boolean present = manager.has(entity, HealthComponent.class);
    assertFalse(present);
  }

  @Test
  void shouldDoNothingWhenEntityHasNoComponentOfThatType() {
    // Arrange
    Entity owner = new Entity(0, 0);
    Entity other = new Entity(1, 0);
    HealthComponent health = new HealthComponent(10, 10);
    manager.add(owner, health);

    // Act
    manager.remove(other, HealthComponent.class);

    // Assert
    boolean ownerPresent = manager.has(owner, HealthComponent.class);
    assertTrue(ownerPresent);
  }
}
