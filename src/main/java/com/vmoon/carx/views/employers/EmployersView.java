package com.vmoon.carx.views.employers;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
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
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vmoon.carx.dto.EmployerDto;
import com.vmoon.carx.services.EmployerService;
import com.vmoon.carx.services.RoleService;
import com.vmoon.carx.utils.Notifications;
import com.vmoon.carx.utils.SecurityUtils;
import com.vmoon.carx.views.MainLayout;
import com.vmoon.carx.views.employerform.EmployerFormView;
import jakarta.annotation.security.RolesAllowed;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.vmoon.carx.utils.ExcelWorkBooks.createEmployersExcelWorkBook;

@PageTitle("Employers")
@Route(value = "employers-view", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed({"ADMIN", "MANAGER"})
public class EmployersView extends Composite<VerticalLayout> {

    private final EmployerService employerService;
    private final RoleService roleService;
    DataProvider<EmployerDto, Void> employersDataProvider;
    @Getter
    Grid<EmployerDto> employersGrid;
    Button exportButton;
    TextField searchEmployersField;
    @Getter
    private Dialog dialog;

    public EmployersView(EmployerService employerService, RoleService roleService) {

        this.employerService = employerService;
        this.roleService = roleService;

        employersGrid = new Grid<>(EmployerDto.class, false);
        employersGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        employersGrid.setWidth("100%");
        employersGrid.setHeight("555px");
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

        exportButton = new Button();
        exportButton.setPrefixComponent(new Icon(VaadinIcon.FILE_TEXT));
        exportButton.setText("Export Employers");
        exportButton.setWidth("min-content");
        exportButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        configureExportButton(exportButton);

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
        layoutRow.setAlignSelf(FlexComponent.Alignment.START, exportButton);

        searchEmployersField = new TextField();
        searchEmployersField.setPlaceholder("Search Employers ...");
        searchEmployersField.setWidth("100%");
        searchEmployersField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchEmployersField.addValueChangeListener(e -> searchEmployers(e.getValue().trim()));

        getContent().add(searchEmployersField);
        getContent().add(employersGrid);
        getContent().add(hr);
        getContent().add(layoutRow);
        if (SecurityUtils.isUserAdmin()) {
            layoutRow.add(addButton);
        }
        layoutRow.add(layoutRow2);
        layoutRow.add(exportButton);
    }


    private void searchEmployers(String value) {
        if (value.isEmpty()) {
            Notifications.warningNotification("Search bar is empty!").open();
            employersGrid.setDataProvider(employersDataProvider);
            employersGrid.getDataProvider().refreshAll();
        } else {
            DataProvider<EmployerDto, Void> dataProvider = DataProvider.fromCallbacks(
                    query -> {
                        PageRequest pageRequest = PageRequest.of(
                                query.getPage(),
                                query.getPageSize(),
                                query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                        );

                        Page<EmployerDto> page = employerService.searchEmployers(value, pageRequest, false);
                        return page.stream();
                    },
                    query -> (int) employerService.countSearchResults(value, false)
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


        if (SecurityUtils.isUserAdmin()) {
            Grid.Column<EmployerDto> deleteColumn = employerDtoGrid.addColumn(new ComponentRenderer<>(employerDto -> {
                Button deleteButton = new Button(new Icon(VaadinIcon.TRASH), buttonClickEvent -> confirmDeleteDialog(employerDto));
                deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
                return deleteButton;
            })).setHeader("Actions");

            employerDtoGrid.setColumnOrder(fullNameColumn, dateOfBirthColumn, addressColumn, emailColumn, phoneColumn, roleColumn, deleteColumn);
        } else {
            employerDtoGrid.setColumnOrder(fullNameColumn, dateOfBirthColumn, addressColumn, emailColumn, phoneColumn, roleColumn);
        }

        employerDtoGrid.addItemDoubleClickListener(event -> {
            EmployerDto employerDto = event.getItem();
            openEditDialog(employerDto);
        });


        employersDataProvider = DataProvider.fromCallbacks(
                query -> {
                    PageRequest pageRequest = PageRequest.of(
                            query.getPage(),
                            query.getPageSize(),
                            query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                    );
                    Page<EmployerDto> page = employerService.allEmployers(pageRequest);
                    return page.stream();
                },
                query -> (int) employerService.count(false)
        );

        employersGrid.setDataProvider(employersDataProvider);
    }

    private void confirmDeleteDialog(EmployerDto employerDto) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Employer " + employerDto.getFullName());
        dialog.setText("Are you sure you want to delete this employer?");
        dialog.setCancelable(true);

        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> deleteEmployer(employerDto));
        dialog.open();
    }

    private void deleteEmployer(EmployerDto employerDto) {
        employerDto.setIsDeleted(true);
        employerService.saveEmployer(employerDto);
        Notifications.successNotification("Employer are successfully deleted!").open();
        employersGrid.getDataProvider().refreshAll();
    }

    private void openEditDialog(EmployerDto employerDto) {

        EmployerFormView employerFormView = new EmployerFormView(roleService, employerService);
        employerFormView.setUpdateFlag(true);
        employerFormView.getH3().setText("Update Employer " + employerDto.getFullName());
        employerFormView.setUpdateEmployer(employerDto);

        employerFormView.getCancelButton().addClickListener(event -> dialog.close());
        employerFormView.getSaveButton().addClickListener(event -> {
            employerService.saveEmployer(employerFormView.getEmployerToUpdate());
            employersGrid.getDataProvider().refreshAll();
            dialog.close();
        });

        dialog = new Dialog(employerFormView);
        dialog.setWidth("auto");
        dialog.setHeight("auto");
        dialog.setDraggable(true);
        dialog.open();
    }

    private void configureExportButton(Button exportButton) {
        exportButton.addClickListener(event -> {
            String fileName = "employers.xlsx";
            StreamResource resource = new StreamResource(fileName, () -> {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                try (Workbook workbook = createEmployersExcelWorkBook(employersGrid.getLazyDataView().getItems().toList())) {
                    workbook.write(bos);
                } catch (IOException e) {
                    Notifications.errorNotification("Error writing file").open();
                    return null;
                }

                return new ByteArrayInputStream(bos.toByteArray());
            });

            Anchor downloadLink = new Anchor(resource, "");
            downloadLink.getElement().setAttribute("download", true);
            downloadLink.getElement().setAttribute("href", resource);
            downloadLink.getElement().setAttribute("style", "display: none;");

            UI.getCurrent().add(downloadLink);
            downloadLink.getElement().callJsFunction("click");
            Notifications.UploadSuccessNotification(fileName).open();
        });
    }
}