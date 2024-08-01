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
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vmoon.carx.dto.EmployerDto;
import com.vmoon.carx.services.EmployerService;
import com.vmoon.carx.utils.Notifications;
import com.vmoon.carx.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


@PageTitle("Employers")
@Route(value = "deleted-employers-view", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed({"ADMIN"})
public class DeletedEmployerView extends Composite<VerticalLayout> {

    private final EmployerService employerService;
    DataProvider<EmployerDto, Void> deletedEmployersDataProvider;
    @Getter
    Grid<EmployerDto> employersGrid;
    TextField searchEmployersField;

    public DeletedEmployerView(EmployerService employerService) {

        this.employerService = employerService;

        employersGrid = new Grid<>(EmployerDto.class, false);
        employersGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        employersGrid.setWidth("100%");
        employersGrid.setHeight("450px");
        employersGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(employersGrid);

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
        layoutRow.addClassName(Padding.LARGE);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        layoutRow2.setHeightFull();
        layoutRow.setFlexGrow(1.0, layoutRow2);
        layoutRow2.setWidth("100%");
        layoutRow2.setHeight("50px");
        layoutRow2.setAlignItems(Alignment.CENTER);
        layoutRow2.setJustifyContentMode(JustifyContentMode.CENTER);

        searchEmployersField = new TextField();
        searchEmployersField.setPlaceholder("Search Employers ...");
        searchEmployersField.setWidth("100%");
        searchEmployersField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchEmployersField.addValueChangeListener(e -> searchEmployers(e.getValue().trim()));

        getContent().add(searchEmployersField);
        getContent().add(employersGrid);
        getContent().add(hr);
        getContent().add(layoutRow);
        layoutRow.add(addButton);
        layoutRow.add(layoutRow2);
    }


    private void searchEmployers(String value) {
        if (value.isEmpty()) {
            Notifications.warningNotification("Search bar is empty!").open();
            employersGrid.setDataProvider(deletedEmployersDataProvider);
            employersGrid.getDataProvider().refreshAll();
        } else {
            DataProvider<EmployerDto, Void> dataProvider = DataProvider.fromCallbacks(
                    query -> {
                        PageRequest pageRequest = PageRequest.of(
                                query.getPage(),
                                query.getPageSize(),
                                query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                        );

                        Page<EmployerDto> page = employerService.searchEmployers(value, pageRequest, true);
                        return page.stream();
                    },
                    query -> (int) employerService.countSearchResults(value, true)
            );

            employersGrid.setDataProvider(dataProvider);
        }

    }

    private void setGridSampleData(Grid<EmployerDto> employerDtoGrid) {
        Grid.Column<EmployerDto> fullNameColumn = employerDtoGrid.addColumn(EmployerDto::getFullName)
                .setHeader("Full Name")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("fullName");

        Grid.Column<EmployerDto> dateOfBirthColumn = employerDtoGrid.addColumn(EmployerDto::getDateOfBirth)
                .setHeader("Date of Birth")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("dateOfBirth");

        Grid.Column<EmployerDto> emailColumn = employerDtoGrid.addColumn(EmployerDto::getEmail)
                .setHeader("Email")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("email");

        Grid.Column<EmployerDto> addressColumn = employerDtoGrid.addColumn(EmployerDto::getAddress)
                .setHeader("Address")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("address");

        Grid.Column<EmployerDto> phoneColumn = employerDtoGrid.addColumn(EmployerDto::getPhone)
                .setHeader("Phone")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("phone");

        Grid.Column<EmployerDto> roleColumn = employerDtoGrid.addColumn(EmployerDto::getRole)
                .setHeader("Role")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("role");

        Grid.Column<EmployerDto> recoveryColumn = employerDtoGrid.addColumn(new ComponentRenderer<>(employerDto -> {
            Button recoveryButton = new Button(new Icon(VaadinIcon.RECYCLE), buttonClickEvent -> confirmRecoveryDialog(employerDto));
            recoveryButton.setTooltipText("Recovery Employer");
            recoveryButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
            return recoveryButton;
        })).setHeader("Actions");

        employerDtoGrid.setColumnOrder(fullNameColumn, dateOfBirthColumn, addressColumn, emailColumn, phoneColumn, roleColumn, recoveryColumn);


        deletedEmployersDataProvider = DataProvider.fromCallbacks(
                query -> {
                    PageRequest pageRequest = PageRequest.of(
                            query.getPage(),
                            query.getPageSize(),
                            query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                    );
                    Page<EmployerDto> page = employerService.allDeletedEmployers(pageRequest);
                    return page.stream();
                },
                query -> (int) employerService.count(true)
        );

        employersGrid.setDataProvider(deletedEmployersDataProvider);
    }

    private void confirmRecoveryDialog(EmployerDto employerDto) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Employer " + employerDto.getFullName());
        dialog.setText("Are you sure you want to recover this employer?");
        dialog.setCancelable(true);

        dialog.setConfirmText("Recover");
        dialog.setConfirmButtonTheme("success primary");
        dialog.addConfirmListener(event -> recoverEmployer(employerDto));
        dialog.open();
    }

    private void recoverEmployer(EmployerDto employerDto) {
        employerDto.setIsDeleted(false);
        employerService.saveEmployer(employerDto);
        Notifications.successNotification("Employer are successfully recovered!").open();
        employersGrid.getDataProvider().refreshAll();
    }


}
