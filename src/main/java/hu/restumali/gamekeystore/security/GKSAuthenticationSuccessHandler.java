package hu.restumali.gamekeystore.security;

import hu.restumali.gamekeystore.model.UserEntity;
import hu.restumali.gamekeystore.repository.UserRepository;
import hu.restumali.gamekeystore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class GKSAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserEntity user = userService.getUserByEmail(authentication.getName());
        HttpSession session = request.getSession();
        session.setAttribute("firstName", user.getFirstName());
        session.setAttribute("lastName", user.getLastName());
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
