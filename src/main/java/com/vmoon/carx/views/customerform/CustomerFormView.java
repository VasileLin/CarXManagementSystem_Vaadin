package com.vmoon.carx.views.customerform;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.validator.BeanValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vmoon.carx.dto.CarBrandDto;
import com.vmoon.carx.dto.CarModelDto;
import com.vmoon.carx.dto.CustomerDto;
import com.vmoon.carx.services.CarBrandService;
import com.vmoon.carx.services.CarModelService;
import com.vmoon.carx.services.CustomerService;
import com.vmoon.carx.views.MainLayout;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@PageTitle("Customer Form")
@Route(value = "customer-form", layout = MainLayout.class)
@Uses(Icon.class)
@Component
@Scope("prototype")
public class CustomerFormView extends Composite<VerticalLayout> {
    TextField nameField;
    TextField phoneTextField;
    EmailField emailField;
    ComboBox<CarModelDto> carModelComboBox;
    TextField carNumberTextField;
    BeanValidationBinder<CustomerDto> validationBinder;
    ComboBox<CarBrandDto> brandComboBox;
    private final CarModelService carModelService;

    @Getter
    H3 h3;

    @Getter @Setter
    CustomerDto customerDto;

    @Getter
    Button saveButton;

    @Getter
    Button cancelButton;

    @Setter
    boolean updateFlag;

    private final CustomerService customerService;
    private final CarBrandService carBrandService;

    public CustomerFormView(CarModelService carModelService, CustomerService customerService, CarBrandService carBrandService) {
        this.carModelService = carModelService;
        this.customerService = customerService;
        this.carBrandService = carBrandService;


        vaadinUI();
        fieldsValidation();
    }

    private void fieldsValidation() {
        validationBinder = new BeanValidationBinder<>(CustomerDto.class);

        validationBinder.forField(nameField)
                .withValidator(new BeanValidator(CustomerDto.class,"name"))
                .bind(CustomerDto::getName,CustomerDto::setName);

        validationBinder.forField(phoneTextField)
                .withValidator( new RegexpValidator("Enter an valid phone number","^(\\+\\d{1,2}\\s?)?1?\\-?\\.?\\s?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$"))
                .bind(CustomerDto::getPhone,CustomerDto::setPhone);

        validationBinder.forField(carNumberTextField)
                .withValidator(new BeanValidator(CustomerDto.class,"carNumber"))
                .bind(CustomerDto::getCarNumber,CustomerDto::setCarNumber);

        validationBinder.forField(emailField)
                .withValidator(new BeanValidator(CustomerDto.class,"email"))
                .bind(CustomerDto::getEmail,CustomerDto::setEmail);

        validationBinder.forField(carModelComboBox)
                .withValidator(new BeanValidator(CustomerDto.class,"carModel"))
                .bind(CustomerDto::getCarModel,CustomerDto::setCarModel);
    }


    private void vaadinUI() {
        VerticalLayout layoutColumn2 = new VerticalLayout();

        h3 = new H3();

        nameField = new TextField();
        nameField.setLabel("Full Name");
        nameField.setWidth("100%");
        nameField.setPrefixComponent(new Icon(VaadinIcon.CLIPBOARD_USER));

        FormLayout formLayout2Col = new FormLayout();


        phoneTextField = new TextField();
        phoneTextField.setLabel("Phone Number");
        phoneTextField.setPrefixComponent(new Icon(VaadinIcon.PHONE));


        emailField = new EmailField();
        emailField.setLabel("Email");
        emailField.setPrefixComponent(new Icon(VaadinIcon.MAILBOX));

        brandComboBox = new ComboBox<>();
        brandComboBox.setLabel("Select Car Brand");
        brandComboBox.setWidthFull();
        brandComboBox.setPrefixComponent(new Icon(VaadinIcon.CAR));
        brandComboBox.addValueChangeListener(event -> setCarModelComboBoxData());
        setCarBrandComboBoxData(brandComboBox);

        carModelComboBox = new ComboBox<>();
        carModelComboBox.setLabel("Car Model");
        carModelComboBox.setPrefixComponent(new Icon(VaadinIcon.CAR));

        carNumberTextField = new TextField();
        carNumberTextField.setLabel("Car No.");
        carNumberTextField.setWidth("100%");
        carNumberTextField.setPrefixComponent(new Icon(VaadinIcon.INPUT));

        Hr hr = new Hr();
        HorizontalLayout layoutRow = new HorizontalLayout();

        saveButton = new Button();
        saveButton.setText("Save");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> saveEmployer());
        saveButton.setPrefixComponent(new Icon(VaadinIcon.CHECK));

        cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setWidth("min-content");
        cancelButton.setPrefixComponent(new Icon(VaadinIcon.CLOSE_SMALL));
        cancelButton.addClickListener(e -> UI.getCurrent().navigate("customers-view"));


        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Customer Registration");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(nameField);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(phoneTextField);
        formLayout2Col.add(emailField);
        formLayout2Col.add(brandComboBox);
        formLayout2Col.add(carModelComboBox);
        layoutColumn2.add(carNumberTextField);
        layoutColumn2.add(hr);
        layoutColumn2.add(layoutRow);
        layoutRow.add(saveButton);
        layoutRow.add(cancelButton);
    }

    private void saveEmployer() {
        if (validationBinder.validate().isOk()) {
            try {
                if (!updateFlag) {
                    customerService.saveCustomer(getCustomerToSave());
                    Notification.show("Customer successfully saved");
                    UI.getCurrent().navigate("customers-view");
                } else {
                    customerService.saveCustomer(getCustomerToUpdate());
                    Notification.show("Customer successfully updated");
                }
            } catch (Exception e) {
                Notification.show("Error saving customer: " + e.getMessage());
            }
        } else {
            Notification.show("Invalid data!");
        }
    }

    private void setCarModelComboBoxData() {
        if (brandComboBox.getValue() != null) {
            CarBrandDto comboBoxValue = brandComboBox.getValue();
            List<CarModelDto> carModelsByBrandId = carModelService.getCarModelsByBrandId(comboBoxValue.getId());
            carModelComboBox.setItems(carModelsByBrandId);
            carModelComboBox.setItemLabelGenerator(CarModelDto::getModel);
        }

    }

    private void setCarBrandComboBoxData(ComboBox<CarBrandDto> brandComboBox) {

        List<CarBrandDto> allBrands = carBrandService.allBrands();
        brandComboBox.setItems(allBrands);
        brandComboBox.setItemLabelGenerator(CarBrandDto::getBrand);
    }

    public CustomerDto getCustomerToUpdate() {
        return CustomerDto.builder()
                .id(customerDto.getId())
                .name(nameField.getValue())
                .phone(phoneTextField.getValue())
                .email(emailField.getValue())
                .carNumber(carNumberTextField.getValue())
                .carModel(carModelComboBox.getValue())
                .carBrand(brandComboBox.getValue())
                .build();
    }

    private CustomerDto getCustomerToSave() {
        return CustomerDto.builder()
                .name(nameField.getValue())
                .phone(phoneTextField.getValue())
                .email(emailField.getValue())
                .carNumber(carNumberTextField.getValue())
                .carModel(carModelComboBox.getValue())
                .carBrand(brandComboBox.getValue())
                .build();
    }

    public void setUpdateCustomer(CustomerDto customerDto) {
        nameField.setValue(customerDto.getName());
        emailField.setValue(customerDto.getEmail());
        phoneTextField.setValue(customerDto.getPhone());
        carNumberTextField.setValue(customerDto.getCarNumber());
        brandComboBox.setItems(carBrandService.allBrands());
        brandComboBox.setValue(customerDto.getCarBrand());
        carModelComboBox.setItems(customerDto.getCarModel());
        carModelComboBox.setValue(customerDto.getCarModel());

        this.customerDto = customerDto;
    }
}
