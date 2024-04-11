package com.vmoon.carx.views.goods;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
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
import com.vmoon.carx.dto.CarBrandDto;
import com.vmoon.carx.dto.GoodsCategoryDto;
import com.vmoon.carx.dto.GoodsDto;
import com.vmoon.carx.mappers.GoodsMapper;
import com.vmoon.carx.services.AcquisitionService;
import com.vmoon.carx.services.CarBrandService;
import com.vmoon.carx.services.GoodsCategoryService;
import com.vmoon.carx.services.GoodsService;
import com.vmoon.carx.views.MainLayout;
import lombok.Getter;
import lombok.Setter;

@PageTitle("Goods Registration")
@Route(value = "good-form", layout = MainLayout.class)
@Uses(Icon.class)
public class GoodsRegistrationView extends Composite<VerticalLayout> {

    private final GoodsService goodsService;
    private final AcquisitionService acquisitionService;
    private final GoodsCategoryService categoryService;
    private final CarBrandService carBrandService;

    @Getter
    private final Button saveButton;

    @Getter
    private final Button cancelButton;
    BeanValidationBinder<GoodsDto> validationBinder;

    @Getter
    private final TextField nameTextField;
    private final TextField carModelTextField;

    @Getter
    private final NumberField costTextField;
    private final DatePicker purchaseDate;

    @Getter
    private final NumberField stockField;
    private GoodsDto goodDto;
    ComboBox<CarBrandDto> brandComboBox;
    ComboBox<GoodsCategoryDto> categoryComboBox;

    @Setter
    private boolean updateFlag;

    @Getter
    H3 h3;
    public GoodsRegistrationView(GoodsService goodsService, AcquisitionService acquisitionService, GoodsCategoryService categoryService, CarBrandService carBrandService) {
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
        stockField = new NumberField();
        carModelTextField = new TextField();
        HorizontalLayout layoutRow = new HorizontalLayout();

        saveButton = new Button();
        saveButton.setText("Save");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setWidth("min-content");


        brandComboBox = new ComboBox<>();
        brandComboBox.setLabel("Select Car Brand");
        brandComboBox.setWidth("min-content");
        brandComboBox.setPrefixComponent(new Icon(VaadinIcon.CAR));
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
        carModelTextField.setLabel("Type compatible car models");
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
        formLayout2Col.add(carModelTextField);
        formLayout2Col.add(brandComboBox);
        formLayout2Col.add(categoryComboBox);
        layoutColumn2.add(layoutRow);
        layoutRow.add(saveButton);
        layoutRow.add(cancelButton);

        fieldsValidation();
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
                .withValidator(new BeanValidator(GoodsDto.class,"costName"))
                .bind(GoodsDto::getCostName,GoodsDto::setCostName);

        validationBinder.forField(costTextField)
                .withValidator(new BeanValidator(GoodsDto.class,"cost"))
                .asRequired("Enter a good cost")
                .bind(GoodsDto::getCost,GoodsDto::setCost);

        validationBinder.forField(purchaseDate)
                .withValidator(new BeanValidator(GoodsDto.class,"date"))
                .bind(GoodsDto::getDate,GoodsDto::setDate);

        validationBinder.forField(stockField)
                .withValidator(new BeanValidator(GoodsDto.class,"stock"))
                .bind(
                        goodsDto -> (double) goodsDto.getStock(),
                        (goodsDto, fieldValue) -> goodsDto.setStock(fieldValue.intValue())
                );

    }


    public void saveGood() {
        if (validationBinder.validate().isOk()) {
            GoodsDto saveGood =  GoodsDto.builder()
                    .costName(nameTextField.getValue())
                    .cost(costTextField.getValue())
                    .date(purchaseDate.getValue())
                    .stock(stockField.getValue().intValue())
                    .carModel(carModelTextField.getValue())
                    .carBrand(brandComboBox.getValue())
                    .category(categoryComboBox.getValue())
                    .build();
            try {
                acquisitionService.saveAcquisition(GoodsMapper.toAcquisitionDto(saveGood));
                goodsService.saveGood(saveGood);
                Notification.show("Good successfully " + (updateFlag ? "updated" : "added"));
            } catch (Exception e) {
                Notification.show("Error saving good :"+ e);
            }
        } else {
            Notification.show("Invalid data, verify data or fill all fields!");
        }
    }

    public GoodsDto getGoodToUpdate() {
        return GoodsDto.builder()
                .id(this.goodDto.getId())
                .costName(nameTextField.getValue())
                .cost(costTextField.getValue())
                .date(purchaseDate.getValue())
                .stock(stockField.getValue().intValue())
                .carModel(carModelTextField.getValue())
                .carBrand(brandComboBox.getValue())
                .category(categoryComboBox.getValue())
                .build();
    }

    public void setUpdateCustomer(GoodsDto goodDto) {
        nameTextField.setValue(goodDto.getCostName());
        costTextField.setValue(goodDto.getCost());
        purchaseDate.setValue(goodDto.getDate());
        stockField.setValue((double)goodDto.getStock());
        carModelTextField.setValue(goodDto.getCarModel());
        brandComboBox.setValue(goodDto.getCarBrand());
        categoryComboBox.setValue(goodDto.getCategory());
        this.goodDto = goodDto;
    }
}