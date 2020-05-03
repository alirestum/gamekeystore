package hu.restumali.gamekeystore.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class GKSLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication != null && authentication.getDetails() != null){
            request.getSession().invalidate();
            request.getSession().removeAttribute("firstName");
            request.getSession().removeAttribute("lastName");
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("/");
    }
}
