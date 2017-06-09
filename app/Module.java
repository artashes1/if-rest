import com.google.inject.AbstractModule;

import models.UserRepositoryImpl;
import models.UserRepository;
import play.libs.Json;

/**
 * This class is a Guice module that tells Guice how to bind several different types.
 */
public class Module extends AbstractModule {
    @Override
    public void configure() {
        bind(UserRepository.class).to(UserRepositoryImpl.class).asEagerSingleton();
    }
}

