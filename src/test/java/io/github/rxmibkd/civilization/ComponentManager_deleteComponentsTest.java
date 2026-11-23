package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComponentManager_deleteComponentsTest {

  private record PositionComponent(int x, int y) implements Component {
  }

  private ComponentManager manager;

  @BeforeEach
  void setUp() {
    manager = new ComponentManager();
  }

  @Test
  void shouldDetachEveryComponentWhenEntityHasSeveral() {
    // Arrange
    Entity entity = new Entity(0, 0);
    HealthComponent health = new HealthComponent(10, 10);
    PositionComponent position = new PositionComponent(1, 2);
    manager.addComponent(entity, health);
    manager.addComponent(entity, position);

    // Act
    manager.deleteComponents(entity);

    // Assert
    boolean healthPresent = manager.hasComponent(entity, HealthComponent.class);
    boolean positionPresent = manager.hasComponent(entity, PositionComponent.class);
    assertFalse(healthPresent);
    assertFalse(positionPresent);
  }

  @Test
  void shouldKeepOtherEntitiesComponentsWhenOneEntityIsCleared() {
    // Arrange
    Entity cleared = new Entity(0, 0);
    Entity other = new Entity(1, 0);
    HealthComponent health = new HealthComponent(10, 10);
    manager.addComponent(cleared, health);
    manager.addComponent(other, health);

    // Act
    manager.deleteComponents(cleared);

    // Assert
    boolean otherPresent = manager.hasComponent(other, HealthComponent.class);
    assertTrue(otherPresent);
  }

  @Test
  void shouldDoNothingWhenEntityHasNoComponents() {
    // Arrange
    Entity owner = new Entity(0, 0);
    Entity other = new Entity(1, 0);
    HealthComponent health = new HealthComponent(10, 10);
    manager.addComponent(owner, health);

    // Act
    manager.deleteComponents(other);

    // Assert
    boolean ownerPresent = manager.hasComponent(owner, HealthComponent.class);
    assertTrue(ownerPresent);
  }
}
