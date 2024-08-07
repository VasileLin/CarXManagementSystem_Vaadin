package com.vmoon.carx.views.users;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vmoon.carx.dto.UserDto;
import com.vmoon.carx.entities.UserEntity;
import com.vmoon.carx.security.AuthenticatedUser;
import com.vmoon.carx.services.RoleService;
import com.vmoon.carx.services.UserService;
import com.vmoon.carx.utils.Notifications;
import com.vmoon.carx.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@PageTitle("Users")
@Route(value = "users-view", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("ADMIN")
@Component
@Scope("prototype")
public class UsersView extends Composite<VerticalLayout> {

    @Value("${app.user.default-password}")
    private String hashedPassword;
    private final UserService userService;
    private final RoleService roleService;
    Grid<UserDto> usersGrid;
    UserEntity authUser;

    public UsersView(UserService userService, RoleService roleService, AuthenticatedUser authenticatedUser) {
        this.userService = userService;
        this.roleService = roleService;
        authUser = authenticatedUser.get().orElseThrow();
        createUI();
    }

    private void createUI() {
        VerticalLayout layoutColumn2 = new VerticalLayout();

        usersGrid = new Grid<>(UserDto.class,false);
        Button addUserButton = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutColumn2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        usersGrid.setWidth("100%");
        usersGrid.getStyle().set("flex-grow", "0");
        usersGrid.addItemDoubleClickListener(event -> openUpdateDialog(event.getItem()));
        setUsersGridData(usersGrid);
        addUserButton.setText("Adding new user");
        addUserButton.setWidth("100%");
        addUserButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addUserButton.addClickListener(event -> openAddDialog());
        getContent().add(layoutColumn2);
        layoutColumn2.add(usersGrid);
        layoutColumn2.add(addUserButton);
    }

    private void openUpdateDialog(UserDto userDto) {
        UserFormView userFormView = new UserFormView(roleService, userService);
        Dialog dialog = new Dialog(userFormView);
        userFormView.setUpdateUser(userDto);
        userFormView.setUpdateFlag(true);
        userFormView.getH3().setText("My Profile");
        userFormView.getSaveButton().setText("Update");
        userFormView.getPasswordTextField().setReadOnly(true);
        userFormView.getPasswordTextField().setRevealButtonVisible(false);

        userFormView.getSaveButton().addClickListener(event -> {
            userFormView.saveUser();
            usersGrid.getDataProvider().refreshAll();
            dialog.close();
        });

        userFormView.getCancelButton().addClickListener(buttonClickEvent -> dialog.close());

        dialog.setWidth("auto");
        dialog.setHeight("auto");
        dialog.setDraggable(true);
        dialog.open();
    }

    private void openAddDialog() {
        UserFormView userFormView = new UserFormView(roleService, userService);
        Dialog dialog = new Dialog(userFormView);

        userFormView.getSaveButton().addClickListener(event -> {
            if (userFormView.validationBinder.validate().isOk()) {
                userFormView.saveUser();
                usersGrid.getDataProvider().refreshAll();
                dialog.close();
            }
        });

        userFormView.getCancelButton().addClickListener(buttonClickEvent -> dialog.close());

        dialog.setWidth("auto");
        dialog.setHeight("auto");
        dialog.setDraggable(true);
        dialog.open();

    }

    private void setUsersGridData(Grid<UserDto> usersGrid) {

        Grid.Column<UserDto> idColumn = usersGrid.addColumn(UserDto::getId)
                .setHeader("ID")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("id");

        Grid.Column<UserDto> usernameColumn = usersGrid.addColumn(UserDto::getUsername)
                .setHeader("Username")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("username");

        Grid.Column<UserDto> rolesColumn = usersGrid.addColumn(UserDto::getRoles)
                .setHeader("User roles")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("roles");

        Grid.Column<UserDto> actionsColumn = usersGrid.addColumn(new ComponentRenderer<>(userDto -> {
            HorizontalLayout layout = new HorizontalLayout();

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH), buttonClickEvent -> confirmDeleteDialog(userDto));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
            deleteButton.setTooltipText("Delete user");

            Button resetButton = new Button(new Icon(VaadinIcon.RECYCLE), buttonClickEvent -> confirmResetDialog(userDto));
            resetButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
            resetButton.setTooltipText("Reset user");
            layout.add(deleteButton, resetButton);
            return layout;
        })).setHeader("Actions");


        usersGrid.setColumnOrder(idColumn,usernameColumn,rolesColumn,actionsColumn);


        DataProvider<UserDto, Void> dataProvider = DataProvider.fromCallbacks(
                query -> {
                    PageRequest pageRequest = PageRequest.of(
                            query.getPage(),
                            query.getPageSize(),
                            query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                    );

                    Page<UserDto> page = userService.list(pageRequest);
                    return page.stream();
                },
                query -> userService.count()
        );

        usersGrid.setDataProvider(dataProvider);
    }

    private void confirmResetDialog(UserDto userDto) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("User "+ userDto.getUsername());
        dialog.setText("Are you sure you want to reset password for this user?\nDefault password 'Parola123++'");
        dialog.setCancelable(true);

        dialog.setConfirmText("Reset");
        dialog.setConfirmButtonTheme("success primary");
        dialog.addConfirmListener(event -> resetPasswordUser(userDto));
        dialog.setCancelText("No");
        dialog.setCancelButtonTheme("warning primary");
        dialog.addCancelListener(event -> dialog.close());
        dialog.open();
    }

    private void confirmDeleteDialog(UserDto userDto) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("User "+ userDto.getUsername());
        dialog.setText("Are you sure you want to delete this user?");
        dialog.setCancelable(true);

        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> deleteUser(userDto));
        dialog.open();
    }

    private void resetPasswordUser(UserDto userDto) {
        userDto.setPassword(hashedPassword);
        userService.update(userDto);
        Notifications.successNotification("Password are successfully reset!").open();
        usersGrid.getDataProvider().refreshAll();
    }

    private void deleteUser(UserDto userDto) {
        if (authUser.getId() == userDto.getId()) {
            Notifications.errorNotification("You cannot delete current logged user!").open();
        } else {
            userDto.setIsDeleted(true);
            userService.update(userDto);
            Notifications.successNotification("User are successfully deleted!").open();
            usersGrid.getDataProvider().refreshAll();
        }

    }
}
