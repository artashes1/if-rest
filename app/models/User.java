package models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

import java.util.Date;

import javax.annotation.Nullable;

import org.bson.types.ObjectId;

import lombok.Data;
import lombok.EqualsAndHashCode;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import utils.ObjectIdSerializer;

@Entity("users")
@Data
@EqualsAndHashCode(exclude = {"password"})
public class User {
	@Id
	@Nullable
	@JsonSerialize(using = ObjectIdSerializer.class)
	private ObjectId id;

	@Required
	private String userName;

	//	@Required
	private String password;

	//	@Required
	private String firstName;

//	@Required
	private String lastName;

	@Email
	private String emailAddress;

//	@Required
	private String streetName;

//	@Required
	private Integer houseNumber;

//	@Required
	private String city;

	@play.data.format.Formats.DateTime(pattern = "yyyy-MM-dd")
	private Date birthDate;

	private String[] roles;
}