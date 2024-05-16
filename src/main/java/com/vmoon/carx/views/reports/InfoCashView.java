package com.vmoon.carx.views.reports;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vmoon.carx.dto.CashDto;
import com.vmoon.carx.dto.GoodsDto;
import com.vmoon.carx.dto.ServiceDto;
import jakarta.annotation.security.RolesAllowed;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@PageTitle("Info Cash")
@Uses(Icon.class)
@Component
@Scope("prototype")
@RolesAllowed({"ADMIN", "MANAGER", "CASHIER"})
public class InfoCashView extends Composite<VerticalLayout> {

    Grid<ServiceDto> servicesGrid;
    Grid<GoodsDto> goodsGrid;
    TextField transactionTextField;
    DatePicker datePicker;
    NumberField totalPriceTextField;
    TextArea infoTextArea;
    TextField customerTextField;

    @Setter
    private CashDto cashDto;

    public InfoCashView() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        FormLayout formLayout3Col = new FormLayout();
        transactionTextField = new TextField();
        datePicker = new DatePicker();
        totalPriceTextField = new NumberField();
        servicesGrid = new Grid<>(ServiceDto.class, false);
        goodsGrid = new Grid<>(GoodsDto.class, false);
        infoTextArea = new TextArea();

        customerTextField = new TextField();
        customerTextField.setLabel("Customer");
        customerTextField.setWidth("100%");
        Hr hr = new Hr();
        FormLayout formLayout2Col = new FormLayout();
        H5 h5 = new H5();
        H3 h3 = new H3();
        H5 h52 = new H5();
        FormLayout formLayout2Col2 = new FormLayout();

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        layoutColumn2.setJustifyContentMode(JustifyContentMode.START);
        layoutColumn2.setAlignItems(Alignment.CENTER);
        formLayout3Col.setWidth("100%");
        formLayout3Col.setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("250px", 2), new ResponsiveStep("500px", 3));
        transactionTextField.setLabel("Transaction number");
        transactionTextField.setWidth("min-content");
        datePicker.setLabel("Date");
        datePicker.setWidth("min-content");
        totalPriceTextField.setLabel("Total price");
        totalPriceTextField.setWidth("min-content");
        layoutColumn2.setAlignSelf(FlexComponent.Alignment.START, formLayout2Col);
        formLayout2Col.setWidth("100%");
        h5.setText("Provided services");
        h5.setWidth("max-content");
        h52.setText("Used goods");
        h52.setWidth("max-content");
        formLayout2Col2.setWidth("100%");
        servicesGrid.setWidth("100%");
        servicesGrid.getStyle().set("flex-grow", "0");
        goodsGrid.setWidth("100%");
        goodsGrid.getStyle().set("flex-grow", "0");
        infoTextArea.setLabel("Cash details");
        infoTextArea.setWidth("100%");
        h3.setText("Cash details");
        h3.setWidth("100%");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.setAlignSelf(FlexComponent.Alignment.START, customerTextField);
        layoutColumn2.add(customerTextField);
        layoutColumn2.add(formLayout3Col);
        formLayout3Col.add(transactionTextField);
        formLayout3Col.add(datePicker);
        formLayout3Col.add(totalPriceTextField);
        layoutColumn2.add(hr);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(h5);
        formLayout2Col.add(h52);
        layoutColumn2.add(formLayout2Col2);
        formLayout2Col2.add(servicesGrid);
        formLayout2Col2.add(goodsGrid);
        layoutColumn2.add(infoTextArea);
        setGridGoodsSampleData(goodsGrid);
        setServicesGridSampleData(servicesGrid);
    }

    public void setGridGoodsSampleData(Grid<GoodsDto> grid) {
        Grid.Column<GoodsDto> idColumn = grid.addColumn(GoodsDto::getId)
                .setHeader("ID")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("id");

        Grid.Column<GoodsDto> nameColumn = grid.addColumn(GoodsDto::getCostName)
                .setHeader("Name")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("costName");

        Grid.Column<GoodsDto> costColumn = grid.addColumn(GoodsDto::getCost)
                .setHeader("Cost")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("cost");

        grid.setColumnOrder(idColumn, nameColumn, costColumn);
    }

    public void setServicesGridSampleData(Grid<ServiceDto> grid) {
        Grid.Column<ServiceDto> idColumn = grid.addColumn(ServiceDto::getId)
                .setHeader("Id")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("id");

        Grid.Column<ServiceDto> nameColumn = grid.addColumn(ServiceDto::getName)
                .setHeader("Name")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("name");

        grid.setColumnOrder(idColumn, nameColumn);

    }

    public void setInfoData() {
        servicesGrid.setItems(cashDto.getServices());
        goodsGrid.setItems(cashDto.getGoods());
        transactionTextField.setValue(cashDto.getTransactionNo());
        datePicker.setValue(cashDto.getDate());
        totalPriceTextField.setValue(cashDto.getPrice());
        infoTextArea.setValue(cashDto.getDetails());
        customerTextField.setValue(cashDto.getCustomer().getName() + " -> " +
                cashDto.getCustomer().getCarBrand().getBrand() + " " +
                cashDto.getCustomer().getCarModel());
    }


}
