package com.campusease.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.security.web.firewall.RequestRejectedException;

public class CustomHttpFirewall extends StrictHttpFirewall {

    @Override
    public FirewalledRequest getFirewalledRequest(HttpServletRequest request) {
        try {
            // Use default StrictHttpFirewall logic
            return super.getFirewalledRequest(request);
        } catch (RequestRejectedException ex) {
            String uri = request.getRequestURI();

            // Allow encoded newlines %0A (LF) and %0D (CR) in the URI by ignoring rejection
            if (uri.contains("%0A") || uri.contains("%0D")) {
                // Return wrapped request without rejecting
                return new FirewalledRequest(request) {

					@Override
					public void reset() {
						// TODO Auto-generated method stub
						
					}};
            }

            // Re-throw any other exception
            throw ex;
        }
    }
}
