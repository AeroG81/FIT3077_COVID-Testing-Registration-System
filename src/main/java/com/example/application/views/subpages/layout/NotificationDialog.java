package com.example.application.views.subpages.layout;

import com.example.application.data.entity.User.Receptionist;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;



public class NotificationDialog extends Dialog {
    private Grid<Receptionist> notificationGrid = null;

    public NotificationDialog (){
        populateGrid();
        HorizontalLayout buttonLayout = configureButtonLayout();
        add(notificationGrid, buttonLayout);
        setWidth("1200px");
    }

    private HorizontalLayout configureButtonLayout() {
        Button closeButton = new Button("Close", e -> {
            close();
        });
        closeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttonLayout = new HorizontalLayout(closeButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        return buttonLayout;
    }

    private void populateGrid() {
        notificationGrid = new Grid<>(Receptionist.class, false);
        notificationGrid.addColumn(r -> r.getAdditionalInfo()).setHeader("Notifications").setTextAlign(ColumnTextAlign.END);
        notificationGrid.setItems();
    }

}
// TODO Notification Dialog, Documentation, Design Rationale, Update notifications