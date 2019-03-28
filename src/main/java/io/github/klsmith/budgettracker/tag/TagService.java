package io.github.klsmith.budgettracker.tag;

import java.util.List;
import java.util.Optional;

import io.github.klsmith.budgettracker.web.AppContext;

public class TagService {

	private final AppContext context;

	public TagService(AppContext context) {
		this.context = context;
	}

	public Tag create(Tag tag) {
		return context.getTagDao().create(tag);
	}

	public Optional<Tag> findById(long id) {
		return context.getTagDao().read(id);
	}

	public Optional<Tag> findByName(String name) {
		return context.getTagDao().read(name);
	}

	public List<Tag> findAll() {
		return context.getTagDao().readAll();
	}

	public void delete(long id) {
		context.getTagDao().delete(id);
	}

}
