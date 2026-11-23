package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComponentStorage_getEntitiesTest {

  private ComponentStorage<HealthComponent> storage;

  @BeforeEach
  void setUp() {
    storage = new ComponentStorage<>();
  }

  @Test
  void shouldReturnEmptyListWhenStorageIsEmpty() {
    // Act
    List<Entity> result = storage.getEntities();

    // Assert
    boolean empty = result.isEmpty();
    assertTrue(empty);
  }

  @Test
  void shouldReturnEntitiesHoldingAComponentWhenComponentsAreAdded() {
    // Arrange
    Entity first = new Entity(0, 0);
    Entity second = new Entity(3, 0);
    HealthComponent health = new HealthComponent(10, 10);
    storage.addComponent(first, health);
    storage.addComponent(second, health);

    // Act
    List<Entity> result = storage.getEntities();

    // Assert
    List<Entity> expected = List.of(first, second);
    assertEquals(expected, result);
  }

  @Test
  void shouldNotContainEntityWhenItsComponentIsRemoved() {
    // Arrange
    Entity first = new Entity(0, 0);
    Entity second = new Entity(1, 0);
    HealthComponent health = new HealthComponent(10, 10);
    storage.addComponent(first, health);
    storage.addComponent(second, health);
    storage.deleteComponent(first);

    // Act
    List<Entity> result = storage.getEntities();

    // Assert
    List<Entity> expected = List.of(second);
    assertEquals(expected, result);
  }

  @Test
  void shouldReturnReadOnlyListWhenCalled() {
    // Arrange
    Entity entity = new Entity(0, 0);
    List<Entity> result = storage.getEntities();

    // Act & Assert
    assertThrows(UnsupportedOperationException.class, () -> result.add(entity));
  }
}
