package com.example.application.views.list;

import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.TestingSite.TestingSiteCollection;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "/testingsite")
@PageTitle("TestingSite | Vaadin CRM")
public class TestingSiteView extends VerticalLayout {
    Grid<TestingSite> grid = new Grid<>(TestingSite.class);
    TextField filterText = new TextField();
    TestingSiteCollection collection = new TestingSiteCollection();

    public TestingSiteView(){
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        add(getToolbar(), grid);
        populateList();
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("name", "description", "websiteUrl");
        grid.getColumns().forEach(col -> col.setWidth("22%"));
        grid.addColumn(site -> site.getPhoneNumber()).setHeader("Phone Number").setWidth("10%");
        grid.addColumn(site -> site.getAddress()).setHeader("Address").setWidth("20%");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    private void populateList(){
        grid.setItems(collection.getCollection());
    }

    private void updateList(String keyword) {
        grid.setItems(collection.searchCollection(keyword));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Search site by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
//        filterText.addValueChangeListener(e -> updateList(filterText.getValue()));
        Button searchButton = new Button("Search");
        searchButton.addClickListener(e -> updateList(filterText.getValue()));
        HorizontalLayout toolbar = new HorizontalLayout(filterText, searchButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
}

