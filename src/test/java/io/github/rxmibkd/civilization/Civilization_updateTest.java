package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Civilization_updateTest {

  private Civilization civilization;

  @BeforeEach
  void setUp() {
    civilization = new Civilization();
  }

  @Test
  void shouldRunSystemsInRegistrationOrderWhenSeveralAreAdded() {
    // Arrange
    List<String> calls = new ArrayList<>();
    GameSystem first = (w, deltaTime) -> calls.add("first");
    GameSystem second = (w, deltaTime) -> calls.add("second");
    civilization.addSystem(first);
    civilization.addSystem(second);

    // Act
    civilization.update(1.0);

    // Assert
    List<String> expected = List.of("first", "second");
    assertEquals(expected, calls);
  }

  @Test
  void shouldPassCivilizationAndDeltaTimeToSystems() {
    // Arrange
    List<Civilization> civilizations = new ArrayList<>();
    List<Double> deltas = new ArrayList<>();
    GameSystem system = (w, deltaTime) -> {
      civilizations.add(w);
      deltas.add(deltaTime);
    };
    civilization.addSystem(system);

    // Act
    civilization.update(0.5);

    // Assert
    Civilization receivedCivilization = civilizations.get(0);
    List<Double> expectedDeltas = List.of(0.5);
    assertSame(civilization, receivedCivilization);
    assertEquals(expectedDeltas, deltas);
  }

  @Test
  void shouldLetSystemModifyComponentsWhenQueryingEntities() {
    // Arrange
    Entity entity = civilization.createEntity();
    HealthComponent health = new HealthComponent(10, 10);
    civilization.addComponent(entity, health);
    GameSystem damage = (w, deltaTime) -> {
      for (Entity target : w.query(HealthComponent.class)) {
        HealthComponent current = w.getComponent(target, HealthComponent.class).get();
        HealthComponent damaged = new HealthComponent(current.current() - 3, current.max());
        w.addComponent(target, damaged);
      }
    };
    civilization.addSystem(damage);

    // Act
    civilization.update(1.0);

    // Assert
    Optional<HealthComponent> result = civilization.getComponent(entity, HealthComponent.class);
    HealthComponent expected = new HealthComponent(7, 10);
    assertEquals(expected, result.get());
  }

  @Test
  void shouldAllowSystemToDestroyEntitiesWhileIteratingQueryResult() {
    // Arrange
    Entity first = civilization.createEntity();
    Entity second = civilization.createEntity();
    HealthComponent health = new HealthComponent(10, 10);
    civilization.addComponent(first, health);
    civilization.addComponent(second, health);
    GameSystem reaper = (w, deltaTime) -> {
      for (Entity target : w.query(HealthComponent.class)) {
        w.deleteEntity(target);
      }
    };
    civilization.addSystem(reaper);

    // Act
    civilization.update(1.0);

    // Assert
    List<Entity> remaining = civilization.query(HealthComponent.class);
    int count = remaining.size();
    assertEquals(0, count);
  }

  @Test
  void shouldDoNothingWhenNoSystemIsRegistered() {
    // Arrange
    Entity entity = civilization.createEntity();

    // Act
    civilization.update(1.0);

    // Assert
    boolean alive = civilization.isEntityAlive(entity);
    assertTrue(alive);
  }

  @Test
  void shouldThrowWhenDeltaTimeIsNaN() {
    // Arrange
    List<String> calls = new ArrayList<>();
    civilization.addSystem((w, deltaTime) -> calls.add("system"));

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> civilization.update(Double.NaN));
    boolean noneRan = calls.isEmpty();
    assertTrue(noneRan);
  }

  @Test
  void shouldThrowWhenDeltaTimeIsInfinite() {
    // Arrange
    List<String> calls = new ArrayList<>();
    civilization.addSystem((w, deltaTime) -> calls.add("system"));

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> civilization.update(Double.POSITIVE_INFINITY));
    boolean noneRan = calls.isEmpty();
    assertTrue(noneRan);
  }

  @Test
  void shouldThrowWhenDeltaTimeIsNegative() {
    // Arrange
    List<String> calls = new ArrayList<>();
    civilization.addSystem((w, deltaTime) -> calls.add("system"));

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> civilization.update(-0.1));
    boolean noneRan = calls.isEmpty();
    assertTrue(noneRan);
  }

  @Test
  void shouldRunSystemsWhenDeltaTimeIsZero() {
    // Arrange
    List<String> calls = new ArrayList<>();
    civilization.addSystem((w, deltaTime) -> calls.add("system"));

    // Act
    civilization.update(0.0);

    // Assert
    List<String> expected = List.of("system");
    assertEquals(expected, calls);
  }
}
