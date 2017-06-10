import com.google.inject.AbstractModule;

import models.RoleDAO;
import models.UserDAO;
import models.impl.RoleDAOImpl;
import models.impl.UserDAOImpl;

/**
 * This class is a Guice module that tells Guice how to bind several different types.
 */
public class Module extends AbstractModule {
    @Override
    public void configure() {
        bind(UserDAO.class).to(UserDAOImpl.class).asEagerSingleton();
        bind(RoleDAO.class).to(RoleDAOImpl.class).asEagerSingleton();
    }
}

