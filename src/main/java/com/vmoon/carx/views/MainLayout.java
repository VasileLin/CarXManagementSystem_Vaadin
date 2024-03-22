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
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vmoon.carx.views.cash.CashView;
import com.vmoon.carx.views.customers.CustomersView;
import com.vmoon.carx.views.employers.EmployersView;
import com.vmoon.carx.views.reports.ReportsView;
import com.vmoon.carx.views.service.ServiceView;
import com.vmoon.carx.views.settings.SettingsView;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    public MainLayout() {
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

            nav.addItem(new SideNavItem("Employers", EmployersView.class, LineAwesomeIcon.PEOPLE_CARRY_SOLID.create()));

            nav.addItem(new SideNavItem("Customers", CustomersView.class, LineAwesomeIcon.MONEY_BILL_WAVE_SOLID.create()));

            nav.addItem(new SideNavItem("Service", ServiceView.class, LineAwesomeIcon.CAR_BATTERY_SOLID.create()));

            nav.addItem(new SideNavItem("Cash", CashView.class, LineAwesomeIcon.CASH_REGISTER_SOLID.create()));

            nav.addItem(new SideNavItem("Reports", ReportsView.class, LineAwesomeIcon.CHART_BAR_SOLID.create()));

            nav.addItem(new SideNavItem("Settings", SettingsView.class, LineAwesomeIcon.ICONS_SOLID.create()));

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

            Avatar avatar = new Avatar("User");
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add("User");
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Sign out", e -> {
               /* authenticatedUser.logout();*/
            });

            layout.add(userMenu);

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
