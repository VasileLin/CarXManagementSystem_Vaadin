package com.vmoon.carx.views.goods;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vmoon.carx.dto.GoodsDto;
import com.vmoon.carx.services.GoodsService;
import com.vmoon.carx.views.MainLayout;
import lombok.Getter;

@PageTitle("Goods Registration")
@Route(value = "good-form", layout = MainLayout.class)
@Uses(Icon.class)
public class GoodsRegistrationView extends Composite<VerticalLayout> {

    private final GoodsService goodsService;

    @Getter
    private final Button saveButton;

    @Getter
    private final Button cancelButton;

    private final TextField nameTextField;
    private final NumberField costTextField;
    private final DatePicker purchaseDate;
    private final NumberField stockField;
    private GoodsDto goodDto;

    @Getter
    private boolean updateFlag;

    @Getter
    H3 h3;
    public GoodsRegistrationView(GoodsService goodsService) {
        this.goodsService = goodsService;
        VerticalLayout layoutColumn2 = new VerticalLayout();

        h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        nameTextField = new TextField();
        costTextField = new NumberField();
        purchaseDate = new DatePicker();
        stockField = new NumberField();
        HorizontalLayout layoutRow = new HorizontalLayout();

        saveButton = new Button();
        saveButton.setText("Save");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setWidth("min-content");

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
        layoutColumn2.add(layoutRow);
        layoutRow.add(saveButton);
        layoutRow.add(cancelButton);
    }

    public void saveGood() {
      GoodsDto saveGood =  GoodsDto.builder()
                .costName(nameTextField.getValue())
                .cost(costTextField.getValue())
                .date(purchaseDate.getValue())
                .stock(stockField.getValue().intValue())
                .build();

        try {
            goodsService.saveGood(saveGood);
        } catch (Exception e) {
            Notification.show("Error saving good :"+ e);
        }
    }

    public GoodsDto getGoodToUpdate() {
        return GoodsDto.builder()
                .id(this.goodDto.getId())
                .costName(nameTextField.getValue())
                .cost(costTextField.getValue())
                .date(purchaseDate.getValue())
                .stock(stockField.getValue().intValue())
                .build();
    }

    public void setUpdateCustomer(GoodsDto goodDto) {
        nameTextField.setValue(goodDto.getCostName());
        costTextField.setValue(goodDto.getCost());
        purchaseDate.setValue(goodDto.getDate());
        stockField.setValue((double)goodDto.getStock());
        this.goodDto = goodDto;
    }
}