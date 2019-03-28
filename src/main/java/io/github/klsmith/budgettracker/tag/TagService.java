package io.github.klsmith.budgettracker.tag;

import java.util.List;
import java.util.Optional;

import io.github.klsmith.budgettracker.web.AppContext;

public class TagService {

	private final AppContext context;

	public TagService(AppContext context) {
		this.context = context;
	}

	public Tag save(Tag tag) {
		final TagDao tagDao = context.getTagDao();
		final String tagName = tag.getName();
		return tagDao.read(tagName)
				.orElseGet(() -> tagDao.create(tagName));
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
