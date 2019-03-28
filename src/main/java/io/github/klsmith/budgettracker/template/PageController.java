package io.github.klsmith.budgettracker.template;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.github.klsmith.budgettracker.money.expense.Expense;
import io.github.klsmith.budgettracker.money.expense.ExpenseBuilder;
import io.github.klsmith.budgettracker.tag.Tag;
import io.github.klsmith.budgettracker.tag.TagService;
import spark.Request;
import spark.Response;
import spark.Service;

public class PageController {

	private final TagService tagService;

	public PageController(TagService tagService) {
		this.tagService = tagService;
	}

	public void route(Service spark) {
		spark.get("/", this::homepage);
		spark.get("/view/manage/tags", this::manageTags);
	}

	private String manageTags(Request request, Response response) throws IOException {
		response.type("text/html");
		final Map<String, List<Tag>> model = new HashMap<>();
		final List<Tag> tags = tagService.findAll();
		model.put("tags", tags);
		final TemplateContext context = TemplateContext.builder()
				.withModel(model)
				.build();
		return Page.MANAGE_TAGS.render(context);
	}

	private String homepage(Request request, Response response) throws IOException {
		response.type("text/html");
		final List<Expense> expenses = new LinkedList<>();
		final ExpenseBuilder builder = Expense.builder()
				.withAmount(new BigDecimal("100.0000"))
				.withDate(LocalDate.of(1993, 8, 31))
				.withTag("Food");
		expenses.add(builder.build());
		builder.withAmount(new BigDecimal("20.0000"));
		builder.removeAllTagsWithName("Food")
				.withTag("Entertainment");
		expenses.add(builder.build());
		builder.withDate(LocalDate.of(1993, 8, 30))
				.removeAllTagsWithName("Entertainment")
				.withTag("Test");
		expenses.add(builder.build());
		final ReportModel model = new ReportModel(expenses);
		final TemplateContext context = TemplateContext.builder()
				.withModel(model)
				.build();
		return Page.REPORT.render(context);
	}

}
