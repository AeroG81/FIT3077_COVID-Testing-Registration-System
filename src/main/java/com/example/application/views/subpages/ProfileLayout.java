package com.example.application.views.subpages;

import com.example.application.data.entity.User.Resident;
import com.example.application.data.entity.User.User;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinSession;

public class ProfileLayout extends HorizontalLayout {

    public ProfileLayout() {
        VaadinSession ui = UI.getCurrent().getSession();
        User user = new Resident(ui.getAttribute("userId").toString(), ui.getAttribute("userGivenName").toString(), ui.getAttribute("userFamilyName").toString(), ui.getAttribute("userName").toString(), ui.getAttribute("userPhoneNumber").toString());

        String name = user.getFamilyName() + " " + user.getGivenName();
        Avatar avatar = new Avatar(name);
        avatar.setAbbreviation(user.getUserName());
        MenuBar menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);

        MenuItem menuItem = menuBar.addItem(avatar);
        SubMenu subMenu = menuItem.getSubMenu();
        subMenu.addItem("Profile", e -> {
            System.out.println("Profile selected");
        });
        subMenu.addItem("Settings", e -> {
            System.out.println("Settings selected");
        });
        subMenu.addItem("Sign out", e -> {
            System.out.println("Sign out selected");
        });

        add(menuBar);
    }
}
