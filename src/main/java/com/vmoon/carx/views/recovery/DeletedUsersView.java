package com.vmoon.carx.views.recovery;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vmoon.carx.dto.UserDto;
import com.vmoon.carx.services.UserService;
import com.vmoon.carx.utils.Notifications;
import com.vmoon.carx.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@PageTitle("Deleted Users")
@Route(value = "deleted-users-view", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("ADMIN")
public class DeletedUsersView extends Composite<VerticalLayout> {

    private final UserService userService;
    Grid<UserDto> usersGrid;

    public DeletedUsersView(UserService userService) {
        this.userService = userService;

        createUI();
    }

    private void createUI() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        usersGrid = new Grid<>(UserDto.class, false);
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutColumn2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        usersGrid.setWidth("100%");
        usersGrid.getStyle().set("flex-grow", "0");
        setUsersGridData(usersGrid);
        getContent().add(layoutColumn2);
        layoutColumn2.add(usersGrid);
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

        Grid.Column<UserDto> recoveryColumn = usersGrid.addColumn(new ComponentRenderer<>(userDto -> {
            Button deleteButton = new Button(new Icon(VaadinIcon.RECYCLE), buttonClickEvent -> confirmRecoveryDialog(userDto));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
            return deleteButton;
        })).setHeader("Actions");

        usersGrid.setColumnOrder(idColumn, usernameColumn, rolesColumn, recoveryColumn);

        DataProvider<UserDto, Void> dataProvider = DataProvider.fromCallbacks(
                query -> {
                    PageRequest pageRequest = PageRequest.of(
                            query.getPage(),
                            query.getPageSize(),
                            query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                    );

                    Page<UserDto> page = userService.listDeleted(pageRequest);
                    return page.stream();
                },
                query -> userService.countDeleted()
        );
        usersGrid.setDataProvider(dataProvider);
    }

    private void confirmRecoveryDialog(UserDto userDto) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("User " + userDto.getUsername());
        dialog.setText("Are you sure you want to recover this user?");
        dialog.setCancelable(true);

        dialog.setConfirmText("Recover");
        dialog.setConfirmButtonTheme("success primary");
        dialog.addConfirmListener(event -> recoverUser(userDto));
        dialog.open();
    }

    private void recoverUser(UserDto userDto) {
        userDto.setIsDeleted(false);
        userService.update(userDto);
        Notifications.successNotification("User are successfully recovered!").open();
        usersGrid.getDataProvider().refreshAll();
    }
}
