package hexanome.fourteen.server;

/**
 * Mapper interface.
 *
 * @param <T> Type to map from
 * @param <E> Type to map to
 */
public interface Mapper<T, E> {
  /**
   * A Map that allows us to take an Object for Mapping.
   *
   * @param t The Object to Map
   * @return The Mapped Object
   */
  E map(T t);
}
