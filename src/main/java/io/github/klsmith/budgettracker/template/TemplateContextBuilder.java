package io.github.klsmith.budgettracker.template;

import java.util.Objects;

public class TemplateContextBuilder {

	private Object model;

	TemplateContextBuilder() {
		model = null;
	}

	public TemplateContextBuilder withModel(Object model) {
		this.model = Objects.requireNonNull(model, "Cannot have a null model");
		return this;
	}

	public Object getModel() {
		return model;
	}

	public TemplateContext build() {
		Objects.requireNonNull(model, "Cannot build because model has not been set");
		return new TemplateContext(this);
	}

}
