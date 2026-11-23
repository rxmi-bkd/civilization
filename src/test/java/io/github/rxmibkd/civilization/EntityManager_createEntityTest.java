package io.github.rxmibkd.civilization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntityManager_createEntityTest {

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
    Entity actual = manager.createEntity();

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  void shouldReturnDistinctIndicesWhenNoSlotIsFree() {
    // Arrange

    // Act
    Entity first = manager.createEntity();
    Entity second = manager.createEntity();

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
    Entity entity = manager.createEntity();

    // Assert
    boolean alive = manager.isEntityAlive(entity);
    assertTrue(alive);
  }

  @Test
  void shouldReuseSlotWithIncrementedGenerationWhenSlotWasFreed() {
    // Arrange
    Entity old = manager.createEntity();
    manager.deleteEntity(old);
    int expectedIndex = old.index();
    int expectedGeneration = old.generation() + 1;

    // Act
    Entity reused = manager.createEntity();

    // Assert
    int actualIndex = reused.index();
    int actualGeneration = reused.generation();
    assertEquals(expectedIndex, actualIndex);
    assertEquals(expectedGeneration, actualGeneration);
  }

  @Test
  void shouldIncrementGenerationEachTimeWhenSlotIsReusedRepeatedly() {
    // Arrange
    Entity first = manager.createEntity();
    manager.deleteEntity(first);
    Entity second = manager.createEntity();
    manager.deleteEntity(second);
    Entity third = manager.createEntity();
    manager.deleteEntity(third);

    // Act
    Entity fourth = manager.createEntity();

    // Assert
    int actualIndex = fourth.index();
    int actualGeneration = fourth.generation();
    assertEquals(0, actualIndex);
    assertEquals(3, actualGeneration);
  }

  @Test
  void shouldNotHandOutSameSlotTwiceWhenEntityIsDestroyedTwice() {
    // Arrange
    Entity entity = manager.createEntity();
    manager.deleteEntity(entity);
    manager.deleteEntity(entity);

    // Act
    Entity first = manager.createEntity();
    Entity second = manager.createEntity();

    // Assert
    int firstIndex = first.index();
    int secondIndex = second.index();
    assertNotEquals(firstIndex, secondIndex);
  }
}
