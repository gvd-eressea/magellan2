// class magellan.client.preferences.MapPreferences
// created on 16.02.2008
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
package magellan.client.preferences;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import magellan.client.swing.MapperPanel;
import magellan.client.swing.preferences.ExtendedPreferencesAdapter;
import magellan.client.swing.preferences.PreferencesAdapter;
import magellan.library.event.GameDataEvent;
import magellan.library.utils.Resources;

public class MapPreferences extends JPanel implements ExtendedPreferencesAdapter {

  // The source component to configure
  private MapperPanel source = null;

  // GUI elements
  private PreferencesAdapter prefMapper = null;
  private List<PreferencesAdapter> subAdapter;
  
  private JCheckBox showNavigation;

  /**
   * Creates a new MapperPanelPreferences object.
   * 
   * @param m
   *          DOCUMENT-ME
   */
  public MapPreferences(MapperPanel m) {
    this.source = m;
    prefMapper = source.getMapper().getPreferencesAdapter();

    subAdapter = new ArrayList<PreferencesAdapter>(2);
    subAdapter.add(new MinimapPreferences(source));
  }

  /**
   * DOCUMENT-ME
   * 
   * 
   */
  public List<PreferencesAdapter> getChildren() {
    return subAdapter;
  }

  /**
   * DOCUMENT-ME
   * 
   * 
   */
  public Component getComponent() {
    JPanel erg = new JPanel(new GridBagLayout());
    
    JPanel helperPanel = new JPanel(new BorderLayout());
    
    helperPanel.setBorder(BorderFactory.createTitledBorder(Resources.get("map.mapperpanelpreferences.border.caption")));
    GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST,
        GridBagConstraints.HORIZONTAL,
        new Insets(3, 3, 3, 3), 0, 0);
    
    showNavigation = new JCheckBox(Resources.get("mapperpanel.prefs.details.chk.shownavigation"), source.getContext().getProperties().getProperty("MapperPannel.Details.showNavigation", "true").equals("true"));
    helperPanel.add(showNavigation,BorderLayout.WEST);
    
    erg.add(helperPanel,gbc);
    gbc.gridy++;
    erg.add(prefMapper.getComponent(),gbc);
    erg.validate();
    return erg;
    // return prefMapper.getComponent();
  }

  public void initPreferences() {
    // TODO: implement it
  }

  /**
   * DOCUMENT-ME
   */
  public void applyPreferences() {
    prefMapper.applyPreferences();

    if (showNavigation.isSelected()!= source.getContext().getProperties().getProperty("MapperPannel.Details.showNavigation", "true").equals("true")){
      // we have a change here
      source.getContext().getProperties().setProperty("MapperPannel.Details.showNavigation", showNavigation.isSelected() ? "true" : "false");
      source.getContext().getEventDispatcher().fire(new GameDataEvent(this,source.getGameData()));
    } else {
      source.getMapper().repaint(100);
    }
    
  }

  /**
   * DOCUMENT-ME
   * 
   * 
   */
  public String getTitle() {
    return Resources.get("mapperpanel.prefs.title");
  }
}
