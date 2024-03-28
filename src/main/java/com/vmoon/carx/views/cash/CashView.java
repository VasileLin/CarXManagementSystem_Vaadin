package com.vmoon.carx.views.cash;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vmoon.carx.dto.*;
import com.vmoon.carx.services.*;
import com.vmoon.carx.utils.Generators;
import com.vmoon.carx.utils.Status;
import com.vmoon.carx.utils.jasper.ReceiptGenerator;
import com.vmoon.carx.views.MainLayout;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@PageTitle("Cash")
@Route(value = "cash-view", layout = MainLayout.class)
@Uses(Icon.class)
public class CashView extends Composite<VerticalLayout> {
    TextArea infoTextArea;
    private final CustomerService customerService;
    private final ServicesService servicesService;
    private final CashService cashService;
    private final GoodsService goodsService;
    private final CompanyService companyService;
    private Double servicesTotalPrice = 0.00;
    private Double goodsTotalPrice = 0.00;
    private Double totalPrice = 0.00;
    private String transactionNo;
    MultiSelectComboBox<ServiceDto> multiSelectComboBox;
    MultiSelectComboBox<GoodsDto> multiSelectGoodsComboBox;
    ComboBox<CustomerDto> customerComboBox;
    List<GoodsDto> selectedGoods;
    Paragraph transactionNoText;
    Grid<ServiceDto> serviceGrid;
    Grid<GoodsDto> costOfGoodsGrid;
    Paragraph priceText;
    File directory;

    public CashView(CustomerService customerService, ServicesService servicesService, CashService cashService, GoodsService goodsService, CompanyService companyService) {
        this.customerService = customerService;
        this.servicesService = servicesService;
        this.cashService = cashService;
        this.goodsService = goodsService;
        this.companyService = companyService;

        vaadinUI();
    }


    public void vaadinUI() {
        FormLayout formLayout2Col = new FormLayout();

        customerComboBox = new ComboBox<>();
        customerComboBox.setLabel("Select customer");
        customerComboBox.setWidth("min-content");
        customerComboBox.setPrefixComponent(new Icon(VaadinIcon.USER_STAR));
        setCustomerComboBoxData(customerComboBox);

        serviceGrid = new Grid<>(ServiceDto.class);
        serviceGrid.setWidth("100%");
        serviceGrid.setHeight("240px");
        serviceGrid.getStyle().set("flex-grow", "0");

        costOfGoodsGrid = new Grid<>(GoodsDto.class, false);
        costOfGoodsGrid.setWidth("100%");
        costOfGoodsGrid.setHeight("240px");
        costOfGoodsGrid.getStyle().set("flex-grow", "0");

        Grid.Column<GoodsDto> nameColumn = costOfGoodsGrid.addColumn(GoodsDto::getCostName)
                .setHeader("Cost Name")
                .setResizable(true)
                .setAutoWidth(true);

        Grid.Column<GoodsDto> costColumn = costOfGoodsGrid.addColumn(GoodsDto::getCost)
                .setHeader("Cost")
                .setResizable(true)
                .setAutoWidth(true);

        Grid.Column<GoodsDto> stockColumn = costOfGoodsGrid.addColumn(GoodsDto::getStock)
                .setHeader("Stock")
                .setResizable(true)
                .setAutoWidth(true);

        Grid.Column<GoodsDto> countColumn = costOfGoodsGrid.addColumn(new NumberRenderer<>(GoodsDto::getQuantity, "%d", Locale.US, "0"))
                .setHeader("Quantity")
                .setKey("quantity")
                .setEditorComponent(new IntegerField());


        Grid.Column<GoodsDto> saveColumn = costOfGoodsGrid.addColumn(new ComponentRenderer<>(cost -> {
            Button savingButton = new Button(new Icon(VaadinIcon.CHECK));
            savingButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);

            Dialog quantityDialog = new Dialog();

            VerticalLayout dialogLayout = new VerticalLayout();
            H6 h6 = new H6();
            h6.setText("Set quantity");
            TextField quantityField = new TextField("Quantity");
            Button saveButton = new Button("Save");

            dialogLayout.add(h6, quantityField, saveButton);
            quantityDialog.add(dialogLayout);

            saveButton.addClickListener(event -> {
                cost.setQuantity(Integer.parseInt(quantityField.getValue()));
                costOfGoodsGrid.getDataProvider().refreshAll();

                goodsTotalPrice = selectedGoods.
                        stream()
                        .mapToDouble(g -> g.getCost() * g.getQuantity())
                        .sum();

                countTotalPrice();
                quantityDialog.close();
            });

            savingButton.addClickListener(e -> quantityDialog.open());
            return savingButton;
        })).setHeader("Actions");


        costOfGoodsGrid.setColumnOrder(nameColumn, costColumn, stockColumn, countColumn, saveColumn);

        multiSelectComboBox = new MultiSelectComboBox<>();
        multiSelectComboBox.setLabel("Select rendered services");
        multiSelectComboBox.setWidth("min-content");
        setMultiSelectServicesData(multiSelectComboBox);

        multiSelectComboBox.addSelectionListener(e -> {
            setGridSampleData(serviceGrid);
            countTotalPrice();
        });

        multiSelectGoodsComboBox = new MultiSelectComboBox<>();
        multiSelectGoodsComboBox.setLabel("Select used goods");
        multiSelectGoodsComboBox.setWidth("100%");
        setMultiSelectGoodsData(multiSelectGoodsComboBox);

        multiSelectGoodsComboBox.addSelectionListener(e -> {
            setGridGoodsSampleData(costOfGoodsGrid);
            countTotalPrice();
        });

        transactionNoText = new Paragraph();
        priceText = new Paragraph();
        Hr hr = new Hr();
        H6 h6 = new H6();
        H6 h4 = new H6();

        Hr hr2 = new Hr();
        hr2.setWidth("100%");
        FormLayout formLayout2Col2 = new FormLayout();
        Button cashButton = new Button();

        infoTextArea = new TextArea();
        infoTextArea.setPrefixComponent(new Icon(VaadinIcon.INFO));
        infoTextArea.setWidth("100%");

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.CENTER);
        getContent().setAlignItems(Alignment.CENTER);

        formLayout2Col.setWidth("100%");
        transactionNoText.setWidth("100%");
        transactionNoText.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        generateRandomTransactionNo();

        priceText.setText("Total Price:" + servicesTotalPrice);
        priceText.setWidth("100%");
        priceText.getStyle().set("font-size", "var(--lumo-font-size-xs)");

        h6.setText("Selected Services");
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, h6);
        h6.setWidth("max-content");

        h4.setText("Used Goods");
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, h4);
        h4.setWidth("max-content");

        formLayout2Col2.setWidth("100%");
        cashButton.setText("Cash");
        cashButton.setWidth("min-content");
        cashButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cashButton.addClickListener(e -> cashServices());
        getContent().add(formLayout2Col);
        formLayout2Col.add(customerComboBox);
        formLayout2Col.add(multiSelectComboBox);
        getContent().add(multiSelectGoodsComboBox);
        getContent().add(hr);
        getContent().add(h6);
        getContent().add(serviceGrid);
        getContent().add(h4);
        getContent().add(costOfGoodsGrid);
        getContent().add(hr2);
        getContent().add(infoTextArea);
        getContent().add(formLayout2Col2);
        formLayout2Col2.add(transactionNoText);
        formLayout2Col2.add(priceText);
        formLayout2Col2.add(cashButton);

    }

    private void cashServices() {
        CashDto cashDto = CashDto.builder()
                .transactionNo(transactionNo)
                .price(totalPrice)
                .date(LocalDate.now())
                .status(Status.SUCCESS.name())
                .details(infoTextArea.getValue())
                .build();

        cashDto.setServices(multiSelectComboBox.getValue());
        cashDto.setGoods(multiSelectGoodsComboBox.getValue());
        cashDto.setCustomer(customerComboBox.getValue());

        try {

            if (cashDto.getGoods().stream().anyMatch(goodsDto -> goodsDto.getQuantity() != 0)) {

                String today = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
                directory = new File("reports/"+today);
                cashDto.setReceiptPath(directory.getPath()+"\\"+transactionNo+ ".pdf");
                cashService.saveCash(cashDto);

                for (GoodsDto goodsDto : cashDto.getGoods()) {
                    int newStock = goodsDto.getStock() - goodsDto.getQuantity();
                    goodsService.updateStock(goodsDto.getId(), newStock);
                }

                Notification.show("Cash successfully registered!");
                generateReceipt();
                customerComboBox.setValue(null);
                multiSelectComboBox.setValue(new HashSet<>());
                multiSelectGoodsComboBox.setValue(new HashSet<>());
                generateRandomTransactionNo();
                totalPrice = 0.00;
                goodsTotalPrice = 0.00;
                servicesTotalPrice = 0.00;
                countTotalPrice();
                infoTextArea.setValue("");
                costOfGoodsGrid.getDataProvider().refreshAll();
                serviceGrid.getDataProvider().refreshAll();

            } else {
                Notification.show("Quantity must be greater than zero!");
            }

        } catch (Exception e) {
            Notification.show("Error saving cash :" + e.getMessage());
        }
    }

    private void generateReceipt() {
        List<ServiceDto> selectedServices = new ArrayList<>(multiSelectComboBox.getValue());
        List<GoodsDto> selectedGoods = new ArrayList<>(multiSelectGoodsComboBox.getValue());
        CompanyDto companyDto = companyService.getAllCompanies().get(0);

        JRBeanCollectionDataSource servicesDataSource = new JRBeanCollectionDataSource(selectedServices);
        JRBeanCollectionDataSource goodsDataSource = new JRBeanCollectionDataSource(selectedGoods);

        URL url = getClass().getClassLoader().getResource("META-INF/resources/logo/logo.png");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("transactionNo", transactionNo);
        parameters.put("totalPrice", totalPrice);
        parameters.put("customerName", customerComboBox.getValue().getName());
        parameters.put("carModel", customerComboBox.getValue().getCarModel());
        parameters.put("carNumber", customerComboBox.getValue().getCarNumber());
        parameters.put("servicesDataSource", servicesDataSource);
        parameters.put("goodsDataSource", goodsDataSource);
        parameters.put("companyName", companyDto.getName());
        parameters.put("iban", companyDto.getIban());
        parameters.put("address", companyDto.getAddress());
        parameters.put("details", infoTextArea.getValue());
        parameters.put("logo", url);

        ReceiptGenerator.cashReceiptGenerator(parameters,directory);

    }


    private void setMultiSelectGoodsData(MultiSelectComboBox<GoodsDto> multiSelectGoodsComboBox) {
        multiSelectGoodsComboBox.setItems(goodsService.allGoods()
                .stream()
                .filter(e -> e.getStock() > 0)
                .toList());

        multiSelectGoodsComboBox.setItemLabelGenerator(GoodsDto::getCostName);
    }

    private void generateRandomTransactionNo() {
        transactionNo = Generators.transactionNoGenerator();
        transactionNoText.setText("Transaction no : " + transactionNo);
    }

    private void setCustomerComboBoxData(ComboBox<CustomerDto> customerComboBox) {
        customerComboBox.setItems(customerService.listCustomers());
        customerComboBox.setItemLabelGenerator(CustomerDto::getName);
    }

    private void setMultiSelectServicesData(MultiSelectComboBox<ServiceDto> multiSelectService) {
        multiSelectService.setItems(servicesService.allServices());
        multiSelectService.setItemLabelGenerator(ServiceDto::getName);
    }

    private void setGridGoodsSampleData(Grid<GoodsDto> costOfGoodsGrid) {

        selectedGoods = multiSelectGoodsComboBox.getValue().stream().toList();
        selectedGoods.forEach(item -> item.setQuantity(1));
        costOfGoodsGrid.setItems(selectedGoods);

        goodsTotalPrice = selectedGoods.
                stream()
                .mapToDouble(g -> g.getCost() * g.getQuantity())
                .sum();
    }

    private void setGridSampleData(Grid<ServiceDto> servicesGrid) {
        List<ServiceDto> selectedServices = multiSelectComboBox.getValue().stream().toList();
        servicesGrid.setItems(selectedServices);

        servicesTotalPrice = selectedServices.
                stream()
                .mapToDouble(ServiceDto::getPrice)
                .sum();

    }

    public void countTotalPrice() {
        totalPrice = goodsTotalPrice + servicesTotalPrice;
        priceText.setText("Total Price:" + totalPrice);
    }


}
