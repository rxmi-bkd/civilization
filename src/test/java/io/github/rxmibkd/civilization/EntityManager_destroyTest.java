package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntityManager_destroyTest {

  private EntityManager manager;

  @BeforeEach
  void setUp() {
    manager = new EntityManager();
  }

  @Test
  void shouldMakeEntityNoLongerAliveWhenEntityIsAlive() {
    // Arrange
    Entity entity = manager.createEntity();

    // Act
    manager.deleteEntity(entity);

    // Assert
    boolean alive = manager.isEntityAlive(entity);
    assertFalse(alive);
  }

  @Test
  void shouldKeepOtherEntitiesAliveWhenEntityIsDestroyed() {
    // Arrange
    Entity first = manager.createEntity();
    Entity second = manager.createEntity();

    // Act
    manager.deleteEntity(first);

    // Assert
    boolean secondAlive = manager.isEntityAlive(second);
    assertTrue(secondAlive);
  }

  @Test
  void shouldDoNothingWhenEntityIsAlreadyDestroyed() {
    // Arrange
    Entity entity = manager.createEntity();
    manager.deleteEntity(entity);

    // Act
    manager.deleteEntity(entity);

    // Assert
    boolean alive = manager.isEntityAlive(entity);
    assertFalse(alive);
  }

  @Test
  void shouldKeepCurrentOccupantAliveWhenStaleHandleOfReusedSlotIsDestroyed() {
    // Arrange
    Entity old = manager.createEntity();
    manager.deleteEntity(old);
    Entity current = manager.createEntity();

    // Act
    manager.deleteEntity(old);

    // Assert
    boolean currentAlive = manager.isEntityAlive(current);
    assertTrue(currentAlive);
  }

  @Test
  void shouldDoNothingWhenEntityIsUnknown() {
    // Arrange
    Entity alive = manager.createEntity();
    Entity unknown = new Entity(999, 0);

    // Act
    manager.deleteEntity(unknown);

    // Assert
    boolean aliveStillAlive = manager.isEntityAlive(alive);
    assertTrue(aliveStillAlive);
  }
}
