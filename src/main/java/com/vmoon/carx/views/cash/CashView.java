package com.vmoon.carx.views.cash;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
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
import com.vmoon.carx.enums.Status;
import com.vmoon.carx.services.*;
import com.vmoon.carx.utils.Generators;
import com.vmoon.carx.utils.MailTools;
import com.vmoon.carx.utils.Notifications;
import com.vmoon.carx.utils.jasper.ReceiptGenerator;
import com.vmoon.carx.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import jakarta.mail.MessagingException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


@PageTitle("Cash")
@Route(value = "cash-view", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed({"ADMIN", "CASHIER"})
public class CashView extends Composite<VerticalLayout> {
    private static final Logger logger = LoggerFactory.getLogger(CashView.class);
    private final CustomerService customerService;
    private final ServicesService servicesService;
    private final CashService cashService;
    private final GoodsService goodsService;
    private final CompanyService companyService;
    private final MailTools mailTools;
    private Double servicesTotalPrice = 0.00;
    private Double goodsTotalPrice = 0.00;
    private Double totalPrice = 0.00;
    private String transactionNo;
    TextArea infoTextArea;
    MultiSelectComboBox<ServiceDto> multiSelectComboBox;
    MultiSelectComboBox<GoodsDto> multiSelectGoodsComboBox;
    ComboBox<CustomerDto> customerComboBox;
    List<GoodsDto> selectedGoods;
    List<ServiceDto> selectedServices;
    Paragraph transactionNoText;
    Grid<ServiceDto> serviceGrid;
    Grid<GoodsDto> costOfGoodsGrid;
    Paragraph priceText;
    File directory;


    public CashView(CustomerService customerService, ServicesService servicesService, CashService cashService, GoodsService goodsService, CompanyService companyService, MailTools mailTools) {
        this.customerService = customerService;
        this.servicesService = servicesService;
        this.cashService = cashService;
        this.goodsService = goodsService;
        this.companyService = companyService;
        this.mailTools = mailTools;

        vaadinUI();
    }


    public void vaadinUI() {
        FormLayout formLayout2Col = new FormLayout();

        customerComboBox = new ComboBox<>();
        customerComboBox.setLabel("Select customer");
        customerComboBox.setWidth("min-content");
        customerComboBox.setPrefixComponent(new Icon(VaadinIcon.USER_STAR));
        customerComboBox.addValueChangeListener(event -> populateMultiselectWithCompatibleGoods());
        setCustomerComboBoxData(customerComboBox);

        serviceGrid = new Grid<>(ServiceDto.class, false);
        serviceGrid.setWidth("100%");
        serviceGrid.setHeight("240px");
        serviceGrid.getStyle().set("flex-grow", "0");
        setServiceGridData(serviceGrid);

        costOfGoodsGrid = new Grid<>(GoodsDto.class, false);
        costOfGoodsGrid.setWidth("100%");
        costOfGoodsGrid.setHeight("240px");
        costOfGoodsGrid.getStyle().set("flex-grow", "0");
        setCostOfGoodsGridData(costOfGoodsGrid);


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
        cashButton.setWidthFull();
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
        formLayout2Col2.add(transactionNoText);
        formLayout2Col2.add(priceText);
        getContent().add(formLayout2Col2);
        getContent().add(cashButton);
    }

    private void setServiceGridData(Grid<ServiceDto> serviceGrid) {

        Grid.Column<ServiceDto> idColumn = serviceGrid.addColumn(ServiceDto::getId)
                .setHeader("ID")
                .setResizable(true)
                .setAutoWidth(true);

        Grid.Column<ServiceDto> nameColumn = serviceGrid.addColumn(ServiceDto::getName)
                .setHeader("Name")
                .setResizable(true)
                .setAutoWidth(true);

        Grid.Column<ServiceDto> priceColumn = serviceGrid.addColumn(ServiceDto::getPrice)
                .setHeader("Price")
                .setResizable(true)
                .setAutoWidth(true);

        AtomicReference<ServiceDto> currentService = new AtomicReference<>();

        Dialog priceChangeDialog = new Dialog();
        priceChangeDialog.setCloseOnEsc(true);

        VerticalLayout dialogLayout = new VerticalLayout();
        H6 h6 = new H6();
        h6.setText("Set new price");
        TextField priceField = new TextField("Price");
        Button saveButton = new Button("Save");

        dialogLayout.add(h6, priceField, saveButton);
        priceChangeDialog.add(dialogLayout);

        saveButton.addClickListener(event -> {
            ServiceDto updatedService = currentService.get();
            updatedService.setPrice(Double.parseDouble(priceField.getValue()));
            servicesTotalPrice = calculateSumOfSelectedServices(selectedServices);
            countTotalPrice();
            serviceGrid.setItems(new ArrayList<>());
            serviceGrid.setItems(selectedServices);
            priceChangeDialog.close();

        });

        Grid.Column<ServiceDto> saveColumn = serviceGrid.addColumn(new ComponentRenderer<>(service -> {
            Button openDialogButton = new Button(new Icon(VaadinIcon.BOOK_DOLLAR));
            openDialogButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
            openDialogButton.setTooltipText("Change price of service");
            openDialogButton.addClickListener(e -> {
                currentService.set(service);
                priceField.setValue(String.valueOf(currentService.get().getPrice()));
                priceChangeDialog.open();
            });
            return openDialogButton;
        })).setHeader("Actions");


        serviceGrid.setColumnOrder(idColumn, nameColumn, priceColumn, saveColumn);

    }


    private void setCostOfGoodsGridData(Grid<GoodsDto> costOfGoodsGrid) {

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

        AtomicReference<GoodsDto> currentCost = new AtomicReference<>();
        Dialog quantityDialog = new Dialog();
        H6 h6 = new H6("Set quantity");
        TextField quantityField = new TextField("Quantity");

        Button saveButton = new Button("Save", event -> {
            try {
                int currentStock = currentCost.get().getStock();
                int quantityFieldValue = Integer.parseInt(quantityField.getValue());
                if (quantityFieldValue < currentStock) {
                    currentCost.get().setQuantity(Integer.parseInt(quantityField.getValue()));
                    goodsTotalPrice = calculateSumOfSelectedGoods(selectedGoods);
                    costOfGoodsGrid.setItems(new ArrayList<>());
                    costOfGoodsGrid.setItems(selectedGoods);
                    countTotalPrice();
                    quantityDialog.close();
                } else {
                    Notification.show("Entered value must be less than available stock!");
                }
            } catch (NumberFormatException e) {
                Notification.show("Entered value must be an number!");
            }
        });

        VerticalLayout dialogLayout = new VerticalLayout(h6, quantityField, saveButton);
        quantityDialog.add(dialogLayout);


        Grid.Column<GoodsDto> saveColumn = costOfGoodsGrid.addColumn(new ComponentRenderer<>(cost -> {
            Button changeCostButton = new Button(new Icon(VaadinIcon.BOOK_DOLLAR));
            changeCostButton.setTooltipText("Change quantity");
            changeCostButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
            changeCostButton.addClickListener(e -> {
                currentCost.set(cost);
                quantityField.setValue(String.valueOf(cost.getQuantity()));
                quantityDialog.open();
            });
            return changeCostButton;
        })).setHeader("Actions");


        costOfGoodsGrid.setColumnOrder(nameColumn, costColumn, stockColumn, countColumn, saveColumn);
    }

    private Double calculateSumOfSelectedGoods(List<GoodsDto> selectedGoods) {
        return selectedGoods.
                stream()
                .mapToDouble(g -> g.getCost() * g.getQuantity())
                .sum();
    }

    private Double calculateSumOfSelectedServices(List<ServiceDto> selectedServices) {
        return selectedServices.
                stream()
                .mapToDouble(ServiceDto::getPrice)
                .sum();
    }

    private void populateMultiselectWithCompatibleGoods() {
        multiSelectGoodsComboBox.setItems(goodsService.allGoods()
                .stream()
                .filter(e -> e.getStock() > 0)
                .filter(e -> e.getCarBrand().equals(customerComboBox.getValue().getCarBrand()))
                .filter(e -> e.getCompatibleModels().contains(customerComboBox.getValue().getCarModel()))
                .toList());

        multiSelectGoodsComboBox.setItemLabelGenerator(GoodsDto::getCostName);
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
                directory = new File("reports/" + today);
                cashDto.setReceiptPath(directory.getPath() + "\\" + transactionNo + ".pdf");
                cashService.saveCash(cashDto);

                for (GoodsDto goodsDto : cashDto.getGoods()) {
                    int newStock = goodsDto.getStock() - goodsDto.getQuantity();
                    goodsService.updateStock(goodsDto.getId(), newStock);
                }

                Notifications.successNotification("Cash successfully registered!").open();
                generateReceipt();

            } else {
                Notifications.warningNotification("Quantity must be greater than zero!").open();
            }

        } catch (Exception e) {
            Notifications.errorNotification("Error saving cash :" + e.getMessage()).open();
        }
    }


    private void sendReceipt(String customerEmail,byte[] pdfContent) {
        if (pdfContent != null) {
            try {
                logger.info("Trying to send email to {}", customerEmail);
                mailTools.sendEmailWithAttachment(customerEmail, pdfContent);
                logger.info("Email sent successfully to {}", customerEmail);
            } catch (MessagingException | IOException e) {
                logger.error("Failed to send email to {}: {}", customerEmail, e.getMessage(), e);
                Notification.show("Failed to send email: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
            UI.getCurrent().getPage().reload();
        }
    }


    private void generateReceipt() {
        List<ServiceDto> selectedServices = new ArrayList<>(multiSelectComboBox.getValue());
        List<GoodsDto> selectedGoods = new ArrayList<>(multiSelectGoodsComboBox.getValue());
        String customerEmail = customerComboBox.getValue().getEmail();
        CompanyDto companyDto = companyService.getAllCompanies().get(0);

        JRBeanCollectionDataSource servicesDataSource = new JRBeanCollectionDataSource(selectedServices);
        JRBeanCollectionDataSource goodsDataSource = new JRBeanCollectionDataSource(selectedGoods);

        URL url = getClass().getClassLoader().getResource("META-INF/resources/logo/logo.png");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("transactionNo", transactionNo);
        parameters.put("totalPrice", totalPrice);
        parameters.put("customerName", customerComboBox.getValue().getName());
        parameters.put("carModel", customerComboBox.getValue().getCarBrand().getBrand() + " " + customerComboBox.getValue().getCarModel());
        parameters.put("carNumber", customerComboBox.getValue().getCarNumber());
        parameters.put("servicesDataSource", servicesDataSource);
        parameters.put("goodsDataSource", goodsDataSource);
        parameters.put("companyName", companyDto.getName());
        parameters.put("iban", companyDto.getIban());
        parameters.put("address", companyDto.getAddress());
        parameters.put("details", infoTextArea.getValue());
        parameters.put("logo", url);

        byte[] pdfContent = ReceiptGenerator.cashReceiptGenerator(parameters, directory);

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Generate receipt");
        dialog.setText("Send receipt at customer email address? \n "+customerEmail);
        dialog.setCancelable(true);

        dialog.setConfirmText("Yes");
        dialog.setConfirmButtonTheme("success primary");
        dialog.addConfirmListener(event -> sendReceipt(customerEmail,pdfContent));
        dialog.setCancelText("No");
        dialog.setCancelButtonTheme("warning primary");
        dialog.addCancelListener(event -> UI.getCurrent().getPage().reload());
        dialog.open();

    }


    private void generateRandomTransactionNo() {
        transactionNo = Generators.transactionNoGenerator();
        transactionNoText.setText("Transaction no : " + transactionNo);
    }

    private void setCustomerComboBoxData(ComboBox<CustomerDto> customerComboBox) {
        customerComboBox.setItems(customerService.listCustomers());
        customerComboBox.setItemLabelGenerator(customer -> customer.getName() + " - " + customer.getCarBrand().getBrand() + " " + customer.getCarModel().getModel());
    }

    private void setMultiSelectServicesData(MultiSelectComboBox<ServiceDto> multiSelectService) {
        multiSelectService.setItems(servicesService.allServices());
        multiSelectService.setItemLabelGenerator(ServiceDto::getName);
    }

    private void setGridGoodsSampleData(Grid<GoodsDto> costOfGoodsGrid) {

        selectedGoods = multiSelectGoodsComboBox.getValue().stream().toList();
        selectedGoods.forEach(item -> item.setQuantity(1));
        costOfGoodsGrid.setItems(selectedGoods);

        goodsTotalPrice = calculateSumOfSelectedGoods(selectedGoods);
    }

    private void setGridSampleData(Grid<ServiceDto> servicesGrid) {

        selectedServices = multiSelectComboBox.getValue().stream().toList();
        servicesGrid.setItems(selectedServices);
        servicesTotalPrice = calculateSumOfSelectedServices(selectedServices);

    }

    public void countTotalPrice() {
        totalPrice = goodsTotalPrice + servicesTotalPrice;
        priceText.setText("Total Price:" + totalPrice);
    }


}
