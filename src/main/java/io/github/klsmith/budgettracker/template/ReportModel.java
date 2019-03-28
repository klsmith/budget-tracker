package io.github.klsmith.budgettracker.template;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.github.klsmith.budgettracker.money.expense.Expense;
import io.github.klsmith.budgettracker.tag.Tag;

public class ReportModel {

	private final List<String> dates;
	private final List<String> tags;
	private final Map<String, Map<String, Expense>> expenses;

	public ReportModel(List<Expense> expenseList) {
		dates = new LinkedList<>();
		tags = new LinkedList<>();
		expenses = new HashMap<>();
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/uuuu");
		for (Expense expense : expenseList) {
			List<Tag> tagList = expense.getTags();
			for (Tag tag : tagList) {
				if (!tags.contains(tag.getName())) {
					tags.add(tag.getName());
				}
				final String date = expense.getDate().format(formatter);
				if (!dates.contains(date)) {
					dates.add(date);
				}
				expenses.computeIfAbsent(tag.getName(), key -> new HashMap<>()) //
						.put(date, expense);
			}
		}
	}

}
