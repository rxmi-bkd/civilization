package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Civilization_deleteEntityTest {

  private Civilization civilization;

  @BeforeEach
  void setUp() {
    civilization = new Civilization();
  }

  @Test
  void shouldMakeEntityNoLongerAliveWhenEntityIsAlive() {
    // Arrange
    Entity entity = civilization.createEntity();

    // Act
    civilization.deleteEntity(entity);

    // Assert
    boolean alive = civilization.isEntityAlive(entity);
    assertFalse(alive);
  }

  @Test
  void shouldDetachComponentsWhenEntityIsAlive() {
    // Arrange
    Entity entity = civilization.createEntity();
    HealthComponent health = new HealthComponent(10, 10);
    civilization.addComponent(entity, health);

    // Act
    civilization.deleteEntity(entity);

    // Assert
    boolean present = civilization.hasComponent(entity, HealthComponent.class);
    assertFalse(present);
  }

  @Test
  void shouldNotLeakComponentsToNewEntityWhenSlotIsReused() {
    // Arrange
    Entity old = civilization.createEntity();
    HealthComponent health = new HealthComponent(10, 10);
    civilization.addComponent(old, health);
    civilization.deleteEntity(old);

    // Act
    Entity reused = civilization.createEntity();

    // Assert
    boolean present = civilization.hasComponent(reused, HealthComponent.class);
    assertFalse(present);
  }

  @Test
  void shouldKeepOtherEntitiesComponentsWhenEntityIsDestroyed() {
    // Arrange
    Entity destroyed = civilization.createEntity();
    Entity other = civilization.createEntity();
    HealthComponent health = new HealthComponent(10, 10);
    civilization.addComponent(destroyed, health);
    civilization.addComponent(other, health);

    // Act
    civilization.deleteEntity(destroyed);

    // Assert
    boolean present = civilization.hasComponent(other, HealthComponent.class);
    assertTrue(present);
  }

  @Test
  void shouldKeepCurrentOccupantWhenStaleHandleIsDestroyed() {
    // Arrange
    Entity old = civilization.createEntity();
    civilization.deleteEntity(old);
    Entity current = civilization.createEntity();
    HealthComponent health = new HealthComponent(10, 10);
    civilization.addComponent(current, health);

    // Act
    civilization.deleteEntity(old);

    // Assert
    boolean alive = civilization.isEntityAlive(current);
    boolean present = civilization.hasComponent(current, HealthComponent.class);
    assertTrue(alive);
    assertTrue(present);
  }
}
