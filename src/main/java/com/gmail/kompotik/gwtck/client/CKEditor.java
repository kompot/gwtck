package com.gmail.kompotik.gwtck.client;

import com.gmail.kompotik.gwtck.client.event.HasSaveHandlers;
import com.gmail.kompotik.gwtck.client.event.SaveEvent;
import com.gmail.kompotik.gwtck.client.event.SaveHandler;
import com.gmail.kompotik.gwtck.client.plugins.CKEditorPlugin;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class CKEditor extends Composite implements HasSaveHandlers<CKEditor>,
    ClickHandler, HasAlignment, HasValueChangeHandlers<String>, HasValue<String> {
  interface Binder extends UiBinder<Widget, CKEditor> { }
  private static Binder binder = GWT.create(Binder.class);

  /**
   * Used for catching Save event
	 * 
   * @param o
   * @return
   */
  private static native String getParentClassname(JavaScriptObject o) /*-{
    	var classname = o.parentNode.getAttribute("class");
		if (classname == null)
    		return o.parentNode.className;
    	return classname;
    }-*/;

	private static class AutoSaveTimer extends Timer {

		protected CKEditor editor;

		public AutoSaveTimer(CKEditor editor) {
			this.editor = editor;
		}

		@Override
		public void run() {
			editor.save();
		}

		public void delay() {
			this.cancel();
			if (editor.config.getAutoSaveLatencyInMillis() > 0) {
				this.schedule(editor.config.getAutoSaveLatencyInMillis());
			}
		}

	}

    protected String name;
    protected JavaScriptObject editor;
    protected Element baseTextArea;
    protected JavaScriptObject dataProcessor;
    protected CKConfig config;
    protected boolean replaced = false;
    protected boolean textWaitingForAttachment = false;
    protected String waitingText;
    protected boolean waitingForDisabling = false;
    protected boolean disabled = false;
    protected Element div;
    protected Node ckEditorNode;
    protected HTML disabledHTML;

	protected AutoSaveTimer autoSaveTimer = new AutoSaveTimer(this);

	protected boolean focused = false;
	protected HorizontalAlignmentConstant hAlign = null;
	protected VerticalAlignmentConstant vAlign = null;


	/**
	 * Creates an editor with the CKConfig.basic configuration. By default, the
	 * CKEditor is enabled in hosted mode ; if not, the CKEditor is replaced by
	 * a simple TextArea
   */
  public CKEditor() {
    this(CKConfig.basic);
  }

  /**
	 * Creates an editor with the given configuration. By default, the CKEditor
	 * is enabled in hosted mode ; if not, the CKEditor is replaced by a simple
	 * TextArea
	 * 
	 * @param config
	 *            The configuration
   */
  public CKEditor(CKConfig config) {
    super();
    this.config = config;
    initCKEditor();
  }

  @Override
  public HandlerRegistration addSaveHandler(SaveHandler<CKEditor> handler) {
    return addHandler(handler, SaveEvent.getType());
  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
    return this.addHandler(handler, ValueChangeEvent.getType());
  }

	private native void destroyInstance()/*-{
		var editor = this.@com.gmail.kompotik.gwtck.client.CKEditor::editor;
		if (editor) {
			editor.destroy();
		}
	}-*/;

	/**
	 * Dispatch a blur CKEditor event to a ValueChangeEvent
	 */
	private void dispatchBlur() {
		ValueChangeEvent.fire(this, this.getHTML());
	}

	private void dispatchKeyPressed() {
		autoSaveTimer.delay();
	}

	@Override
	public HorizontalAlignmentConstant getHorizontalAlignment() {
		return hAlign;
    }

  @Override
  public VerticalAlignmentConstant getVerticalAlignment() {
	return vAlign;
  }

	/**
	 * Returns the editor text
	 * 
	 * @return the editor text
	 */
  public String getHTML() {
    String result = null;
    if (replaced) {
      result = getNativeHTML();
    } else {
      result = waitingText;
    }
    // workaround for http://cksource.com/forums/viewtopic.php?t=18276
    if (result != null && result.length() < 10) {
      result = result.replaceAll("<[bB][rR]\\s*/>", "").trim();
    }
    return result;
  }

  private native String getNativeHTML() /*-{
	  var e = this.@com.gmail.kompotik.gwtck.client.CKEditor::editor;
    return e.getData();
  }-*/;

  public native JavaScriptObject getSelection() /*-{
    var e = this.@com.gmail.kompotik.gwtck.client.CKEditor::editor;
	return e.getSelection();
  }-*/;

  public native String getSelectedHtml() /*-{
    var e = this.@com.gmail.kompotik.gwtck.client.CKEditor::editor;
    var selection = e.getSelection();
    if (selection) {
    var bookmarks = selection.createBookmarks(),
        range = selection.getRanges()[ 0 ],
        fragment = range.clone().cloneContents();

      selection.selectBookmarks( bookmarks );

      var retval = "",
         childList = fragment.getChildren(),
         childCount = childList.count();
      for ( var i = 0; i < childCount; i++ )
      {
         var child = childList.getItem( i );
         retval += ( child.getOuterHtml?
            child.getOuterHtml() : child.getText() );
      }
      return retval;
    }
  }-*/;

  /**
   * Initialize the editor
   */
  private void initCKEditor() {
      div = DOM.createDiv();
    baseTextArea = DOM.createTextArea();
    name = HTMLPanel.createUniqueId();
    div.appendChild(baseTextArea);
    DOM.setElementAttribute(baseTextArea, "name", name);
    this.sinkEvents(Event.ONCLICK | Event.KEYEVENTS);
    SimplePanel simplePanel = new SimplePanel();
	simplePanel.getElement().appendChild(div);
	initWidget(simplePanel);
  }

	/**
	 * Replace the text Area by a CKEditor Instance
	 */
  protected void initInstance() {
		if (!replaced && !disabled) {
      replaced = true;
      replaceTextArea(baseTextArea, this.config.getConfigObject());
      for (CKEditorPlugin plugin : config.getPlugins()) {
        plugin.setCkEditor(this);
      }
			if (textWaitingForAttachment) {
				textWaitingForAttachment = false;
				setHTML(waitingText);
			}
			if (hAlign != null) {
				setHorizontalAlignment(hAlign);
			}
			if (vAlign != null) {
				setVerticalAlignment(vAlign);
			}
			if (this.config.isFocusOnStartup()) {
				this.focused = true;
				setAddFocusOnLoad(focused);
    }
			if (waitingForDisabling) {
				this.waitingForDisabling = false;
				setEnabled(this.disabled);
			}

			listenToBlur();
			listenToKey();
		}
  }

	public boolean isDisabled() {
		return disabled;
	}

	private native void listenToBlur() /*-{
		var me = this;
		var e = this.@com.gmail.kompotik.gwtck.client.CKEditor::editor;
		e.on('blur', function(ev) {
			me.@com.gmail.kompotik.gwtck.client.CKEditor::dispatchBlur()();
		});
	}-*/;

	private native void listenToKey() /*-{
		var me = this;
		var e = this.@com.gmail.kompotik.gwtck.client.CKEditor::editor;
		e.on('key', function(ev) {
			me.@com.gmail.kompotik.gwtck.client.CKEditor::dispatchKeyPressed()();
		});
	}-*/;

	@Override
	public void onClick(ClickEvent event) {
		if (event.getRelativeElement().getAttribute("name").equals("submit")) {
			event.stopPropagation();
			save();
		}
	}

	/**
	 * Dispatch a save event
	 */
	protected void save() {
		SaveEvent.fire(this, this, this.getHTML());
	}

//  @Override
//  protected void onLoad() {
//    final Timer timer = new Timer() {
//      @Override
//      public void run() {
//        initInstance();
//        this.cancel();
//      }
//    };
//    timer.schedule(500);
//  }

  @Override
  protected void onLoad() {
    initInstance();
  }

  private native void replaceTextArea(Object o, JavaScriptObject config) /*-{
    this.@com.gmail.kompotik.gwtck.client.CKEditor::editor = $wnd.CKEDITOR.replace(o, config);
  }-*/;

	private native void setAddFocusOnLoad(boolean focus)/*-{
		var e = this.@com.gmail.kompotik.gwtck.client.CKEditor::editor;
		e.on('dataReady', function(ev) {
			if (focus) {
				e.focus();
				var lastc = e.document.getBody().getLast();
				e.getSelection().selectElement(lastc);
				var range = e.getSelection().getRanges()[0];
				if (range != null) {
					range.collapse(false);
					range.setStart(lastc, range.startOffset);
					try {
						range.setEnd(lastc, range.endOffset);
					} catch (err) {
					}
					range.select();
				}
			}
		});
	}-*/;

	/**
	 * Use to disable CKEditor's instance
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		//FIXME : rework this part to remove the !
		boolean disabled = !enabled;

		if (this.disabled != disabled) {
			this.disabled = disabled;

			if (disabled) {
				ScrollPanel scroll = new ScrollPanel();
				disabledHTML = new HTML();
				disabledHTML.setStyleName("GWTCKEditor-Disabled");
				scroll.setWidget(disabledHTML);

				if (config.getWidth() != null)
					scroll.setWidth(config.getWidth());

				if (config.getHeight() != null)
					scroll.setHeight(config.getHeight());

				String htmlString;

				if (replaced) {
					htmlString = getHTML();
				} else {
					htmlString = waitingText;
				}

				DivElement divElement = DivElement.as(this.getElement().getFirstChildElement());
				Node node = divElement.getFirstChild();
				while (node != null) {
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						com.google.gwt.dom.client.Element element = com.google.gwt.dom.client.Element.as(node);
						if (element.getTagName().equalsIgnoreCase("textarea")) {
							destroyInstance();
							replaced = false;
							divElement.removeChild(node);
							ckEditorNode = node;
						}
					}
					node = node.getNextSibling();
				}
				disabledHTML.setHTML(htmlString);
				div.appendChild(scroll.getElement());

			} else {
				if (ckEditorNode != null) {
					DivElement divElement = DivElement.as(this.getElement().getFirstChildElement());
					Node node = divElement.getFirstChild();
					while (node != null) {
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							com.google.gwt.dom.client.Element element = com.google.gwt.dom.client.Element.as(node);
							if (element.getTagName().equalsIgnoreCase("div")) {
								divElement.removeChild(node);

							}
						}
						node = node.getNextSibling();
					}
					div.appendChild(baseTextArea);
					initInstance();

				}
			}
		}

	}

	/**
	 * Set the focus natively if ckEditor is attached, alerts you if it's not
	 * the case.
	 * 
	 * @param focus
	 */
	public void setFocus(boolean focus) {
		if (replaced == true) {
			setNativeFocus(focus);
		} else {
			Window.alert("You can't set the focus on startup with the method setFocus(boolean focus).\n"
          + "If you want to add focus to your instance on startup, use the config object\n"
          + "with the method setFocusOnStartup(boolean focus) instead.");
		}
	}

  @Override
  public void setSize(String width, String height) {
    super.setSize(width, height);
  }

  @Override
  public void setHeight(String height) {
    config.setHeight(height);
  }

	@Override
	public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
		this.hAlign = align;
		if (replaced)
			this.getElement().getParentElement().setAttribute("align", align.getTextAlignString());
	}

	/**
	 * Set the editor's html
	 * 
	 * @param html
	 *            The html string to set
	 */
  public void setHTML(final String html) {
    if (replaced) {
      setNativeHTML(html);
    } else {
      waitingText = html;
      textWaitingForAttachment = true;
    }
  }

//  public void setHTML(final String html) {
//    final Timer timer = new Timer() {
//      @Override
//      public void run() {
//        if (replaced) {
//          setNativeHTML(html);
//          this.cancel();
//        } else {
//          waitingText = html;
//          textWaitingForAttachment = true;
//          this.schedule(500);
//        }
//      }
//    };
//    timer.schedule(500);
//  }

	private native void setNativeFocus(boolean focus)/*-{
		if (focus) {
			var e = this.@com.gmail.kompotik.gwtck.client.CKEditor::editor;
			if (e) {
				e.focus();

				var lastc = e.document.getBody().getLast();
				e.getSelection().selectElement(lastc);
				var range = e.getSelection().getRanges()[0];
				range.collapse(false);
				range.setStart(lastc, range.startOffset);
				try {
					range.setEnd(lastc, range.endOffset);
				} catch (err) {
				}
				range.select();
			}
		}
	}-*/;

	private native void setNativeHTML(String html) /*-{
	  var e = this.@com.gmail.kompotik.gwtck.client.CKEditor::editor;
	  e.setData(html);
  }-*/;

	@Override
	public void setVerticalAlignment(VerticalAlignmentConstant align) {
		this.vAlign = align;
		if (replaced)
			this.getElement().getParentElement().setAttribute("style", "vertical-align:" + align.getVerticalAlignString());

	}

  @Override
  public void setWidth(String width) {
    config.setWidth(width);
  }

  public native void insertHtml(String text) /*-{
    var e = this.@com.gmail.kompotik.gwtck.client.CKEditor::editor;
    e.insertHtml(text);
  }-*/;

  public native void insertHtmlSimple(String textBeforeSelection, String textAfterSelection) /*-{
    var e = this.@com.gmail.kompotik.gwtck.client.CKEditor::editor;
    e.insertHtml(textBeforeSelection + e.getSelection().getNative() + textAfterSelection);
  }-*/;

  /**
   * Оборачивает все выбранные области в переданные куски html. При этом выбранная область
   * будет добавлена в конец списка детей корневой ноды переданного html.
   *
   * Известный баг: если в выделение попали ноды разных уровней, то метод некорректно найдет
   * стартовую и конечную ноды, контент между которыми нужно заключить в переданный html.
   * Проблема решается просто - нужно за одну итерацию брать одного парента начальной или конечно ноды
   * и смотреть, а не являются ли они братьями. Если являются - все ок, мы нашли все. Если не является,
   * то в худшем случае будет обернут весь контент.
   *
   * @param textBeforeSelection
   * @param textAfterSelection
   */
  public native void insertHtml(String textBeforeSelection, String textAfterSelection) /*-{
    var findValidParent = function () {
      var validTags = {p: 1, div: 1, ul: 1, table: 1};

      return function(startNode){
        var
            validParent = null,
            found = false,
            currentNode = startNode;

        while (!found) {
          if (validTags[currentNode.$.nodeName.toLowerCase()]) {
            found = true;
            validParent = currentNode;
            break;
          }
          currentNode = currentNode.getParent();
        }

        return validParent;
      }
    }();

    var e = this.@com.gmail.kompotik.gwtck.client.CKEditor::editor;
    var ranges = e.getSelection().getRanges();

    var atLeastOneGoodRegion = false;
    for ( var range, i = 0 ; i < ranges.length ; i++ ) {
      range = ranges[i];
      if (range.collapsed)
        continue;

      atLeastOneGoodRegion = true;
      var
        startNode = findValidParent(range.getBoundaryNodes().startNode),
        endNode = findValidParent(range.getBoundaryNodes().endNode),
        wrapper = $wnd.CKEDITOR.dom.element.createFromHtml(textBeforeSelection + textAfterSelection);

      startNode.insertBeforeMe(wrapper);

      var currentNode = startNode.$;
      while (currentNode !== endNode.$) {
        var nextNode = currentNode.nextSibling;
        wrapper.$.appendChild(currentNode);
        currentNode = nextNode;
      }
      wrapper.$.appendChild(endNode.$);
    }

    if (!atLeastOneGoodRegion) {
      e.insertHtml(textBeforeSelection + textAfterSelection);
    }


  }-*/;

  public native void addUiItem(String name, String label, String command) /*-{
    var e = this.@com.gmail.kompotik.gwtck.client.CKEditor::editor;
    e.ui.add(name, CKEDITOR.UI_BUTTON,
      {
        label : label,
        command : command
      });
  }-*/;

  public native void addButton(String name, String label, String command) /*-{
    var e = this.@com.gmail.kompotik.gwtck.client.CKEditor::editor;
    e.ui.addButton(name,
      {
        label : label,
        command : command
      });
  }-*/;

  public native void addPlugin(String name, String callBackFunctionName) /*-{
    $wnd.CKEDITOR.plugins.add(name,{
        init:function(e){
            var cmd = e.addCommand(name, {exec:callBackFunctionName})
            cmd.modes = { wysiwyg:1, source:1 }
            cmd.canUndo = false
            e.ui.addButton(name,
              {
                label:'Upload an Image..',
                command:name,
                icon:this.path+'images/icon.png'
              }
            )
        }
    });
  }-*/;

  public native JavaScriptObject getEditor() /*-{
    return this.@com.gmail.kompotik.gwtck.client.CKEditor::editor;
  }-*/;

  @Override
  public String getValue() {
    return getHTML();
  }

  @Override
  public void setValue(String value) {
    setHTML(value);
  }

  @Override
  public void setValue(String value, boolean fireEvents) {
    setHTML(value);
  }
}
