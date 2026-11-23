package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Civilization_removeComponentTest {

  private Civilization civilization;

  @BeforeEach
  void setUp() {
    civilization = new Civilization();
  }

  @Test
  void shouldDetachComponentWhenEntityHasOne() {
    // Arrange
    Entity entity = civilization.createEntity();
    HealthComponent health = new HealthComponent(10, 10);
    civilization.addComponent(entity, health);

    // Act
    civilization.deleteComponent(entity, HealthComponent.class);

    // Assert
    boolean present = civilization.hasComponent(entity, HealthComponent.class);
    assertFalse(present);
  }

  @Test
  void shouldKeepEntityAliveWhenComponentIsRemoved() {
    // Arrange
    Entity entity = civilization.createEntity();
    HealthComponent health = new HealthComponent(10, 10);
    civilization.addComponent(entity, health);

    // Act
    civilization.deleteComponent(entity, HealthComponent.class);

    // Assert
    boolean alive = civilization.isEntityAlive(entity);
    assertTrue(alive);
  }

  @Test
  void shouldDoNothingWhenEntityHasNoComponent() {
    // Arrange
    Entity entity = civilization.createEntity();

    // Act
    civilization.deleteComponent(entity, HealthComponent.class);

    // Assert
    boolean present = civilization.hasComponent(entity, HealthComponent.class);
    assertFalse(present);
  }
}
