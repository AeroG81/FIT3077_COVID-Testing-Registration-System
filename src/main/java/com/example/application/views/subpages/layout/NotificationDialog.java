package com.example.application.views.subpages.layout;

import com.example.application.data.entity.User.UserCollection;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import java.net.http.HttpResponse;
import java.util.ArrayList;


public class NotificationDialog extends Dialog {
    private Grid<String> notificationGrid = null;

    private UserCollection userCollection = new UserCollection();

    public NotificationDialog (){
        populateGrid();
        HorizontalLayout buttonLayout = configureButtonLayout();
        add(notificationGrid, buttonLayout);
        setWidth("600px");
        setModal(false);
        setDraggable(true);
        setResizable(true);
    }

    private HorizontalLayout configureButtonLayout() {
        Button closeButton = new Button("Clear All", e -> {
            try {
                HttpResponse<String> response = userCollection.clearNotifications(UI.getCurrent().getSession().getAttribute("userId").toString(),UI.getCurrent().getSession().getAttribute("testingSiteId").toString());
                if (response.statusCode()==200){
                    Notification noti = Notification.show("Notification cleared");
                    noti.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
                else
                    throw new Exception("Unable to clear notification");
            } catch (Exception exception){
                System.out.println(exception);
            }
            close();
        });
        closeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttonLayout = new HorizontalLayout(closeButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        return buttonLayout;
    }

    private void populateGrid() {
        notificationGrid = new Grid<>();
        notificationGrid.addColumn(Object::toString).setHeader("Notifications").setTextAlign(ColumnTextAlign.START);
        ArrayList<String> notifications = new UserCollection().getNotificationsByReceptionistId(UI.getCurrent().getSession().getAttribute("userId").toString());
        notificationGrid.setItems(notifications);
    }

}
// TODO Notification Dialog, Documentation, Design Rationale, Update notifications