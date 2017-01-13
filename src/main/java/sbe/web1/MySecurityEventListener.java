package sbe.web1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.access.event.AuthorizedEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationFailureCredentialsExpiredEvent;
import org.springframework.security.authentication.event.AuthenticationFailureDisabledEvent;
import org.springframework.security.authentication.event.AuthenticationFailureExpiredEvent;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.session.SessionCreationEvent;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.web.authentication.session.SessionFixationProtectionEvent;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

@Component
public class MySecurityEventListener {
    static final Logger LOG = LoggerFactory.getLogger(MySecurityEventListener.class);

    @EventListener
    public void onAuthenticationFailureBadCredentialsEvent(AuthenticationFailureBadCredentialsEvent e) {
        LOG.info("<+>##SECURITY EVENT##<+> : AuthenticationFailureBadCredentialsEvent, auth = {}", e.getAuthentication());
        LOG.warn("<+>##SECURITY EVENT##<+> : AuthenticationFailureBadCredentialsEvent, ex", e.getException());
    }

    @EventListener
    public void onAuthenticationFailureCredentialsExpiredEvent(AuthenticationFailureCredentialsExpiredEvent e) {
        LOG.info("<+>##SECURITY EVENT##<+> : AuthenticationFailureCredentialsExpiredEvent, auth = {}", e.getAuthentication());
        LOG.warn("<+>##SECURITY EVENT##<+> : AuthenticationFailureCredentialsExpiredEvent, ex", e.getException());
    }

    @EventListener
    public void onAuthenticationFailureDisabledEvent(AuthenticationFailureDisabledEvent e) {
        LOG.info("<+>##SECURITY EVENT##<+> : AuthenticationFailureDisabledEvent, auth = {}", e.getAuthentication());
        LOG.warn("<+>##SECURITY EVENT##<+> : AuthenticationFailureDisabledEvent, ex", e.getException());
    }

    @EventListener
    public void onAuthenticationFailureExpiredEvent(AuthenticationFailureExpiredEvent e) {
        LOG.info("<+>##SECURITY EVENT##<+> : AuthenticationFailureExpiredEvent, auth = {}", e.getAuthentication());
        LOG.warn("<+>##SECURITY EVENT##<+> : AuthenticationFailureExpiredEvent, ex", e.getException());
    }

    @EventListener
    public void onAuthenticationFailureLockedEvent(AuthenticationFailureLockedEvent e) {
        LOG.info("<+>##SECURITY EVENT##<+> : AuthenticationFailureLockedEvent, auth = {}", e.getAuthentication());
        LOG.warn("<+>##SECURITY EVENT##<+> : AuthenticationFailureLockedEvent, ex", e.getException());
    }

    @EventListener
    public void onAuthenticationSuccessEvent(AuthenticationSuccessEvent e) {
        LOG.info("<+>##SECURITY EVENT##<+> : AuthenticationSuccessEvent = {}", e.getAuthentication());
    }

    @EventListener
    public void onAuthorizationFailureEvent(AuthorizationFailureEvent e) {
        LOG.info("<+>##SECURITY EVENT##<+> : AuthorizationFailureEvent, auth = {}", e.getAuthentication());
        LOG.warn("<+>##SECURITY EVENT##<+> : AuthorizationFailureEvent, ex", e.getAccessDeniedException());
    }

    @EventListener
    public void onAuthorizedEvent(AuthorizedEvent e) {
        LOG.info("<+>##SECURITY EVENT##<+> : AuthorizedEvent = {}", e.getAuthentication());
    }

    @EventListener
    public void onHttpSessionCreatedEvent(HttpSessionCreatedEvent e) {
        LOG.info("<+>##SECURITY EVENT##<+> : HttpSessionCreatedEvent, id={}", e.getSession().getId());
    }

    @EventListener
    public void onHttpSessionDestroyedEvent(HttpSessionDestroyedEvent e) {
        LOG.info("<+>##SECURITY EVENT##<+> : HttpSessionDestroyedEvent, id={}", e.getId());
    }

    @EventListener
    public void onSessionCreationEvent(SessionCreationEvent e) {
        LOG.info("<+>##SECURITY EVENT##<+> : SessionCreationEvent");
    }

    @EventListener
    public void onSessionDestroyedEvent(SessionDestroyedEvent e) {
        LOG.info("<+>##SECURITY EVENT##<+> : SessionDestroyedEvent, id={}", e.getId());
    }

    @EventListener
    public void onSessionFixationProtectionEvent(SessionFixationProtectionEvent e) {
        LOG.info("<+>##SECURITY EVENT##<+> : SessionFixationProtectionEvent, old={}, new={}", e.getOldSessionId(), e.getNewSessionId());
    }
}
