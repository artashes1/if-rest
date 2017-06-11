package controllers;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import org.bson.types.ObjectId;

import handlers.RestControllerAction;
import handlers.UserHandler;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
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
			return badRequestAsync(form);
		}
		if (form.get().getId() != null) {
			form.errors().put("id", Collections.singletonList(new ValidationError("mismatch", "Id of object shuold not be specified")));
			return badRequestAsync(form);
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
		final ObjectId objectId = new ObjectId(id);
		final Form<User> form = formFactory.form(User.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequestAsync(form);
		}
		if (!objectId.equals(form.get().getId())) {
			form.errors().put("id", Collections.singletonList(new ValidationError("mismatch", "Id of object does mismatch URL path")));
			return badRequestAsync(form);
		}
		return handler.update(form.get()).thenApplyAsync(
			optionalUser -> optionalUser.map(r -> ok(Json.toJson(r)))
				.orElseGet(Results::notFound), ec.current()
		);
	}

	private CompletableFuture<Result> badRequestAsync(final Form<User> form) {
		return supplyAsync(form::errorsAsJson, ec.current()).thenApplyAsync(Results::badRequest);
	}
}
