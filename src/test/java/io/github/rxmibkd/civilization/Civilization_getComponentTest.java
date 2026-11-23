package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Civilization_getComponentTest {

  private Civilization civilization;

  @BeforeEach
  void setUp() {
    civilization = new Civilization();
  }

  @Test
  void shouldReturnComponentWhenEntityHasOne() {
    // Arrange
    Entity entity = civilization.createEntity();
    HealthComponent health = new HealthComponent(10, 10);
    civilization.addComponent(entity, health);

    // Act
    Optional<HealthComponent> result = civilization.getComponent(entity, HealthComponent.class);

    // Assert
    Optional<HealthComponent> expected = Optional.of(health);
    assertEquals(expected, result);
  }

  @Test
  void shouldReturnEmptyWhenEntityHasNone() {
    // Arrange
    Entity entity = civilization.createEntity();

    // Act
    Optional<HealthComponent> result = civilization.getComponent(entity, HealthComponent.class);

    // Assert
    boolean present = result.isPresent();
    assertFalse(present);
  }

  @Test
  void shouldThrowWhenEntityIsNull() {
    // Arrange

    // Act & Assert
    assertThrows(NullPointerException.class, () -> civilization.getComponent(null, HealthComponent.class));
  }

  @Test
  void shouldThrowWhenTypeIsNull() {
    // Arrange
    Entity entity = civilization.createEntity();

    // Act & Assert
    assertThrows(NullPointerException.class, () -> civilization.getComponent(entity, null));
  }
}
