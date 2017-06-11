package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

import java.util.Date;

import javax.annotation.Nullable;

import org.bson.types.ObjectId;

import lombok.Data;
import lombok.EqualsAndHashCode;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Pattern;
import play.data.validation.Constraints.Required;
import utils.ObjectIdSerializer;
import utils.PasswordValidator.Password;

@Entity("users")
@Data
@EqualsAndHashCode(exclude = { "password" })
public class User {
	@Id
	@Nullable
	@JsonSerialize(using = ObjectIdSerializer.class)
	private ObjectId id;

	@Required
	@MinLength(3)
	private String userName;

	@Password
	private String password;

	@Required
	@Pattern(value = "^[a-zA-Z]*$", message = "This field should contains only characters")
	private String firstName;

	@Required
	@Pattern(value = "^[a-zA-Z]*$", message = "This field should contains only characters")
	private String lastName;

	@Required
	@Email
	private String emailAddress;

	@Required
	private String streetName;

	@Required
	private Integer houseNumber;

	@Required
	@Pattern(value = "^[a-zA-Z]*$", message = "This field should contains only characters")
	private String city;

	@play.data.format.Formats.DateTime(pattern = "yyyy-MM-dd")
	// TODO timezone should not be here, but without it there is a bug when updating user
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "CET")
	@Required
	private Date birthDate;

	@Required
	private String[] roles;
}