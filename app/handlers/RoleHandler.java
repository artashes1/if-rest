package handlers;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.util.List;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import models.Role;
import models.RoleDAO;
import play.libs.concurrent.HttpExecutionContext;

/**
 * Handles requests of Role entity.
 */
public class RoleHandler {

	private final RoleDAO roleDAO;
	private final HttpExecutionContext ec;

	@Inject
	public RoleHandler(final RoleDAO roleDAO, final HttpExecutionContext ec) {
		this.roleDAO = roleDAO;
		this.ec = ec;
	}

	public CompletionStage<List<Role>> findAll() {
		return supplyAsync(roleDAO::findAll, ec.current());
	}
}
