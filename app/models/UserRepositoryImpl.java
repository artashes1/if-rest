package models;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bson.types.ObjectId;

import play.libs.concurrent.HttpExecutionContext;

/**
 * A repository that provides a non-blocking API with a custom execution context.
 */
@Singleton
public class UserRepositoryImpl implements UserRepository {

	private final UserDAO userDAO;
	private final HttpExecutionContext ec;

	@Inject
	public UserRepositoryImpl(final UserDAO userDAO, final HttpExecutionContext ec) {
		this.userDAO = userDAO;
		this.ec = ec;
	}

	@Override
	public CompletionStage<List<User>> list() {
		return supplyAsync(() -> userDAO.find().asList(), ec.current());
	}

	@Override
	public CompletionStage<User> save(User user) {
		return supplyAsync(() -> userDAO.get((ObjectId) userDAO.save(user).getId()), ec.current());
	}

	@Override
	public CompletionStage<Optional<User>> get(final ObjectId id) {
		return supplyAsync(() -> Optional.ofNullable(userDAO.get(id)), ec.current());
	}
}
