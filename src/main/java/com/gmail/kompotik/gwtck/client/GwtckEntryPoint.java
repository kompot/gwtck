package com.gmail.kompotik.gwtck.client;

import com.gmail.kompotik.gwtck.client.plugins.CKEditorPlugin;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class GwtckEntryPoint implements EntryPoint {
  public void onModuleLoad() {
    final CKConfig full = CKConfig.basic;
    // we've got patch in CKEDITOR_MODIFIED so that it replaces BR element with a single whitespace
    full.setRemoveFormatTags("br,b,big,code,del,dfn,em,font,i,ins,kbd," +
        "q,samp,small,span,strike,strong,sub,sup,tt,u,var");
    ToolbarLine line1 = new ToolbarLine();
    LinkPlugin linkPlugin = new LinkPlugin(full);
    line1.add(linkPlugin);
    Toolbar t = new Toolbar();
    t.add(line1);
    full.setToolbar(t);
    // plugin disables base64 encoded images pasting
    full.addExtraPlugin("imagepaste");
    final CKEditor editor = new CKEditor(full);
    RootPanel.get().add(editor);
  }

  public class LinkPlugin extends CKEditorPlugin {
    public LinkPlugin(CKConfig ckConfig) {
      super(ckConfig);
      this.ckConfig = ckConfig;
    }

    @Override
    public void execute() {
      Window.alert("Link plugin is clicked");
    }

    @Override
    public boolean labelShouldBeVisible() {
      return true;
    }

    @Override
    public boolean iconShouldBeVisible() {
      return false;
    }

    @Override
    public String getIconPath() {
      return null;
    }

    @Override
    public String getLabel() {
      return "Link popup";
    }
  }
}
