package com.example.application.views.Subpages;

import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.TestingSite.TestingSiteCollection;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "/testingsite")
@PageTitle("Site")
/**
 * This is the List view of testing sites
 */
public class TestingSitesView extends VerticalLayout {
    Grid<TestingSite> grid = new Grid<>(TestingSite.class);
    TextField filterText = new TextField();
    TestingSiteCollection collection = new TestingSiteCollection();

    /**
     * populate the layout with components
     */
    public TestingSitesView(){
        configureGrid();
        populateList();
        add(getToolbar(), grid);
        setSizeFull();
    }

    /**
     * Configuring Grid/List
     */
    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("name", "description");
        grid.getColumns().forEach(col -> col.setWidth("20%"));
        grid.addColumn(site -> site.getAddress().toString()).setHeader("Address").setWidth("20%");
        grid.addColumn(site -> site.getFacilityType()).setHeader("Facility Type").setWidth("15%");
        grid.addColumn(site -> site.getOperationTime()).setHeader("Operation Time").setWidth("10%");
        grid.addColumn(site -> site.getWaitingTime()).setHeader("Waiting Time").setWidth("10%");
        grid.setHeight("20%");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    /**
     * Populate the Grid of testing site
     */
    private void populateList(){
        grid.setItems(collection.getCollection());
    }

    /**
     * Update the grid based on the keyword typed in the search field
     */
    private void updateList(String keyword) {
        grid.setItems(collection.searchCollection(keyword));
    }

    /**
     * Configuring Toolbar for the grid
     */
    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Search site by suburb or facility type");
        filterText.setWidth("300px");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        Button searchButton = new Button("Search");
        searchButton.addClickListener(e -> updateList(filterText.getValue()));
        Button systemBookingPromptButton = new Button("Book a Test");
        systemBookingPromptButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        systemBookingPromptButton.addClickListener(f -> systemBookingPromptButton.getUI().ifPresent(ui -> ui.navigate("systembooking")));
        HorizontalLayout toolbar = new HorizontalLayout(filterText, searchButton, systemBookingPromptButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
}

