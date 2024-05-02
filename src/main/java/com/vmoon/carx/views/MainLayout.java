package com.vmoon.carx.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vmoon.carx.entities.UserEntity;
import com.vmoon.carx.security.AuthenticatedUser;
import com.vmoon.carx.views.cash.CashView;
import com.vmoon.carx.views.customers.CustomersView;
import com.vmoon.carx.views.employers.EmployersView;
import com.vmoon.carx.views.recovery.DeletedCustomersView;
import com.vmoon.carx.views.recovery.DeletedEmployerView;
import com.vmoon.carx.views.recovery.DeletedUsersView;
import com.vmoon.carx.views.reports.ReportsView;
import com.vmoon.carx.views.service.ServiceView;
import com.vmoon.carx.views.settings.SettingsView;
import com.vmoon.carx.views.users.UsersView;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private final AuthenticatedUser authenticatedUser;
    private final AccessAnnotationChecker accessChecker;

    private H2 viewTitle;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {

        Image logo = new Image("logo/logo.png", "Logo");
        logo.setWidth("100px");

        H1 appName = new H1("CarX Management System");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        VerticalLayout headerLayout = new VerticalLayout(logo, appName);
        headerLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        headerLayout.setPadding(false);
        headerLayout.setSpacing(true);

        Header header = new Header(headerLayout);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        if (accessChecker.hasAccess(EmployersView.class)) {
            nav.addItem(new SideNavItem("Employers", EmployersView.class, LineAwesomeIcon.PEOPLE_CARRY_SOLID.create()));
        }

        if (accessChecker.hasAccess(CustomersView.class)) {
            nav.addItem(new SideNavItem("Customers", CustomersView.class, LineAwesomeIcon.USER_TAG_SOLID.create()));
        }

        if (accessChecker.hasAccess(ServiceView.class)) {
            nav.addItem(new SideNavItem("Service", ServiceView.class, LineAwesomeIcon.OIL_CAN_SOLID.create()));
        }

        if (accessChecker.hasAccess(CashView.class)) {
            nav.addItem(new SideNavItem("Cash", CashView.class, LineAwesomeIcon.CASH_REGISTER_SOLID.create()));
        }

        if (accessChecker.hasAccess(ReportsView.class)) {
            nav.addItem(new SideNavItem("Reports", ReportsView.class, LineAwesomeIcon.CHART_BAR_SOLID.create()));
        }

        if (accessChecker.hasAccess(SettingsView.class)) {
            nav.addItem(new SideNavItem("Settings", SettingsView.class, LineAwesomeIcon.TOOLS_SOLID.create()));
        }

        if (accessChecker.hasAccess(UsersView.class)) {
            nav.addItem(new SideNavItem("Users", UsersView.class, LineAwesomeIcon.USER_SHIELD_SOLID.create()));
        }

        if (accessChecker.hasAccess(DeletedCustomersView.class)) {
            nav.addItem(new SideNavItem("Deleted customers", DeletedCustomersView.class, LineAwesomeIcon.USER_SLASH_SOLID.create()));
        }

        if (accessChecker.hasAccess(DeletedEmployerView.class)) {
            nav.addItem(new SideNavItem("Deleted employers", DeletedEmployerView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
        }

        if (accessChecker.hasAccess(DeletedUsersView.class)) {
            nav.addItem(new SideNavItem("Deleted users", DeletedUsersView.class, LineAwesomeIcon.USER_NINJA_SOLID.create()));
        }

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<UserEntity> maybeUser = authenticatedUser.get();

            if (maybeUser.isPresent()) {

                UserEntity user = maybeUser.get();
                Avatar avatar = new Avatar("User");
                avatar.setThemeName("xsmall");
                avatar.getElement().setAttribute("tabindex", "-1");

                MenuBar userMenu = new MenuBar();
                userMenu.setThemeName("tertiary-inline contrast");

                MenuItem userName = userMenu.addItem("");
                Div div = new Div();
                div.add(avatar);
                div.add(user.getUsername());
                div.add(new Icon("lumo", "dropdown"));
                div.getElement().getStyle().set("display", "flex");
                div.getElement().getStyle().set("align-items", "center");
                div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
                userName.add(div);
                userName.getSubMenu().addItem("Sign out", e -> authenticatedUser.logout());

                layout.add(userMenu);
            }


        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
