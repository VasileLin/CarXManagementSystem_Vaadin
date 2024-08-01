package com.vmoon.carx.views.reports;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vmoon.carx.dto.CashDto;
import com.vmoon.carx.dto.CashGridDto;
import com.vmoon.carx.services.CashService;
import com.vmoon.carx.utils.Notifications;
import jakarta.annotation.security.RolesAllowed;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

import static com.vmoon.carx.utils.ExcelWorkBooks.createTransactionsExcelWorkBook;


@PageTitle("Revenues")
@Uses(Icon.class)
@Component
@Scope("prototype")
@RolesAllowed({"ADMIN", "MANAGER", "CASHIER"})
public class RevenuesView extends Composite<VerticalLayout> {

    private final CashService cashService;
    private final InfoCashView infoCashView;
    DatePicker fromDatePicker;
    DatePicker toDatePicker;
    Grid<CashGridDto> cashGrid;
    TextField searchTransactionsTextBox;
    DataProvider<CashGridDto, Void> dataProvider;
    Dialog dialog;

    public RevenuesView(CashService cashService, InfoCashView infoCashView) {
        this.cashService = cashService;
        this.infoCashView = infoCashView;

        initializeCashReport();
    }

    private void initializeCashReport() {
        HorizontalLayout layoutRow = new HorizontalLayout();

        cashGrid = new Grid<>(CashGridDto.class, false);
        cashGrid.setWidth("100%");
        cashGrid.setHeight("450px");
        cashGrid.getStyle().set("flex-grow", "0");
        cashGrid.addItemDoubleClickListener(item -> openInfoDialog(cashService.getCashByTransactionNo(item.getItem().getTransactionNo())));
        setCashGridSampleData(cashGrid);

        fromDatePicker = new DatePicker();
        fromDatePicker.addValueChangeListener(e -> cashGrid.getDataProvider().refreshAll());
        fromDatePicker.setValue(LocalDate.now());
        fromDatePicker.setLabel("From");
        fromDatePicker.setWidth("min-content");

        toDatePicker = new DatePicker();
        toDatePicker.setValue(LocalDate.now());
        toDatePicker.addValueChangeListener(e -> cashGrid.getDataProvider().refreshAll());
        toDatePicker.setLabel("To");
        toDatePicker.setWidth("min-content");

        searchTransactionsTextBox = new TextField();
        searchTransactionsTextBox.setLabel("Search by transaction number");
        searchTransactionsTextBox.setPlaceholder("Enter transaction number ...");
        searchTransactionsTextBox.setWidth("80%");
        searchTransactionsTextBox.setTooltipText("Search by transaction number from receipt");
        searchTransactionsTextBox.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchTransactionsTextBox.addValueChangeListener(e -> searchCash(e.getValue().trim()));

        Button exportButton = new Button();
        exportButton.setText("Export");
        layoutRow.setAlignSelf(FlexComponent.Alignment.END, exportButton);
        exportButton.setWidth("min-content");
        exportButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        exportButton.setPrefixComponent(new Icon(VaadinIcon.DOWNLOAD));
        configureExportButton(exportButton);

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(LumoUtility.Gap.XLARGE);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("80px");

        VerticalLayout layoutColumn2 = new VerticalLayout();
        layoutColumn2.setHeightFull();
        layoutRow.setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        getContent().add(layoutRow);
        getContent().add(cashGrid);
        layoutRow.add(fromDatePicker);
        layoutRow.add(toDatePicker);
        layoutRow.add(searchTransactionsTextBox);
        layoutRow.add(layoutColumn2);
        layoutRow.add(exportButton);
    }

    private void setCashGridSampleData(Grid<CashGridDto> grid) {
        Grid.Column<CashGridDto> transactionColumn = grid.addColumn(CashGridDto::getTransactionNo)
                .setHeader("Transaction Number")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("transactionNo");

        Grid.Column<CashGridDto> dateColumn = grid.addColumn(CashGridDto::getDate)
                .setHeader("Date")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("date");

        Grid.Column<CashGridDto> priceColumn = grid.addColumn(CashGridDto::getPrice)
                .setHeader("Final Price")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("price");

        Grid.Column<CashGridDto> statusColumn = grid.addColumn(CashGridDto::getStatus)
                .setHeader("Status")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("status");

        Grid.Column<CashGridDto> receiptColumn = grid.addColumn(new ComponentRenderer<>(cash -> {
            Button saveButton = new Button(new Icon(VaadinIcon.DOWNLOAD), buttonClickEvent -> {
                CashDto cashDto = cashService.getCashByTransactionNo(cash.getTransactionNo());
                String receiptPath = cashDto.getReceiptPath();
                File file = new File(receiptPath);

                if (file.exists()) {
                    StreamResource resource = new StreamResource(file.getName(),
                            () -> {
                                try {
                                    return new FileInputStream(file);
                                } catch (FileNotFoundException e) {

                                    Notifications.errorNotification("File not found").open();
                                    return new ByteArrayInputStream(new byte[0]);
                                }
                            });


                    Anchor downloadLink = new Anchor(resource, "");
                    downloadLink.getElement().setAttribute("download", true);
                    downloadLink.getElement().setAttribute("href", resource);
                    downloadLink.getElement().setAttribute("style", "display: none;");

                    UI.getCurrent().add(downloadLink);
                    downloadLink.getElement().callJsFunction("click");
                } else {
                    Notifications.warningNotification("The requested file does not exist.").open();
                }
            });
            saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
            saveButton.setTooltipText("Download receipt");
            return saveButton;
        })).setHeader("Actions");

        grid.setColumnOrder(transactionColumn, dateColumn, priceColumn, statusColumn, receiptColumn);


        dataProvider = DataProvider.fromCallbacks(
                query -> {
                    PageRequest pageRequest = PageRequest.of(
                            query.getPage(),
                            query.getPageSize(),
                            query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                    );

                    Page<CashGridDto> page = cashService.allCashDate(pageRequest, fromDatePicker.getValue(), toDatePicker.getValue());
                    return page.stream();
                },
                query -> (int) cashService.countDateResult(fromDatePicker.getValue(), toDatePicker.getValue())
        );

        grid.setDataProvider(dataProvider);
    }

    private void searchCash(String value) {
        DataProvider<CashGridDto, Void> dataProvider = DataProvider.fromCallbacks(
                query -> {
                    PageRequest pageRequest = PageRequest.of(
                            query.getPage(),
                            query.getPageSize(),
                            query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                    );

                    Page<CashGridDto> page = cashService.searchCash(value, pageRequest);
                    return page.stream();
                },
                query -> (int) cashService.countSearchResults(value)
        );

        cashGrid.setDataProvider(dataProvider);
    }

    private void configureExportButton(Button exportButton) {
        exportButton.addClickListener(event -> {
            String fileName = "transactions.xlsx";
            StreamResource resource = new StreamResource(fileName, () -> {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                List<CashGridDto> gridDtoList = cashGrid.getLazyDataView().getItems().toList();

                if (gridDtoList.isEmpty()) {
                    Notifications.warningNotification("Grid is empty!").open();
                } else {
                    try (Workbook workbook = createTransactionsExcelWorkBook(gridDtoList)) {
                        workbook.write(bos);
                    } catch (IOException e) {
                        Notifications.errorNotification("Error writing file").open();
                    }
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

    private void openInfoDialog(CashDto item) {
        infoCashView.setCashDto(item);
        infoCashView.setInfoData();

        dialog = new Dialog(infoCashView);
        dialog.setWidth("70%");
        dialog.setHeight("auto");
        dialog.setDraggable(true);
        dialog.open();

    }

}
