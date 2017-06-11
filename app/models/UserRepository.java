package models;

import java.util.List;

import javax.annotation.Nullable;

import org.bson.types.ObjectId;

/**
 * Data access object to manage user's storage
 *
 * @author Artashes Balyan.
 */
public interface UserRepository {
	/**
	 * Find all users
	 *
	 * @return list of all users
	 */
	List<User> findAll();

	/**
	 * Finds user by Id
	 *
	 * @return user or null if user with specified id not found
	 */
	@Nullable
	User find(ObjectId id);

	/**
	 * Stores (saves) user
	 *
	 * @return Id of created user
	 */
	ObjectId store(User user);

	/**
	 * Delete user by Id
	 *
	 * @return true if deleted
	 */
	boolean delete(ObjectId id);
}
