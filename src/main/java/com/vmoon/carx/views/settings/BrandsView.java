package com.vmoon.carx.views.settings;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vmoon.carx.dto.CarBrandDto;
import com.vmoon.carx.dto.CarModelDto;
import com.vmoon.carx.services.CarBrandService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Brands")
@Component
@Scope("prototype")
public class BrandsView extends Composite<VerticalLayout> {

    private final CarBrandService carBrandService;
    private final VerticalLayout addBrandContent;
    private final AddBrandModelView addModelContent;

    ComboBox<CarBrandDto> brandComboBox;
    Grid<CarModelDto> carModelGrid;
    Dialog dialog;

    public BrandsView(CarBrandService carBrandService, AddBrandView addBrandView, AddBrandModelView addBrandModelView) {
        this.carBrandService = carBrandService;
        this.addBrandContent = addBrandView.getContent();
        this.addModelContent = addBrandModelView;

        createUI();
    }

    private void createUI() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();

        brandComboBox = new ComboBox<>();
        brandComboBox.setLabel("Select Brand");
        brandComboBox.setWidth("100%");
        brandComboBox.addValueChangeListener(event -> updateModelGrid());
        setSelectSampleData(brandComboBox);

        H6 h6 = new H6();
        h6.setText("Models of selected brand");
        h6.setWidth("max-content");

        carModelGrid = new Grid<>(CarModelDto.class,false);
        carModelGrid.setWidth("100%");
        carModelGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(carModelGrid);

        Hr hr = new Hr();
        FormLayout formLayout2Col = new FormLayout();
        Button addBrandButton = new Button();
        addBrandButton.setText("Add new brand");
        addBrandButton.setWidth("min-content");
        addBrandButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addBrandButton.addClickListener(event -> openAddDialog());

        Button addModelButton = new Button();
        addModelButton.setText("Add new model to selected brand");
        addModelButton.setWidth("100%");
        addModelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addModelButton.addClickListener(event -> openAddModelDialog());

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);
        layoutColumn2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("1500px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Brands Manager");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(brandComboBox);
        layoutColumn2.add(h6);
        layoutColumn2.add(carModelGrid);
        layoutColumn2.add(hr);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(addBrandButton);
        formLayout2Col.add(addModelButton);
    }

    private void openAddModelDialog() {
        if (brandComboBox.getValue() != null) {
            dialog = new Dialog(addModelContent);
            addModelContent.setCarBrandDto(brandComboBox.getValue());
            addModelContent.getSaveButton().addClickListener(event -> {
                    addModelContent.saveModel();
                    setSelectSampleData(brandComboBox);
                    dialog.close();
            });
            dialog.open();
        } else {
            Notification.show("First chose a brand!");
        }

    }

    private void openAddDialog() {
        dialog = new Dialog(addBrandContent);
        dialog.open();
    }

    private void updateModelGrid() {
        if (brandComboBox.getValue() != null) {
            carModelGrid.setItems(new ArrayList<>());
            carModelGrid.setItems(brandComboBox.getValue().getCarModels());
        }
    }


    private void setSelectSampleData(ComboBox<CarBrandDto> carBrandSelect) {
        List<CarBrandDto> carBrands = carBrandService.allBrands();
        carBrandSelect.setItems(carBrands);
        carBrandSelect.setItemLabelGenerator(CarBrandDto::getBrand);
    }

    private void setGridSampleData(Grid<CarModelDto> carModelGrid) {

        Grid.Column<CarModelDto> idColumn = carModelGrid.addColumn(CarModelDto::getId)
                .setHeader("ID")
                .setResizable(true)
                .setAutoWidth(true);

        Grid.Column<CarModelDto> modelColumn = carModelGrid.addColumn(CarModelDto::getModel)
                .setHeader("Model")
                .setResizable(true)
                .setAutoWidth(true);

        Grid.Column<CarModelDto> yearColumn = carModelGrid.addColumn(CarModelDto::getYear)
                .setHeader("Year")
                .setResizable(true)
                .setAutoWidth(true);

        carModelGrid.setColumnOrder(idColumn, modelColumn, yearColumn);
    }
}
