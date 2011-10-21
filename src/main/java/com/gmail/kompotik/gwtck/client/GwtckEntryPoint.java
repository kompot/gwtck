package com.gmail.kompotik.gwtck.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class GwtckEntryPoint implements EntryPoint {
  public void onModuleLoad() {
    final CKEditor editor = new CKEditor(CKConfig.basic);
    RootPanel.get().add(editor);
  }
}
