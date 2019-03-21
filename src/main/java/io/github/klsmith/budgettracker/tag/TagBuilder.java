package io.github.klsmith.budgettracker.tag;

import java.util.Objects;

public class TagBuilder {

    private long id;
    private String name;

    TagBuilder(Tag tag) {
        this(tag.getId(), tag.getName());
    }

    TagBuilder(long id, String name) {
        this();
        withId(id);
        withName(name);
    }

    TagBuilder() {
        this.id = -1;
        this.name = null;
    }

    public TagBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public long getId() {
        return id;
    }

    public TagBuilder withName(String name) {
        this.name = Objects.requireNonNull(name, "Cannot have a null name.");
        return this;
    }

    public String getName() {
        return name;
    }

    public Tag build() {
        Objects.requireNonNull("Cannot build because name was not set.");
        return new Tag(this);
    }

}
