package controllers;

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

	private final UserRepository repository;

	@Inject
	public UserHandler(final UserRepository repository) {
		this.repository = repository;
	}

	public CompletionStage<List<User>> findAll() {
		return repository.list();
	}

	public CompletionStage<User> create(final User user) {
		return repository.save(user);
	}

	public CompletionStage<Optional<User>> find(final ObjectId id) {
		return repository.get(id);
	}

	public CompletionStage<Optional<User>> update(final ObjectId id, final User user) {
		if (user.getId() == null || !id.equals(user.getId())) {
			throw new IllegalArgumentException("Id of DTO does not mutch URL");
		}
		return repository.get(id);//repository.update(id, user).thenApplyAsync(optionalData -> optionalData.map(this::transformEntityToDto), ec.current());
	}
}
