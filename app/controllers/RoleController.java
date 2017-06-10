package controllers;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import handlers.RestControllerAction;
import handlers.RoleHandler;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

/**
 * Simple controller, that returns all Roles.
 */
@With(RestControllerAction.class)
public class RoleController extends Controller {
	private final RoleHandler handler;
	private final HttpExecutionContext ec;

	@Inject
	public RoleController(final RoleHandler roleHandler, final HttpExecutionContext ec) {
		this.handler = roleHandler;
		this.ec = ec;
	}

	public CompletionStage<Result> findAll() {
		return handler.findAll()
			.thenApplyAsync(roles -> ok(Json.toJson(roles)), ec.current());
	}
}
