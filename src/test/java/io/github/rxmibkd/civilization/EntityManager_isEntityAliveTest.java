package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntityManager_isEntityAliveTest {

  private EntityManager manager;

  @BeforeEach
  void setUp() {
    manager = new EntityManager();
  }

  @Test
  void shouldReturnTrueWhenEntityIsCreated() {
    // Arrange
    Entity entity = manager.createEntity();

    // Act
    boolean alive = manager.isEntityAlive(entity);

    // Assert
    assertTrue(alive);
  }

  @Test
  void shouldReturnFalseWhenEntityIsDestroyed() {
    // Arrange
    Entity entity = manager.createEntity();
    manager.deleteEntity(entity);

    // Act
    boolean alive = manager.isEntityAlive(entity);

    // Assert
    assertFalse(alive);
  }

  @Test
  void shouldReturnFalseWhenHandleIsStaleAfterSlotReuse() {
    // Arrange
    Entity old = manager.createEntity();
    manager.deleteEntity(old);
    manager.createEntity();

    // Act
    boolean alive = manager.isEntityAlive(old);

    // Assert
    assertFalse(alive);
  }

  @Test
  void shouldReturnFalseWhenIndexIsBeyondCreatedSlots() {
    // Arrange
    manager.createEntity();
    Entity unknown = new Entity(999, 0);

    // Act
    boolean alive = manager.isEntityAlive(unknown);

    // Assert
    assertFalse(alive);
  }

  @Test
  void shouldReturnFalseWhenIndexIsNegative() {
    // Arrange
    Entity negative = new Entity(-1, 0);

    // Act
    boolean alive = manager.isEntityAlive(negative);

    // Assert
    assertFalse(alive);
  }

  @Test
  void shouldReturnFalseWhenGenerationIsNewerThanSlot() {
    // Arrange
    Entity entity = manager.createEntity();
    int index = entity.index();
    int newerGeneration = entity.generation() + 1;
    Entity future = new Entity(index, newerGeneration);

    // Act
    boolean alive = manager.isEntityAlive(future);

    // Assert
    assertFalse(alive);
  }
}
