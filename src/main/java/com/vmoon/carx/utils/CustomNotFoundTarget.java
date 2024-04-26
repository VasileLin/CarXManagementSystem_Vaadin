package com.vmoon.carx.utils;


import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.RouteNotFoundError;
import com.vmoon.carx.entities.Role;
import com.vmoon.carx.enums.UserRoles;
import com.vmoon.carx.security.AuthenticatedUser;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CustomNotFoundTarget extends RouteNotFoundError {
    private final AuthenticatedUser authenticatedUser;



    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        if (authenticatedUser.get().isEmpty()) {
            event.rerouteTo("login");
        } else {
            Set<Role> userRoles = authenticatedUser.get().get().getRoles();

            if (userRoles.stream().anyMatch(role -> UserRoles.ADMINISTRATOR.getValue().equals(role.getName()))) {
                event.rerouteTo("employers-view");
            } else if (userRoles.stream().anyMatch(role -> UserRoles.MANAGER.getValue().equals(role.getName()))) {
                event.rerouteTo("settings-view");
            } else if (userRoles.stream().anyMatch(role -> UserRoles.CASHIER.getValue().equals(role.getName()))) {
                event.rerouteTo("customers-view");
            }
        }

        return HttpServletResponse.SC_OK;
    }
}
