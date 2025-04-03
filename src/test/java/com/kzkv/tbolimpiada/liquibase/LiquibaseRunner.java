package com.kzkv.tbolimpiada.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;

public class LiquibaseRunner {

	private static final String CHANGELOG_PATH = "db/changelog/db.changelog-test.yaml";

	public static void runMigrations(String jdbcUrl, String username, String password) throws Exception {
		try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
			Database database = DatabaseFactory.getInstance()
					.findCorrectDatabaseImplementation(new JdbcConnection(connection));

			Liquibase liquibase = new Liquibase(CHANGELOG_PATH, new ClassLoaderResourceAccessor(), database);
			liquibase.update();
		}
	}
}