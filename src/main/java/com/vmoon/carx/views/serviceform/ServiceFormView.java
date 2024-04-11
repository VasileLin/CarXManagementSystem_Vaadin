package com.vmoon.carx.views.serviceform;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.validator.BeanValidator;
import com.vaadin.flow.data.validator.DoubleRangeValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vmoon.carx.dto.ServiceDto;
import com.vmoon.carx.services.ServicesService;
import com.vmoon.carx.views.MainLayout;
import lombok.Getter;
import lombok.Setter;

@PageTitle("Service Form")
@Route(value = "service-form", layout = MainLayout.class)
@Uses(Icon.class)
public class ServiceFormView extends Composite<VerticalLayout> {

    NumberField priceField;
    TextField serviceNameTextField;

    @Getter
    Button saveButton;

    @Getter
    Button cancelButton;

    @Getter
    H3 h3;

    @Getter
    ServiceDto serviceDto;

    @Setter
    boolean updateFlag;
    BeanValidationBinder<ServiceDto> validationBinder;
    private final ServicesService servicesService;

    public ServiceFormView(ServicesService servicesService) {
        this.servicesService = servicesService;

        vaadinUI();
        fieldsValidation();
    }

    private void fieldsValidation() {
        validationBinder = new BeanValidationBinder<>(ServiceDto.class);

        validationBinder.forField(serviceNameTextField)
                .withValidator(new BeanValidator(ServiceDto.class,"name"))
                .asRequired("Service name is required")
                .bind(ServiceDto::getName,ServiceDto::setName);


        validationBinder.forField(priceField)
                .withValidator(new DoubleRangeValidator("Price is wrong",0.00,5000000.00))
                .asRequired("Price is required")
                .bind(ServiceDto::getPrice,ServiceDto::setPrice);

    }

    public void vaadinUI(){
        VerticalLayout layoutColumn2 = new VerticalLayout();

        h3 = new H3();

        serviceNameTextField = new TextField();
        serviceNameTextField.setLabel("Service Name");
        serviceNameTextField.setWidth("100%");

        priceField = new NumberField();
        priceField.setLabel("Price");
        priceField.setWidth("100%");

        Hr hr = new Hr();
        HorizontalLayout layoutRow = new HorizontalLayout();

        saveButton = new Button();
        saveButton.setText("Save");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> saveService());


        cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setWidth("min-content");
        cancelButton.addClickListener(e -> UI.getCurrent().navigate("service-view"));

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.addClassName(Gap.SMALL);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Service Registration");
        h3.setWidth("100%");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(serviceNameTextField);
        layoutColumn2.add(priceField);
        layoutColumn2.add(hr);
        layoutColumn2.add(layoutRow);
        layoutRow.add(saveButton);
        layoutRow.add(cancelButton);
    }

    private void saveService() {

        if (validationBinder.validate().isOk()) {
            try {
                if (!updateFlag) {
                    servicesService.saveService(getServiceToSave());
                    Notification.show("Service successfully saved");
                    UI.getCurrent().navigate("service-view");
                } else {
                    servicesService.saveService(getServiceToUpdate());
                    Notification.show("Service successfully updated");
                }
            } catch (Exception e) {
                Notification.show("Error saving service: " + e.getMessage());
            }
        } else {
            Notification.show("Invalid data!");
        }
    }

    public ServiceDto getServiceToUpdate() {
        return ServiceDto.builder()
                .id(serviceDto.getId())
                .name(serviceNameTextField.getValue())
                .price(priceField.getValue())
                .build();
    }

    private ServiceDto getServiceToSave() {
        return ServiceDto.builder()
                .name(serviceNameTextField.getValue())
                .price(priceField.getValue())
                .build();
    }

    public void setUpdateService(ServiceDto serviceDto) {
        serviceNameTextField.setValue(serviceDto.getName());
        priceField.setValue(serviceDto.getPrice());
        this.serviceDto = serviceDto;
    }
}
