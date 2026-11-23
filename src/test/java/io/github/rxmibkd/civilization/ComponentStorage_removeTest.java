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
    storage.addComponent(entity, health);

    // Act
    storage.deleteComponent(entity);

    // Assert
    boolean present = storage.hasComponent(entity);
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
    storage.addComponent(first, firstHealth);
    storage.addComponent(second, secondHealth);
    storage.addComponent(third, thirdHealth);

    // Act
    storage.deleteComponent(first);

    // Assert
    Optional<HealthComponent> secondResult = storage.getComponent(second);
    Optional<HealthComponent> thirdResult = storage.getComponent(third);
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
    storage.addComponent(entity, original);
    storage.deleteComponent(entity);

    // Act
    storage.addComponent(entity, health);

    // Assert
    Optional<HealthComponent> result = storage.getComponent(entity);
    Optional<HealthComponent> expected = Optional.of(health);
    assertEquals(expected, result);
  }

  @Test
  void shouldDoNothingWhenEntityHasNoComponent() {
    // Arrange
    Entity present = new Entity(0, 0);
    Entity absent = new Entity(5, 0);
    HealthComponent health = new HealthComponent(10, 10);
    storage.addComponent(present, health);

    // Act
    storage.deleteComponent(absent);

    // Assert
    boolean presentStillThere = storage.hasComponent(present);
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
    storage.addComponent(current, health);

    // Act
    storage.deleteComponent(stale);

    // Assert
    boolean currentPresent = storage.hasComponent(current);
    assertTrue(currentPresent);
  }
}
