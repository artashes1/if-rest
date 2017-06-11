package handlers;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import org.bson.types.ObjectId;

import models.User;
import models.UserRepository;
import play.libs.concurrent.HttpExecutionContext;

/**
 * Handles presentation of User, which map to JSON.
 */
public class UserHandler {

	private final UserRepository userRepository;
	private final HttpExecutionContext ec;

	@Inject
	public UserHandler(final UserRepository userRepository, final HttpExecutionContext ec) {
		this.userRepository = userRepository;
		this.ec = ec;
	}

	public CompletionStage<List<User>> findAll() {
		return supplyAsync(userRepository::findAll, ec.current());
	}

	public CompletionStage<User> create(final User user) {
		return supplyAsync(() -> userRepository.add(user), ec.current());
	}

	public CompletionStage<Optional<User>> find(final ObjectId id) {
		return supplyAsync(() -> Optional.ofNullable(userRepository.find(id)), ec.current());
	}

	public CompletionStage<Optional<User>> update(final ObjectId id, final User user) {
		return supplyAsync(() -> Optional.ofNullable(userRepository.update(user)), ec.current());
	}
}
