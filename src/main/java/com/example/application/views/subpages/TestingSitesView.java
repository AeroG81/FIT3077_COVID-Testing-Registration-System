package com.example.application.views.subpages;

import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.TestingSite.TestingSiteCollection;
import com.example.application.views.subpages.layout.ProfileAvatarLayout;
import com.example.application.views.subpages.layout.TestingSiteListLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
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
    /**
     * populate the layout with components
     */
    public TestingSitesView(){
        add(new ProfileAvatarLayout(), new TestingSiteListLayout());
    }
}

