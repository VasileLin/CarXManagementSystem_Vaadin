package com.vmoon.carx.views.cash;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vmoon.carx.entities.Employer;
import com.vmoon.carx.views.MainLayout;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Cash")
@Route(value = "cash-view", layout = MainLayout.class)
@Uses(Icon.class)
public class CashView extends Composite<VerticalLayout> {

    public CashView() {
        FormLayout formLayout2Col = new FormLayout();
        ComboBox comboBox = new ComboBox();
        MultiSelectComboBox multiSelectComboBox = new MultiSelectComboBox();
        Paragraph textSmall = new Paragraph();
        Paragraph textSmall2 = new Paragraph();
        Hr hr = new Hr();
        H6 h6 = new H6();
        Grid basicGrid = new Grid(Employer.class);
        Hr hr2 = new Hr();
        FormLayout formLayout2Col2 = new FormLayout();
        Button buttonPrimary = new Button();
        Button buttonSecondary = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.CENTER);
        getContent().setAlignItems(Alignment.CENTER);
        formLayout2Col.setWidth("100%");
        comboBox.setLabel("Customer");
        comboBox.setWidth("min-content");
        setComboBoxSampleData(comboBox);
        multiSelectComboBox.setLabel("Select rendered services");
        multiSelectComboBox.setWidth("min-content");
        setMultiSelectComboBoxSampleData(multiSelectComboBox);
        textSmall.setText("Transaction no");
        textSmall.setWidth("100%");
        textSmall.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        textSmall2.setText("Total Price");
        textSmall2.setWidth("100%");
        textSmall2.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        h6.setText("Selected Services");
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, h6);
        h6.setWidth("max-content");
        basicGrid.setWidth("100%");
        basicGrid.setHeight("480px");
        basicGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(basicGrid);
        formLayout2Col2.setWidth("100%");
        buttonPrimary.setText("Cash");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSecondary.setText("Generate receipt");
        buttonSecondary.setWidth("min-content");
        getContent().add(formLayout2Col);
        formLayout2Col.add(comboBox);
        formLayout2Col.add(multiSelectComboBox);
        formLayout2Col.add(textSmall);
        formLayout2Col.add(textSmall2);
        getContent().add(hr);
        getContent().add(h6);
        getContent().add(basicGrid);
        getContent().add(hr2);
        getContent().add(formLayout2Col2);
        formLayout2Col2.add(buttonPrimary);
        formLayout2Col2.add(buttonSecondary);
    }

    record SampleItem(String value, String label, Boolean disabled) {
    }

    private void setComboBoxSampleData(ComboBox comboBox) {
        List<SampleItem> sampleItems = new ArrayList<>();
        sampleItems.add(new SampleItem("first", "First", null));
        sampleItems.add(new SampleItem("second", "Second", null));
        sampleItems.add(new SampleItem("third", "Third", Boolean.TRUE));
        sampleItems.add(new SampleItem("fourth", "Fourth", null));
        comboBox.setItems(sampleItems);
        comboBox.setItemLabelGenerator(item -> ((SampleItem) item).label());
    }

    private void setMultiSelectComboBoxSampleData(MultiSelectComboBox multiSelectComboBox) {
        List<SampleItem> sampleItems = new ArrayList<>();
        sampleItems.add(new SampleItem("first", "First", null));
        sampleItems.add(new SampleItem("second", "Second", null));
        sampleItems.add(new SampleItem("third", "Third", Boolean.TRUE));
        sampleItems.add(new SampleItem("fourth", "Fourth", null));
        multiSelectComboBox.setItems(sampleItems);
        multiSelectComboBox.setItemLabelGenerator(item -> ((SampleItem) item).label());
    }

    private void setGridSampleData(Grid grid) {

    }


}
