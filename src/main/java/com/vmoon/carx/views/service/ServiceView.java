package com.vmoon.carx.views.service;

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
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vmoon.carx.dto.ServiceDto;
import com.vmoon.carx.services.ServicesService;
import com.vmoon.carx.views.MainLayout;
import com.vmoon.carx.views.serviceform.ServiceFormView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@PageTitle("Service")
@Route(value = "service-view", layout = MainLayout.class)
@Uses(Icon.class)
public class ServiceView extends Composite<VerticalLayout> {

    Grid<ServiceDto> servicesGrid;
    private final ServicesService servicesService;
    Dialog dialog;

    public ServiceView(ServicesService servicesService) {

        this.servicesService = servicesService;

        vaadinUI();
    }

    public void vaadinUI() {
        VerticalLayout layoutColumn2 = new VerticalLayout();

        servicesGrid = new Grid<>(ServiceDto.class,false);
        servicesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        servicesGrid.setWidth("100%");
        servicesGrid.setHeight("555px");
        servicesGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(servicesGrid);

        VerticalLayout layoutColumn3 = new VerticalLayout();
        Hr hr = new Hr();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button addServiceButton = new Button();
        addServiceButton.setText("Add Service");
        addServiceButton.setWidth("min-content");
        addServiceButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addServiceButton.addClickListener(e -> UI.getCurrent().navigate("service-form"));
        addServiceButton.setPrefixComponent(new Icon(VaadinIcon.PLUS));


        HorizontalLayout layoutRow2 = new HorizontalLayout();
        Button exportButton = new Button();
        exportButton.setText("Export Services");
        exportButton.setPrefixComponent(new Icon(VaadinIcon.FILE_TEXT));
        layoutRow.setAlignSelf(FlexComponent.Alignment.START, exportButton);
        exportButton.setWidth("min-content");
        exportButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidthFull();
        layoutColumn3.setWidth("100%");
        layoutColumn3.getStyle().set("flex-grow", "1");
        layoutRow.setWidthFull();
        layoutColumn3.setFlexGrow(1.0, layoutRow);
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
        getContent().add(layoutColumn2);
        layoutColumn2.add(servicesGrid);
        layoutColumn2.add(layoutColumn3);
        layoutColumn3.add(hr);
        layoutColumn3.add(layoutRow);
        layoutRow.add(addServiceButton);
        layoutRow.add(layoutRow2);
        layoutRow.add(exportButton);
    }

    private void setGridSampleData(Grid<ServiceDto> grid) {

        Grid.Column<ServiceDto> idColumn = grid.addColumn(ServiceDto::getId)
                .setHeader("ID")
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

        Grid.Column<ServiceDto> priceColumn = grid.addColumn(ServiceDto::getPrice)
                .setHeader("Price")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("price");


        grid.setColumnOrder(idColumn, nameColumn,priceColumn);

        grid.addItemDoubleClickListener(event -> {
            ServiceDto serviceDto = event.getItem();
            openEditDialog(serviceDto);
        });

        DataProvider<ServiceDto, Void> dataProvider = DataProvider.fromCallbacks(
                query -> {
                    PageRequest pageRequest = PageRequest.of(
                            query.getPage(),
                            query.getPageSize(),
                            query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                    );
                    Page<ServiceDto> page = servicesService.allServices(pageRequest);
                    return page.stream();
                },
                query -> (int) servicesService.count()
        );

        grid.setDataProvider(dataProvider);
    }

    private void openEditDialog(ServiceDto serviceDto) {
        ServiceFormView serviceFormView = new ServiceFormView(servicesService);
        serviceFormView.setUpdateFlag(true);
        serviceFormView.getH3().setText("Update Service "+ serviceDto.getName());
        serviceFormView.setUpdateService(serviceDto);

        serviceFormView.getCancelButton().addClickListener(event -> dialog.close());
        serviceFormView.getSaveButton().addClickListener(event -> {
            servicesService.saveService(serviceFormView.getServiceToUpdate());
            servicesGrid.getDataProvider().refreshAll();
            dialog.close();
        });

        dialog = new Dialog(serviceFormView);
        dialog.setWidth("auto");
        dialog.setHeight("auto");
        dialog.setDraggable(true);
        dialog.open();
    }


}
