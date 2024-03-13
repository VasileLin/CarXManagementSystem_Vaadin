package com.vmoon.carx.views.reports;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vmoon.carx.entities.Employer;
import com.vmoon.carx.views.MainLayout;

import java.time.LocalDate;

@PageTitle("Reports")
@Route(value = "report-view", layout = MainLayout.class)
@Uses(Icon.class)
public class ReportsView extends Composite<VerticalLayout> {

    public ReportsView() {
        Tabs tabs = new Tabs();
        HorizontalLayout layoutRow = new HorizontalLayout();
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        DatePicker datePicker2 = new DatePicker();
        datePicker2.setValue(LocalDate.now());
        VerticalLayout layoutColumn2 = new VerticalLayout();
        Button buttonPrimary = new Button();
        Grid basicGrid = new Grid(Employer.class);
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setAlignSelf(FlexComponent.Alignment.START, tabs);
        tabs.setWidth("100%");
        setTabsSampleData(tabs);
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.XLARGE);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("80px");
        datePicker.setLabel("From");
        datePicker.setWidth("min-content");
        datePicker2.setLabel("To");
        datePicker2.setWidth("min-content");
        layoutColumn2.setHeightFull();
        layoutRow.setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        buttonPrimary.setText("Export");
        layoutRow.setAlignSelf(FlexComponent.Alignment.END, buttonPrimary);
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        basicGrid.setWidth("100%");
        basicGrid.setHeight("600px");
        basicGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(basicGrid);
        getContent().add(tabs);
        getContent().add(layoutRow);
        layoutRow.add(datePicker);
        layoutRow.add(datePicker2);
        layoutRow.add(layoutColumn2);
        layoutRow.add(buttonPrimary);
        getContent().add(basicGrid);
    }

    private void setTabsSampleData(Tabs tabs) {
        tabs.add(new Tab("Dashboard"));
        tabs.add(new Tab("Payment"));
        tabs.add(new Tab("Shipping"));
    }

    private void setGridSampleData(Grid grid) {
    }

}
