package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Civilization_removeSystemTest {

  private Civilization civilization;

  @BeforeEach
  void setUp() {
    civilization = new Civilization();
  }

  @Test
  void shouldNotRunSystemOnUpdateWhenSystemIsRemoved() {
    // Arrange
    List<String> calls = new ArrayList<>();
    GameSystem system = (w, deltaTime) -> calls.add("system");
    civilization.addSystem(system);

    // Act
    civilization.removeSystem(system);

    // Assert
    civilization.update(1.0);
    assertEquals(List.of(), calls);
  }

  @Test
  void shouldKeepOtherSystemsWhenOneSystemIsRemoved() {
    // Arrange
    List<String> calls = new ArrayList<>();
    GameSystem first = (w, deltaTime) -> calls.add("first");
    GameSystem second = (w, deltaTime) -> calls.add("second");
    civilization.addSystem(first);
    civilization.addSystem(second);

    // Act
    civilization.removeSystem(first);

    // Assert
    civilization.update(1.0);
    List<String> expected = List.of("second");
    assertEquals(expected, calls);
  }

  @Test
  void shouldDoNothingWhenSystemIsNotRegistered() {
    // Arrange
    List<String> calls = new ArrayList<>();
    GameSystem registered = (w, deltaTime) -> calls.add("registered");
    GameSystem unknown = (w, deltaTime) -> calls.add("unknown");
    civilization.addSystem(registered);

    // Act
    civilization.removeSystem(unknown);

    // Assert
    civilization.update(1.0);
    List<String> expected = List.of("registered");
    assertEquals(expected, calls);
  }

  @Test
  void shouldAllowSystemToRemoveItselfWhileUpdating() {
    // Arrange
    List<String> calls = new ArrayList<>();
    GameSystem[] holder = new GameSystem[1];
    holder[0] = (w, deltaTime) -> {
      calls.add("once");
      w.removeSystem(holder[0]);
    };
    civilization.addSystem(holder[0]);

    // Act
    civilization.update(1.0);
    civilization.update(1.0);

    // Assert
    List<String> expected = List.of("once");
    assertEquals(expected, calls);
  }
}
