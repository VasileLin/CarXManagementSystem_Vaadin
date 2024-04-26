package com.vmoon.carx.views.users;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.validator.BeanValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vmoon.carx.dto.RoleDto;
import com.vmoon.carx.dto.UserDto;
import com.vmoon.carx.services.RoleService;
import com.vmoon.carx.services.UserService;
import com.vmoon.carx.utils.Notifications;
import com.vmoon.carx.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@PageTitle("User Form")
@Route(value = "user-form", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("ADMIN")
@Component
@Scope("prototype")
public class UserFormView extends Composite<VerticalLayout> {

    private final RoleService roleService;
    private final UserService userService;
    BeanValidationBinder<UserDto> validationBinder;
    TextField usernameTextField;
    @Getter
    PasswordField passwordTextField;
    MultiSelectComboBox<RoleDto> userRoleComboBox;
    private UserDto updateUserDto;

    @Getter
    H3 h3;

    @Setter
    private boolean updateFlag;
    @Getter
    Button saveButton;
    @Getter
    Button cancelButton;

    public UserFormView(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;

        createUI();
        fieldsValidation();
    }

    private void createUI() {
        VerticalLayout layoutColumn2 = new VerticalLayout();

        h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        usernameTextField = new TextField();
        usernameTextField.setLabel("Username");
        passwordTextField = new PasswordField();
        userRoleComboBox = new MultiSelectComboBox<>();
        HorizontalLayout layoutRow = new HorizontalLayout();

        saveButton = new Button();
        cancelButton = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Add user");
        saveButton.setText("Save");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        passwordTextField.setLabel("Password");
        userRoleComboBox.setLabel("User roles");
        userRoleComboBox.setWidth("100%");
        setRolesComboBoxData(userRoleComboBox);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.setText("Cancel");
        cancelButton.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(usernameTextField);
        formLayout2Col.add(passwordTextField);
        layoutColumn2.add(userRoleComboBox);
        layoutColumn2.add(layoutRow);
        layoutRow.add(saveButton);
        layoutRow.add(cancelButton);
    }

    private void fieldsValidation() {

        validationBinder = new BeanValidationBinder<>(UserDto.class);

        validationBinder.forField(usernameTextField)
                .withValidator(new BeanValidator(UserDto.class,"username"))
                .bind(UserDto::getUsername,UserDto::setUsername);

        validationBinder.forField(passwordTextField)
                .withValidator( new RegexpValidator("Enter an valid password","^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[-+!@#$*])(?=\\S+$).{8,}$"))
                .bind(UserDto::getPassword,UserDto::setPassword);

        validationBinder.forField(userRoleComboBox)
                .withValidator(new BeanValidator(UserDto.class,"roles"))
                .bind(UserDto::getRoles,UserDto::setRoles);

    }

    public void saveUser() {
        try {
            if (validationBinder.validate().isOk()) {
                if (!updateFlag) {
                    userService.add(getUserToSave());
                    Notifications.successNotification("User added successfully!").open();
                } else {
                    userService.update(getUserToUpdate());
                    Notifications.successNotification("User updated successfully!").open();
                }
            }
        } catch (Exception e) {
            Notifications.errorNotification("Error while saving user -> "+ e.getMessage()).open();
        }
    }

    private UserDto getUserToSave(){
        return UserDto.builder()
                .username(usernameTextField.getValue())
                .password(passwordTextField.getValue())
                .roles(userRoleComboBox.getSelectedItems())
                .build();
    }

    private UserDto getUserToUpdate() {
        return UserDto.builder()
                .id(updateUserDto.getId())
                .username(usernameTextField.getValue())
                .password(passwordTextField.getValue())
                .roles(userRoleComboBox.getSelectedItems())
                .build();
    }

    private void setRolesComboBoxData(MultiSelectComboBox<RoleDto> rolesComboBox) {
        rolesComboBox.setItems(roleService.findAllRoles());
        rolesComboBox.setItemLabelGenerator(RoleDto::getName);
    }

    public void setUpdateUser(UserDto userDto) {
        usernameTextField.setValue(userDto.getUsername());
        passwordTextField.setValue(userDto.getPassword());
        userRoleComboBox.setItems(roleService.findAllRoles());
        userRoleComboBox.setValue(userDto.getRoles());
        this.updateUserDto = userDto;
    }
}
