package com.vmoon.carx.views.recovery;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vmoon.carx.dto.CustomerDto;
import com.vmoon.carx.services.CustomerService;
import com.vmoon.carx.utils.Notifications;
import com.vmoon.carx.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@PageTitle("Deleted customers")
@Route(value = "deleted-customers-view", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed({"ADMIN"})
public class DeletedCustomersView extends Composite<VerticalLayout> {

    private final CustomerService customerService;

    Grid<CustomerDto> customersGrid;
    TextField searchCustomersField;
    DataProvider<CustomerDto, Void> deletedCustomersDataProvider;


    public DeletedCustomersView(CustomerService customerService) {
        this.customerService = customerService;
        VerticalLayout layoutColumn2 = new VerticalLayout();
        customersGrid = new Grid<>(CustomerDto.class, false);
        customersGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        customersGrid.setWidth("100%");
        customersGrid.setHeight("555px");
        customersGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(customersGrid);

        VerticalLayout layoutColumn3 = new VerticalLayout();
        VerticalLayout layoutColumn4 = new VerticalLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        HorizontalLayout layoutRow2 = new HorizontalLayout();

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
        getContent().add(layoutColumn4);
        layoutColumn2.add(searchCustomersField);
        layoutColumn2.add(customersGrid);
        layoutColumn4.add(layoutRow);
        layoutRow.add(layoutRow2);
    }


    private void searchCustomers(String value) {
        if (value.isEmpty()) {
            Notifications.warningNotification("Search bar is empty!").open();
            customersGrid.setDataProvider(deletedCustomersDataProvider);
            customersGrid.getDataProvider().refreshAll();
        } else {
            DataProvider<CustomerDto, Void> dataProvider = DataProvider.fromCallbacks(
                    query -> {
                        PageRequest pageRequest = PageRequest.of(
                                query.getPage(),
                                query.getPageSize(),
                                query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                        );

                        Page<CustomerDto> page = customerService.searchDeletedCustomers(value, pageRequest);
                        return page.stream();
                    },
                    query -> (int) customerService.countDeletedSearchResults(value)
            );

            customersGrid.setDataProvider(dataProvider);
        }

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

        Grid.Column<CustomerDto> carModelBrandColumn = grid.addColumn(customer -> {
                    String model = customer.getCarModel() != null ? customer.getCarModel().getModel() : "N/A";
                    String brand = customer.getCarBrand() != null ? customer.getCarBrand().getBrand() : "N/A";
                    return brand + " " + model;
                }).setHeader("Car Model and Brand")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true);

        Grid.Column<CustomerDto> emailColumn = grid.addColumn(CustomerDto::getEmail)
                .setHeader("Email")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("email");

        Grid.Column<CustomerDto> recoveryColumn = grid.addColumn(new ComponentRenderer<>(customerDto -> {
            Button deleteButton = new Button(new Icon(VaadinIcon.RECYCLE), buttonClickEvent -> confirmRecoveryDialog(customerDto));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
            return deleteButton;
        })).setHeader("Actions");

        grid.setColumnOrder(nameColumn, phoneColumn, emailColumn, carModelBrandColumn, carNumberColumn, recoveryColumn);


        deletedCustomersDataProvider = DataProvider.fromCallbacks(
                query -> {
                    PageRequest pageRequest = PageRequest.of(
                            query.getPage(),
                            query.getPageSize(),
                            query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                    );
                    Page<CustomerDto> page = customerService.allDeletedCustomers(pageRequest);
                    return page.stream();
                },

                query -> (int) customerService.countDeleted()
        );

        grid.setDataProvider(deletedCustomersDataProvider);
    }

    public void recoveryCustomer(CustomerDto customerDto) {
        customerDto.setDeleted(false);
        customerService.saveCustomer(customerDto);
        customersGrid.getDataProvider().refreshAll();
        Notifications.successNotification("Customer are successfully recovered!").open();
    }

    private void confirmRecoveryDialog(CustomerDto customerDto) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Customer " + customerDto.getName());
        dialog.setText("Are you sure you want to recovery this customer?");
        dialog.setCancelable(true);

        dialog.setConfirmText("Recover");
        dialog.setConfirmButtonTheme("success primary");
        dialog.addConfirmListener(event -> recoveryCustomer(customerDto));
        dialog.open();
    }

}
