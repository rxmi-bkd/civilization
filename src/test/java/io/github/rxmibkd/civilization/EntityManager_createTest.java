package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntityManager_createTest {

  private EntityManager manager;

  @BeforeEach
  void setUp() {
    manager = new EntityManager();
  }

  @Test
  void shouldReturnFirstSlotAtGenerationZeroWhenNoEntityCreatedYet() {
    // Arrange
    Entity expected = new Entity(0, 0);

    // Act
    Entity actual = manager.create();

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  void shouldReturnDistinctIndicesWhenNoSlotIsFree() {
    // Arrange

    // Act
    Entity first = manager.create();
    Entity second = manager.create();

    // Assert
    int firstIndex = first.index();
    int secondIndex = second.index();
    assertEquals(0, firstIndex);
    assertEquals(1, secondIndex);
  }

  @Test
  void shouldReturnAliveEntity() {
    // Arrange

    // Act
    Entity entity = manager.create();

    // Assert
    boolean alive = manager.isAlive(entity);
    assertTrue(alive);
  }

  @Test
  void shouldReuseSlotWithIncrementedGenerationWhenSlotWasFreed() {
    // Arrange
    Entity old = manager.create();
    manager.destroy(old);
    int expectedIndex = old.index();
    int expectedGeneration = old.generation() + 1;

    // Act
    Entity reused = manager.create();

    // Assert
    int actualIndex = reused.index();
    int actualGeneration = reused.generation();
    assertEquals(expectedIndex, actualIndex);
    assertEquals(expectedGeneration, actualGeneration);
  }

  @Test
  void shouldIncrementGenerationEachTimeWhenSlotIsReusedRepeatedly() {
    // Arrange
    Entity first = manager.create();
    manager.destroy(first);
    Entity second = manager.create();
    manager.destroy(second);
    Entity third = manager.create();
    manager.destroy(third);

    // Act
    Entity fourth = manager.create();

    // Assert
    int actualIndex = fourth.index();
    int actualGeneration = fourth.generation();
    assertEquals(0, actualIndex);
    assertEquals(3, actualGeneration);
  }

  @Test
  void shouldNotHandOutSameSlotTwiceWhenEntityIsDestroyedTwice() {
    // Arrange
    Entity entity = manager.create();
    manager.destroy(entity);
    manager.destroy(entity);

    // Act
    Entity first = manager.create();
    Entity second = manager.create();

    // Assert
    int firstIndex = first.index();
    int secondIndex = second.index();
    assertNotEquals(firstIndex, secondIndex);
  }
}
