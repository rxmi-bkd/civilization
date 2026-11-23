package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComponentManager_addComponentTest {

  private record PositionComponent(int x, int y) implements Component {
  }

  private ComponentManager manager;

  @BeforeEach
  void setUp() {
    manager = new ComponentManager();
  }

  @Test
  void shouldMakeComponentRetrievableWhenEntityHasNone() {
    // Arrange
    Entity entity = new Entity(0, 0);
    HealthComponent health = new HealthComponent(10, 10);

    // Act
    manager.addComponent(entity, health);

    // Assert
    Optional<HealthComponent> result = manager.getComponent(entity, HealthComponent.class);
    Optional<HealthComponent> expected = Optional.of(health);
    assertEquals(expected, result);
  }

  @Test
  void shouldReplaceComponentWhenEntityAlreadyHasOneOfSameType() {
    // Arrange
    Entity entity = new Entity(0, 0);
    HealthComponent original = new HealthComponent(10, 10);
    HealthComponent replacement = new HealthComponent(5, 10);
    manager.addComponent(entity, original);

    // Act
    manager.addComponent(entity, replacement);

    // Assert
    Optional<HealthComponent> result = manager.getComponent(entity, HealthComponent.class);
    Optional<HealthComponent> expected = Optional.of(replacement);
    assertEquals(expected, result);
  }

  @Test
  void shouldKeepBothComponentsWhenTypesDiffer() {
    // Arrange
    Entity entity = new Entity(0, 0);
    HealthComponent health = new HealthComponent(10, 10);
    PositionComponent position = new PositionComponent(1, 2);

    // Act
    manager.addComponent(entity, health);
    manager.addComponent(entity, position);

    // Assert
    Optional<HealthComponent> healthResult = manager.getComponent(entity, HealthComponent.class);
    Optional<PositionComponent> positionResult = manager.getComponent(entity, PositionComponent.class);
    Optional<HealthComponent> expectedHealth = Optional.of(health);
    Optional<PositionComponent> expectedPosition = Optional.of(position);
    assertEquals(expectedHealth, healthResult);
    assertEquals(expectedPosition, positionResult);
  }

  @Test
  void shouldThrowWhenEntityIndexIsNegative() {
    // Arrange
    Entity entity = new Entity(-1, 0);
    HealthComponent health = new HealthComponent(10, 10);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> manager.addComponent(entity, health));
  }
}
