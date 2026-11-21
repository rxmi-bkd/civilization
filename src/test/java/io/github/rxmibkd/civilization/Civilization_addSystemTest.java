package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Civilization_addSystemTest {

  private Civilization civilization;

  @BeforeEach
  void setUp() {
    civilization = new Civilization();
  }

  @Test
  void shouldRunSystemOnUpdateWhenSystemIsAdded() {
    // Arrange
    List<String> calls = new ArrayList<>();
    GameSystem system = (w, deltaTime) -> calls.add("system");

    // Act
    civilization.addSystem(system);

    // Assert
    civilization.update(1.0);
    List<String> expected = List.of("system");
    assertEquals(expected, calls);
  }
}
