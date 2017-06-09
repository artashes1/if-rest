package controllers;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import org.bson.types.ObjectId;

import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.With;

@With(RestControllerAction.class)
public class UserController extends Controller {

	private final HttpExecutionContext ec;
	private final UserHandler handler;
	private final FormFactory formFactory;

	@Inject
	public UserController(final HttpExecutionContext ec, final UserHandler handler, final FormFactory formFactory) {
		this.ec = ec;
		this.handler = handler;
		this.formFactory = formFactory;
	}

	public CompletionStage<Result> findAll() {
		return handler.findAll()
			.thenApplyAsync(users -> ok(Json.toJson(users)), ec.current());
	}

	public CompletionStage<Result> create() {
		final Form<User> form = formFactory.form(User.class).bindFromRequest();
		if (form.hasErrors()) {
			return supplyAsync(form::errorsAsJson, ec.current()).thenApplyAsync(Results::badRequest);
		}
		return handler.create(form.get())
			.thenApplyAsync(savedUser -> created(Json.toJson(savedUser)), ec.current());
	}

	public CompletionStage<Result> find(final String id) {
		return handler.find(new ObjectId(id)).thenApplyAsync(
			optionalUser -> optionalUser.map(user -> ok(Json.toJson(user)))
				.orElseGet(Results::notFound), ec.current()
		);
	}

	public CompletionStage<Result> update(final String id) {
		final Form<User> form = formFactory.form(User.class).bindFromRequest();
		if (form.hasErrors()) {
			return supplyAsync(form::errorsAsJson, ec.current()).thenApplyAsync(Results::badRequest);
		}
		return handler.update(new ObjectId(id), form.get()).thenApplyAsync(
			optionalUser -> optionalUser.map(r -> ok(Json.toJson(r)))
				.orElseGet(Results::notFound), ec.current()
		);
	}
}
