package com.vmoon.carx.views.settings;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vmoon.carx.utils.SecurityUtils;
import com.vmoon.carx.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Settings")
@Route(value = "settings-view", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed({"ADMIN", "MANAGER"})
public class SettingsView extends Composite<VerticalLayout> {

    private final VerticalLayout companyDataContent;
    private final VerticalLayout goodsDataContent;
    private final VerticalLayout brandManagementContent;
    private final VerticalLayout goodsCategoriesContent;


    public SettingsView(CompanyDataContent companyDataContentBean,
                        CostOfGoodsContent costOfGoodsContentBean,
                        BrandsView brandsViewBean, GoodsCategoryContent goodsCategoriesContent) {

        this.companyDataContent = companyDataContentBean.getContent();
        this.goodsDataContent = costOfGoodsContentBean.getContent();
        this.brandManagementContent = brandsViewBean.getContent();
        this.goodsCategoriesContent = goodsCategoriesContent.getContent();

        Tabs tabs = new Tabs();
        Tab tabCostOfGoods = new Tab("Cost of goods");
        Tab tabBrandData = new Tab("Brand management");
        Tab tabCompanyData = new Tab("Company data");
        Tab tabGoodsCategories = new Tab("Goods Categories");

        if (SecurityUtils.isUserAdmin()) {
            tabs.add(tabCostOfGoods, tabCompanyData, tabBrandData,tabGoodsCategories);
        } else if (SecurityUtils.isUserManager()) {
            tabs.add(tabCostOfGoods, tabBrandData);
        }

        tabs.addSelectedChangeListener(event -> updateVisibleContent(tabs.getSelectedTab()));
        getContent().add(tabs, goodsDataContent, companyDataContent, brandManagementContent,goodsCategoriesContent);
        updateVisibleContent(tabCostOfGoods);
    }

    private void updateVisibleContent(Tab selectedTab) {
        goodsDataContent.setVisible(false);
        brandManagementContent.setVisible(false);
        companyDataContent.setVisible(false);
        goodsCategoriesContent.setVisible(false);
        companyDataContent.setAlignItems(FlexComponent.Alignment.CENTER);

        if ("Cost of goods".equals(selectedTab.getLabel())) {
            goodsDataContent.setVisible(true);
        } else if ("Company data".equals(selectedTab.getLabel())) {
            companyDataContent.setVisible(true);
        } else if ("Brand management".equals(selectedTab.getLabel())) {
            brandManagementContent.setVisible(true);
        } else if ("Goods Categories".equals(selectedTab.getLabel())) {
            goodsCategoriesContent.setVisible(true);
        }
    }
}
