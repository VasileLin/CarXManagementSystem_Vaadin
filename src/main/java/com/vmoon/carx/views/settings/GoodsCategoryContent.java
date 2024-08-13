package com.vmoon.carx.views.settings;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vmoon.carx.dto.GoodsCategoryDto;
import com.vmoon.carx.services.CategoriesService;
import com.vmoon.carx.utils.Notifications;
import com.vmoon.carx.utils.SecurityUtils;
import com.vmoon.carx.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@PageTitle("Goods Categories")
@Uses(Icon.class)
@Route(value = "goods-categories", layout = MainLayout.class)
@RolesAllowed({"ADMIN", "MANAGER"})
@Scope("prototype")
public class GoodsCategoryContent extends Composite<VerticalLayout> {

    private final CategoriesService categoriesService;
    DataProvider<GoodsCategoryDto, Void> categoriesDataProvider;
    @Getter
    Grid<GoodsCategoryDto> categoriesGrid;
    TextField searchCategoriesField;
    @Getter
    private Dialog dialog;

    public GoodsCategoryContent(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
        categoriesGrid = new Grid<>(GoodsCategoryDto.class, false);
        categoriesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        categoriesGrid.setWidth("100%");
        categoriesGrid.setHeight("450px");
        categoriesGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(categoriesGrid);

        Hr hr = new Hr();
        HorizontalLayout layoutRow = new HorizontalLayout();

        Button addButton = new Button();
        addButton.setText("Add Category");
        addButton.setWidth("min-content");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.setPrefixComponent(new Icon(VaadinIcon.PLUS));
        addButton.addClickListener(e -> UI.getCurrent().navigate("category-form"));
        HorizontalLayout layoutRow2 = new HorizontalLayout();

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(LumoUtility.Padding.LARGE);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        layoutRow2.setHeightFull();
        layoutRow.setFlexGrow(1.0, layoutRow2);
        layoutRow2.setWidth("100%");
        layoutRow2.setHeight("50px");
        layoutRow2.setAlignItems(FlexComponent.Alignment.CENTER);
        layoutRow2.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        searchCategoriesField = new TextField();
        searchCategoriesField.setPlaceholder("Search Categories ...");
        searchCategoriesField.setWidth("100%");
        searchCategoriesField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchCategoriesField.addValueChangeListener(e -> searchCategories(e.getValue().trim()));

        getContent().add(searchCategoriesField);
        getContent().add(categoriesGrid);
        getContent().add(hr);
        getContent().add(layoutRow);
        if (SecurityUtils.isUserAdmin()) {
            layoutRow.add(addButton);
        }
        layoutRow.add(layoutRow2);
    }

    private void searchCategories(String value) {
        if (value.isEmpty()) {
            Notifications.warningNotification("Search bar is empty!").open();
            categoriesGrid.setDataProvider(categoriesDataProvider);
            categoriesGrid.getDataProvider().refreshAll();
        } else {
            DataProvider<GoodsCategoryDto, Void> dataProvider = DataProvider.fromCallbacks(
                    query -> {
                        PageRequest pageRequest = PageRequest.of(
                                query.getPage(),
                                query.getPageSize(),
                                query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                        );

                        Page<GoodsCategoryDto> page = categoriesService.searchCategories(value, pageRequest, false);
                        return page.stream();
                    },
                    query -> (int) categoriesService.countSearchResults(value, false)
            );

            categoriesGrid.setDataProvider(dataProvider);
        }
    }

    private void setGridSampleData(Grid<GoodsCategoryDto> categoriesGrid) {
        Grid.Column<GoodsCategoryDto> idColumn = categoriesGrid.addColumn(GoodsCategoryDto::getId)
                .setHeader("ID")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("id");

        Grid.Column<GoodsCategoryDto> nameColumn = categoriesGrid.addColumn(GoodsCategoryDto::getName)
                .setHeader("Name")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("name");


        Grid.Column<GoodsCategoryDto> deleteColumn = categoriesGrid.addColumn(new ComponentRenderer<>(categoryDto -> {
            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH), buttonClickEvent -> confirmDeleteDialog(categoryDto));
            deleteButton.setTooltipText("Delete Category");
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
            return deleteButton;
        })).setHeader("Actions");

        categoriesGrid.setColumnOrder(idColumn, nameColumn,deleteColumn);

        categoriesGrid.addItemDoubleClickListener(event -> {
            GoodsCategoryDto categoryDto = event.getItem();
            openEditDialog(categoryDto);
        });

        DataProvider<GoodsCategoryDto, Void> dataProvider = DataProvider.fromCallbacks(
                query -> {
                    PageRequest pageRequest = PageRequest.of(
                            query.getPage(),
                            query.getPageSize(),
                            query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                    );
                    Page<GoodsCategoryDto> page = categoriesService.allCategories(pageRequest,false);
                    return page.stream();
                },
                query -> (int) categoriesService.count(false)
        );

        categoriesGrid.setDataProvider(dataProvider);
    }

    private void openEditDialog(GoodsCategoryDto categoryDto) {
        GoodsCategoryForm goodsCategoryForm = new GoodsCategoryForm(categoriesService);
        goodsCategoryForm.setUpdateFlag(true);
        goodsCategoryForm.getH3().setText("Update Good Category " + categoryDto.getName());
        goodsCategoryForm.setUpdateCategory(categoryDto);

        goodsCategoryForm.getCancelButton().addClickListener(event -> dialog.close());
        goodsCategoryForm.getSaveButton().addClickListener(event -> {
            categoriesService.saveCategory(goodsCategoryForm.getCategoryToUpdate());
            categoriesGrid.getDataProvider().refreshAll();
            dialog.close();
        });

        dialog = new Dialog(goodsCategoryForm);
        dialog.setWidth("auto");
        dialog.setHeight("auto");
        dialog.setDraggable(true);
        dialog.open();
    }

    private void confirmDeleteDialog(GoodsCategoryDto categoryDto) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Category " + categoryDto.getName());
        dialog.setText("Are you sure you want to delete this category?");
        dialog.setCancelable(true);

        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> deleteCategory(categoryDto));
        dialog.open();
    }

    private void deleteCategory(GoodsCategoryDto categoryDto) {
        categoryDto.setIsDeleted(true);
        categoriesService.saveCategory(categoryDto);
        Notifications.successNotification("Category are successfully deleted!").open();
        categoriesGrid.getDataProvider().refreshAll();
    }
}
