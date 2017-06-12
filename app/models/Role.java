package models;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(value="roles", noClassnameStored=true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
	@Id
	String id;

	String description;
}
