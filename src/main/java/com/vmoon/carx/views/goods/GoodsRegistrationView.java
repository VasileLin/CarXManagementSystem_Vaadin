package com.vmoon.carx.views.goods;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.validator.BeanValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vmoon.carx.dto.*;
import com.vmoon.carx.mappers.GoodsMapper;
import com.vmoon.carx.services.*;
import com.vmoon.carx.utils.Notifications;
import com.vmoon.carx.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@PageTitle("Goods Registration")
@Route(value = "good-form", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed({"ADMIN", "MANAGER"})
public class GoodsRegistrationView extends Composite<VerticalLayout> {

    private final GoodsService goodsService;
    private final AcquisitionService acquisitionService;
    private final CategoriesService categoryService;
    private final CarBrandService carBrandService;
    @Getter
    private final Button saveButton;
    @Getter
    private final Button cancelButton;
    @Getter
    private final TextField nameTextField;
    @Getter
    private final MultiSelectComboBox<CarModelDto> carModelMultiSelect;
    @Getter
    private final NumberField costTextField;
    private final DatePicker purchaseDate;
    @Getter
    private final NumberField stockField;
    @Getter
    private final ComboBox<CarBrandDto> brandComboBox;
    @Getter
    private final ComboBox<GoodsCategoryDto> categoryComboBox;
    @Getter
    BeanValidationBinder<GoodsDto> validationBinder;
    @Getter
    H3 h3;
    @Setter
    private GoodsDto goodDto;
    @Setter
    private boolean updateFlag;

    public GoodsRegistrationView(GoodsService goodsService, AcquisitionService acquisitionService, CategoriesService categoryService, CarBrandService carBrandService) {
        this.goodsService = goodsService;
        this.acquisitionService = acquisitionService;
        this.categoryService = categoryService;
        this.carBrandService = carBrandService;
        VerticalLayout layoutColumn2 = new VerticalLayout();

        h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        nameTextField = new TextField();
        costTextField = new NumberField();
        purchaseDate = new DatePicker();
        purchaseDate.setValue(LocalDate.now());
        stockField = new NumberField();
        carModelMultiSelect = new MultiSelectComboBox<>();
        HorizontalLayout layoutRow = new HorizontalLayout();

        saveButton = new Button();
        saveButton.setText("Save");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.setPrefixComponent(new Icon(VaadinIcon.PLUS_CIRCLE));

        cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setWidth("min-content");
        cancelButton.setPrefixComponent(new Icon(VaadinIcon.CLOSE));

        brandComboBox = new ComboBox<>();
        brandComboBox.setLabel("Select Car Brand");
        brandComboBox.setWidth("min-content");
        brandComboBox.setPrefixComponent(new Icon(VaadinIcon.CAR));
        brandComboBox.addValueChangeListener(event -> getBrandModels());
        setCarBrandComboBoxData(brandComboBox);

        categoryComboBox = new ComboBox<>();
        categoryComboBox.setLabel("Select good category");
        categoryComboBox.setWidth("min-content");
        categoryComboBox.setPrefixComponent(new Icon(VaadinIcon.BULLETS));
        setCategoryComboBoxData(categoryComboBox);

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Goods Registration");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        nameTextField.setLabel("Name");
        costTextField.setLabel("Cost");
        carModelMultiSelect.setLabel("Type compatible car models");
        costTextField.setWidth("min-content");
        purchaseDate.setLabel("Purchase date");
        stockField.setLabel("Stock");
        stockField.setWidth("min-content");
        layoutRow.addClassName(LumoUtility.Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(nameTextField);
        formLayout2Col.add(costTextField);
        formLayout2Col.add(purchaseDate);
        formLayout2Col.add(stockField);
        formLayout2Col.add(brandComboBox);
        formLayout2Col.add(carModelMultiSelect);
        formLayout2Col.add(categoryComboBox);
        layoutColumn2.add(layoutRow);
        layoutRow.add(saveButton);
        layoutRow.add(cancelButton);

        fieldsValidation();
    }

    private void getBrandModels() {
        if (brandComboBox.getValue().getCarModels() != null) {
            List<CarModelDto> carModels = brandComboBox.getValue().getCarModels();
            carModelMultiSelect.setItems(carModels);
            carModelMultiSelect.setItemLabelGenerator(carModel -> carModel.getModel() + "-" + carModel.getYear());
        }
    }

    private void setCategoryComboBoxData(ComboBox<GoodsCategoryDto> categoryComboBox) {
        categoryComboBox.setItems(categoryService.getAllCategories());
        categoryComboBox.setItemLabelGenerator(GoodsCategoryDto::getName);
    }

    private void setCarBrandComboBoxData(ComboBox<CarBrandDto> brandComboBox) {
        brandComboBox.setItems(carBrandService.allBrands());
        brandComboBox.setItemLabelGenerator(CarBrandDto::getBrand);
    }

    private void fieldsValidation() {
        validationBinder = new BeanValidationBinder<>(GoodsDto.class);

        validationBinder.forField(nameTextField)
                .withValidator(new BeanValidator(GoodsDto.class, "costName"))
                .bind(GoodsDto::getCostName, GoodsDto::setCostName);

        validationBinder.forField(costTextField)
                .withValidator(new BeanValidator(GoodsDto.class, "cost"))
                .asRequired("Enter a good cost")
                .bind(GoodsDto::getCost, GoodsDto::setCost);

        validationBinder.forField(purchaseDate)
                .withValidator(new BeanValidator(GoodsDto.class, "date"))
                .asRequired("Enter a good purchase date")
                .bind(GoodsDto::getDate, GoodsDto::setDate);

        validationBinder.forField(stockField)
                .withValidator(new BeanValidator(GoodsDto.class, "stock"))
                .asRequired("Stock number is required")
                .bind(
                        goodsDto -> (double) goodsDto.getStock(),
                        (goodsDto, fieldValue) -> goodsDto.setStock(fieldValue.intValue())
                );

        validationBinder.forField(categoryComboBox)
                .withValidator(new BeanValidator(GoodsDto.class, "category"))
                .bind(GoodsDto::getCategory, GoodsDto::setCategory);

        validationBinder.forField(brandComboBox)
                .withValidator(new BeanValidator(GoodsDto.class, "carBrand"))
                .bind(GoodsDto::getCarBrand, GoodsDto::setCarBrand);

        validationBinder.forField(carModelMultiSelect)
                .withValidator(new BeanValidator(GoodsDto.class, "compatibleModels"))
                .asRequired("Select a good compatible models")
                .bind(GoodsDto::getCompatibleModels, GoodsDto::setCompatibleModels);

    }


    public void saveGood() {
        if (validationBinder.validate().isOk()) {
            goodDto = GoodsDto.builder()
                    .costName(nameTextField.getValue())
                    .cost(costTextField.getValue())
                    .date(purchaseDate.getValue())
                    .stock(stockField.getValue().intValue())
                    .carBrand(brandComboBox.getValue())
                    .category(categoryComboBox.getValue())
                    .build();
            try {
                AcquisitionDto acquisitionDto = GoodsMapper.INSTANCE.toAcquisitionDto(goodDto);
                acquisitionDto.setTotalPrice(goodDto.getStock()*goodDto.getCost());
                acquisitionService.saveAcquisition(acquisitionDto);
                GoodsDto savedGood = goodsService.saveGood(goodDto);
                savedGood.setCompatibleModels(carModelMultiSelect.getValue());
                savedGood.setCategory(categoryComboBox.getValue());
                goodsService.saveGood(savedGood);
                Notifications.successNotification("Good successfully " + (updateFlag ? "updated" : "added")).open();
            } catch (Exception e) {
                Notifications.errorNotification("Error saving good :" + e).open();
            }

        } else {
            Notifications.warningNotification("Invalid data, verify data or fill all fields!").open();
        }
    }

    public GoodsDto getGoodToUpdate() {
        return GoodsDto.builder()
                .id(this.goodDto.getId())
                .costName(nameTextField.getValue())
                .cost(costTextField.getValue())
                .date(purchaseDate.getValue())
                .stock(stockField.getValue().intValue())
                .compatibleModels(carModelMultiSelect.getValue())
                .carBrand(brandComboBox.getValue())
                .category(categoryComboBox.getValue())
                .build();
    }

    public void setUpdateCustomer(GoodsDto goodDto) {
        nameTextField.setValue(goodDto.getCostName());
        costTextField.setValue(goodDto.getCost());
        purchaseDate.setValue(goodDto.getDate());
        stockField.setValue((double) goodDto.getStock());
        brandComboBox.setItems(carBrandService.allBrands());
        brandComboBox.setValue(goodDto.getCarBrand());
        carModelMultiSelect.setItems(goodDto.getCompatibleModels());
        carModelMultiSelect.setValue(goodDto.getCompatibleModels());
        categoryComboBox.setItems(categoryService.getAllCategories());
        categoryComboBox.setValue(goodDto.getCategory());

        this.goodDto = goodDto;
    }
}