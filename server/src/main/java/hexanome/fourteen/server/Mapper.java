package hexanome.fourteen.server;

/**
 * Mapper interface.
 *
 * @param <T> Type to map from
 * @param <E> Type to map to
 */
public interface Mapper<T, E> {
  E map(T t);
}
