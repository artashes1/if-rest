package handlers;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import org.bson.types.ObjectId;

import models.User;
import models.UserRepository;

/**
 * Handles presentation of User, which map to JSON.
 */
public class UserHandler {

	private final UserRepository userRepository;

	@Inject
	public UserHandler(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public CompletionStage<List<User>> findAll() {
		return supplyAsync(userRepository::findAll);
	}

	public CompletionStage<User> create(final User user) {
		return supplyAsync(() -> userRepository.store(user))
			.thenComposeAsync(id -> supplyAsync(() -> userRepository.find(id)));
	}

	public CompletionStage<Optional<User>> find(final ObjectId id) {
		return supplyAsync(() -> Optional.ofNullable(userRepository.find(id)));
	}

	public CompletionStage<Optional<User>> update(final User user) {
		return supplyAsync(() -> userRepository.find(user.getId()))
			.thenComposeAsync(u -> {
				if (u == null) {
					return supplyAsync(Optional::<User>empty);
				} else {
					return supplyAsync(() -> userRepository.store(user)).thenComposeAsync(this::find);
				}
			});
	}
}
