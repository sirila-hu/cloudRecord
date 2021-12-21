package Listener;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

@Component
public class SirilahuHttpSessionEventPublisher extends HttpSessionEventPublisher {
    private static SessionRegistry sessionRegistry;

    public static void setSessionRegistry(SessionRegistry sessionRegistry) {
        SirilahuHttpSessionEventPublisher.sessionRegistry = sessionRegistry;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        System.out.println("被销毁的session: "+session.getId());

        //从登陆session列表中查找销毁session id
        SessionInformation sessionRegistried = sessionRegistry.getSessionInformation(session.getId());

        //当该session确实注册过时标记过期
        if (sessionRegistried != null)
        {
            sessionRegistried.expireNow();
        }
    }
}
