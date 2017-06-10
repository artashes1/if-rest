package utils;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static play.libs.F.Tuple;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.Payload;

import utils.PasswordValidator.Password;
import play.data.validation.Constraints.Validator;

/**
 * Custom validator for password
 *
 * @author Artashes Balyan.
 */
public class PasswordValidator extends Validator<String> implements ConstraintValidator<Password, String> {
	final static String message = "Password must be minimal 6 characters, must contain at least one capital character and one special character";
	private final static int NIM_LENGTH = 6;
	private final static Pattern regexU = Pattern.compile(".*[A-Z].*");
	private final static Pattern regexL = Pattern.compile(".*[a-z].*");
	private final static Pattern regexS = Pattern.compile(".*\\W.*");

	@Override
	public void initialize(final Password constraintAnnotation) {
	}

	@Override
	public boolean isValid(final String password) {
		if (password == null || password.length() < NIM_LENGTH) {
			return false;
		}
		return regexU.matcher(password).matches() && regexL.matcher(password).matches() && regexS.matcher(password).matches();
	}

	@Override
	public Tuple<String, Object[]> getErrorMessageKey() {
		return Tuple(message, new Object[]{});
	}

	@Target({ FIELD })
	@Retention(RUNTIME)
	@Constraint(validatedBy = PasswordValidator.class)
	public @interface Password {
		String message() default PasswordValidator.message;

		Class<?>[] groups() default {};

		Class<? extends Payload>[] payload() default {};
	}
}
