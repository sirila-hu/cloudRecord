package com.example.top_sirilahu.authentication;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class UserSessionControlAuthenticationStrategy extends ConcurrentSessionControlAuthenticationStrategy
{
    private boolean exceptionIfMaximumExceeded = false;

    public UserSessionControlAuthenticationStrategy(SessionRegistry sessionRegistry) {
        super(sessionRegistry);
    }

    @Override
    protected void allowableSessionsExceeded(List<SessionInformation> sessions, int allowableSessions, SessionRegistry registry) throws SessionAuthenticationException {
        if (!exceptionIfMaximumExceeded && sessions != null) {
            sessions.sort(Comparator.comparing(SessionInformation::getLastRequest));
            int maximumSessionsExceededBy = sessions.size() - allowableSessions;
            List<SessionInformation> sessionsToBeExpired = sessions.subList(0, maximumSessionsExceededBy);
            Iterator var6 = sessionsToBeExpired.iterator();

            while(var6.hasNext()) {
                SessionInformation session = (SessionInformation)var6.next();
                session.expireNow();
            }

        } else {
            throw new SessionAuthenticationException(this.messages.getMessage("ConcurrentSessionControlAuthenticationStrategy.exceededAllowed", new Object[]{allowableSessions}, "Maximum sessions of {0} for this principal exceeded"));
        }
    }

    @Override
    public void setExceptionIfMaximumExceeded(boolean exceptionIfMaximumExceeded) {
        this.exceptionIfMaximumExceeded = exceptionIfMaximumExceeded;
    }
}
