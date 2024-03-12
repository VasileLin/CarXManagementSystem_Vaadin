package com.vmoon.carx.views.employers;

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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vmoon.carx.dto.EmployerDto;
import com.vmoon.carx.services.EmployerService;
import com.vmoon.carx.services.RoleService;
import com.vmoon.carx.views.MainLayout;
import com.vmoon.carx.views.employerform.EmployerUpdateForm;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@PageTitle("Employers")
@Route(value = "employers-view", layout = MainLayout.class)
@Uses(Icon.class)
public class EmployersView extends Composite<VerticalLayout> {
    @Getter
    Grid employersGrid;

    @Getter
    private Dialog dialog;
    private final EmployerService employerService;
    private final RoleService roleService;

    public EmployersView(EmployerService employerService, RoleService roleService) {

        this.employerService = employerService;
        this.roleService = roleService;

        employersGrid = new Grid<>(EmployerDto.class, false);

        Hr hr = new Hr();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        Button buttonPrimary2 = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        employersGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        employersGrid.setWidth("100%");
        employersGrid.setHeight("555px");
        employersGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(employersGrid);
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Padding.LARGE);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        buttonPrimary.setText("Add Employer");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonPrimary.addClickListener(e -> UI.getCurrent().navigate("employer-form"));
        layoutRow2.setHeightFull();
        layoutRow.setFlexGrow(1.0, layoutRow2);
        layoutRow2.setWidth("100%");
        layoutRow2.setHeight("50px");
        layoutRow2.setAlignItems(Alignment.CENTER);
        layoutRow2.setJustifyContentMode(JustifyContentMode.CENTER);
        buttonPrimary2.setText("Export Employers");
        layoutRow.setAlignSelf(FlexComponent.Alignment.START, buttonPrimary2);
        buttonPrimary2.setWidth("min-content");
        buttonPrimary2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getContent().add(employersGrid);
        getContent().add(hr);
        getContent().add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(layoutRow2);
        layoutRow.add(buttonPrimary2);
    }

    private void setGridSampleData(Grid<EmployerDto> employerDtoGrid) {
        Grid.Column<EmployerDto> fullNameColumn = employerDtoGrid.addColumn(EmployerDto::getFullName)
                .setHeader("Full Name")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true);

        Grid.Column<EmployerDto> dateOfBirthColumn = employerDtoGrid.addColumn(EmployerDto::getDateOfBirth)
                .setHeader("Date of Birth")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true);

        Grid.Column<EmployerDto> emailColumn = employerDtoGrid.addColumn(EmployerDto::getEmail)
                .setHeader("Email")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true);

        Grid.Column<EmployerDto> addressColumn = employerDtoGrid.addColumn(EmployerDto::getAddress)
                .setHeader("Address")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true);

        Grid.Column<EmployerDto> phoneColumn = employerDtoGrid.addColumn(EmployerDto::getPhone)
                .setHeader("Email")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true);

        Grid.Column<EmployerDto> roleColumn = employerDtoGrid.addColumn(EmployerDto::getRole)
                .setHeader("Role")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true);

        employerDtoGrid.setColumnOrder(fullNameColumn, dateOfBirthColumn, addressColumn, emailColumn, phoneColumn, roleColumn);

        employerDtoGrid.addItemDoubleClickListener(event -> {
            EmployerDto employerDto = event.getItem();
            openEditDialog(employerDto);
        });

        DataProvider<EmployerDto, Void> dataProvider = DataProvider.fromCallbacks(
                query -> {
                    Page<EmployerDto> page = employerService.allEmployers(
                            PageRequest.of(query.getPage(), query.getPageSize()));
                    return page.stream();
                },
                query -> (int) employerService.count()
        );

        employersGrid.setDataProvider(dataProvider);
    }

    private void openEditDialog(EmployerDto employerDto) {
        EmployerUpdateForm employerUpdateForm = new EmployerUpdateForm(roleService, employerService,this);
        employerUpdateForm.setUpdateEmployer(employerDto);
        dialog = new Dialog(employerUpdateForm);
        dialog.setWidth("auto");
        dialog.setHeight("auto");
        dialog.setDraggable(true);
        dialog.open();
    }


}