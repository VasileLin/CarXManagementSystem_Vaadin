package com.vmoon.carx.views.reports;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vmoon.carx.dto.GoodsDto;
import com.vmoon.carx.services.GoodsService;
import com.vmoon.carx.utils.Notifications;
import jakarta.annotation.security.RolesAllowed;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import static com.vmoon.carx.utils.ExcelWorkBooks.createCostsExcelWorkBook;


@RolesAllowed({"ADMIN", "MANAGER"})
@Component
@Scope("prototype")
public class CostOfGoodsView extends Composite<VerticalLayout> {

    private final GoodsService goodsService;
    DatePicker fromGoodsDatePicker;
    DatePicker toGoodsDatePicker;
    Grid<GoodsDto> goodsGrid;
    Button exportCostsButton;

    public CostOfGoodsView(GoodsService goodsService) {
        this.goodsService = goodsService;
        initializeCostsReport();
    }

    private void initializeCostsReport() {
        HorizontalLayout layoutRow = new HorizontalLayout();
        goodsGrid = new Grid<>(GoodsDto.class, false);

        fromGoodsDatePicker = new DatePicker();
        fromGoodsDatePicker.addValueChangeListener(e -> goodsGrid.getDataProvider().refreshAll());
        fromGoodsDatePicker.setValue(LocalDate.now());
        toGoodsDatePicker = new DatePicker();
        toGoodsDatePicker.setValue(LocalDate.now());
        toGoodsDatePicker.addValueChangeListener(e -> goodsGrid.getDataProvider().refreshAll());
        VerticalLayout layoutColumn2 = new VerticalLayout();
        exportCostsButton = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(LumoUtility.Gap.XLARGE);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("80px");
        fromGoodsDatePicker.setLabel("From");
        fromGoodsDatePicker.setWidth("min-content");
        toGoodsDatePicker.setLabel("To");
        toGoodsDatePicker.setWidth("min-content");
        layoutColumn2.setHeightFull();
        layoutRow.setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        exportCostsButton.setText("Export");
        layoutRow.setAlignSelf(FlexComponent.Alignment.END, exportCostsButton);
        exportCostsButton.setWidth("min-content");
        exportCostsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        exportCostsButton.setPrefixComponent(new Icon(VaadinIcon.DOWNLOAD));
        configureCostsExportButton(exportCostsButton);
        goodsGrid.setWidth("100%");
        goodsGrid.setHeight("600px");
        goodsGrid.getStyle().set("flex-grow", "0");
        setGoodsGridSampleData(goodsGrid);
        getContent().add(layoutRow);
        getContent().add(goodsGrid);
        layoutRow.add(fromGoodsDatePicker);
        layoutRow.add(toGoodsDatePicker);
        layoutRow.add(layoutColumn2);
        layoutRow.add(exportCostsButton);

    }

    private void setGoodsGridSampleData(Grid<GoodsDto> goodsGrid) {
        Grid.Column<GoodsDto> nameColumn = goodsGrid.addColumn(GoodsDto::getCostName)
                .setHeader("Name")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("costName");

        Grid.Column<GoodsDto> costColumn = goodsGrid.addColumn(GoodsDto::getCost)
                .setHeader("Cost")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("cost");

        Grid.Column<GoodsDto> dateColumn = goodsGrid.addColumn(GoodsDto::getDate)
                .setHeader("Purchased date")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("date");

        Grid.Column<GoodsDto> statusColumn = goodsGrid.addColumn(GoodsDto::getStock)
                .setHeader("Available stock")
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true)
                .setSortProperty("stock");

        goodsGrid.setColumnOrder(nameColumn, costColumn, dateColumn, statusColumn);

        DataProvider<GoodsDto, Void> dataProvider = DataProvider.fromCallbacks(
                query -> {
                    PageRequest pageRequest = PageRequest.of(
                            query.getPage(),
                            query.getPageSize(),
                            query.getSortOrders().isEmpty() ? Sort.unsorted() : VaadinSpringDataHelpers.toSpringDataSort(query)
                    );
                    Page<GoodsDto> page = goodsService.allGoodsDate(pageRequest, fromGoodsDatePicker.getValue(), toGoodsDatePicker.getValue());
                    return page.stream();
                },
                query -> (int) goodsService.countDateResult(fromGoodsDatePicker.getValue(), toGoodsDatePicker.getValue())
        );

        goodsGrid.setDataProvider(dataProvider);
    }


    private void configureCostsExportButton(Button exportButton) {
        exportButton.addClickListener(event -> {
            String filename = "costs.xlsx";
            StreamResource resource = new StreamResource(filename, () -> {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                try (Workbook workbook = createCostsExcelWorkBook(goodsGrid.getLazyDataView().getItems().toList())) {
                    workbook.write(bos);
                } catch (IOException e) {
                    Notifications.errorNotification("Error writing file").open();
                }

                return new ByteArrayInputStream(bos.toByteArray());
            });

            Anchor downloadLink = new Anchor(resource, "");
            downloadLink.getElement().setAttribute("download", true);
            downloadLink.getElement().setAttribute("href", resource);
            downloadLink.getElement().setAttribute("style", "display: none;");

            UI.getCurrent().add(downloadLink);
            downloadLink.getElement().callJsFunction("click");
            Notifications.UploadSuccessNotification(filename).open();
        });
    }
}
