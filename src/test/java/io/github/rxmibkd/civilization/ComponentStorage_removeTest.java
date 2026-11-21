package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComponentStorage_removeTest {

  private ComponentStorage<HealthComponent> storage;

  @BeforeEach
  void setUp() {
    storage = new ComponentStorage<>();
  }

  @Test
  void shouldDetachComponentWhenEntityHasOne() {
    // Arrange
    Entity entity = new Entity(0, 0);
    HealthComponent health = new HealthComponent(10, 10);
    storage.add(entity, health);

    // Act
    storage.remove(entity);

    // Assert
    boolean present = storage.has(entity);
    int size = storage.size();
    assertFalse(present);
    assertEquals(0, size);
  }

  @Test
  void shouldKeepOtherComponentsWhenRemovedEntityIsNotLast() {
    // Arrange
    Entity first = new Entity(0, 0);
    Entity second = new Entity(1, 0);
    Entity third = new Entity(2, 0);
    HealthComponent firstHealth = new HealthComponent(1, 10);
    HealthComponent secondHealth = new HealthComponent(2, 10);
    HealthComponent thirdHealth = new HealthComponent(3, 10);
    storage.add(first, firstHealth);
    storage.add(second, secondHealth);
    storage.add(third, thirdHealth);

    // Act
    storage.remove(first);

    // Assert
    Optional<HealthComponent> secondResult = storage.get(second);
    Optional<HealthComponent> thirdResult = storage.get(third);
    Optional<HealthComponent> expectedSecond = Optional.of(secondHealth);
    Optional<HealthComponent> expectedThird = Optional.of(thirdHealth);
    int size = storage.size();
    assertEquals(expectedSecond, secondResult);
    assertEquals(expectedThird, thirdResult);
    assertEquals(2, size);
  }

  @Test
  void shouldAllowAddingAgainWhenComponentWasRemoved() {
    // Arrange
    Entity entity = new Entity(0, 0);
    HealthComponent original = new HealthComponent(10, 10);
    HealthComponent health = new HealthComponent(4, 10);
    storage.add(entity, original);
    storage.remove(entity);

    // Act
    storage.add(entity, health);

    // Assert
    Optional<HealthComponent> result = storage.get(entity);
    Optional<HealthComponent> expected = Optional.of(health);
    assertEquals(expected, result);
  }

  @Test
  void shouldDoNothingWhenEntityHasNoComponent() {
    // Arrange
    Entity present = new Entity(0, 0);
    Entity absent = new Entity(5, 0);
    HealthComponent health = new HealthComponent(10, 10);
    storage.add(present, health);

    // Act
    storage.remove(absent);

    // Assert
    boolean presentStillThere = storage.has(present);
    int size = storage.size();
    assertTrue(presentStillThere);
    assertEquals(1, size);
  }

  @Test
  void shouldKeepCurrentComponentWhenStaleHandleIsRemoved() {
    // Arrange
    Entity current = new Entity(0, 1);
    Entity stale = new Entity(0, 0);
    HealthComponent health = new HealthComponent(10, 10);
    storage.add(current, health);

    // Act
    storage.remove(stale);

    // Assert
    boolean currentPresent = storage.has(current);
    assertTrue(currentPresent);
  }
}
