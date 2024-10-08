package com.vmoon.carx.config;

import com.vmoon.carx.services.*;
import com.vmoon.carx.views.employerform.EmployerFormView;
import com.vmoon.carx.views.goods.GoodsRegistrationView;
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

    @Bean(name = "addGoodsView")
    @Scope("prototype")
    public GoodsRegistrationView goodsRegistrationView(GoodsService goodsService, AcquisitionService acquisitionService, CategoriesService goodsCategoryService,CarBrandService carBrandService) {
        return new GoodsRegistrationView(goodsService,acquisitionService,goodsCategoryService,carBrandService);
    }

}
