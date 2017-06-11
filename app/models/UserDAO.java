package models;

import java.util.List;

import javax.annotation.Nullable;

import org.bson.types.ObjectId;

/**
 * Data access object to manage user's storage
 *
 * @author Artashes Balyan.
 */
public interface UserDAO {
	/**
	 * Find all users
	 *
	 * @return list of all users
	 */
	List<User> findAll();

	/**
	 * Creates new user
	 *
	 * @return created user
	 */
	User add(User user);

	/**
	 * Updates existing user
	 *
	 * @return updated user or null if user with specified id not found
	 */
	@Nullable
	User update(User user);

	/**
	 * Finds user by Id
	 *
	 * @return user or null if user with specified id not found
	 */
	@Nullable
	User find(ObjectId id);
}
