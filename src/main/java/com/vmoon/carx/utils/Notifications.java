package com.vmoon.carx.utils;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_TERTIARY_INLINE;

public class Notifications {

    public static Notification successNotification(String message) {
        Notification notification = new Notification();
        notification.setDuration(4000);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.BOTTOM_END);

        Icon icon = VaadinIcon.CHECK_CIRCLE.create();


        var layout = new HorizontalLayout(icon, new Text(message),
                createCloseBtn(notification));
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        notification.add(layout);

        return notification;
    }

    public static Notification errorNotification(String message) {
        Notification notification = new Notification();
        notification.setDuration(4000);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.BOTTOM_END);

        Icon icon = VaadinIcon.WARNING.create();


        var layout = new HorizontalLayout(icon, new Text(message),
                createCloseBtn(notification));
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        notification.add(layout);

        return notification;
    }

    public static Notification warningNotification(String message) {
        Notification notification = new Notification();
        notification.setDuration(4000);
        notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
        notification.setPosition(Notification.Position.BOTTOM_END);

        Icon icon = VaadinIcon.WARNING.create();

        var layout = new HorizontalLayout(icon, new Text(message),
                createCloseBtn(notification));
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        notification.add(layout);

        return notification;
    }



    public static Notification UploadSuccessNotification(String filename) {
        Notification notification = new Notification();
        notification.setDuration(4000);
        notification.setPosition(Notification.Position.BOTTOM_END);

        Icon icon = VaadinIcon.CHECK_CIRCLE.create();
        icon.setColor("var(--lumo-success-color)");

        Div uploadSuccessful = new Div(new Text("Upload successful"));
        uploadSuccessful.getStyle()
                .set("font-weight", "600")
                .setColor("var(--lumo-success-text-color)");

        Span fileName = new Span(filename);
        fileName.getStyle()
                .set("font-size", "var(--lumo-font-size-s)")
                .set("font-weight", "600");

        Div info = new Div(uploadSuccessful,
                new Div(fileName, new Text(" is now available in "),
                        new Anchor("#", "Downloads")));

        info.getStyle()
                .set("font-size", "var(--lumo-font-size-s)")
                .setColor("var(--lumo-secondary-text-color)");

        var layout = new HorizontalLayout(icon, info,
                createCloseBtn(notification));
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        notification.add(layout);

        return notification;
    }


    public static Button createCloseBtn(Notification notification) {
        Button closeBtn = new Button(VaadinIcon.CLOSE_SMALL.create(),
                clickEvent -> notification.close());
        closeBtn.addThemeVariants(LUMO_TERTIARY_INLINE);

        return closeBtn;
    }

}
