package com.vmoon.carx.views.settings;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.validator.BeanValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vmoon.carx.dto.GoodsCategoryDto;
import com.vmoon.carx.services.CategoriesService;
import com.vmoon.carx.utils.Notifications;
import com.vmoon.carx.utils.SecurityUtils;
import com.vmoon.carx.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PageTitle("Category Form")
@Route(value = "category-form", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed({"ADMIN", "MANAGER"})
public class GoodsCategoryForm extends Composite<VerticalLayout> {
    private static final Logger logger = LoggerFactory.getLogger(GoodsCategoryForm.class);
    private final CategoriesService goodsCategoryService;
    TextField name;
    @Getter
    Button saveButton;
    @Getter
    Button cancelButton;
    @Getter
    H3 h3;
    BeanValidationBinder<GoodsCategoryDto> validationBinder;
    @Getter
    GoodsCategoryDto categoryDto;
    @Setter
    boolean updateFlag;


    public GoodsCategoryForm(CategoriesService goodsCategoryService) {
        this.goodsCategoryService = goodsCategoryService;
        vaadinUI();
        fieldsValidation();
    }

    private void fieldsValidation() {
        validationBinder = new BeanValidationBinder<>(GoodsCategoryDto.class);

        validationBinder.forField(name)
                .withValidator(new BeanValidator(GoodsCategoryDto.class, "name"))
                .bind(GoodsCategoryDto::getName, GoodsCategoryDto::setName);

    }

    public void vaadinUI() {
        VerticalLayout layoutColumn2 = new VerticalLayout();

        h3 = new H3();
        name = new TextField();
        name.setLabel("Category Name");
        name.setWidth("100%");
        name.setPrefixComponent(new Icon(VaadinIcon.CLIPBOARD_USER));

        FormLayout formLayout2Col = new FormLayout();
        Hr hr = new Hr();
        HorizontalLayout layoutRow = new HorizontalLayout();

        saveButton = new Button();
        saveButton.addClickListener(e -> saveCategory());
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
        h3.setText("Goods Categories Registration");
        h3.setWidth("100%");

        formLayout2Col.setWidth("100%");
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
        layoutColumn2.add(name);
        layoutColumn2.add(formLayout2Col);
        layoutColumn2.add(hr);
        layoutColumn2.add(layoutRow);
        if (SecurityUtils.isUserAdmin()) {
            layoutRow.add(saveButton);
        }
        layoutRow.add(cancelButton);
    }

    public void saveCategory() {
        if (validationBinder.validate().isOk()) {
            try {
                if (!updateFlag) {
                    goodsCategoryService.saveCategory(getCategoryToSave());
                    UI.getCurrent().navigate("goods-categories");
                } else {
                    goodsCategoryService.saveCategory(getCategoryToUpdate());
                }

            } catch (Exception e) {
                logger.error("Error saving category {}", e.getMessage());
                Notifications.errorNotification("Error saving category: " + e.getMessage()).open();
            }
        } else {
            Notifications.warningNotification("Invalid data!").open();
        }

    }

    public GoodsCategoryDto getCategoryToSave() {
        return GoodsCategoryDto.builder()
                .name(name.getValue())
                .isDeleted(false)
                .build();
    }


    public void setUpdateCategory(GoodsCategoryDto goodsCategoryDto) {
        name.setValue(goodsCategoryDto.getName());
        this.categoryDto = goodsCategoryDto;
    }

    public GoodsCategoryDto getCategoryToUpdate() {
        return GoodsCategoryDto.builder()
                .id(categoryDto.getId())
                .name(name.getValue())
                .isDeleted(false)
                .build();
    }

}
