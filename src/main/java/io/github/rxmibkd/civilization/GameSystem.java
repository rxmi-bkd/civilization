package io.github.rxmibkd.civilization;

/**
 * Logic of the entity component system.
 * <br/>
 * A system holds no state about entities. On each update it queries the {@link Civilization} for the
 * entities that have the components it cares about and acts on them.
 */
@FunctionalInterface
public interface GameSystem {

  /**
   * Runs this system once.
   *
   * @param civilization the civilization to read from and modify
   * @param deltaTime    time elapsed since the previous update, in seconds
   */
  void update(Civilization civilization, double deltaTime);
}
