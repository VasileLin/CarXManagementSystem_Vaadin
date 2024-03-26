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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vmoon.carx.dto.CashDto;
import com.vmoon.carx.dto.CashGridDto;
import com.vmoon.carx.dto.GoodsDto;
import com.vmoon.carx.services.CashService;
import com.vmoon.carx.services.GoodsService;
import com.vmoon.carx.views.MainLayout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;

@PageTitle("Reports")
@Route(value = "report-view", layout = MainLayout.class)
@Uses(Icon.class)
public class ReportsView extends Composite<VerticalLayout> {

    private VerticalLayout revenuesContent;
    private VerticalLayout costsContent;
    private final CashService cashService;
    private final GoodsService goodsService;
    DatePicker fromDatePicker;
    DatePicker toDatePicker;
    DatePicker fromGoodsDatePicker;
    DatePicker toGoodsDatePicker;
    Grid<GoodsDto> goodsGrid;
    Grid<CashGridDto> cashGrid;
    TextField searchCustomersField;
    Dialog dialog;
    DataProvider<CashGridDto, Void> dataProvider;

    public ReportsView(CashService cashService, GoodsService goodsService) {
        this.cashService = cashService;
        this.goodsService = goodsService;
        Tabs tabs = new Tabs();
        Tab tabRevenuesReport = new Tab("Revenues");
        Tab tabCashReport = new Tab("Costs");
        tabs.add(tabRevenuesReport,tabCashReport);

        initializeCashReport();
        initializeCostsReport();

        tabs.addSelectedChangeListener(event -> updateVisibleContent(tabs.getSelectedTab()));

        getContent().add(tabs, revenuesContent,costsContent);

        updateVisibleContent(tabRevenuesReport);

    }

    private void initializeCostsReport() {
        HorizontalLayout layoutRow = new HorizontalLayout();
        goodsGrid = new Grid<>(GoodsDto.class,false);

        cashGrid.addItemDoubleClickListener(item -> openInfoDialog(cashService.getCashByTransactionNo(item.getItem().getTransactionNo())));
        fromGoodsDatePicker = new DatePicker();
        fromGoodsDatePicker.addValueChangeListener(e -> goodsGrid.getDataProvider().refreshAll());
        fromGoodsDatePicker.setValue(LocalDate.now());
        toGoodsDatePicker = new DatePicker();
        toGoodsDatePicker.setValue(LocalDate.now());
        toGoodsDatePicker.addValueChangeListener(e -> goodsGrid.getDataProvider().refreshAll());
        VerticalLayout layoutColumn2 = new VerticalLayout();
        Button exportButton = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.XLARGE);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("80px");
        fromGoodsDatePicker.setLabel("From");
        fromGoodsDatePicker.setWidth("min-content");
        toGoodsDatePicker.setLabel("To");
        toGoodsDatePicker.setWidth("min-content");
        layoutColumn2.setHeightFull();
        layoutRow.setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        exportButton.setText("Export");
        layoutRow.setAlignSelf(FlexComponent.Alignment.END, exportButton);
        exportButton.setWidth("min-content");
        exportButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        goodsGrid.setWidth("100%");
        goodsGrid.setHeight("600px");
        goodsGrid.getStyle().set("flex-grow", "0");
        setGoodsGridSampleData(goodsGrid);
        getContent().add(layoutRow);
        layoutRow.add(fromGoodsDatePicker);
        layoutRow.add(toGoodsDatePicker);
        layoutRow.add(layoutColumn2);
        layoutRow.add(exportButton);

        costsContent = new VerticalLayout(layoutRow,goodsGrid);
        costsContent.setVisible(false);
    }

    private void openInfoDialog(CashDto item) {
        InfoCashView infoCashView = new InfoCashView();
        infoCashView.setCashDto(item);
        infoCashView.setInfoData();

        dialog = new Dialog(infoCashView);
        dialog.setWidth("70%");
        dialog.setHeight("auto");
        dialog.setDraggable(true);
        dialog.open();

    }

    private void setGoodsGridSampleData(Grid<GoodsDto> goodsGrid) {
        Grid.Column<GoodsDto> nameColumn = goodsGrid.addColumn(GoodsDto::getCostName)
                .setHeader("Name")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("costName");

        Grid.Column<GoodsDto> costColumn = goodsGrid.addColumn(GoodsDto::getCost)
                .setHeader("Cost")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("cost");

        Grid.Column<GoodsDto> dateColumn = goodsGrid.addColumn(GoodsDto::getDate)
                .setHeader("Purchased date")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("date");

        Grid.Column<GoodsDto> statusColumn = goodsGrid.addColumn(GoodsDto::getStock)
                .setHeader("Available stock")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("stock");

        goodsGrid.setColumnOrder(nameColumn,costColumn,dateColumn,statusColumn);

        DataProvider<GoodsDto, Void> dataProvider = DataProvider.fromCallbacks(
                query -> {
                    PageRequest pageRequest = PageRequest.of(
                            query.getPage(),
                            query.getPageSize(),
                            query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                    );

                    Page<GoodsDto> page = goodsService.allGoodsDate(pageRequest,fromGoodsDatePicker.getValue(),toGoodsDatePicker.getValue());
                    return page.stream();
                },
                query -> (int) goodsService.countDateResult(fromGoodsDatePicker.getValue(),toGoodsDatePicker.getValue())
        );

        goodsGrid.setDataProvider(dataProvider);
    }

    private void updateVisibleContent(Tab selectedTab) {
        revenuesContent.setVisible(false);
        costsContent.setVisible(false);
        costsContent.setAlignItems(FlexComponent.Alignment.CENTER);

        if ("Revenues".equals(selectedTab.getLabel())) {
            revenuesContent.setVisible(true);
        } else if ("Costs".equals(selectedTab.getLabel())) {
            costsContent.setVisible(true);
        }
    }

    private void initializeCashReport() {
        HorizontalLayout layoutRow = new HorizontalLayout();
        cashGrid = new Grid<>(CashGridDto.class,false);


        fromDatePicker = new DatePicker();
        fromDatePicker.addValueChangeListener(e -> cashGrid.getDataProvider().refreshAll());
        fromDatePicker.setValue(LocalDate.now());
        toDatePicker = new DatePicker();
        toDatePicker.setValue(LocalDate.now());
        toDatePicker.addValueChangeListener(e -> cashGrid.getDataProvider().refreshAll());

        searchCustomersField = new TextField();
        searchCustomersField.setLabel("Search by transaction number");
        searchCustomersField.setPlaceholder("Enter transaction number ...");
        searchCustomersField.setWidth("80%");
        searchCustomersField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchCustomersField.addValueChangeListener(e -> searchCash(e.getValue().trim()));

        VerticalLayout layoutColumn2 = new VerticalLayout();
        Button exportButton = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.XLARGE);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("80px");
        fromDatePicker.setLabel("From");
        fromDatePicker.setWidth("min-content");
        toDatePicker.setLabel("To");
        toDatePicker.setWidth("min-content");
        layoutColumn2.setHeightFull();
        layoutRow.setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        exportButton.setText("Export");
        layoutRow.setAlignSelf(FlexComponent.Alignment.END, exportButton);
        exportButton.setWidth("min-content");
        exportButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cashGrid.setWidth("100%");
        cashGrid.setHeight("600px");
        cashGrid.getStyle().set("flex-grow", "0");
        setCashGridSampleData(cashGrid);
        getContent().add(layoutRow);
        layoutRow.add(fromDatePicker);
        layoutRow.add(toDatePicker);
        layoutRow.add(searchCustomersField);
        layoutRow.add(layoutColumn2);
        layoutRow.add(exportButton);

        revenuesContent = new VerticalLayout(layoutRow,cashGrid);
        revenuesContent.setVisible(false);
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

                                    Notification.show("File not found", 3000, Notification.Position.MIDDLE);
                                    return new ByteArrayInputStream(new byte[0]);
                                }
                            });


                    Anchor downloadLink = new Anchor(resource, "");
                    downloadLink.getElement().setAttribute("download", true);
                    downloadLink.getElement().setAttribute("href", resource);
                    downloadLink.getElement().setAttribute("style","display: none;");

                    UI.getCurrent().add(downloadLink);
                    downloadLink.getElement().callJsFunction("click");
                } else {

                    Notification.show("The requested file does not exist.", 3000, Notification.Position.MIDDLE);
                }
            });
            saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_SMALL);
            return saveButton;
        })).setHeader("Actions");

        grid.setColumnOrder(transactionColumn,dateColumn,priceColumn,statusColumn,receiptColumn);


        dataProvider = DataProvider.fromCallbacks(
                query -> {
                    PageRequest pageRequest = PageRequest.of(
                            query.getPage(),
                            query.getPageSize(),
                            query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                    );

                    Page<CashGridDto> page = cashService.allCashDate(pageRequest,fromDatePicker.getValue(),toDatePicker.getValue());
                    return page.stream();
                },
                query -> (int) cashService.countDateResult(fromDatePicker.getValue(),toDatePicker.getValue())
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

}
