package models.impl;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.MongoClient;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;

import models.Role;
import models.RoleDAO;

/**
 * Implementation of RoleDAO
 * @author Artashes Balyan.
 */
public class RoleDAOImpl extends BasicDAO<Role, String> implements RoleDAO {
	private static final Config config = ConfigFactory.load();

	@Singleton
	@Inject
	public RoleDAOImpl(final Morphia morphia) {
		super(
			new MongoClient(config.getString("mongodb.host"), config.getInt("mongodb.port")),
			morphia,
			config.getString("mongodb.db")
		);
		initRolesList();
	}

	/**
	 * This is not "production" code, roles are initialised to simplify assessment
	 */
	private void initRolesList() {
		if (this.count() == 0) {
			Stream.of(
				new Role("administrator", "Administrator"),
				new Role("manager", "Manager"),
				new Role("developer", "Software developer"),
				new Role("designer", "Designer"),
				new Role("sales_agent", "Sales agent")
			).forEach(this::save);
		}
	}

	@Override
	public List<Role> findAll() {
		return this.find().asList();
	}
}