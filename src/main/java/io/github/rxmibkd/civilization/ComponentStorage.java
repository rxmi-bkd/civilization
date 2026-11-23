package io.github.rxmibkd.civilization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Stores the components of a single type, using a sparse set.
 * <br/>
 * The {@code sparse} list is indexed by entity index and gives the position of the entity's
 * component in the dense lists, or {@link #ABSENT} if there is none. The dense lists are packed and
 * parallel: {@code denseEntities.get(i)} owns {@code denseComponents.get(i)}. This gives O(1)
 * add, get and remove, and iteration over contiguous data.
 * <br/>
 * Lookups compare the full {@link Entity}, generation included, so a stale handle never matches
 * the component of a later occupant of the same slot.
 *
 * @param <T> the component type held by this storage
 */
class ComponentStorage<T extends Component> {

  /** Marker in {@code sparse} for an entity index that has no component. */
  private static final int ABSENT = -1;

  private final List<Integer> sparse = new ArrayList<>();
  private final List<Entity> denseEntities = new ArrayList<>();
  private final List<T> denseComponents = new ArrayList<>();

  /**
   * Attaches a component to an entity, replacing any component already stored for its slot.
   *
   * @param entity    the entity receiving the component
   * @param component the component to attach
   * @throws IllegalArgumentException if the entity index is negative
   */
  void addComponent(Entity entity, T component) {
    int index = entity.index();

    if (index < 0) {
      throw new IllegalArgumentException("entity index must not be negative: " + index);
    }

    ensureCapacity(index);

    int position = sparse.get(index);

    if (position == ABSENT) {
      sparse.set(index, denseEntities.size());
      denseEntities.add(entity);
      denseComponents.add(component);
    }
    else {
      denseEntities.set(position, entity);
      denseComponents.set(position, component);
    }
  }

  /**
   * Returns the component attached to an entity.
   *
   * @param entity the entity to look up
   * @return the component, or empty if the entity has none
   */
  Optional<T> getComponent(Entity entity) {
    int position = positionOf(entity);
    return position == ABSENT ? Optional.empty() : Optional.of(denseComponents.get(position));
  }

  /**
   * Checks whether an entity has a component in this storage.
   *
   * @param entity the entity to check
   * @return {@code true} if the entity has a component
   */
  boolean hasComponent(Entity entity) {
    return positionOf(entity) != ABSENT;
  }

  /**
   * Detaches the component of an entity. Does nothing if the entity has none.
   *
   * @param entity the entity losing its component
   */
  void deleteComponent(Entity entity) {
    int position = positionOf(entity);

    if (position == ABSENT) {
      return;
    }

    int last = denseEntities.size() - 1;
    Entity moved = denseEntities.get(last);

    denseEntities.set(position, moved);
    denseComponents.set(position, denseComponents.get(last));
    sparse.set(moved.index(), position);

    denseEntities.remove(last);
    denseComponents.remove(last);
    sparse.set(entity.index(), ABSENT);
  }

  /**
   * Returns the number of components stored.
   *
   * @return the number of components
   */
  int size() {
    return denseEntities.size();
  }

  /**
   * Returns the entities that have a component in this storage.
   * <br/>
   * The returned list is a read-only view that reflects later changes to the storage, so copy it
   * before adding or removing components while iterating.
   *
   * @return the entities holding a component
   */
  List<Entity> getEntities() {
    return Collections.unmodifiableList(denseEntities);
  }

  /**
   * Returns the position of an entity's component in the dense lists.
   *
   * @return the position, or {@link #ABSENT} if the entity has no component
   */
  private int positionOf(Entity entity) {
    int index = entity.index();

    if (index < 0 || index >= sparse.size()) {
      return ABSENT;
    }

    int position = sparse.get(index);

    if (position == ABSENT || !denseEntities.get(position).equals(entity)) {
      return ABSENT;
    }

    return position;
  }

  private void ensureCapacity(int index) {
    while (sparse.size() <= index) {
      sparse.add(ABSENT);
    }
  }
}
