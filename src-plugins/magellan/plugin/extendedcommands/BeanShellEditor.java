// class magellan.plugin.extendedcommands.BeanShellEditor
// created on 24.02.2008
//
// Copyright 2003-2008 by magellan project team
//
// Author : $Author: $
// $Id: $
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program (see doc/LICENCE.txt); if not, write to the
// Free Software Foundation, Inc., 
// 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
// 
package magellan.plugin.extendedcommands;

import java.awt.Dimension; 
import javax.swing.JEditorPane; 
import javax.swing.JViewport; 
import javax.swing.plaf.TextUI; 
import javax.swing.text.Document; 
import javax.swing.text.EditorKit; 
import javax.swing.text.StyledEditorKit; 

/**
 * A TextArea for BeanShell-Skripts. Including Syntax-Highlighting 
 *
 * @author bodum 
 */
public class BeanShellEditor extends JEditorPane { 
  /**
   * Create the TextArea 
   */ 
  public BeanShellEditor() { 
    setDocument(new BeanShellSyntaxDocument()); 
    EditorKit editorKit = new StyledEditorKit() { 
      public Document createDefaultDocument() { 
        return new BeanShellSyntaxDocument(); 
      } 
    }; 
    setEditorKitForContentType("text/beanshell", editorKit);
    setContentType("text/beanshell"); 
  } 
  
  /**
   * Override to get no Line-Wraps 
   */
  public boolean getScrollableTracksViewportWidth() { 
    if (getParent() instanceof JViewport) { 
      JViewport port = (JViewport) getParent(); 
      TextUI ui = getUI(); 
      int w = port.getWidth(); 
      
      Dimension min = ui.getMinimumSize(this); 
      Dimension max = ui.getMaximumSize(this); 
      Dimension pref = ui.getPreferredSize(this); 
      if ((w >= pref.width)) { 
        return true; 
      } 
    } 
    return false; 
  } 
}