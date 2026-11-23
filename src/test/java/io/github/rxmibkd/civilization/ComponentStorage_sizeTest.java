package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComponentStorage_sizeTest {

  private ComponentStorage<HealthComponent> storage;

  @BeforeEach
  void setUp() {
    storage = new ComponentStorage<>();
  }

  @Test
  void shouldReturnZeroWhenStorageIsEmpty() {
    // Act
    int size = storage.size();

    // Assert
    assertEquals(0, size);
  }

  @Test
  void shouldReturnNumberOfComponentsWhenComponentsAreAdded() {
    // Arrange
    Entity first = new Entity(0, 0);
    Entity second = new Entity(1, 0);
    HealthComponent health = new HealthComponent(10, 10);
    storage.addComponent(first, health);
    storage.addComponent(second, health);

    // Act
    int size = storage.size();

    // Assert
    assertEquals(2, size);
  }
}
