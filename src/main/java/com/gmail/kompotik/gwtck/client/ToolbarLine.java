/**
 *  This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.gmail.kompotik.gwtck.client;

import java.util.ArrayList;
import java.util.Collection;

import com.gmail.kompotik.gwtck.client.CKConfig.LINE_TYPE;
import com.gmail.kompotik.gwtck.client.CKConfig.TOOLBAR_OPTIONS;
import com.gmail.kompotik.gwtck.client.plugins.CKEditorPlugin;
import com.google.gwt.core.client.JavaScriptObject;

public class ToolbarLine {
//	private ArrayList<TOOLBAR_OPTIONS> blocks;
	private ArrayList<String> blocksString;

	private LINE_TYPE type = LINE_TYPE.NORMAL;
	
	public ToolbarLine(){
//		blocks = new ArrayList<TOOLBAR_OPTIONS>();
    blocksString = new ArrayList<String>();
	}
	
	public ToolbarLine(LINE_TYPE t){
		this();
		type = t;
	}
	
	public void add(TOOLBAR_OPTIONS t){
		blocksString.add(t.toString());
	}

  public void add(CKEditorPlugin plugin){
		blocksString.add(plugin.getPluginName());
	}
	
	public void addAll(Collection<TOOLBAR_OPTIONS> options){
    for (TOOLBAR_OPTIONS option : options) {
      add(option);
    }
	}
	
	public void addAll(TOOLBAR_OPTIONS[] options){
		for(int i=0;i<options.length;i++){
			blocksString.add(options[i].toString());
		}
	}
	
	public void addBlockSeparator(){
		blocksString.add(TOOLBAR_OPTIONS._.toString());
	}
	
	public Object getRepresentation() {
		if (type == LINE_TYPE.SEPARATOR) {
			return getSeparator();
		} else {
			JavaScriptObject array = JavaScriptObject.createArray();
			for(String opt : blocksString){
				if(opt.equals(TOOLBAR_OPTIONS._.toString()))
					array = addToArray(array,"-");
				else
					array = addToArray(array, opt);
			}
			return array;
		}
	}
	
	private static native String getSeparator() /*-{
		var temp = new String("/");
		return temp;
	}-*/;
	
	private static native JavaScriptObject addToArray(JavaScriptObject base, String option) /*-{
		base[base.length] = option;
		return base;
	}-*/;
	
}