package models;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity("roles")
@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude = "description")
public class Role {
	@Id
	String id;

	String description;
}
