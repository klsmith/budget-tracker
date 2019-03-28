package io.github.klsmith.budgettracker.template;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

public enum Page {

	REPORT("templates/report.twig"), //
	MANAGE_TAGS("templates/manage-tags.twig");

	private final String path;

	private Page(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public PebbleTemplate getTemplate() {
		final PebbleEngine engine = new PebbleEngine.Builder().build();
		return engine.getTemplate(getPath());
	}

	public String render(TemplateContext context) throws IOException {
		final PebbleTemplate template = getTemplate();
		final Writer writer = new StringWriter();
		template.evaluate(writer, context.toMap());
		return writer.toString();
	}

}
