package com.vmoon.carx.views.recovery;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vmoon.carx.dto.ServiceDto;
import com.vmoon.carx.services.ServicesService;
import com.vmoon.carx.utils.Notifications;
import com.vmoon.carx.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@PageTitle("Deleted Services")
@Route(value = "deleted-services-view", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed({"ADMIN"})
public class DeletedServicesView extends Composite<VerticalLayout> {
    private final ServicesService servicesService;
    DataProvider<ServiceDto, Void> deletedServicesDataProvider;
    @Getter
    Grid<ServiceDto> servicesGrid;
    TextField searchServicesField;

    public DeletedServicesView(ServicesService servicesService) {

        this.servicesService = servicesService;

        servicesGrid = new Grid<>(ServiceDto.class, false);
        servicesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        servicesGrid.setWidth("100%");
        servicesGrid.setHeight("555px");
        servicesGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(servicesGrid);

        Hr hr = new Hr();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button addButton = new Button();
        addButton.setText("Add Employer");
        addButton.setWidth("min-content");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.setPrefixComponent(new Icon(VaadinIcon.PLUS));
        addButton.addClickListener(e -> UI.getCurrent().navigate("employer-form"));

        HorizontalLayout layoutRow2 = new HorizontalLayout();

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(LumoUtility.Padding.LARGE);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        layoutRow2.setHeightFull();
        layoutRow.setFlexGrow(1.0, layoutRow2);
        layoutRow2.setWidth("100%");
        layoutRow2.setHeight("50px");
        layoutRow2.setAlignItems(FlexComponent.Alignment.CENTER);
        layoutRow2.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        searchServicesField = new TextField();
        searchServicesField.setPlaceholder("Search Employers ...");
        searchServicesField.setWidth("100%");
        searchServicesField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchServicesField.addValueChangeListener(e -> searchEmployers(e.getValue().trim()));

        getContent().add(searchServicesField);
        getContent().add(servicesGrid);
        getContent().add(hr);
        getContent().add(layoutRow);
        layoutRow.add(addButton);
        layoutRow.add(layoutRow2);
    }


    private void searchEmployers(String value) {
        if (value.isEmpty()) {
            servicesGrid.setDataProvider(deletedServicesDataProvider);
            servicesGrid.getDataProvider().refreshAll();
        } else {
            DataProvider<ServiceDto, Void> dataProvider = DataProvider.fromCallbacks(
                    query -> {
                        PageRequest pageRequest = PageRequest.of(
                                query.getPage(),
                                query.getPageSize(),
                                query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                        );

                        Page<ServiceDto> page = servicesService.searchService(value, pageRequest, true);
                        return page.stream();
                    },
                    query -> servicesService.countDeleted()
            );

            servicesGrid.setDataProvider(dataProvider);
        }

    }

    private void setGridSampleData(Grid<ServiceDto> serviceDtoGrid) {
        Grid.Column<ServiceDto> idColumn = serviceDtoGrid.addColumn(ServiceDto::getId)
                .setHeader("ID")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("id");

        Grid.Column<ServiceDto> nameColumn = serviceDtoGrid.addColumn(ServiceDto::getName)
                .setHeader("Name")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("name");

        Grid.Column<ServiceDto> priceColumn = serviceDtoGrid.addColumn(ServiceDto::getPrice)
                .setHeader("Price")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("price");

        Grid.Column<ServiceDto> recoveryColumn = serviceDtoGrid.addColumn(new ComponentRenderer<>(serviceDto -> {
            Button recoveryButton = new Button(new Icon(VaadinIcon.RECYCLE), buttonClickEvent -> confirmRecoveryDialog(serviceDto));
            recoveryButton.setTooltipText("Recovery Service");
            recoveryButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
            return recoveryButton;
        })).setHeader("Actions");

        serviceDtoGrid.setColumnOrder(idColumn, nameColumn,priceColumn, recoveryColumn);


        deletedServicesDataProvider = DataProvider.fromCallbacks(
                query -> {
                    PageRequest pageRequest = PageRequest.of(
                            query.getPage(),
                            query.getPageSize(),
                            query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                    );
                    Page<ServiceDto> page = servicesService.allDeletedServices(pageRequest);
                    return page.stream();
                },
                query -> servicesService.countDeleted()
        );

        servicesGrid.setDataProvider(deletedServicesDataProvider);
    }

    private void confirmRecoveryDialog(ServiceDto serviceDto) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Service " + serviceDto.getName());
        dialog.setText("Are you sure you want to recover this service?");
        dialog.setCancelable(true);

        dialog.setConfirmText("Recover");
        dialog.setConfirmButtonTheme("success primary");
        dialog.addConfirmListener(event -> recoverService(serviceDto));
        dialog.open();
    }

    private void recoverService(ServiceDto serviceDto) {
        serviceDto.setIsDeleted(false);
        servicesService.saveService(serviceDto);
        Notifications.successNotification("Service successfully recovered!").open();
        servicesGrid.getDataProvider().refreshAll();
    }


}
