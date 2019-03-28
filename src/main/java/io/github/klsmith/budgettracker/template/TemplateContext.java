package io.github.klsmith.budgettracker.template;

import java.util.HashMap;
import java.util.Map;

public class TemplateContext {

	private final Object model;

	public TemplateContext(TemplateContextBuilder builder) {
		this.model = builder.getModel();
	}

	public static TemplateContextBuilder builder() {
		return new TemplateContextBuilder();
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<>();
		map.put("model", model);
		return map;
	}

}
