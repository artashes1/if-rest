package models;

import java.util.List;

/**
 * Data access object to manage role's storage
 * @author Artashes Balyan.
 */
public interface RoleDAO {
	/**
	 * Find all roles
	 * @return list of all users
	 */
	List<Role> findAll();
}
