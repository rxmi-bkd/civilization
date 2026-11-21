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
    Entity entity = manager.create();

    // Act
    manager.destroy(entity);

    // Assert
    boolean alive = manager.isAlive(entity);
    assertFalse(alive);
  }

  @Test
  void shouldKeepOtherEntitiesAliveWhenEntityIsDestroyed() {
    // Arrange
    Entity first = manager.create();
    Entity second = manager.create();

    // Act
    manager.destroy(first);

    // Assert
    boolean secondAlive = manager.isAlive(second);
    assertTrue(secondAlive);
  }

  @Test
  void shouldDoNothingWhenEntityIsAlreadyDestroyed() {
    // Arrange
    Entity entity = manager.create();
    manager.destroy(entity);

    // Act
    manager.destroy(entity);

    // Assert
    boolean alive = manager.isAlive(entity);
    assertFalse(alive);
  }

  @Test
  void shouldKeepCurrentOccupantAliveWhenStaleHandleOfReusedSlotIsDestroyed() {
    // Arrange
    Entity old = manager.create();
    manager.destroy(old);
    Entity current = manager.create();

    // Act
    manager.destroy(old);

    // Assert
    boolean currentAlive = manager.isAlive(current);
    assertTrue(currentAlive);
  }

  @Test
  void shouldDoNothingWhenEntityIsUnknown() {
    // Arrange
    Entity alive = manager.create();
    Entity unknown = new Entity(999, 0);

    // Act
    manager.destroy(unknown);

    // Assert
    boolean aliveStillAlive = manager.isAlive(alive);
    assertTrue(aliveStillAlive);
  }
}
