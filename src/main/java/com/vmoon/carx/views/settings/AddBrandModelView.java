package com.vmoon.carx.views.settings;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
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
import com.vmoon.carx.dto.CarModelDto;
import com.vmoon.carx.services.CarModelService;
import com.vmoon.carx.utils.DialogManager;
import com.vmoon.carx.utils.Notifications;
import jakarta.annotation.security.RolesAllowed;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@PageTitle("Add Brand Model View")
@Route(value = "brand-model-form")
@Uses(Icon.class)
@RolesAllowed({"ADMIN", "MANAGER"})
@Component
@Scope("prototype")
public class AddBrandModelView extends Composite<VerticalLayout> {

    private final CarModelService carModelService;
    @Getter
    Button saveButton;
    @Setter
    CarBrandDto carBrandDto;
    CarModelDto updateModel;
    TextField modelTextField;
    NumberField yearNumberField;
    BeanValidationBinder<CarModelDto> validationBinder;
    @Setter
    private boolean updateFlag;

    public AddBrandModelView(CarModelService carModelService) {
        this.carModelService = carModelService;

        createUI();
        validateFields();

    }

    private void validateFields() {
        validationBinder = new BeanValidationBinder<>(CarModelDto.class);

        validationBinder.forField(modelTextField)
                .withValidator(new BeanValidator(CarModelDto.class, "model"))
                .asRequired("Car model is required!")
                .bind(CarModelDto::getModel, CarModelDto::setModel);

        validationBinder.forField(yearNumberField)
                .withValidator(new BeanValidator(CarModelDto.class, "year"))
                .asRequired("Production year is required")
                .bind(
                        carModelDto -> (double) carModelDto.getYear(),
                        (carModelDto, fieldValue) -> carModelDto.setYear(fieldValue.intValue())
                );
    }

    private void createUI() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();

        modelTextField = new TextField();
        modelTextField.setLabel("Model");
        modelTextField.setWidth("min-content");

        yearNumberField = new NumberField();
        yearNumberField.setLabel("Year");
        yearNumberField.setWidth("468px");

        HorizontalLayout layoutRow = new HorizontalLayout();

        saveButton = new Button();
        saveButton.setText("Save");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setWidth("min-content");
        cancelButton.addClickListener(event -> DialogManager.closeAll());

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);
        layoutColumn2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("500px");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Add brand model");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        layoutRow.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(LumoUtility.Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(modelTextField);
        layoutColumn2.add(yearNumberField);
        layoutColumn2.add(layoutRow);
        layoutRow.add(saveButton);
        layoutRow.add(cancelButton);
    }


    public void saveModel() {
        if (validationBinder.validate().isOk()) {

            CarModelDto carModelDto = CarModelDto.builder()
                    .model(modelTextField.getValue())
                    .year(yearNumberField.getValue().intValue())
                    .build();

            if (carBrandDto != null) {
                carModelDto.setCarBrand(carBrandDto);
            }

            if (updateFlag) {
                saveUpdatedModel();
                Notifications.successNotification("Model of " + updateModel.getCarBrand().getBrand() + " updated successfully!").open();
            } else {
                carModelService.saveModel(carModelDto);
                Notifications.successNotification("Model of " + carBrandDto.getBrand() + " saved successfully!").open();
            }

        } else {
            Notifications.errorNotification("Error saving brand model, complete all inputs!").open();
        }
    }

    public void setUpdateModel(CarModelDto carModelDto) {
        modelTextField.setValue(carModelDto.getModel());
        yearNumberField.setValue((double) carModelDto.getYear());
        this.updateModel = carModelDto;
    }

    private void saveUpdatedModel() {
        updateModel.setModel(modelTextField.getValue());
        updateModel.setYear(yearNumberField.getValue().intValue());

        if (validationBinder.validate().isOk()) {
            carModelService.saveModel(updateModel);
            modelTextField.setValue("");
            yearNumberField.setValue(0D);
        }
    }
}
