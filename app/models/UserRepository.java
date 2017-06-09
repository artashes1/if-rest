package models;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.bson.types.ObjectId;

public interface UserRepository {

    CompletionStage<List<User>> list();

    CompletionStage<User> save(User user);

    CompletionStage<Optional<User>> get(ObjectId id);
}

