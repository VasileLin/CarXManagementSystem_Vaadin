package com.vmoon.carx.config;

import com.vaadin.flow.component.dialog.Dialog;
import com.vmoon.carx.services.EmployerService;
import com.vmoon.carx.services.RoleService;
import com.vmoon.carx.views.employerform.EmployerFormView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ViewBeans {

    @Bean(name = "updateViewBean")
    @Scope("prototype")
    public EmployerFormView employerUpdateForm(RoleService roleService, EmployerService employerService){
        return new EmployerFormView(roleService,employerService);
    }

}
