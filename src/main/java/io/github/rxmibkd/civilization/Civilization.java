package io.github.rxmibkd.civilization;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Entry point of the entity component system.
 * <br/>
 * Owns an {@link EntityManager} and a {@link ComponentManager} and keeps them consistent: an
 * entity can only receive components while it is alive, and destroying an entity also detaches all
 * of its components. Also runs the registered {@link GameSystem}s.
 */
public class Civilization {

  private final EntityManager entityManager = new EntityManager();
  private final ComponentManager componentManager = new ComponentManager();
  private final List<GameSystem> systems = new ArrayList<>();

  /**
   * Creates a new entity.
   *
   * @return a handle to the new entity
   */
  public Entity createEntity() {
    return entityManager.createEntity();
  }

  /**
   * Destroys an entity and detaches all of its components. Does nothing if the entity is not alive.
   *
   * @param entity the entity to destroy
   */
  public void deleteEntity(Entity entity) {
    if (entityManager.isEntityAlive(entity)) {
      componentManager.deleteComponents(entity);
      entityManager.deleteEntity(entity);
    }
  }

  /**
   * Checks whether a handle still refers to a living entity.
   *
   * @param entity the handle to check
   * @return {@code true} if the entity has not been destroyed
   */
  public boolean isEntityAlive(Entity entity) {
    return entityManager.isEntityAlive(entity);
  }

  /**
   * Attaches a component to an entity, replacing any component of the same type it already has.
   *
   * @param entity    the entity receiving the component
   * @param component the component to attach
   * @param <T>       the component type
   * @throws IllegalArgumentException if the entity is not alive
   */
  public <T extends Component> void addComponent(Entity entity, T component) {
    if (!entityManager.isEntityAlive(entity)) {
      throw new IllegalArgumentException("entity is not alive: " + entity);
    }
    componentManager.addComponent(entity, component);
  }

  /**
   * Returns the component of the given type attached to an entity.
   *
   * @param entity the entity to look up
   * @param type   the component type
   * @param <T>    the component type
   * @return the component, or empty if the entity has none of that type
   */
  public <T extends Component> Optional<T> getComponent(Entity entity, Class<T> type) {
    return componentManager.getComponent(entity, type);
  }

  /**
   * Checks whether an entity has a component of the given type.
   *
   * @param entity the entity to check
   * @param type   the component type
   * @return {@code true} if the entity has a component of that type
   */
  public boolean hasComponent(Entity entity, Class<? extends Component> type) {
    return componentManager.hasComponent(entity, type);
  }

  /**
   * Detaches the component of the given type from an entity. Does nothing if it has none.
   *
   * @param entity the entity losing the component
   * @param type   the component type
   */
  public void deleteComponent(Entity entity, Class<? extends Component> type) {
    componentManager.deleteComponent(entity, type);
  }

  /**
   * Finds the living entities that have a component of every given type.
   * <br/>
   * The result is a snapshot, so systems can safely add or remove components, or destroy
   * entities, while iterating over it.
   *
   * @param types the component types an entity must all have
   * @return the matching entities, empty if none match
   * @throws IllegalArgumentException if no type is given
   */
  @SafeVarargs
  public final List<Entity> query(Class<? extends Component>... types) {
    return componentManager.query(types);
  }

  /**
   * Registers a system. Systems run in registration order on each {@link #update(double)}.
   *
   * @param system the system to register
   */
  public void addSystem(GameSystem system) {
    systems.add(system);
  }

  /**
   * Unregisters a system. Does nothing if the system is not registered.
   *
   * @param system the system to unregister
   */
  public void removeSystem(GameSystem system) {
    systems.remove(system);
  }

  /**
   * Runs every registered system once, in registration order.
   *
   * @param deltaTime time elapsed since the previous update, in seconds
   */
  public void update(double deltaTime) {
    for (GameSystem system : List.copyOf(systems)) {
      system.update(this, deltaTime);
    }
  }
}
