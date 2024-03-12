package com.vmoon.carx.views.settings;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vmoon.carx.entities.Employer;
import com.vmoon.carx.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Settings")
@Route(value = "settings-view", layout = MainLayout.class)
@Uses(Icon.class)
public class SettingsView extends Composite<VerticalLayout> {

    public SettingsView() {
        Tabs tabs = new Tabs();
        Grid basicGrid = new Grid(Employer.class);
        Hr hr = new Hr();
        Button buttonPrimary = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        tabs.setWidth("100%");
        setTabsSampleData(tabs);
        basicGrid.setWidth("100%");
        basicGrid.setHeight("600px");
        basicGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(basicGrid);
        buttonPrimary.setText("Add Costs");
        getContent().setAlignSelf(FlexComponent.Alignment.START, buttonPrimary);
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getContent().add(tabs);
        getContent().add(basicGrid);
        getContent().add(hr);
        getContent().add(buttonPrimary);
    }

    private void setTabsSampleData(Tabs tabs) {
        tabs.add(new Tab("Dashboard"));
        tabs.add(new Tab("Payment"));
        tabs.add(new Tab("Shipping"));
    }

    private void setGridSampleData(Grid grid) {

    }

}
