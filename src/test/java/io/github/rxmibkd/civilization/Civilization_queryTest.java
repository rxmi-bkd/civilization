package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Civilization_queryTest {

  private Civilization civilization;

  @BeforeEach
  void setUp() {
    civilization = new Civilization();
  }

  @Test
  void shouldReturnEntitiesHavingTheComponentWhenTheyExist() {
    // Arrange
    Entity withHealth = civilization.createEntity();
    Entity withoutHealth = civilization.createEntity();
    HealthComponent health = new HealthComponent(10, 10);
    civilization.addComponent(withHealth, health);

    // Act
    List<Entity> result = civilization.query(HealthComponent.class);

    // Assert
    List<Entity> expected = List.of(withHealth);
    assertEquals(expected, result);
  }

  @Test
  void shouldNotReturnEntityWhenItIsDestroyed() {
    // Arrange
    Entity entity = civilization.createEntity();
    HealthComponent health = new HealthComponent(10, 10);
    civilization.addComponent(entity, health);
    civilization.destroyEntity(entity);

    // Act
    List<Entity> result = civilization.query(HealthComponent.class);

    // Assert
    boolean empty = result.isEmpty();
    assertTrue(empty);
  }
}
