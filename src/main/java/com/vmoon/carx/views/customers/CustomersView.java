package com.vmoon.carx.views.customers;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vmoon.carx.entities.Employer;
import com.vmoon.carx.views.MainLayout;

@PageTitle("Customers")
@Route(value = "customes-view", layout = MainLayout.class)
@Uses(Icon.class)
public class CustomersView extends Composite<VerticalLayout> {

    public CustomersView() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        Grid stripedGrid = new Grid(Employer.class);
        VerticalLayout layoutColumn3 = new VerticalLayout();
        VerticalLayout layoutColumn4 = new VerticalLayout();
        Hr hr = new Hr();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        Button buttonPrimary2 = new Button();
        getContent().setHeightFull();
        getContent().setWidthFull();
        getContent().setSpacing(false);
        getContent().addClassName(Padding.SMALL);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        stripedGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        stripedGrid.setWidth("100%");
        stripedGrid.setHeight("555px");
        stripedGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(stripedGrid);
        layoutColumn2.setFlexGrow(1.0, layoutColumn3);
        layoutColumn3.setWidthFull();
        layoutColumn3.setSpacing(false);
        layoutColumn3.setPadding(false);
        layoutColumn4.setWidth("100%");
        layoutColumn4.getStyle().set("flex-grow", "1");
        layoutRow.setWidthFull();
        layoutColumn4.setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        buttonPrimary.setText("Add Customer");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        layoutRow2.setHeightFull();
        layoutRow.setFlexGrow(1.0, layoutRow2);
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.setHeight("50px");
        layoutRow2.setAlignItems(Alignment.CENTER);
        layoutRow2.setJustifyContentMode(JustifyContentMode.CENTER);
        buttonPrimary2.setText("Export Customers");
        layoutRow.setAlignSelf(FlexComponent.Alignment.START, buttonPrimary2);
        buttonPrimary2.setWidth("min-content");
        buttonPrimary2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getContent().add(layoutColumn2);
        layoutColumn2.add(stripedGrid);
        layoutColumn2.add(layoutColumn3);
        layoutColumn3.add(layoutColumn4);
        layoutColumn4.add(hr);
        layoutColumn4.add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(layoutRow2);
        layoutRow.add(buttonPrimary2);
    }

    private void setGridSampleData(Grid grid) {

    }

}
