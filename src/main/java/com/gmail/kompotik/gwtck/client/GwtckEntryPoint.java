package com.gmail.kompotik.gwtck.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class GwtckEntryPoint implements EntryPoint {
  public void onModuleLoad() {
    final CKConfig full = CKConfig.full;
    // we've got patch in CKEDITOR_MODIFIED so that it replaces BR element with a single whitespace
    full.setRemoveFormatTags("br,b,big,code,del,dfn,em,font,i,ins,kbd," +
        "q,samp,small,span,strike,strong,sub,sup,tt,u,var");
    final CKEditor editor = new CKEditor(full);
    RootPanel.get().add(editor);
  }
}
