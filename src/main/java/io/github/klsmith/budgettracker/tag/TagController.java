package io.github.klsmith.budgettracker.tag;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.Service;

public class TagController {

	private static final Logger logger = LoggerFactory.getLogger(TagController.class);

	private static final String PARAM = ":param";
	private static final String RESPONSE_TYPE_APPLICATION_JSON = "application/json";

	private final TagService service;

	public TagController(TagService service) {
		this.service = Objects.requireNonNull(service, "Cannot have a null TagService.");
	}

	public void route(Service spark) {
		spark.post("/api/tag", this::postTag);
		spark.get("/api/tag/" + PARAM, this::getTag);
		spark.delete("/api/tag/" + PARAM, this::deleteTag);
	}

	public String postTag(Request request, Response response) {
		response.type(RESPONSE_TYPE_APPLICATION_JSON);
		return parseJson(request.body())
				.map(service::save)
				.map(this::toJson)
				.orElseThrow(RuntimeException::new);
	}

	public String getTag(Request request, Response response) {
		response.type(RESPONSE_TYPE_APPLICATION_JSON);
		final String param = request.params(PARAM);
		try {
			final long id = Long.parseLong(param);
			return service.findById(id)
					.map(this::toJson)
					.orElseGet(() -> {
						response.status(404);
						return String.format("{ \"message\"=\"Cannot find %s with id=%s\" }",
								Tag.class.getSimpleName(), Long.valueOf(id));
					});
		} catch (NumberFormatException e) {
			// not an id, try finding by name
		}
		return service.findByName(param)
				.map(this::toJson)
				.orElseGet(() -> {
					response.status(404);
					return String.format("{ \"message\"=\"Cannot find %s with name=%s\" }",
							Tag.class.getSimpleName(), Long.valueOf(param));
				});
	}

	public String deleteTag(Request request, Response response) {
		response.type(RESPONSE_TYPE_APPLICATION_JSON);
		final long id = Long.parseLong(request.params(PARAM));
		service.delete(id);
		return "{ \"message\"=\"Successfully deleted Tag with id=" + id + "\" }";
	}

	private Optional<Tag> parseJson(String json) {
		try {
			return Optional.ofNullable(new Gson()
					.fromJson(json, TagBuilder.class)
					.build());
		} catch (RuntimeException e) {
			logger.warn("Error parsing Tag", e);
		}
		return Optional.empty();
	}

	private String toJson(Tag tag) {
		return new Gson().toJson(tag);
	}

}
