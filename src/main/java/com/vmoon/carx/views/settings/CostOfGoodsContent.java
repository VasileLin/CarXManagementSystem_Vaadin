package com.vmoon.carx.views.settings;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vmoon.carx.dto.CarBrandDto;
import com.vmoon.carx.dto.CarModelDto;
import com.vmoon.carx.dto.GoodsCategoryDto;
import com.vmoon.carx.dto.GoodsDto;
import com.vmoon.carx.mappers.GoodsMapper;
import com.vmoon.carx.services.AcquisitionService;
import com.vmoon.carx.services.CarBrandService;
import com.vmoon.carx.services.GoodsCategoryService;
import com.vmoon.carx.services.GoodsService;
import com.vmoon.carx.views.goods.GoodsRegistrationView;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class CostOfGoodsContent  extends Composite<VerticalLayout> {

    Grid<GoodsDto> goodsDtoGrid;
    Dialog dialog;
    private final GoodsService goodsService;
    private final AcquisitionService acquisitionService;
    private final GoodsCategoryService goodsCategoryService;
    private final CarBrandService carBrandService;
    GoodsRegistrationView goodsRegistrationView;
    VerticalLayout categoryLayout;

    public CostOfGoodsContent(GoodsService goodsService, AcquisitionService acquisitionService, GoodsCategoryService goodsCategoryService, CarBrandService carBrandService) {
        this.goodsService = goodsService;
        this.acquisitionService = acquisitionService;
        this.goodsCategoryService = goodsCategoryService;
        this.carBrandService = carBrandService;

        initializeCostOfGoodsContent();
    }

    private void initializeCostOfGoodsContent() {
        FlexLayout flexLayout = new FlexLayout();
        flexLayout.setWidthFull();

        Accordion accordion = new Accordion();
        accordion.setHeight("500px");
        accordion.getStyle().set("flex-grow", "1");
        accordion.getStyle().set("overflow-y", "auto");
        setAccordionCategoryData(accordion);

        TextField searchGoods = new TextField();
        searchGoods.setPlaceholder("Search Goods ...");
        searchGoods.setWidthFull();
        searchGoods.setClearButtonVisible(true);
        searchGoods.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchGoods.addClassName("search-bar");

        goodsDtoGrid = new Grid<>(GoodsDto.class, false);
        goodsDtoGrid.setHeight("500px");
        goodsDtoGrid.getStyle().set("flex-grow", "6");
        setGridSampleData(goodsDtoGrid);

        VerticalLayout gridLayout = new VerticalLayout(searchGoods, goodsDtoGrid);
        gridLayout.setPadding(false);
        gridLayout.setSpacing(false);
        gridLayout.setWidthFull();

        flexLayout.add(accordion, gridLayout);
        flexLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        flexLayout.setFlexBasis("auto", accordion);
        flexLayout.setFlexBasis("auto", gridLayout);

        flexLayout.setFlexGrow(1, accordion);
        flexLayout.setFlexGrow(6, gridLayout);

        Button addCostButton = new Button("Add Costs");
        addCostButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addCostButton.getStyle().set("margin-top", "50px");
        addCostButton.addClickListener(e -> openAddDialog());

        getContent().add(flexLayout,addCostButton);

    }

    private void setAccordionCategoryData(Accordion accordion) {
        List<CarBrandDto> carBrands = carBrandService.allBrands();

        for (GoodsCategoryDto category : goodsCategoryService.getAllCategories()) {
            categoryLayout = new VerticalLayout();
            categoryLayout.setPadding(false);
            categoryLayout.setSpacing(false);
            categoryLayout.getStyle().set("max-height", "200px");
            categoryLayout.getStyle().set("overflow-y", "auto");

            TextField searchBrands = new TextField();
            searchBrands.setPlaceholder("Search brand ...");
            searchBrands.setWidthFull();
            searchBrands.setClearButtonVisible(true);
            searchBrands.setPrefixComponent(new Icon(VaadinIcon.SEARCH));

            VerticalLayout brandsContainer = new VerticalLayout();
            brandsContainer.setPadding(false);
            brandsContainer.setSpacing(false);
            for (CarBrandDto carBrand : carBrands) {
                Button brandButton = createBrandButton(category, carBrand);
                brandsContainer.add(brandButton);
            }
            categoryLayout.add(searchBrands, brandsContainer);

            searchBrands.addValueChangeListener(e -> searchBrand(e.getValue(), category, brandsContainer, carBrands));

            accordion.add(category.getName(), categoryLayout);
        }

    }

    private void searchBrand(String searchTerm, GoodsCategoryDto category, VerticalLayout brandsContainer, List<CarBrandDto> carBrands) {
        List<CarBrandDto> filteredBrands = carBrands.stream()
                .filter(carBrand -> carBrand.getBrand().toLowerCase().contains(searchTerm.toLowerCase()))
                .toList();

        brandsContainer.removeAll();
        for (CarBrandDto filteredBrand : filteredBrands) {
            brandsContainer.add(createBrandButton(category, filteredBrand));
        }
    }

    private Button createBrandButton(GoodsCategoryDto category, CarBrandDto carBrand) {
        Button brandButton = new Button(carBrand.getBrand(), buttonClickEvent -> updateGoodsGrid(category.getId(), carBrand.getId()));
        brandButton.setWidthFull();
        brandButton.setIcon(VaadinIcon.CAR.create());
        return brandButton;
    }

    private void updateGoodsGrid(int categoryId, int brandId) {
        DataProvider<GoodsDto, Void> dataProvider = DataProvider.fromCallbacks(
                query -> {
                    PageRequest pageRequest = PageRequest.of(
                            query.getPage(),
                            query.getPageSize(),
                            query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                    );

                    Page<GoodsDto> page = goodsService.fetchGoodsForCategoryAndBrand(categoryId,brandId,pageRequest);
                    return page.stream();
                },
                query -> (int) goodsService.countSearchResults(categoryId,brandId)
        );

        goodsDtoGrid.setDataProvider(dataProvider);
    }



    private void openAddDialog() {

        goodsRegistrationView = new GoodsRegistrationView(goodsService,acquisitionService,goodsCategoryService,carBrandService);
        dialog = new Dialog(goodsRegistrationView);

        goodsRegistrationView.getCancelButton().addClickListener(e -> dialog.close());
        goodsRegistrationView.getSaveButton().addClickListener(e -> {
            try {
                goodsRegistrationView.saveGood();
                goodsDtoGrid.getDataProvider().refreshAll();
                dialog.close();
            } catch (Exception ex) {
                Notification.show("Failed registering good: " + ex.getMessage());
            }

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
                .setSortProperty("name");

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

        Grid.Column<GoodsDto> modelColumn = grid.addColumn(model -> model.getCompatibleModels().stream()
                        .map(CarModelDto::getModel)
                        .collect(Collectors.joining(", ")))
                .setHeader("Compatible Models")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("carModel");

        Grid.Column<GoodsDto> actionColumn = grid.addColumn(new ComponentRenderer<>(
                good -> {
                    Button buyButton = new Button(new Icon(VaadinIcon.DOLLAR), buttonClickEvent -> openAcquisitionDialog(good));

                    buyButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
                    return buyButton;
                })).setHeader("Actions");

        grid.addItemDoubleClickListener(event -> {
            GoodsDto goodDto = event.getItem();
            openEditDialog(goodDto);
        });


        grid.setColumnOrder(nameColumn, costColumn, stockColumn, dateColumn, modelColumn, actionColumn);

    }

    private void openEditDialog(GoodsDto goodDto) {
        goodsRegistrationView = new GoodsRegistrationView(goodsService,acquisitionService,goodsCategoryService,carBrandService);

        goodsRegistrationView.getH3().setText("Update Good " + goodDto.getCostName());
        goodsRegistrationView.setUpdateCustomer(goodDto);
        goodsRegistrationView.setUpdateFlag(true);

        goodsRegistrationView.getCancelButton().addClickListener(event -> dialog.close());
        goodsRegistrationView.getSaveButton().addClickListener(event -> {
            goodsService.saveGood(goodsRegistrationView.getGoodToUpdate());
            Notification.show("Good updated successfully!");
            goodsDtoGrid.getDataProvider().refreshAll();
            dialog.close();
        });

        dialog = new Dialog(goodsRegistrationView);
        dialog.setWidth("auto");
        dialog.setHeight("auto");
        dialog.setDraggable(true);
        dialog.open();
    }

    private void openAcquisitionDialog(GoodsDto goodDto) {
        goodsRegistrationView = new GoodsRegistrationView(goodsService,acquisitionService,goodsCategoryService,carBrandService);

        goodsRegistrationView.getH3().setText("Purchase good : " + goodDto.getCostName());
        goodsRegistrationView.getCostTextField().setReadOnly(true);
        goodsRegistrationView.getNameTextField().setReadOnly(true);
        goodsRegistrationView.getBrandComboBox().setReadOnly(true);
        goodsRegistrationView.getCategoryComboBox().setReadOnly(true);
        goodsRegistrationView.getCarModelMultiSelect().setReadOnly(true);

        goodsRegistrationView.setUpdateCustomer(goodDto);
        int actualStock = goodDto.getStock();

        goodsRegistrationView.getCancelButton().addClickListener(event -> dialog.close());

        goodsRegistrationView.getSaveButton().addClickListener(event -> {
            double setStock =  goodsRegistrationView.getStockField().getValue();
            acquisitionService.saveAcquisition(GoodsMapper.toAcquisitionDto(goodDto));

            goodsRegistrationView.getStockField().setValue(actualStock + setStock);
            goodsService.saveGood(goodsRegistrationView.getGoodToUpdate());
            Notification.show("Acquisition successfully saved!");
            goodsDtoGrid.getDataProvider().refreshAll();
            dialog.close();
        });

        dialog = new Dialog(goodsRegistrationView);
        dialog.setWidth("auto");
        dialog.setHeight("auto");
        dialog.setDraggable(true);
        dialog.open();
    }
}
