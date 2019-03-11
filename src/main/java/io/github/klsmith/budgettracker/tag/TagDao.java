package io.github.klsmith.budgettracker.tag;

import java.util.List;
import java.util.Optional;

public interface TagDao {

    public Tag create(Tag tag);

    public Tag create(String tagName);

    public Tag map(long entryId, String tagName);

    public Optional<Tag> read(String tagName);

    public Optional<Tag> read(long id);

    public List<Tag> readForEntry(long entryId);

}
