package integration;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static play.test.Helpers.GET;
import static play.test.Helpers.POST;
import static play.test.Helpers.PUT;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import models.User;
import models.UserRepository;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Http.Status;
import play.mvc.Result;
import play.test.WithApplication;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserIntegrationTest extends WithApplication {

	private UserRepository userRepository;
	private User sampleUser;

	@Override
	protected Application provideApplication() {
		return new GuiceApplicationBuilder().build();
	}

	@Before
	public void initialize() {
		// to ensure that we start with clean DB
		userRepository = app.injector().instanceOf(UserRepository.class);
	}

	@After
	public void cleanCollection() {
		if (sampleUser != null) {
			userRepository.delete(sampleUser.getId());
		}
	}

	@Test
	public void testUserAdd_Success() {
		final User user = new User();
		user.setUserName("artashes_" + new Random().nextInt());
		user.setPassword("Artashes123!");
		user.setRoles(new String[]{ "developer" });
		user.setFirstName("Artashes");
		user.setLastName("Balyan");
		user.setEmailAddress("artashes@sample.com");
		user.setBirthDate(new Date());
		user.setCity("Amsterdam");
		user.setHouseNumber(111);
		user.setStreetName("Aresh");

		final Http.RequestBuilder request = new Http.RequestBuilder()
			.method(POST)
			.bodyJson(Json.toJson(user))
			.uri("/api/v1/users");

		final Result result = route(app, request);
		assertEquals(Status.CREATED, result.status());

		// Store created user for later tests
		this.sampleUser = parseObject(result, User.class);
	}

	@Test
	public void testUserAdd_ValidationFail() {
		final User user = new User();

		final Http.RequestBuilder request = new Http.RequestBuilder()
			.method(POST)
			.bodyJson(Json.toJson(user))
			.uri("/api/v1/users");

		final Result result = route(app, request);
		assertEquals(Status.BAD_REQUEST, result.status());

		final Map<String, List<String>> errors = parseObject(result, new TypeReference<Map<String, List<String>>>() {
		});

		assertThat(errors.get("userName"), hasItem("This field is required"));
		assertThat(errors.get("password"),
			hasItem("Password must be minimal 6 characters, must contain at least one capital character and one special character"));
		assertThat(errors.get("roles"), hasItem("This field is required"));
		assertThat(errors.get("firstName"), hasItem("This field is required"));
		assertThat(errors.get("lastName"), hasItem("This field is required"));
		assertThat(errors.get("emailAddress"), hasItem("This field is required"));
		assertThat(errors.get("birthDate"), hasItem("This field is required"));
		assertThat(errors.get("streetName"), hasItem("This field is required"));
		assertThat(errors.get("houseNumber"), hasItem("This field is required"));
		assertThat(errors.get("city"), hasItem("This field is required"));
	}

	@Test
	public void testUserList_Success() {
		testUserAdd_Success();
		final Http.RequestBuilder request = new Http.RequestBuilder()
			.method(GET)
			.uri("/api/v1/users");

		final Result result = route(app, request);
		assertEquals(Status.OK, result.status());

		final List<User> users = parseObject(result, new TypeReference<List<User>>() {
		});
		assertThat(users, hasItem(sampleUser));
	}

	@Test
	public void testUserGet_Success() {
		testUserAdd_Success();
		final Http.RequestBuilder request = new Http.RequestBuilder()
			.method(GET)
			.uri("/api/v1/users/" + sampleUser.getId());

		final Result result = route(app, request);
		assertEquals(Status.OK, result.status());

		final User user = parseObject(result, User.class);
		assertEquals(sampleUser, user);
	}

	@Test
	public void testUserGet_NotFound() {
		final Http.RequestBuilder request = new Http.RequestBuilder()
			.method(GET)
			.uri("/api/v1/users/" + new ObjectId());

		final Result result = route(app, request);
		assertEquals(Status.NOT_FOUND, result.status());
	}

	@Test
	public void testUserUpdate_Success() {
		testUserAdd_Success();
		final long fiveDaysInMillis = 5 * 24 * 60 * 60 * 1000;
		sampleUser.setPassword("NewPassword=OK");
		sampleUser.setRoles(new String[]{"developer", "administrator"});
		sampleUser.setFirstName("FirstName");
		sampleUser.setLastName("LastName");
		sampleUser.setEmailAddress("balyan@test.com");
		sampleUser.setBirthDate(new Date(sampleUser.getBirthDate().getTime() + fiveDaysInMillis));
		sampleUser.setStreetName("Hoekssteeg");
		sampleUser.setHouseNumber(22);
		sampleUser.setCity("City");

		final Http.RequestBuilder request = new Http.RequestBuilder()
			.method(PUT)
			.bodyJson(Json.toJson(sampleUser))
			.uri("/api/v1/users/" + sampleUser.getId());

		final Result result = route(app, request);
		assertEquals(Status.OK, result.status());

		final User user = parseObject(result, User.class);
		assertEquals(sampleUser, user);
	}

	@Test
	public void testUserUpdate_ValidationFail() {
		testUserAdd_Success();
		sampleUser.setUserName("a");
		sampleUser.setPassword("NewPasswordNotOK");
		sampleUser.setFirstName("First Name");
		sampleUser.setLastName("Last Name");
		sampleUser.setCity("City with spaces");
		sampleUser.setEmailAddress("invalid");

		final Http.RequestBuilder request = new Http.RequestBuilder()
			.method(PUT)
			.bodyJson(Json.toJson(sampleUser))
			.uri("/api/v1/users/" + sampleUser.getId());

		final Result result = route(app, request);
		assertEquals(Status.BAD_REQUEST, result.status());

		final Map<String, List<String>> errors = parseObject(result, new TypeReference<Map<String, List<String>>>() {
		});

		assertThat(errors.get("userName"), hasItem("Minimum length is 3"));
		assertThat(errors.get("password"),
			hasItem("Password must be minimal 6 characters, must contain at least one capital character and one special character"));
		assertThat(errors.get("firstName"), hasItem("This field should contains only characters"));
		assertThat(errors.get("lastName"), hasItem("This field should contains only characters"));
		assertThat(errors.get("city"), hasItem("This field should contains only characters"));
		assertThat(errors.get("emailAddress"), hasItem("Valid email required"));
	}

	private static <T> T parseObject(final Result result, Class<T> clazz) {
		final JsonNode jsonNode = Json.parse(contentAsString(result));
		return Json.fromJson(jsonNode, clazz);
	}

	private static <T> T parseObject(final Result result, TypeReference<?> typeReference) {
		final JsonNode jsonNode = Json.parse(contentAsString(result));
		try {
			return new ObjectMapper().readValue(jsonNode.traverse(), typeReference);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
