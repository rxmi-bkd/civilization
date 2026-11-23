package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Civilization_addComponentTest {

  private Civilization civilization;

  @BeforeEach
  void setUp() {
    civilization = new Civilization();
  }

  @Test
  void shouldMakeComponentRetrievableWhenEntityIsAlive() {
    // Arrange
    Entity entity = civilization.createEntity();
    HealthComponent health = new HealthComponent(10, 10);

    // Act
    civilization.addComponent(entity, health);

    // Assert
    Optional<HealthComponent> result = civilization.getComponent(entity, HealthComponent.class);
    Optional<HealthComponent> expected = Optional.of(health);
    assertEquals(expected, result);
  }

  @Test
  void shouldThrowWhenEntityIsDestroyed() {
    // Arrange
    Entity entity = civilization.createEntity();
    HealthComponent health = new HealthComponent(10, 10);
    civilization.deleteEntity(entity);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> civilization.addComponent(entity, health));
  }

  @Test
  void shouldThrowWhenEntityWasNeverCreated() {
    // Arrange
    Entity entity = new Entity(42, 0);
    HealthComponent health = new HealthComponent(10, 10);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> civilization.addComponent(entity, health));
  }

  @Test
  void shouldThrowWhenEntityIsNull() {
    // Arrange
    HealthComponent health = new HealthComponent(10, 10);

    // Act & Assert
    assertThrows(NullPointerException.class, () -> civilization.addComponent(null, health));
  }

  @Test
  void shouldThrowWhenComponentIsNull() {
    // Arrange
    Entity entity = civilization.createEntity();

    // Act & Assert
    assertThrows(NullPointerException.class, () -> civilization.addComponent(entity, null));
  }
}
