package io.github.rxmibkd.civilization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Keeps one {@link ComponentStorage} per component type and routes operations to the right one.
 *
 * <p>Storages are created lazily, the first time a component of a given type is added. Components
 * are matched by their exact class: a component added as {@code HealthComponent} is retrieved with
 * {@code HealthComponent.class}.
 */
class ComponentManager {

    /**
     * Storage of each component type. Each value is a {@code ComponentStorage<T>} for the {@code T}
     * of its key, which is why the casts below are safe.
     */
    private final Map<Class<? extends Component>, ComponentStorage<? extends Component>> storages = new HashMap<>();

    /**
     * Attaches a component to an entity, replacing any component of the same type it already has.
     *
     * @param entity    the entity receiving the component
     * @param component the component to attach
     * @param <T>       the component type
     * @throws IllegalArgumentException if the entity index is negative
     */
    @SuppressWarnings("unchecked")
    <T extends Component> void add(Entity entity, T component) {
        Class<T> type = (Class<T>) component.getClass();
        ComponentStorage<T> storage = (ComponentStorage<T>) storages.computeIfAbsent(type, key -> new ComponentStorage<>());
        storage.add(entity, component);
    }

    /**
     * Returns the component of the given type attached to an entity.
     *
     * @param entity the entity to look up
     * @param type   the component type
     * @param <T>    the component type
     * @return the component, or empty if the entity has none of that type
     */
    <T extends Component> Optional<T> get(Entity entity, Class<T> type) {
        ComponentStorage<T> storage = storageOf(type);
        return storage == null ? Optional.empty() : storage.get(entity);
    }

    /**
     * Checks whether an entity has a component of the given type.
     *
     * @param entity the entity to check
     * @param type   the component type
     * @return {@code true} if the entity has a component of that type
     */
    boolean has(Entity entity, Class<? extends Component> type) {
        ComponentStorage<? extends Component> storage = storages.get(type);
        return storage != null && storage.has(entity);
    }

    /**
     * Detaches the component of the given type from an entity. Does nothing if it has none.
     *
     * @param entity the entity losing the component
     * @param type   the component type
     */
    void remove(Entity entity, Class<? extends Component> type) {
        ComponentStorage<? extends Component> storage = storages.get(type);
        if (storage != null) {
            storage.remove(entity);
        }
    }

    /**
     * Detaches every component from an entity. Does nothing if it has none.
     *
     * @param entity the entity losing its components
     */
    void removeAll(Entity entity) {
        for (ComponentStorage<? extends Component> storage : storages.values()) {
            storage.remove(entity);
        }
    }

    /**
     * Finds the entities that have a component of every given type.
     * <p>Only the smallest of the matching storages is walked, so the cost depends on the rarest
     * component rather than on the number of entities. The result is a snapshot: components can be
     * added or removed while iterating over it.
     *
     * @param types the component types an entity must all have
     * @return the matching entities, empty if none match
     * @throws IllegalArgumentException if no type is given
     */
    @SafeVarargs
    final List<Entity> query(Class<? extends Component>... types) {
        if (types.length == 0) {
            throw new IllegalArgumentException("at least one component type is required");
        }

        ComponentStorage<? extends Component> smallest = null;

        for (Class<? extends Component> type : types) {
            ComponentStorage<? extends Component> storage = storages.get(type);

            if (storage == null) {
                return new ArrayList<>();
            }

            if (smallest == null || storage.size() < smallest.size()) {
                smallest = storage;
            }
        }

        List<Entity> entities = new ArrayList<>();

        for (Entity entity : smallest.entities()) {
            if (hasAll(entity, types)) {
                entities.add(entity);
            }
        }

        return entities;
    }

    private boolean hasAll(Entity entity, Class<? extends Component>[] types) {
        for (Class<? extends Component> type : types) {
            if (!has(entity, type)) {
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    private <T extends Component> ComponentStorage<T> storageOf(Class<T> type) {
        return (ComponentStorage<T>) storages.get(type);
    }
}
