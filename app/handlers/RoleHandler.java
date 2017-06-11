package handlers;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.util.List;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import models.Role;
import models.RoleRepository;
import play.libs.concurrent.HttpExecutionContext;

/**
 * Handles requests of Role entity.
 */
public class RoleHandler {

	private final RoleRepository roleRepository;
	private final HttpExecutionContext ec;

	@Inject
	public RoleHandler(final RoleRepository roleRepository, final HttpExecutionContext ec) {
		this.roleRepository = roleRepository;
		this.ec = ec;
	}

	public CompletionStage<List<Role>> findAll() {
		return supplyAsync(roleRepository::findAll, ec.current());
	}
}
