package io.github.klsmith.budgettracker;

import spark.Spark;

public class Main {

    public static void main(String[] args) {
        Spark.port(4567);
        Spark.get("/", (request, response) -> "Project: budget-tracker");
    }

}
