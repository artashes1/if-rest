import com.google.inject.AbstractModule;

import models.RoleRepository;
import models.UserRepository;
import models.impl.RoleRepositoryImpl;
import models.impl.UserRepositoryImpl;

/**
 * This class is a Guice module that tells Guice how to bind several different types.
 */
public class Module extends AbstractModule {
    @Override
    public void configure() {
        bind(UserRepository.class).to(UserRepositoryImpl.class).asEagerSingleton();
        bind(RoleRepository.class).to(RoleRepositoryImpl.class).asEagerSingleton();
    }
}

