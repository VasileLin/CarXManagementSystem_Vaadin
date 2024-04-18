package com.vmoon.carx.utils;

import com.vaadin.flow.component.dialog.Dialog;

import java.util.ArrayList;
import java.util.List;

public class DialogManager {
    private static final List<Dialog> openDialogs = new ArrayList<>();

    public static void registerDialog(Dialog dialog) {
        openDialogs.add(dialog);
    }

    public static void closeDialog(Dialog dialog) {
        dialog.close();
        openDialogs.remove(dialog);
    }

    public static void closeAll() {
        new ArrayList<>(openDialogs).forEach(Dialog::close);
        openDialogs.clear();
    }
}
