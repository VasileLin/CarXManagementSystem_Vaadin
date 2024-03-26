package com.vmoon.carx.views.settings;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vmoon.carx.dto.CompanyDto;
import com.vmoon.carx.dto.GoodsDto;
import com.vmoon.carx.services.CompanyService;
import com.vmoon.carx.services.GoodsService;
import com.vmoon.carx.views.MainLayout;
import com.vmoon.carx.views.goods.GoodsRegistrationView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@PageTitle("Settings")
@Route(value = "settings-view", layout = MainLayout.class)
@Uses(Icon.class)
public class SettingsView extends Composite<VerticalLayout> {

    Grid<GoodsDto> basicGrid;
    private VerticalLayout costOfGoodsContent;
    private VerticalLayout companyDataContent;
    Dialog dialog;
    private final GoodsService goodsService;
    private final CompanyService companyService;
    TextField nameTextField;
    TextField addressField;
    TextField ibanField;

    public SettingsView(GoodsService goodsService, CompanyService companyService) {
        this.goodsService = goodsService;
        this.companyService = companyService;

        Tabs tabs = new Tabs();
        Tab tabCostOfGoods = new Tab("Cost of goods");
        Tab tabCompanyData = new Tab("Company data");
        tabs.add(tabCostOfGoods, tabCompanyData);

        initializeCostOfGoodsContent();
        initializeCompanyDataContent();
        initializeCompanyData();

        tabs.addSelectedChangeListener(event -> updateVisibleContent(tabs.getSelectedTab()));

        getContent().add(tabs, costOfGoodsContent, companyDataContent);

        updateVisibleContent(tabCostOfGoods);
    }

    private void updateVisibleContent(Tab selectedTab) {
        costOfGoodsContent.setVisible(false);
        companyDataContent.setVisible(false);
        companyDataContent.setAlignItems(FlexComponent.Alignment.CENTER);

        if ("Cost of goods".equals(selectedTab.getLabel())) {
            costOfGoodsContent.setVisible(true);
        } else if ("Company data".equals(selectedTab.getLabel())) {
            companyDataContent.setVisible(true);
        }
    }

    private void initializeCompanyDataContent() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        layoutColumn2.setAlignItems(FlexComponent.Alignment.CENTER);
        Button saveButton;
        H3 h3;
        h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        nameTextField = new TextField();
        addressField = new TextField();
        ibanField = new TextField();
        HorizontalLayout layoutRow = new HorizontalLayout();

        saveButton = new Button();
        saveButton.setText("Save");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> saveCompanyData());

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Company Data");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        nameTextField.setLabel("Name");
        addressField.setLabel("Address");
        addressField.setWidth("min-content");
        ibanField.setLabel("IBAN");
        ibanField.setWidth("min-content");
        layoutRow.addClassName(LumoUtility.Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(nameTextField);
        formLayout2Col.add(addressField);
        formLayout2Col.add(ibanField);
        layoutColumn2.add(layoutRow);
        layoutRow.add(saveButton);

        companyDataContent = new VerticalLayout(layoutColumn2);
        companyDataContent.setVisible(false);
    }

    private void initializeCompanyData() {
        List<CompanyDto> companyDto = companyService.getAllCompanies();

        if (!companyDto.isEmpty()) {
            nameTextField.setValue(companyDto.get(0).getName());
            addressField.setValue(companyDto.get(0).getAddress());
            ibanField.setValue(companyDto.get(0).getIban());
        }
    }

    private void saveCompanyData() {
        CompanyDto companyDto = CompanyDto.builder()
                .name(nameTextField.getValue())
                .address(addressField.getValue())
                .iban(ibanField.getValue())
                .build();

        companyService.saveCompany(companyDto);
        Notification.show("Company data updated");
    }

    private void initializeCostOfGoodsContent() {
        basicGrid = new Grid<>(GoodsDto.class,false);
        basicGrid.setWidth("100%");
        basicGrid.setHeight("600px");
        setGridSampleData(basicGrid);



        Button addCostButton = new Button("Add Costs");
        addCostButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addCostButton.addClickListener(e -> openAddDialog());

        costOfGoodsContent = new VerticalLayout(basicGrid, addCostButton);
        costOfGoodsContent.setVisible(false);
    }

    private void openAddDialog() {
        GoodsRegistrationView goodsRegistrationView = new GoodsRegistrationView(goodsService);

        dialog = new Dialog(goodsRegistrationView);

        goodsRegistrationView.getCancelButton().addClickListener(e -> dialog.close());

        goodsRegistrationView.getSaveButton().addClickListener(e -> {
             goodsRegistrationView.saveGood();
             basicGrid.getDataProvider().refreshAll();
             dialog.close();
             Notification.show("Good successfully registered!");
        });


        dialog.setWidth("auto");
        dialog.setHeight("auto");
        dialog.setDraggable(true);
        dialog.open();
    }


    private void setGridSampleData(Grid<GoodsDto> grid) {

        Grid.Column<GoodsDto> nameColumn = grid.addColumn(GoodsDto::getCostName)
                .setHeader("Name")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("costName");

        Grid.Column<GoodsDto> costColumn = grid.addColumn(GoodsDto::getCost)
                .setHeader("Cost")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("cost");

        Grid.Column<GoodsDto> stockColumn = grid.addColumn(GoodsDto::getStock)
                .setHeader("Stock")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("stock");

        Grid.Column<GoodsDto> dateColumn = grid.addColumn(GoodsDto::getDate)
                .setHeader("Payment date")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("date");

        grid.addItemDoubleClickListener(event -> {
            GoodsDto goodDto = event.getItem();
            openEditDialog(goodDto);
        });


        grid.setColumnOrder(nameColumn,costColumn,stockColumn,dateColumn);

        DataProvider<GoodsDto, Void> dataProvider = DataProvider.fromCallbacks(
                query -> {
                    PageRequest pageRequest = PageRequest.of(
                            query.getPage(),
                            query.getPageSize(),
                            query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                    );
                    Page<GoodsDto> page = goodsService.allGoods(pageRequest);
                    return page.stream();
                },

                query -> (int) goodsService.count()
        );

        grid.setDataProvider(dataProvider);

    }

    private void openEditDialog(GoodsDto goodDto) {
        GoodsRegistrationView goodsRegistrationView = new GoodsRegistrationView(goodsService);

        goodsRegistrationView.getH3().setText("Update Good " + goodDto.getCostName());
        goodsRegistrationView.setUpdateCustomer(goodDto);

        goodsRegistrationView.getCancelButton().addClickListener(event -> dialog.close());
        goodsRegistrationView.getSaveButton().addClickListener(event -> {
            goodsService.saveGood(goodsRegistrationView.getGoodToUpdate());
            Notification.show("Good updated successfully!");
            basicGrid.getDataProvider().refreshAll();
            dialog.close();
        });

        dialog = new Dialog(goodsRegistrationView);
        dialog.setWidth("auto");
        dialog.setHeight("auto");
        dialog.setDraggable(true);
        dialog.open();
    }

}
