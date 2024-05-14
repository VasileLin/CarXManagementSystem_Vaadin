package com.vmoon.carx.views.reports;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vmoon.carx.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Reports")
@Route(value = "report-view", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed({"ADMIN","MANAGER"})
public class ReportsView extends Composite<VerticalLayout> {

    private final VerticalLayout revenuesContent;
    private final VerticalLayout costsContent;


    public ReportsView(CostOfGoodsView costOfGoodsView, RevenuesView revenuesView) {

        Tabs tabs = new Tabs();
        Tab tabRevenuesReport = new Tab("Revenues");
        Tab tabCashReport = new Tab("Costs");
        tabs.add(tabRevenuesReport,tabCashReport);

        this.revenuesContent = revenuesView.getContent();
        this.costsContent = costOfGoodsView.getContent();

        tabs.addSelectedChangeListener(event -> updateVisibleContent(tabs.getSelectedTab()));

        getContent().add(tabs, revenuesContent,costsContent);

        updateVisibleContent(tabRevenuesReport);

    }

    private void updateVisibleContent(Tab selectedTab) {
        revenuesContent.setVisible(false);
        costsContent.setVisible(false);
        costsContent.setAlignItems(FlexComponent.Alignment.CENTER);

        if ("Revenues".equals(selectedTab.getLabel())) {
            revenuesContent.setVisible(true);
        } else if ("Costs".equals(selectedTab.getLabel())) {
            costsContent.setVisible(true);
        }
    }










}
