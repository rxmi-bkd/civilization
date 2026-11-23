package io.github.rxmibkd.civilization;

/**
 * Handle identifying an entity in the entity component system.
 * <br/>
 * An entity is only an identifier. The {@code index} is the slot the entity occupies, and the
 * {@code generation} distinguishes successive occupants of the same slot, so that a stale handle
 * can be detected once its entity has been destroyed and the slot reused.
 *
 * @param index      slot position of the entity
 * @param generation generation of the slot when the entity was created
 */
public record Entity(int index, int generation) {

  /**
   * Creates an entity handle.
   *
   * @throws IllegalArgumentException if the index or the generation is negative
   */
  public Entity {
    if (index < 0) {
      throw new IllegalArgumentException("index must not be negative: " + index);
    }
    if (generation < 0) {
      throw new IllegalArgumentException("generation must not be negative: " + generation);
    }
  }
}
