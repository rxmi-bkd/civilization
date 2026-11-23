package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComponentManager_queryTest {

  private record PositionComponent(int x, int y) implements Component {
  }

  private ComponentManager manager;

  @BeforeEach
  void setUp() {
    manager = new ComponentManager();
  }

  @Test
  void shouldReturnEntitiesHavingTheTypeWhenSingleTypeIsGiven() {
    // Arrange
    Entity first = new Entity(0, 0);
    Entity second = new Entity(1, 0);
    Entity third = new Entity(2, 0);
    HealthComponent health = new HealthComponent(10, 10);
    PositionComponent position = new PositionComponent(1, 2);
    manager.addComponent(first, health);
    manager.addComponent(second, position);
    manager.addComponent(third, health);

    // Act
    List<Entity> result = manager.query(HealthComponent.class);

    // Assert
    List<Entity> expected = List.of(first, third);
    assertEquals(expected, result);
  }

  @Test
  void shouldReturnOnlyEntitiesHavingEveryTypeWhenSeveralTypesAreGiven() {
    // Arrange
    Entity both = new Entity(0, 0);
    Entity healthOnly = new Entity(1, 0);
    Entity positionOnly = new Entity(2, 0);
    HealthComponent health = new HealthComponent(10, 10);
    PositionComponent position = new PositionComponent(1, 2);
    manager.addComponent(both, health);
    manager.addComponent(both, position);
    manager.addComponent(healthOnly, health);
    manager.addComponent(positionOnly, position);

    // Act
    List<Entity> result = manager.query(HealthComponent.class, PositionComponent.class);

    // Assert
    List<Entity> expected = List.of(both);
    assertEquals(expected, result);
  }

  @Test
  void shouldReturnEmptyListWhenATypeWasNeverAdded() {
    // Arrange
    Entity entity = new Entity(0, 0);
    HealthComponent health = new HealthComponent(10, 10);
    manager.addComponent(entity, health);

    // Act
    List<Entity> result = manager.query(HealthComponent.class, PositionComponent.class);

    // Assert
    boolean empty = result.isEmpty();
    assertTrue(empty);
  }

  @Test
  void shouldReturnEmptyListWhenNoEntityHasEveryType() {
    // Arrange
    Entity healthOnly = new Entity(0, 0);
    Entity positionOnly = new Entity(1, 0);
    HealthComponent health = new HealthComponent(10, 10);
    PositionComponent position = new PositionComponent(1, 2);
    manager.addComponent(healthOnly, health);
    manager.addComponent(positionOnly, position);

    // Act
    List<Entity> result = manager.query(HealthComponent.class, PositionComponent.class);

    // Assert
    boolean empty = result.isEmpty();
    assertTrue(empty);
  }

  @Test
  void shouldNotChangeResultWhenComponentIsRemovedAfterQuery() {
    // Arrange
    Entity entity = new Entity(0, 0);
    HealthComponent health = new HealthComponent(10, 10);
    manager.addComponent(entity, health);
    List<Entity> result = manager.query(HealthComponent.class);

    // Act
    manager.deleteComponent(entity, HealthComponent.class);

    // Assert
    List<Entity> expected = List.of(entity);
    assertEquals(expected, result);
  }

  @Test
  void shouldThrowWhenNoTypeIsGiven() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> manager.query());
  }
}
