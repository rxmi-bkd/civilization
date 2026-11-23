package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Civilization_isEntityAliveTest {

  private Civilization civilization;

  @BeforeEach
  void setUp() {
    civilization = new Civilization();
  }

  @Test
  void shouldReturnTrueWhenEntityIsCreated() {
    // Arrange
    Entity entity = civilization.createEntity();

    // Act
    boolean result = civilization.isEntityAlive(entity);

    // Assert
    assertTrue(result);
  }

  @Test
  void shouldReturnFalseWhenEntityIsDestroyed() {
    // Arrange
    Entity entity = civilization.createEntity();
    civilization.deleteEntity(entity);

    // Act
    boolean result = civilization.isEntityAlive(entity);

    // Assert
    assertFalse(result);
  }

  @Test
  void shouldThrowWhenEntityIsNull() {
    // Arrange

    // Act & Assert
    assertThrows(NullPointerException.class, () -> civilization.isEntityAlive(null));
  }
}
