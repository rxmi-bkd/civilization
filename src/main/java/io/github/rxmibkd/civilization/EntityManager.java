package io.github.rxmibkd.civilization;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Creates and destroys entities, recycling slots and tracking their generations.
 * <br/>
 * Destroying an entity increments the generation of its slot, which invalidates every handle
 * previously issued for that slot.
 */
class EntityManager {

  /**
   * Current generation of each slot, indexed by slot position: {@code generations.get(i)} is the
   * generation of slot {@code i}. Its size is therefore the number of slots ever created. A slot is
   * a reusable position; its generation counts how many entities have occupied it before the
   * current one.
   */
  private final List<Integer> generations = new ArrayList<>();

  /** Indices of slots whose entity was destroyed and that are available for reuse. */
  private final Deque<Integer> freeIndices = new ArrayDeque<>();

  /**
   * Creates a new entity, reusing a free slot when one is available.
   *
   * @return a handle to the new entity
   */
  Entity createEntity() {
    int index;

    if (freeIndices.isEmpty()) {
      index = generations.size();
      generations.add(0);
    }
    else {
      index = freeIndices.pop();
    }

    return new Entity(index, generations.get(index));
  }

  /**
   * Destroys an entity and frees its slot. Does nothing if the entity is not alive.
   *
   * @param entity the entity to destroy
   */
  void deleteEntity(Entity entity) {
    if (isEntityAlive(entity)) {
      generations.set(entity.index(), entity.generation() + 1);
      freeIndices.push(entity.index());
    }
  }

  /**
   * Checks whether a handle still refers to the current occupant of its slot.
   *
   * @param entity the handle to check
   * @return {@code true} if the entity has not been destroyed
   */
  boolean isEntityAlive(Entity entity) {
    int index = entity.index();
    return index >= 0 && index < generations.size() && generations.get(index) == entity.generation();
  }
}
