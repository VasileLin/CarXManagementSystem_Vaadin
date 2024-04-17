package com.vmoon.carx.views.settings;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vmoon.carx.dto.CompanyDto;
import com.vmoon.carx.services.CompanyService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@PageTitle("Company Data")
@Component
@Scope("prototype")
public class CompanyDataContent extends Composite<VerticalLayout> {

    TextField nameTextField;
    TextField addressField;
    TextField ibanField;

    private final CompanyService companyService;

    public CompanyDataContent(CompanyService companyService) {
        this.companyService = companyService;
        initializeCompanyDataContent();
        initializeCompanyData();
    }

    private void initializeCompanyDataContent() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        layoutColumn2.setAlignItems(FlexComponent.Alignment.CENTER);
        Button saveButton;
        H3 h3;
        h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        nameTextField = new TextField();
        addressField = new TextField();
        ibanField = new TextField();
        HorizontalLayout layoutRow = new HorizontalLayout();

        saveButton = new Button();
        saveButton.setText("Save");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> saveCompanyData());

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Company Data");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        nameTextField.setLabel("Name");
        addressField.setLabel("Address");
        addressField.setWidth("min-content");
        ibanField.setLabel("IBAN");
        ibanField.setWidth("min-content");
        layoutRow.addClassName(LumoUtility.Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(nameTextField);
        formLayout2Col.add(addressField);
        formLayout2Col.add(ibanField);
        layoutColumn2.add(layoutRow);
        layoutRow.add(saveButton);

    }

    private void initializeCompanyData() {
        List<CompanyDto> companyDto = companyService.getAllCompanies();

        if (!companyDto.isEmpty()) {
            nameTextField.setValue(companyDto.get(0).getName());
            addressField.setValue(companyDto.get(0).getAddress());
            ibanField.setValue(companyDto.get(0).getIban());
        }
    }

    private void saveCompanyData() {
        CompanyDto companyDto = CompanyDto.builder()
                .name(nameTextField.getValue())
                .address(addressField.getValue())
                .iban(ibanField.getValue())
                .build();

        companyService.saveCompany(companyDto);
        Notification.show("Company data updated");
    }
}
