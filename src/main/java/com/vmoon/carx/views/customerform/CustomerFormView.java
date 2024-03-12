package com.vmoon.carx.views.customerform;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vmoon.carx.views.MainLayout;

@PageTitle("Customer Form")
@Route(value = "customer-form", layout = MainLayout.class)
@Uses(Icon.class)
public class CustomerFormView extends Composite<VerticalLayout> {

    public CustomerFormView() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        TextField textField = new TextField();
        FormLayout formLayout2Col = new FormLayout();
        TextField textField2 = new TextField();
        EmailField emailField = new EmailField();
        TextField textField3 = new TextField();
        TextField textField4 = new TextField();
        Hr hr = new Hr();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        Button buttonSecondary = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Customer Registration");
        h3.setWidth("100%");
        textField.setLabel("Full Name");
        formLayout2Col.setWidth("100%");
        textField2.setLabel("Phone Number");
        emailField.setLabel("Email");
        textField3.setLabel("Car Model");
        textField4.setLabel("Car No.");
        textField4.setWidth("min-content");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        buttonPrimary.setText("Save");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSecondary.setText("Cancel");
        buttonSecondary.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(textField);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(textField2);
        formLayout2Col.add(emailField);
        formLayout2Col.add(textField3);
        formLayout2Col.add(textField4);
        layoutColumn2.add(hr);
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonSecondary);
    }
}
