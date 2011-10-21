package com.gmail.kompotik.gwtck.client.plugins;

import com.gmail.kompotik.gwtck.client.CKConfig;
import com.gmail.kompotik.gwtck.client.CKEditor;
import com.google.gwt.core.client.JavaScriptObject;

public abstract class CKEditorPlugin {
  protected CKEditor ckEditor;
  protected CKConfig ckConfig;
  public abstract void execute();
  public abstract boolean labelShouldBeVisible();
  public abstract boolean iconShouldBeVisible();
  public final String getNamespaceName() {
    return this.getClass().getName().substring(0, this.getClass().getName().lastIndexOf('.'));
  }
  public final String getPluginName() {
    return this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.') + 1);
  }
  public abstract String getIconPath();
  public abstract String getLabel();

  public CKEditorPlugin(CKConfig ckConfig) {
    this.ckConfig = ckConfig;
    this.ckConfig.addPlugin(this);
  }

  public void setCkEditor(CKEditor ckEditor) {
    this.ckEditor = ckEditor;
    register();
  }

  private void register() {
    registerCallback(getNamespaceName(), getPluginName(), this);
  }

  private JavaScriptObject getEditor() {
    return ckEditor.getEditor();
  }

  public native void registerCallback(String nameSpace, String functionName, Object x)/*-{
    var e = x.@com.gmail.kompotik.gwtck.client.plugins.CKEditorPlugin::getEditor()();
    if (!e[nameSpace]) {
      e[nameSpace] = {};
    }
    e[nameSpace][functionName] = function () {
      x.@com.gmail.kompotik.gwtck.client.plugins.CKEditorPlugin::execute()();
    }
  }-*/;
}