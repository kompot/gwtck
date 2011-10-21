package com.gmail.kompotik.gwtck.client;

import java.util.ArrayList;
import java.util.List;

import com.gmail.kompotik.gwtck.client.plugins.CKEditorPlugin;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.LocaleInfo;

public class CKConfig {
	private String toolbarName;
	private Toolbar toolbar;
	private String uiColor;
	private String height;
	private String width;
	private String breakLineChars;
	private String enterMode;
	private boolean ignoreEmptyParagraph;
    private boolean forcePasteAsPlainText;
	private String selfClosingEnd;
	private boolean resizeEnabled;
	private int resizeMinWidth;
	private int resizeMinHeight;
	private int resizeMaxWidth;
	private int resizeMaxHeight;
	private int baseFloatZIndex;
	private int tabIndex;
	private boolean useFormPanel = true;
	private String language;
	private boolean entities;
	private boolean entities_greek;
	private boolean entities_latin;
    private List<CKEditorPlugin> plugins;
	private boolean focusOnStartup;
	private String fileBrowserBrowseUrl;
	private String fileBrowserImageBrowseUrl;
	private String fileBrowserImageBrowseLinkUrl;
	private String filebrowserFlashBrowseUrl;
	private String filebrowserUploadUrl;
	private String filebrowserImageUploadUrl;
	private String filebrowserFlashUploadUrl;
	private List<String> extraPlugins;
	private String topSharedSpace;
	private String bottomSharedSpace;

	private List<String> fontNames;
	private List<String> fontSizes;
	private int autoSaveLatencyInMillis;

	private boolean tableResize;
	private boolean shouldEnterSave = false;
	private String skin;
	
	JavaScriptObject config = JavaScriptObject.createObject();

  /**
	 * Defines existing toolbar configuration in CKEDITOR environment
	 */
	public enum PRESET_TOOLBAR {
		BASIC, FULL
	}
	
	/**
	 * Defines existing toolbar options ; use _ as "-" separator
	 */
	public enum TOOLBAR_OPTIONS {
      Source, Save, NewPage, Preview, Templates, Cut, Copy, Paste, PasteText, PasteFromWord, Print, SpellChecker, Scayt, Undo, Redo, Find, Replace, SelectAll, RemoveFormat, Form, Checkbox, Radio, TextField, Textarea, Select, Button, ImageButton, HiddenField, Bold, Italic, Underline, Strike, Subscript, Superscript, NumberedList, BulletedList, Outdent, Indent, Blockquote, JustifyLeft, JustifyCenter, JustifyRight, JustifyBlock, Link, Unlink, Anchor, Image, Flash, Table, HorizontalRule, Smiley, SpecialChar, PageBreak, Styles, Format, Font, FontSize, TextColor, BGColor, Maximize, ShowBlocks, About, _
  }
	
	public enum AVAILABLE_PLUGINS {
      a11yhelp, about, adobeair, ajax, autogrow, bbcode, clipboard, colordialog, devtools, dialog, div, docprops, find, flash, forms, iframe, iframedialog, image, link, liststyle, pagebreak, pastefromword, pasteext, placeholder, scayt, showblocks, smiley, specialchar, styles, stylesheetparser, table, tableresize, tabletools, templates, uicolor, wsc, xml;
	}

	enum LINE_TYPE {
		NORMAL, SEPARATOR
	}
	
	/**
	 * Default full configuration
	 */
	public static CKConfig full = new CKConfig(PRESET_TOOLBAR.FULL);
	
	/**
	 * Default basic configuration
	 */
	public static CKConfig basic = new CKConfig(PRESET_TOOLBAR.BASIC);
	
	private boolean toolbarCanCollapse;
	
	/**
	 * Creates a default config with the FULL toolbar
	 */
	public CKConfig() {
		this(PRESET_TOOLBAR.FULL);
	}
	
	/**
	 * Creates a default config with the given PRESET_TOOLBAR
	 * 
	 * @param toolbar
	 *            the PRESET_TOOLBAR to use
	 */
	public CKConfig(PRESET_TOOLBAR toolbar) {
		initConfig();
        this.plugins = new ArrayList<CKEditorPlugin>();
		fontNames = new ArrayList<String>();
		fontSizes = new ArrayList<String>();
		extraPlugins = new ArrayList<String>();
        autoSaveLatencyInMillis = 0;
		setToolbar(toolbar);
		LocaleInfo l = LocaleInfo.getCurrentLocale();
		// GWT.log("Locale : "+l.getLocaleName(),null);//always returns default
		if (l.getLocaleName().equals("default")) {
			//GWT.log("[gwt-CKEditor]Locale Property : " + getLocaleProperty(), null);
			this.setLanguage(getLocaleProperty());
		} else {
			this.setLanguage(LocaleInfo.getCurrentLocale().getLocaleName().split("_")[0]);
        }
	}

	private native void initConfig() /*-{
	}-*/;
	
	private native String getLocaleProperty() /*-{
		if ($wnd.__gwt_Locale)
			return $wnd.__gwt_Locale;
		return "en";
	}-*/;
	
	/**
	 * Set the browser's Url to Open when selecting images, link and flash
	 * 
	 * @param fileBrowserBrowseUrl
	 *            the Url to use
	 */
	public void setFileBrowserBrowseUrl(String fileBrowserBrowseUrl) {
		this.fileBrowserBrowseUrl = fileBrowserBrowseUrl;
		setNativeFileBrowserBrowseUrl(fileBrowserBrowseUrl);
	}

	public void setShouldEnterSave(boolean enterShouldSave) {
		this.shouldEnterSave = enterShouldSave;
		setNativeShouldEnterSave(enterShouldSave);
	}

	private native void setNativeShouldEnterSave(boolean enterShouldSave)/*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.keystrokes += [
				13, 'save' ];
	}-*/;

	/**
	 * Set the Image browser's Url to Open when selecting images
	 * 
	 * @param fileBrowserImageBrowseUrl
	 *            the Url to use
	 */
	public void setFileBrowserImageBrowseUrl(String fileBrowserImageBrowseUrl) {
		this.fileBrowserImageBrowseUrl = fileBrowserImageBrowseUrl;
		setNativeFileBrowserImageBrowseUrl(fileBrowserImageBrowseUrl);
	}

	/**
	 * Set the Image browser's link tab Url to Open when selecting images
	 * 
	 * @param fileBrowserImageBrowseLinkUrl
	 *            the Url to use
	 */
	public void setFileBrowserImageBrowseLinkUrl(String fileBrowserImageBrowseLinkUrl) {
		this.fileBrowserImageBrowseLinkUrl = fileBrowserImageBrowseLinkUrl;
		setNativeFileBrowserImageBrowseLinkUrl(fileBrowserImageBrowseLinkUrl);
	}

	/**
	 * Sets the toolbar to a pre-defined one ; this will unset any Toolbar set
	 * before
	 * 
	 * @param toolbar
	 *            The preset toolbar to use
	 */
	public void setToolbar(PRESET_TOOLBAR toolbar) {
		if (toolbar == PRESET_TOOLBAR.BASIC) {
			toolbarName = "Basic";
		} else if (toolbar == PRESET_TOOLBAR.FULL) {
			toolbarName = "Full";
		}
	}

	public void setTableResize(boolean tableResize) {
		this.tableResize = tableResize;
		setNativeTableResize(tableResize);
	}
	
	/**
	 * Load the toolbar from the CKEDITOR.config.toolbar_{name} toolbar
	 * configuration (see CKEditor doc for further details) ; this will unset
	 * any Toolbar set before
	 * 
	 * @param name
	 *            The toolbar name
	 */
	public void setToolbarName(String name) {
		this.toolbarName = name;
	}
	
	/**
	 * Sets the toolbar from an options list ; this will unset any
	 * PRESET_TOOLBAR set before
	 * 
	 * @param t
	 *            Options list
	 */
	public void setToolbar(Toolbar t) {
		toolbarName = null;
		toolbar = t;
	}
	
	/**
	 * Define the editor's background color (uiColor must be defined in CSS
	 * Style)
	 * 
	 * @param uiColor
	 *            The background color
	 */
	public void setUiColor(String uiColor) {
		this.uiColor = uiColor;
		setNativeUiColor(uiColor);
	}

	/**
	 * Define if the ckEditor instance should have the focus on startup or not
	 * 
	 * @param focus
	 *            The toolbar name
	 */
	public void setFocusOnStartup(boolean focus) {
		setNativeFocusOnStartup(focus);
		this.focusOnStartup = focus;
	}
	
	/**
	 * Set the height of the editing area (relative or absolute)
	 * 
	 * @param height
	 *            The editing area height
	 */
	public void setHeight(String height) {
		this.height = height;
		setNativeHeight(height);
	}

	/**
	 * Set the width of the editor (relative or absolute)
	 * 
	 * @param width
	 *            The editor width
	 */
	public void setWidth(String width) {
		this.width = width;
		setNativeWidth(width);
	}
	
	/**
	 * Defines the base Z-index for floating dialogs and popups.
	 * 
	 * @param zIndex
	 *            The base Z-index for floating dialogs and popups.
	 */
	public void setBaseFloatZIndex(int zIndex) {
		baseFloatZIndex = zIndex;
		setNativeBaseFloatZIndex(zIndex);
	}
	
	/**
	 * The user interface language localization to use. If empty, the editor
	 * automatically localize the editor to the user language, if supported,
	 * otherwise the CKEDITOR.config.defaultLanguage language is used.
	 * 
	 * @param language
	 */
	public void setLanguage(String language) {
		this.language = language;
		setNativeLanguage(language);
	}
	
	/**
	 * Define if the editor can be resized or not
	 * 
	 * @param resizeEnabled
	 */
	public void setResizeEnabled(boolean resizeEnabled) {
		this.resizeEnabled = resizeEnabled;
		setNativeResizeEnabled(resizeEnabled);
	}
	
	
	/**
	 * The minimum editor width, in pixels, when resizing it with the resize
	 * handle.
	 * 
	 * @param resizeMinWidth
	 */
	public void setResizeMinWidth(int resizeMinWidth) {
		this.resizeMinWidth = resizeMinWidth;
		setNativeMinWidth(resizeMinWidth);
	}

	/**
	 * The minimum editor height, in pixels, when resizing it with the resize
	 * handle.
	 * 
	 * @param resizeMinHeight
	 */
	public void setResizeMinHeight(int resizeMinHeight) {
		this.resizeMinHeight = resizeMinHeight;
		setNativeMinHeight(resizeMinHeight);
	}
	
	/**
	 * The maximum editor width, in pixels, when resizing it with the resize
	 * handle.
	 * 
	 * @param resizeMaxWidth
	 */
	public void setResizeMaxWidth(int resizeMaxWidth) {
		this.resizeMaxWidth = resizeMaxWidth;
		setNativeMaxWidth(resizeMaxWidth);
	}
	
	/**
	 * The maximum editor height, in pixels, when resizing it with the resize
	 * handle.
	 * 
	 * @param resizeMaxHeight
	 */
	public void setResizeMaxHeight(int resizeMaxHeight) {
		this.resizeMaxHeight = resizeMaxHeight;
		setNativeMaxHeight(resizeMaxHeight);
	}

	/**
	 * Whether to use HTML entities in the output.
	 * 
	 * @param entities
	 */
	public void setEntities(boolean entities) {
		this.entities = entities;
		setNativeEntities(entities);
	}

	/**
	 * Whether to convert some symbols, mathematical symbols, and Greek letters
	 * to HTML entities. This may be more relevant for users typing text written
	 * in Greek. The list of entities can be found at the W3C HTML 4.01
	 * Specification, section 24.3.1.
	 * 
	 * @param entitiesGreek
	 */
	public void setEntities_greek(boolean entitiesGreek) {
		entities_greek = entitiesGreek;
		setNativeEntitiesGreek(entitiesGreek);
	}

	/**
	 * Whether to convert some Latin characters (Latin alphabet No. 1, ISO
	 * 8859-1) to HTML entities. The list of entities can be found at the W3C
	 * HTML 4.01 Specification, section 24.2.1.
	 * 
	 * @param entitiesLatin
	 */
	public void setEntities_latin(boolean entitiesLatin) {
		entities_latin = entitiesLatin;
		setNativeEntitiesLatin(entitiesLatin);
	}
	
	/**
	 * The characters to use when occurs a breakLine (e.g: "\n")
	 * 
	 * @param breakLineChars
	 */
	public void setBreakLineChars(String breakLineChars) {
		this.breakLineChars = breakLineChars;
		
	}
	
	/**
	 * The tabIndex for tinyMCE instance
	 * 
	 * @param tabIndex
	 */
	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
		setNativeTabIndex(tabIndex);
	}
	
	
	/**
	 * The way a tag self Close himself (e.g : "/>" or " />"
	 * 
	 * @param selfClosingEnd
	 */
	public void setSelfClosingEnd(String selfClosingEnd) {
		this.selfClosingEnd = selfClosingEnd;
	}
	
	
	/**
	 * What kind of tag will be written when pressing Enter (p|div|br)
	 * 
	 * @param enterMode
	 */
	public void setEnterMode(String enterMode) {
		this.enterMode = enterMode;
		setNativeEnterMode(enterMode);
	}

  public void setIgnoreEmptyParagraph(boolean ignoreEmptyParagraph) {
    this.ignoreEmptyParagraph = ignoreEmptyParagraph;
    setNativeIgnoreEmptyParagraph(ignoreEmptyParagraph);
  }

  public void setForcePasteAsPlainText(boolean forcePasteAsPlainText) {
    this.forcePasteAsPlainText = forcePasteAsPlainText;
    setNativeForcePasteAsPlainText(forcePasteAsPlainText);
  }

	/**
	 * Add an extra plugin to the Editor's configuration
	 * 
	 * @param plugin
	 *            Plugin to add
	 */
	public void addExtraPlugin(AVAILABLE_PLUGINS plugin) {
		addExtraPlugin(plugin.toString());
	}

	/**
	 * Add an extra plugin to the Editor's configuration
	 * 
	 * @param plugin
	 *            Plugin to add
	 */
	public void addExtraPlugin(String plugin) {
		if (!extraPlugins.contains(plugin)) {
			extraPlugins.add(plugin);
			applyExtraPlugin();
		}
	}

	public void setToolbarCanCollapse(boolean canCollapse) {
		toolbarCanCollapse = canCollapse;
		setNativeToolbarCanCollapse(toolbarCanCollapse);
	}

	/**
	 * Defines the id of the top common toolbar div
	 * 
	 * @param divId
	 */
	public void setTopSharedSpace(String divId) {
		this.topSharedSpace = divId;
		setNativeTopSharedSpace(divId);
	}

	/**
	 * Defines the is of the bottom common toolbar div
	 * 
	 * @param divId
	 */
	public void setBottomSharedSpace(String divId) {
		this.bottomSharedSpace = divId;
		setNativeBottomSharedSpace(divId);
	}

	private void applyExtraPlugin() {
		if (!extraPlugins.isEmpty()) {
			String extraPlugin = extraPlugins.get(0);
			for (int i = 1; i < extraPlugins.size(); i++) {
				extraPlugin = extraPlugin.concat("," + extraPlugins.get(i));
			}
			setNativeExtraPlugins(extraPlugin);
		}
	}

	/**
	 * Returns a CKEDITOR.config object with the defined configuration
	 * 
	 * @return a CKEDITOR.config object
	 */
	public JavaScriptObject getConfigObject() {
		if (toolbarName != null) {
			setToolbarNameObject(toolbarName);
		} else {
			setToolbarObject(toolbar.getRepresentation());
		}
		return config;
	}

  public List<CKEditorPlugin> getPlugins() {
    return plugins;
  }

  public void setRemoveFormatTags(String removeFormatTags) {
    setNativeRemoveFormatTags(removeFormatTags);
  }

  private native void setNativeRemoveFormatTags(String removeFormatTags)/*-{
    this.@com.gmail.kompotik.gwtck.client.CKConfig::config.removeFormatTags = removeFormatTags;
  }-*/;

  public void setRemoveFormatAttributes(String removeFormatAttributes) {
    setNativeRemoveFormatAttributes(removeFormatAttributes);
  }

  private native void setNativeRemoveFormatAttributes(String removeFormatAttributes)/*-{
    this.@com.gmail.kompotik.gwtck.client.CKConfig::config.removeFormatAttributes = removeFormatAttributes;
  }-*/;

  public void setFormatTags(String tags) {
    setNativeFormatTags(tags);
  }

  private native void setNativeFormatTags(String tags)/*-{
    this.@com.gmail.kompotik.gwtck.client.CKConfig::config.format_tags = tags;
	}-*/;

  public void setContentsCss(String contentsCss) {
    setNativeContentsCss(contentsCss);
  }

  private native void setNativeContentsCss(String contentsCss)/*-{
    this.@com.gmail.kompotik.gwtck.client.CKConfig::config.contentsCss = contentsCss;
	}-*/;

  public void setStylesComboStyleCombo(String path) {
    setNativeStyleCombo(path);
  }

  private native void setNativeStyleCombo(String path)/*-{
    this.@com.gmail.kompotik.gwtck.client.CKConfig::config.stylesCombo_stylesSet = path;
	}-*/;

  public void setFormat(String element, String className) {
    setNativeFormat(element, className);
  }

  private native void setNativeFormat(String elementName, String className)/*-{
    this.@com.gmail.kompotik.gwtck.client.CKConfig::config.format_bb = "{ element : 'p', attributes : { class : 'normalPara1' } }";
    this.@com.gmail.kompotik.gwtck.client.CKConfig::config.format_p = "{ element : 'p' }";
  }-*/;

	private native void setNativeFileBrowserBrowseUrl(String browseUrl)/*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.filebrowserBrowseUrl = browseUrl;
	}-*/;

	private native void setNativeFileBrowserImageBrowseUrl(String browseUrl) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.filebrowserImageBrowseUrl = browseUrl;
	}-*/;

	private native void setNativeFileBrowserImageBrowseLinkUrl(String browseUrl) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.filebrowserImageBrowseLinkUrl = browseUrl;
	}-*/;

	private native void setNativeEnterMode(String enterMode)/*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.enterMode = enterMode;
	}-*/;

	private native void setNativeIgnoreEmptyParagraph(boolean ignoreEmptyParagraph)/*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.ignoreEmptyParagraph = ignoreEmptyParagraph;
	}-*/;

  private native void setNativeForcePasteAsPlainText(boolean forcePasteAsPlainText)/*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.forcePasteAsPlainText = forcePasteAsPlainText;
	}-*/;
	
	private native void setToolbarNameObject(String name) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.toolbar = name;
	}-*/;
	
	private native void setNativeUiColor(String uiColor) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.uiColor = uiColor;
	}-*/;
	
	private native void setNativeHeight(String height) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.height = height;
	}-*/;
	
	private native void setNativeWidth(String width) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.width = width;
	}-*/;
	
	private native void setNativeBaseFloatZIndex(int zIndex) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.baseFloatZIndex = zIndex;
	}-*/;
	
	private native void setNativeLanguage(String language) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.language = language;
	}-*/;

	private native void setNativeResizeEnabled(boolean resizeEnabled) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.resize_enabled = resizeEnabled;
	}-*/;

	
	private native void setNativeMaxWidth(int width) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.resize_maxWidth = width;
	}-*/;
	
	private native void setNativeMinWidth(int width) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.resize_minWidth = width;
	}-*/;
	
	private native void setNativeMaxHeight(int height) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.resize_maxHeight = height;
	}-*/;
	
	private native void setNativeMinHeight(int height) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.resize_minHeight = height;
	}-*/;
	
	private native void setNativeEntities(boolean entities) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.entities = entities;
	}-*/;
	
	private native void setNativeEntitiesGreek(boolean entitiesGreek) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.entities_greek = entitiesGreek;
	}-*/;
	
	private native void setNativeEntitiesLatin(boolean entitiesLatin) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.entities_latin = entitiesLatin;
	}-*/;
	
	private native void setToolbarObject(JavaScriptObject toolbarArray) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.toolbar_temp = toolbarArray;
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.toolbar = 'temp';
	}-*/;

	private native void setNativeTableResize(boolean tableResize) /*-{
		if (tableResize) {
			this.@com.gmail.kompotik.gwtck.client.CKConfig::config.extraPlugins = 'tableresize';
		} else {
			this.@com.gmail.kompotik.gwtck.client.CKConfig::config.extraPlugins = '';
		}
	}-*/;
	private native void setNativeTabIndex(int tabIndex) /*-{
	  this.@com.gmail.kompotik.gwtck.client.CKConfig::config.tabIndex = tabIndex;
    }-*/;

	private native void setNativeFocusOnStartup(boolean focus) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.startupFocus = focus;
	}-*/;

	private native void setNativeFilebrowserFlashBrowseUrl(String fileBrowserFlashBrowserUrl) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.fileBrowserFlashBrowserUrl = fileBrowserFlashBrowserUrl;
	}-*/;

	private native void setNativeFilebrowserUploadUrl(String filebrowserUploadUrl) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.filebrowserUploadUrl = filebrowserUploadUrl;
	}-*/;

	private native void setNativeFilebrowserImageUploadUrl(String filebrowserImageUploadUrl) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.filebrowserImageUploadUrl = filebrowserImageUploadUrl;
	}-*/;

	private native void setNativeFilebrowserFlashUploadUrl(String filebrowserFlashUploadUrl) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.filebrowserFlashUploadUrl = filebrowserFlashUploadUrl;
	}-*/;

	private native void addNativeFontName(String fontName) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.font_names = this.@com.gmail.kompotik.gwtck.client.CKConfig::config.font_names
				+ ";" + fontName;
	}-*/;

	private native void setNativeExtraPlugins(String extraPlugin) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.extraPlugins = extraPlugin;
	}-*/;

	private native void setNativeTopSharedSpace(String divId) /*-{
		if (this.@com.gmail.kompotik.gwtck.client.CKConfig::config.sharedSpaces == null) {
			this.@com.gmail.kompotik.gwtck.client.CKConfig::config.sharedSpaces = {};
		}
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.sharedSpaces.top = divId;
	}-*/;

	private native void setNativeBottomSharedSpace(String divId) /*-{
		if (this.@com.gmail.kompotik.gwtck.client.CKConfig::config.sharedSpaces == null) {
			this.@com.gmail.kompotik.gwtck.client.CKConfig::config.sharedSpaces = {};
		}
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.sharedSpaces.bottom = divId;
	}-*/;

	private native void setNativeToolbarCanCollapse(boolean canCollapse) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.toolbarCanCollapse = canCollapse;
	}-*/;

	/**
	 * Clear all user-defined font_names
	 */
	public native void clearFontNames() /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.font_names = "";
	}-*/;

	private void applyFontNames() {
		clearFontNames();
		for (String fontName : fontNames) {
			addNativeFontName(fontName);
		}
	}

	private native void addNativeFontSize(String fontSize) /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.fontSize_sizes = this.@com.gmail.kompotik.gwtck.client.CKConfig::config.fontSize_sizes
				+ ";" + fontSize;
	}-*/;

	/**
	 * Clear all user-defined font_sizes
	 */
	public native void clearFontSizes() /*-{
		this.@com.gmail.kompotik.gwtck.client.CKConfig::config.fontSize_sizes = "";
	}-*/;

	private void applyFontSizes() {
		clearFontSizes();
		for (String fontSize : fontSizes) {
			addNativeFontSize(fontSize);
		}
	}

  public native String getBasePath() /*-{
    return this.@com.gmail.kompotik.gwtck.client.CKConfig::config.basePath;
	}-*/;

  public void addPlugin(CKEditorPlugin plugin) {
    if (!pluginExists(plugin.getPluginName())) {
      addPluginNative(plugin.getPluginName(), plugin.getNamespaceName(), plugin.getIconPath(), plugin.getLabel());
    }
    plugins.add(plugin);
    if (plugin.labelShouldBeVisible() || !plugin.iconShouldBeVisible()) {
      addLabelVisibility(plugin.getPluginName(), plugin.labelShouldBeVisible(), plugin.iconShouldBeVisible());
    }
    setNativeExtraPlugins(getPluginsConcat());
  }

  private String getPluginsConcat() {
    String result = "";
    if (plugins.size() == 0) {
      return result;
    }
    for (CKEditorPlugin plugin : plugins) {
      result += plugin.getPluginName() + ",";
    }
    return result.substring(0, result.length() - 1);
  }

  private native boolean pluginExists(String name) /*-{
    if ($wnd.CKEDITOR.plugins.get(name) != null)
      return true;
    else
      return false;
  }-*/;

  private native void addLabelVisibility(String pluginName, boolean labelShouldBeVisible, boolean iconShouldBeVisible) /*-{
    var div = $wnd.document.createElement("div");
    var result = "<p>x</p><style type=\"text/css\">";
    if (labelShouldBeVisible) {
      result += 'div.cke_toolbox .cke_button .cke_button_' + pluginName + ' .cke_label{display:inline;display:inline-block;}';
    }
    if (!iconShouldBeVisible) {
      result += 'div.cke_toolbox .cke_button .cke_button_' + pluginName + ' .cke_icon{display:none;}';
    }
    div.innerHTML = result + "</style>";
    $wnd.document.body.appendChild(div.getElementsByTagName('style')[0]);
  }-*/;

  private native void addPluginNative(String name, String nameSpace, String iconPath, String label) /*-{
    $wnd.CKEDITOR.plugins.add(name,{
        init:function(e){
//            console.log("addPluginNative", e);
            var cmd = e.addCommand(name, {exec:function() { e[nameSpace][name](); }})
            cmd.modes = { wysiwyg:1, source:0 }
            cmd.canUndo = true
            e.ui.addButton(name, {
                label: label,
                command: name,
                icon: iconPath
              }
            )
        }
    });
  }-*/;

	/**
	 * Returns the config height
	 * 
	 * @return height
	 */
	public String getHeight() {
		return height;
	}

	/**
	 * Returns the config width
	 * 
	 * @return width
	 */
	public String getWidth() {
		return width;
	}

	public String getUiColor() {
		return uiColor;
	}

	public int getResizeMinWidth() {
		return resizeMinWidth;
	}

	public int getResizeMinHeight() {
		return resizeMinHeight;
	}

	public boolean isFocusOnStartup() {
		return focusOnStartup;
	}
	public int getResizeMaxWidth() {
		return resizeMaxWidth;
	}

	public int getResizeMaxHeight() {
		return resizeMaxHeight;
	}

	public int getBaseFloatZIndex() {
		return baseFloatZIndex;
	}

	public String getLanguage() {
		return language;
	}

	public boolean isEntities() {
		return entities;
	}

	public boolean isEntities_greek() {
		return entities_greek;
	}

	public boolean isEntities_latin() {
		return entities_latin;
	}

	public String getBreakLineChars() {
		return breakLineChars;
	}

	public String getSelfClosingEnd() {
		return selfClosingEnd;
	}

	public String getEnterMode() {
		return enterMode;
	}

  public boolean getIgnoreEmptyParagraph() {
    return ignoreEmptyParagraph;
  }

  public boolean getForcePasteAsPlainText() {
    return forcePasteAsPlainText;
  }

  public boolean isResizeEnabled() {
		return resizeEnabled;
	}

	public void setUseFormPanel(boolean useFormPanel) {
		this.useFormPanel = useFormPanel;
	}

	public boolean isUsingFormPanel() {
		return useFormPanel;
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public void setFilebrowserFlashBrowseUrl(String filebrowserFlashBrowseUrl) {
		this.filebrowserFlashBrowseUrl = filebrowserFlashBrowseUrl;
		setNativeFilebrowserFlashBrowseUrl(filebrowserFlashBrowseUrl);
	}
	
	public void setFilebrowserUploadUrl(String filebrowserUploadUrl) {
		this.filebrowserUploadUrl = filebrowserUploadUrl;
		setNativeFilebrowserUploadUrl(filebrowserUploadUrl);
	}
	
	public void setFilebrowserImageUploadUrl(String filebrowserImageUploadUrl) {
		this.filebrowserImageUploadUrl = filebrowserImageUploadUrl;
		setNativeFilebrowserImageUploadUrl(filebrowserImageUploadUrl);
	}
	
	public void setFilebrowserFlashUploadUrl(String filebrowserFlashUploadUrl) {
		this.filebrowserFlashUploadUrl = filebrowserFlashUploadUrl;
		setNativeFilebrowserFlashUploadUrl(filebrowserFlashUploadUrl);
	}
	
	public String getFileBrowserBrowseUrl() {
		return fileBrowserBrowseUrl;
	}

	public String getFileBrowserImageBrowseUrl() {
		return fileBrowserImageBrowseUrl;
	}

	public String getFileBrowserImageBrowseLinkUrl() {
		return fileBrowserImageBrowseLinkUrl;
	}

	public String getFilebrowserFlashBrowseUrl() {
		return filebrowserFlashBrowseUrl;
	}

	public String getFilebrowserUploadUrl() {
		return filebrowserUploadUrl;
	}

	public String getFilebrowserImageUploadUrl() {
		return filebrowserImageUploadUrl;
	}

	public String getFilebrowserFlashUploadUrl() {
		return filebrowserFlashUploadUrl;
	}

	/**
	 * Id of common top toolbar div
	 * 
	 * @return
	 */
	public String getTopSharedSpace() {
		return topSharedSpace;
	}

	/**
	 * Id of common bottom toolbar div
	 * 
	 * @return
	 */
	public String getBottomSharedSpace() {
		return bottomSharedSpace;
	}

	/**
	 * Adds a font_name to the font_names list
	 * 
	 * @param fontName
	 */
	public void addFontName(String fontName) {
		fontNames.add(fontName);
		applyFontNames();
	}

	/**
	 * Adds a font_size to the fontSize_sizes list
	 * 
	 * @param fontSize
	 *            The size string, formatted as
	 *            "label/{font-size in CSS style}". example : '16/16px'.
	 */
	public void addFontSize(String fontSize) {
		fontSizes.add(fontSize);
		applyFontSizes();
	}

	/**
	 * @return the max time between two key pressed before launching a save
	 *         event.
	 */
	public int getAutoSaveLatencyInMillis() {
		return autoSaveLatencyInMillis;
	}

	/**
	 * Define the max time between two key pressed before launching a save
	 * event. Set it to 0 if you don't want to use auto-save (default).
	 * 
	 * @param autoSaveLatencyInMillis
	 */
	public void setAutoSaveLatencyInMillis(int autoSaveLatencyInMillis) {
		this.autoSaveLatencyInMillis = autoSaveLatencyInMillis;
	}
	
}
