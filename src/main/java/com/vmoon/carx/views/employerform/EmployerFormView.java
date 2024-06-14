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
import com.vaadin.flow.component.icon.VaadinIcon;
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
import com.vmoon.carx.utils.Notifications;
import com.vmoon.carx.utils.SecurityUtils;
import com.vmoon.carx.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PageTitle("Employer Form")
@Route(value = "employer-form", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed({"ADMIN", "MANAGER"})
public class EmployerFormView extends Composite<VerticalLayout> {
    private static final Logger logger = LoggerFactory.getLogger(EmployerFormView.class);
    private final RoleService roleService;
    private final EmployerService employerService;
    TextField fullName;
    DatePicker dateOfBirthPicker;
    TextField phoneNumber;
    Select<RoleDto> roleSelect;
    EmailField emailField;
    TextArea address;
    @Getter
    Button saveButton;
    @Getter
    Button cancelButton;
    @Getter
    H3 h3;
    BeanValidationBinder<EmployerDto> validationBinder;
    @Getter
    EmployerDto employerDto;
    @Setter
    boolean updateFlag;


    public EmployerFormView(RoleService roleService, EmployerService employerService) {
        this.roleService = roleService;
        this.employerService = employerService;

        vaadinUI();
        fieldsValidation();
    }

    private void fieldsValidation() {
        validationBinder = new BeanValidationBinder<>(EmployerDto.class);

        validationBinder.forField(fullName)
                .withValidator(new BeanValidator(EmployerDto.class, "fullName"))
                .bind(EmployerDto::getFullName, EmployerDto::setFullName);

        validationBinder.forField(emailField)
                .withValidator(new BeanValidator(EmployerDto.class, "email"))
                .bind(EmployerDto::getEmail, EmployerDto::setEmail);

        validationBinder.forField(address)
                .withValidator(new BeanValidator(EmployerDto.class, "address"))
                .bind(EmployerDto::getAddress, EmployerDto::setAddress);

        validationBinder.forField(roleSelect)
                .withValidator(new BeanValidator(EmployerDto.class, "role"))
                .bind(EmployerDto::getRole, EmployerDto::setRole);

        validationBinder.forField(dateOfBirthPicker)
                .withValidator(new BeanValidator(EmployerDto.class, "dateOfBirth"))
                .bind(EmployerDto::getDateOfBirth, EmployerDto::setDateOfBirth);

        validationBinder.forField(phoneNumber)
                .withValidator(new RegexpValidator("Enter an valid phone number", "^(\\+\\d{1,2}\\s?)?1?\\-?\\.?\\s?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$"))
                .bind(EmployerDto::getPhone, EmployerDto::setPhone);

    }

    public void vaadinUI() {
        VerticalLayout layoutColumn2 = new VerticalLayout();

        h3 = new H3();
        fullName = new TextField();
        fullName.setLabel("Full Name");
        fullName.setWidth("100%");
        fullName.setPrefixComponent(new Icon(VaadinIcon.CLIPBOARD_USER));

        dateOfBirthPicker = new DatePicker();
        dateOfBirthPicker.setLabel("Date of Birth");
        dateOfBirthPicker.setPrefixComponent(new Icon(VaadinIcon.CALENDAR));

        phoneNumber = new TextField();
        phoneNumber.setLabel("Phone Number");
        phoneNumber.setPrefixComponent(new Icon(VaadinIcon.PHONE));

        roleSelect = new Select<>();
        roleSelect.setLabel("Role");
        roleSelect.setWidth("min-content");
        setSelectSampleData(roleSelect);
        roleSelect.setPrefixComponent(new Icon(VaadinIcon.USER_CHECK));

        emailField = new EmailField();
        emailField.setLabel("Email");
        emailField.setPrefixComponent(new Icon(VaadinIcon.MAILBOX));

        address = new TextArea();
        address.setPrefixComponent(new Icon(VaadinIcon.USER_CARD));

        FormLayout formLayout2Col = new FormLayout();
        Hr hr = new Hr();
        HorizontalLayout layoutRow = new HorizontalLayout();

        saveButton = new Button();
        saveButton.addClickListener(e -> saveEmployer());
        saveButton.setPrefixComponent(new Icon(VaadinIcon.CHECK));

        cancelButton = new Button();
        cancelButton.setPrefixComponent(new Icon(VaadinIcon.CLOSE_SMALL));

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
        cancelButton.addClickListener(e -> UI.getCurrent().navigate("employers-view"));

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
        if (SecurityUtils.isUserAdmin()) {
            layoutRow.add(saveButton);
        }
        layoutRow.add(cancelButton);
    }

    public void saveEmployer() {
        if (validationBinder.validate().isOk()) {
            try {
                if (!updateFlag) {
                    employerService.saveEmployer(getEmployerToSave());
                    UI.getCurrent().navigate("employers-view");
                } else {
                    employerService.saveEmployer(getEmployerToUpdate());
                }

            } catch (Exception e) {
                logger.error("Error saving employer {}", e.getMessage());
                Notifications.errorNotification("Error saving employer: " + e.getMessage()).open();
            }
        } else {
            Notifications.warningNotification("Invalid data!").open();
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


    public void setUpdateEmployer(EmployerDto employerDto) {
        fullName.setValue(employerDto.getFullName());
        address.setValue(employerDto.getAddress());
        emailField.setValue(employerDto.getEmail());
        phoneNumber.setValue(employerDto.getPhone());
        dateOfBirthPicker.setValue(employerDto.getDateOfBirth());
        roleSelect.setValue(employerDto.getRole());
        this.employerDto = employerDto;
    }

    public EmployerDto getEmployerToUpdate() {
        return EmployerDto.builder()
                .id(employerDto.getId())
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
