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
import org.mindrot.jbcrypt.BCrypt;

import models.User;
import models.UserRepository;

/**
 * Implementation of UserRepository
 *
 * @author Artashes Balyan.
 */
public class UserRepositoryImpl extends BasicDAO<User, ObjectId> implements UserRepository {
	private static final Config config = ConfigFactory.load();

	private Function<User, User> cleanPassword = u -> {
		if (u != null) {
			u.setPassword(null);
		}
		return u;
	};

	@Singleton
	@Inject
	public UserRepositoryImpl(final Morphia morphia) {
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
		return get(saveUser(user));
	}

	@Nullable
	@Override
	public User update(final User user) {
		if (get(user.getId()) != null) {
			return find(saveUser(user));
		}
		return null;
	}

	@Nullable
	@Override
	public User find(final ObjectId id) {
		return cleanPassword.apply(get(id));
	}

	@Nullable
	@Override
	public User delete(final ObjectId id) {
		final User user = find(id);
		if (user != null) {
			deleteById(id);
		}
		return user;
	}

	private ObjectId saveUser(final User user) {
		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
		return (ObjectId) save(user).getId();
	}
}