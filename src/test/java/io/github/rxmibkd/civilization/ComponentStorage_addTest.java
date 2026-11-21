package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComponentStorage_addTest {

  private ComponentStorage<HealthComponent> storage;

  @BeforeEach
  void setUp() {
    storage = new ComponentStorage<>();
  }

  @Test
  void shouldMakeComponentRetrievableWhenEntityHasNone() {
    // Arrange
    Entity entity = new Entity(0, 0);
    HealthComponent health = new HealthComponent(10, 10);

    // Act
    storage.add(entity, health);

    // Assert
    Optional<HealthComponent> result = storage.get(entity);
    Optional<HealthComponent> expected = Optional.of(health);
    assertEquals(expected, result);
  }

  @Test
  void shouldReplaceComponentWhenEntityAlreadyHasOne() {
    // Arrange
    Entity entity = new Entity(0, 0);
    HealthComponent original = new HealthComponent(10, 10);
    HealthComponent replacement = new HealthComponent(5, 10);
    storage.add(entity, original);

    // Act
    storage.add(entity, replacement);

    // Assert
    Optional<HealthComponent> result = storage.get(entity);
    Optional<HealthComponent> expected = Optional.of(replacement);
    int size = storage.size();
    assertEquals(expected, result);
    assertEquals(1, size);
  }

  @Test
  void shouldReplaceStaleComponentWhenSlotIsReusedByNewGeneration() {
    // Arrange
    Entity old = new Entity(0, 0);
    Entity current = new Entity(0, 1);
    HealthComponent oldHealth = new HealthComponent(10, 10);
    HealthComponent health = new HealthComponent(7, 10);
    storage.add(old, oldHealth);

    // Act
    storage.add(current, health);

    // Assert
    Optional<HealthComponent> currentResult = storage.get(current);
    Optional<HealthComponent> expected = Optional.of(health);
    boolean oldPresent = storage.has(old);
    int size = storage.size();
    assertEquals(expected, currentResult);
    assertFalse(oldPresent);
    assertEquals(1, size);
  }

  @Test
  void shouldGrowWhenEntityIndexIsBeyondCurrentSize() {
    // Arrange
    Entity entity = new Entity(1000, 0);
    HealthComponent health = new HealthComponent(10, 10);

    // Act
    storage.add(entity, health);

    // Assert
    Optional<HealthComponent> result = storage.get(entity);
    Optional<HealthComponent> expected = Optional.of(health);
    assertEquals(expected, result);
  }

  @Test
  void shouldThrowWhenEntityIndexIsNegative() {
    // Arrange
    Entity entity = new Entity(-1, 0);
    HealthComponent health = new HealthComponent(10, 10);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> storage.add(entity, health));
  }
}
