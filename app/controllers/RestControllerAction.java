package controllers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.inject.Singleton;

import akka.actor.ActorSystem;
import play.Logger;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;

public class RestControllerAction extends play.mvc.Action.Simple {
	private static final String ACCEPTABLE_MIME_TYPE = "application/json";
	private final Logger.ALogger logger = play.Logger.of("application.RestControllerAction");

	private final HttpExecutionContext ec;

	@Singleton
	@Inject
	public RestControllerAction(final HttpExecutionContext ec, final ActorSystem actorSystem) {
		this.ec = ec;
	}

	public CompletionStage<Result> call(final Http.Context ctx) {
		if (logger.isTraceEnabled()) {
			logger.trace("call: ctx = " + ctx);
		}
		if (ctx.request().accepts(ACCEPTABLE_MIME_TYPE)) {
			return doCall(ctx);
		} else {
			return CompletableFuture.completedFuture(
				status(Http.Status.NOT_ACCEPTABLE, String.format("Only '%s' MIME_TYPE is acceptable", ACCEPTABLE_MIME_TYPE))
			);
		}
	}

	private CompletionStage<Result> doCall(final Http.Context ctx) {
		return delegate.call(ctx).handleAsync((result, e) -> {
			if (e != null) {
				if (e instanceof CompletionException) {
					logger.error("Direct exception " + e.getMessage(), e);
				} else {
					logger.error("Unknown exception " + e.getMessage(), e);
				}
				return internalServerError(e.getMessage());
			} else {
				return result;
			}
		}, ec.current());
	}
}
