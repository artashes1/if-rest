package controllers;

import javax.inject.Inject;

import models.RoleDAO;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

/**
 * Simple controller, that returns all User Roles. It does not follows Play Framework's reactive ideology.
 */
@With(RestControllerAction.class)
public class RoleController extends Controller {
	private final RoleDAO roleDAO;

	@Inject
	public RoleController(final RoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}

	public Result findAll() {
		return ok(Json.toJson(roleDAO.find().asList()));
	}
}
