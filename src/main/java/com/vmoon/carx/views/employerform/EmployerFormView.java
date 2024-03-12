package com.vmoon.carx.views.employerform;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.validator.BeanValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vmoon.carx.dto.EmployerDto;
import com.vmoon.carx.dto.RoleDto;
import com.vmoon.carx.services.EmployerService;
import com.vmoon.carx.services.RoleService;
import com.vmoon.carx.views.MainLayout;

@PageTitle("Employer Form")
@Route(value = "employer-form", layout = MainLayout.class)
@Uses(Icon.class)
public class EmployerFormView extends Composite<VerticalLayout> {
    TextField fullName;
    DatePicker dateOfBirthPicker;
    TextField phoneNumber;
    Select<RoleDto> roleSelect;
    EmailField emailField;
    TextArea address;
    Button saveButton;
    Button cancelButton;
    BeanValidationBinder<EmployerDto> validationBinder;

    private final RoleService roleService;
    private final EmployerService employerService;



    public EmployerFormView(RoleService roleService, EmployerService employerService) {
        this.roleService = roleService;
        this.employerService = employerService;

        vaadinUI();
        fieldsValidation();


    }

    private void fieldsValidation() {
        validationBinder = new BeanValidationBinder<>(EmployerDto.class);

        validationBinder.forField(fullName)
                .withValidator(new BeanValidator(EmployerDto.class,"fullName"))
                .bind(EmployerDto::getFullName,EmployerDto::setFullName);

        validationBinder.forField(emailField)
                .withValidator(new BeanValidator(EmployerDto.class,"email"))
                .bind(EmployerDto::getEmail,EmployerDto::setEmail);

        validationBinder.forField(phoneNumber)
                .withValidator( new RegexpValidator("Enter an valid phone number","^(\\+\\d{1,2}\\s?)?1?\\-?\\.?\\s?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$"))
                .bind(EmployerDto::getPhone,EmployerDto::setPhone);

    }

    public void vaadinUI() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        fullName = new TextField();
        fullName.setLabel("Full Name");
        fullName.setWidth("100%");

        dateOfBirthPicker = new DatePicker();
        dateOfBirthPicker.setLabel("Date of Birth");

        phoneNumber = new TextField();
        phoneNumber.setLabel("Phone Number");

        roleSelect = new Select<>();
        roleSelect.setLabel("Role");
        roleSelect.setWidth("min-content");
        setSelectSampleData(roleSelect);

        emailField = new EmailField();
        emailField.setLabel("Email");

        address = new TextArea();


        FormLayout formLayout2Col = new FormLayout();
        Hr hr = new Hr();
        HorizontalLayout layoutRow = new HorizontalLayout();

        saveButton = new Button();
        saveButton.addClickListener(e -> saveEmployer());
        cancelButton = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.addClassName(Gap.XSMALL);
        layoutColumn2.addClassName(Padding.SMALL);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Employer Registration");
        h3.setWidth("100%");

        formLayout2Col.setWidth("100%");

        address.setLabel("Address");
        address.setWidth("100%");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        saveButton.setText("Save");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.setText("Cancel");
        cancelButton.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(fullName);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(dateOfBirthPicker);
        formLayout2Col.add(phoneNumber);
        formLayout2Col.add(emailField);
        formLayout2Col.add(roleSelect);
        layoutColumn2.add(address);
        layoutColumn2.add(hr);
        layoutColumn2.add(layoutRow);
        layoutRow.add(saveButton);
        layoutRow.add(cancelButton);
    }

    public void saveEmployer() {
        if (validationBinder.validate().isOk()) {
            try {
                    employerService.saveEmployer(getEmployerToSave());
                    Notification.show("Employer successfully saved");
                    UI.getCurrent().navigate("employers-view");

            } catch (Exception e) {
                Notification.show("Error saving employer: " + e.getMessage());
            }
        } else {
            Notification.show("Invalid data!");
        }

    }

    public EmployerDto getEmployerToSave() {
        return EmployerDto.builder()
                .fullName(fullName.getValue())
                .email(emailField.getValue())
                .phone(phoneNumber.getValue())
                .address(address.getValue())
                .dateOfBirth(dateOfBirthPicker.getValue())
                .role(roleSelect.getValue())
                .build();
    }


    private void setSelectSampleData(Select<RoleDto> select) {
        select.setItems(roleService.findAllRoles());
        select.setItemLabelGenerator(RoleDto::getName);
    }


}
