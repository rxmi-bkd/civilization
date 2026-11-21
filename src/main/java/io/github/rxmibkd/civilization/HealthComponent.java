package io.github.rxmibkd.civilization;

/**
 * Component holding the health of an entity.
 *
 * <p>Immutable: systems replace the component with a new instance to change an entity's health.
 *
 * @param current current health points, between 0 and {@code max}
 * @param max     maximum health points, strictly positive
 */
public record HealthComponent(int current, int max) implements Component {

  public HealthComponent {
    if (max <= 0) {
      throw new IllegalArgumentException("max must be positive: " + max);
    }
    if (current < 0 || current > max) {
      throw new IllegalArgumentException("current must be between 0 and max: " + current);
    }
  }
}
