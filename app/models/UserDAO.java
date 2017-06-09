package models;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.MongoClient;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bson.types.ObjectId;

public class UserDAO extends BasicDAO<User, ObjectId> {
	private static final Config config = ConfigFactory.load();

	@Singleton
	@Inject
	public UserDAO(final Morphia morphia) {
		super(
			new MongoClient(config.getString("mongodb.host"), config.getInt("mongodb.port")),
			morphia,
			config.getString("mongodb.db")
		);
	}
}
