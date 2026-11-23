package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Civilization_hasComponentTest {

  private Civilization civilization;

  @BeforeEach
  void setUp() {
    civilization = new Civilization();
  }

  @Test
  void shouldReturnTrueWhenEntityHasComponent() {
    // Arrange
    Entity entity = civilization.createEntity();
    HealthComponent health = new HealthComponent(10, 10);
    civilization.addComponent(entity, health);

    // Act
    boolean result = civilization.hasComponent(entity, HealthComponent.class);

    // Assert
    assertTrue(result);
  }

  @Test
  void shouldReturnFalseWhenEntityHasNoComponent() {
    // Arrange
    Entity entity = civilization.createEntity();

    // Act
    boolean result = civilization.hasComponent(entity, HealthComponent.class);

    // Assert
    assertFalse(result);
  }

  @Test
  void shouldThrowWhenEntityIsNull() {
    // Arrange

    // Act & Assert
    assertThrows(NullPointerException.class, () -> civilization.hasComponent(null, HealthComponent.class));
  }

  @Test
  void shouldThrowWhenTypeIsNull() {
    // Arrange
    Entity entity = civilization.createEntity();

    // Act & Assert
    assertThrows(NullPointerException.class, () -> civilization.hasComponent(entity, null));
  }
}
