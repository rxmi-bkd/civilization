package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComponentManager_addTest {

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
    manager.add(entity, health);

    // Assert
    Optional<HealthComponent> result = manager.get(entity, HealthComponent.class);
    Optional<HealthComponent> expected = Optional.of(health);
    assertEquals(expected, result);
  }

  @Test
  void shouldReplaceComponentWhenEntityAlreadyHasOneOfSameType() {
    // Arrange
    Entity entity = new Entity(0, 0);
    HealthComponent original = new HealthComponent(10, 10);
    HealthComponent replacement = new HealthComponent(5, 10);
    manager.add(entity, original);

    // Act
    manager.add(entity, replacement);

    // Assert
    Optional<HealthComponent> result = manager.get(entity, HealthComponent.class);
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
    manager.add(entity, health);
    manager.add(entity, position);

    // Assert
    Optional<HealthComponent> healthResult = manager.get(entity, HealthComponent.class);
    Optional<PositionComponent> positionResult = manager.get(entity, PositionComponent.class);
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
    assertThrows(IllegalArgumentException.class, () -> manager.add(entity, health));
  }
}
