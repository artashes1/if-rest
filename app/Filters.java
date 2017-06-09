import javax.inject.Inject;
import javax.inject.Singleton;

import play.filters.headers.SecurityHeadersFilter;
import play.filters.hosts.AllowedHostsFilter;
import play.http.DefaultHttpFilters;

/**
 * This class configures filters that run on every request. This
 * class is queried by Play to get a list of filters.
 */
@Singleton
public class Filters extends DefaultHttpFilters {

    @Inject
    public Filters(AllowedHostsFilter allowedHostsFilter, SecurityHeadersFilter securityHeadersFilter) {
        super(allowedHostsFilter, securityHeadersFilter);
    }
}
