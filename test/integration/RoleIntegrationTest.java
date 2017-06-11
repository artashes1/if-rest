package integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.hasItem;
import static play.test.Helpers.GET;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;

import org.junit.Test;

import models.Role;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

public class RoleIntegrationTest extends WithApplication {

	@Override
	protected Application provideApplication() {
		return new GuiceApplicationBuilder().build();
	}

	@Test
	public void testRoleList_Success() {
		final Http.RequestBuilder request = new Http.RequestBuilder()
			.method(GET)
			.uri("/api/v1/roles");

		final Result result = route(app, request);

		assertEquals(play.mvc.Http.Status.OK, result.status());
		final JsonNode jsonNode = Json.parse(contentAsString(result));
		final Role[] roles = Json.fromJson(jsonNode, Role[].class);
		assertThat(Arrays.asList(roles), hasItem(new Role("manager", "Manager")));
	}
}
