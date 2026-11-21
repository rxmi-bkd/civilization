package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Civilization_createEntityTest {

  private Civilization civilization;

  @BeforeEach
  void setUp() {
    civilization = new Civilization();
  }

  @Test
  void shouldReturnAliveEntity() {
    // Act
    Entity entity = civilization.createEntity();

    // Assert
    boolean alive = civilization.isAlive(entity);
    assertTrue(alive);
  }

  @Test
  void shouldReturnDistinctEntitiesWhenCalledTwice() {
    // Act
    Entity first = civilization.createEntity();
    Entity second = civilization.createEntity();

    // Assert
    assertNotEquals(first, second);
  }
}
