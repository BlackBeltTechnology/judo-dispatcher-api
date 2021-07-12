package hu.blackbelt.judo.dispatcher.api;

public interface Sequence<T> {

    /**
     * Default start value.
     */
    Long DEFAULT_START = 1L;

    /**
     * Default increment.
     */
    Long DEFAULT_INCREMENT = 1L;

    /**
     * Type (discriminator) key.
     */
    String TYPE_KEY = "type";

    /**
     * Get next sequence value.
     *
     * @param sequenceName sequence name
     * @return next value
     */
    T getNextValue(String sequenceName);

    /**
     * Get current sequence value.
     *
     * @param sequenceName sequence name
     * @return current value
     */
    default T getCurrentValue(String sequenceName) {
        throw new UnsupportedOperationException("Getting current value of sequence '" + sequenceName + "' is not supported");
    }
}
