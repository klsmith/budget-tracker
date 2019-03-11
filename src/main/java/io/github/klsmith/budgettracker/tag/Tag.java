package io.github.klsmith.budgettracker.tag;

import java.util.Objects;

/**
 * Represents an arbitrary tag that can be used for sorting/grouping other
 * objects in the system.
 */
public class Tag {

    private final long id;
    private final String name;

    /**
     * Constructs a tag with the given name and an id of -1.
     */
    public Tag(String name) {
        this(-1, name);
    }

    /**
     * Constructs a tag with the given name and id.
     */
    public Tag(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return a unique {@link Tag} identifier.
     */
    public long getId() {
        return id;
    }

    /**
     * @return the name of the tag.
     */
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tag) {
            final Tag other = (Tag) obj;
            return Objects.equals(id, other.id)
                    && Objects.equals(name, other.name);
        }
        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{"
                + "id=" + id + ", "
                + "name=" + name
                + "}";
    }

}
