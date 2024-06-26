package com.vmoon.carx.views.settings;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.validator.BeanValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vmoon.carx.dto.CarBrandDto;
import com.vmoon.carx.services.CarBrandService;
import com.vmoon.carx.utils.DialogManager;
import com.vmoon.carx.utils.Notifications;
import jakarta.annotation.security.RolesAllowed;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@PageTitle("Add Brand View")
@Route(value = "brand-form")
@Uses(Icon.class)
@RolesAllowed({"ADMIN", "MANAGER"})
@Component
@Scope("prototype")
public class AddBrandView extends Composite<VerticalLayout> {

    private final CarBrandService brandService;
    TextField brandNameTextField;
    BeanValidationBinder<CarBrandDto> carBrandBeanValidationBinder;
    @Setter
    private CarBrandDto updateBrand;
    @Getter
    private Button saveButton;
    @Setter
    private boolean updateFlag;

    public AddBrandView(CarBrandService brandService) {
        this.brandService = brandService;

        createUI();
        fieldsValidation();
    }

    private void createUI() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        h3.setText("Add Brand");
        h3.setWidth("100%");

        brandNameTextField = new TextField();
        brandNameTextField.setLabel("Brand Name");
        brandNameTextField.setWidth("100%");

        HorizontalLayout layoutRow = new HorizontalLayout();

        saveButton = new Button();
        saveButton.setText("Save");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.setPrefixComponent(new Icon(VaadinIcon.CHECK_CIRCLE));

        Button cancelButton = new Button();
        cancelButton.setPrefixComponent(new Icon(VaadinIcon.CLOSE));
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
        layoutRow.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(LumoUtility.Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        cancelButton.setText("Cancel");
        cancelButton.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(brandNameTextField);
        layoutColumn2.add(layoutRow);
        layoutRow.add(saveButton);
        layoutRow.add(cancelButton);
    }

    private void fieldsValidation() {
        carBrandBeanValidationBinder = new BeanValidationBinder<>(CarBrandDto.class);
        carBrandBeanValidationBinder.forField(brandNameTextField)
                .withValidator(new BeanValidator(CarBrandDto.class, "brand"))
                .asRequired("Brand name is required!")
                .bind(CarBrandDto::getBrand, CarBrandDto::setBrand);
    }

    public void saveBrand() {
        if (carBrandBeanValidationBinder.validate().isOk()) {
            if (!updateFlag) {
                CarBrandDto carBrand = CarBrandDto.builder()
                        .brand(brandNameTextField.getValue())
                        .build();
                brandService.saveBrand(carBrand);
                DialogManager.closeAll();
                Notifications.successNotification("Brand saved successfully!").open();
            } else {
                saveUpdatedBrand();
                Notifications.successNotification("Brand updated successfully!").open();
            }

        } else {
            Notifications.warningNotification("Invalid brand name!").open();
        }
    }

    public void setUpdatedBrand(CarBrandDto carBrandDto) {
        brandNameTextField.setValue(carBrandDto.getBrand());
        this.updateBrand = carBrandDto;
    }

    private void saveUpdatedBrand() {
        updateBrand.setBrand(brandNameTextField.getValue());
        brandService.saveBrand(updateBrand);
        DialogManager.closeAll();
    }
}
