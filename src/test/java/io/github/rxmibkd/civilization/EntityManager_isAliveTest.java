package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntityManager_isAliveTest {

  private EntityManager manager;

  @BeforeEach
  void setUp() {
    manager = new EntityManager();
  }

  @Test
  void shouldReturnTrueWhenEntityIsCreated() {
    // Arrange
    Entity entity = manager.create();

    // Act
    boolean alive = manager.isAlive(entity);

    // Assert
    assertTrue(alive);
  }

  @Test
  void shouldReturnFalseWhenEntityIsDestroyed() {
    // Arrange
    Entity entity = manager.create();
    manager.destroy(entity);

    // Act
    boolean alive = manager.isAlive(entity);

    // Assert
    assertFalse(alive);
  }

  @Test
  void shouldReturnFalseWhenHandleIsStaleAfterSlotReuse() {
    // Arrange
    Entity old = manager.create();
    manager.destroy(old);
    manager.create();

    // Act
    boolean alive = manager.isAlive(old);

    // Assert
    assertFalse(alive);
  }

  @Test
  void shouldReturnFalseWhenIndexIsBeyondCreatedSlots() {
    // Arrange
    manager.create();
    Entity unknown = new Entity(999, 0);

    // Act
    boolean alive = manager.isAlive(unknown);

    // Assert
    assertFalse(alive);
  }

  @Test
  void shouldReturnFalseWhenIndexIsNegative() {
    // Arrange
    Entity negative = new Entity(-1, 0);

    // Act
    boolean alive = manager.isAlive(negative);

    // Assert
    assertFalse(alive);
  }

  @Test
  void shouldReturnFalseWhenGenerationIsNewerThanSlot() {
    // Arrange
    Entity entity = manager.create();
    int index = entity.index();
    int newerGeneration = entity.generation() + 1;
    Entity future = new Entity(index, newerGeneration);

    // Act
    boolean alive = manager.isAlive(future);

    // Assert
    assertFalse(alive);
  }
}
