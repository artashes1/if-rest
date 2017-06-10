package models.impl;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.MongoClient;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.bson.types.ObjectId;

import models.User;
import models.UserDAO;

/**
 * Implementation of UserDAO
 *
 * @author Artashes Balyan.
 */
public class UserDAOImpl extends BasicDAO<User, ObjectId> implements UserDAO {
	private static final Config config = ConfigFactory.load();

	private Function<User, User> cleanPassword = u -> {
		u.setPassword(null);
		return u;
	};

	@Singleton
	@Inject
	public UserDAOImpl(final Morphia morphia) {
		super(
			new MongoClient(config.getString("mongodb.host"), config.getInt("mongodb.port")),
			morphia,
			config.getString("mongodb.db")
		);
	}

	@Override
	public List<User> findAll() {
		return this.find().asList().stream().map(cleanPassword).collect(Collectors.toList());
	}

	@Override
	public User add(final User user) {
		return get((ObjectId) save(user).getId());
	}

	@Nullable
	@Override
	public User update(final User user) {
		if (get(user.getId()) != null) {
			return find((ObjectId) save(user).getId());
		}
		return null;
	}

	@Nullable
	@Override
	public User find(final ObjectId id) {
		return cleanPassword.apply(get(id));
	}
}