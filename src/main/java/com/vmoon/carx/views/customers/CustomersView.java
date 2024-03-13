package com.vmoon.carx.views.customers;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vmoon.carx.dto.CustomerDto;
import com.vmoon.carx.services.CustomerService;
import com.vmoon.carx.views.MainLayout;
import com.vmoon.carx.views.customerform.CustomerFormView;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@PageTitle("Customers")
@Route(value = "customers-view", layout = MainLayout.class)
@RouteAlias(value = "",layout = MainLayout.class)
@Uses(Icon.class)
public class CustomersView extends Composite<VerticalLayout> {

    Grid<CustomerDto> customersGrid;

    private final CustomerService customerService;

    @Getter
    private Dialog dialog;

    TextField searchCustomersField;


    public CustomersView(CustomerService customerService) {
        this.customerService = customerService;
        VerticalLayout layoutColumn2 = new VerticalLayout();
        customersGrid = new Grid<>(CustomerDto.class,false);
        customersGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        customersGrid.setWidth("100%");
        customersGrid.setHeight("555px");
        customersGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(customersGrid);

        VerticalLayout layoutColumn3 = new VerticalLayout();
        VerticalLayout layoutColumn4 = new VerticalLayout();
        Hr hr = new Hr();
        HorizontalLayout layoutRow = new HorizontalLayout();

        Button addButton = new Button();
        addButton.setPrefixComponent(new Icon(VaadinIcon.PLUS));
        addButton.setText("Add Customer");
        addButton.setWidth("min-content");
        addButton.addClickListener(e -> UI.getCurrent().navigate("customer-form"));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout layoutRow2 = new HorizontalLayout();
        Button exportButton = new Button();
        exportButton.setPrefixComponent(new Icon(VaadinIcon.FILE_TEXT));
        exportButton.setText("Export Customers");
        layoutRow.setAlignSelf(FlexComponent.Alignment.START, exportButton);
        exportButton.setWidth("min-content");
        exportButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().setHeightFull();
        getContent().setWidthFull();
        getContent().setSpacing(false);
        getContent().addClassName(Padding.SMALL);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
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
        layoutRow2.setHeightFull();
        layoutRow.setFlexGrow(1.0, layoutRow2);
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.setHeight("50px");
        layoutRow2.setAlignItems(Alignment.CENTER);
        layoutRow2.setJustifyContentMode(JustifyContentMode.CENTER);

        searchCustomersField = new TextField();
        searchCustomersField.setPlaceholder("Search Customers ...");
        searchCustomersField.setWidth("100%");
        searchCustomersField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchCustomersField.addValueChangeListener(e -> searchCustomers(e.getValue().trim()));


        getContent().add(layoutColumn2);
        layoutColumn2.add(searchCustomersField);
        layoutColumn2.add(customersGrid);
        layoutColumn4.add(hr);
        layoutColumn4.add(layoutRow);
        layoutRow.add(addButton);
        layoutRow.add(layoutRow2);
        layoutRow.add(exportButton);
    }

    private void searchCustomers(String value) {
        DataProvider<CustomerDto, Void> dataProvider = DataProvider.fromCallbacks(
                query -> {
                    PageRequest pageRequest = PageRequest.of(
                            query.getPage(),
                            query.getPageSize(),
                            query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                    );

                    Page<CustomerDto> page = customerService.searchCustomers(value, pageRequest);
                    return page.stream();
                },
                query -> (int) customerService.countSearchResults(value)
        );

        customersGrid.setDataProvider(dataProvider);
    }

    private void setGridSampleData(Grid<CustomerDto> grid) {
        Grid.Column<CustomerDto> nameColumn = grid.addColumn(CustomerDto::getName)
                .setHeader("Full Name")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("name");

        Grid.Column<CustomerDto> phoneColumn = grid.addColumn(CustomerDto::getPhone)
                .setHeader("Phone")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("phone");

        Grid.Column<CustomerDto> carNumberColumn = grid.addColumn(CustomerDto::getCarNumber)
                .setHeader("Car Number")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("carNumber");

        Grid.Column<CustomerDto> carModelColumn = grid.addColumn(CustomerDto::getCarModel)
                .setHeader("Car Model")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("carModel");

        Grid.Column<CustomerDto> emailColumn = grid.addColumn(CustomerDto::getEmail)
                .setHeader("Email")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("email");

        grid.setColumnOrder(nameColumn,phoneColumn,emailColumn,carModelColumn,carNumberColumn);

        grid.addItemDoubleClickListener(event -> {
           CustomerDto customerDto = event.getItem();
           openEditDialog(customerDto);
        });

        DataProvider<CustomerDto, Void> dataProvider = DataProvider.fromCallbacks(
          query -> {
              PageRequest pageRequest = PageRequest.of(
                      query.getPage(),
                      query.getPageSize(),
                      query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
              );
              Page<CustomerDto> page = customerService.allCustomers(pageRequest);
              return page.stream();
          },

          query -> (int) customerService.count()
        );

        grid.setDataProvider(dataProvider);
    }

    private void openEditDialog(CustomerDto customerDto) {
        CustomerFormView customerFormView = new CustomerFormView(customerService);
        customerFormView.setUpdateFlag(true);
        customerFormView.getH3().setText("Update Customer " + customerDto.getName());
        customerFormView.setUpdateCustomer(customerDto);

        customerFormView.getCancelButton().addClickListener(event -> dialog.close());
        customerFormView.getSaveButton().addClickListener(event -> {
            customerService.saveCustomer(customerFormView.getCustomerToUpdate());
            customersGrid.getDataProvider().refreshAll();
            dialog.close();
        });

        dialog = new Dialog(customerFormView);
        dialog.setWidth("auto");
        dialog.setHeight("auto");
        dialog.setDraggable(true);
        dialog.open();
    }

}
