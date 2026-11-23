package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class Entity_constructorTest {

  @Test
  void shouldThrowWhenIndexIsNegative() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> new Entity(-1, 0));
  }

  @Test
  void shouldThrowWhenGenerationIsNegative() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> new Entity(0, -1));
  }

  @Test
  void shouldKeepValuesWhenTheyAreZero() {
    // Act
    Entity entity = new Entity(0, 0);

    // Assert
    assertEquals(0, entity.index());
    assertEquals(0, entity.generation());
  }
}
