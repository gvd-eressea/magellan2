/*
 *  Copyright (C) 2000-2004 Roger Butenuth, Andreas Gampe,
 *                          Stefan Goetz, Sebastian Pappert,
 *                          Klaas Prause, Enno Rehling,
 *                          Sebastian Tusk, Ulrich Kuester,
 *                          Ilja Pavkovic
 *
 * This file is part of the Eressea Java Code Base, see the
 * file LICENSING for the licensing information applying to
 * this file.
 *
 */

package magellan.client.preferences;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import magellan.client.swing.InternationalizedPanel;
import magellan.client.swing.preferences.ExtendedPreferencesAdapter;
import magellan.client.swing.preferences.PreferencesAdapter;
import magellan.library.utils.Resources;


/**
 * DOCUMENT-ME
 *
 * @author $Author: $
 * @version $Revision: 269 $
 */
public class ResourcePreferences extends InternationalizedPanel implements ExtendedPreferencesAdapter {
	private JButton btnAdd = null;
	private JButton btnRemove = null;
	private JButton btnEdit = null;
	private JList lstPaths = null;
	private Properties settings = null;
	private List<PreferencesAdapter> subAdapter;

	/**
	 * Creates a new ResourceSettings object.
	 */
	public ResourcePreferences(Properties settings) {
		this.settings = settings;
		initComponents();
	}

  /**
   * 
   */
	private void initComponents() {
    this.setLayout(new BorderLayout());
    JPanel panel = new JPanel(new java.awt.GridBagLayout());

		lstPaths = new JList(getWrappedURLs(Resources.getStaticPaths())); // later we need to assume that this list's model is a DefaultListModel!
    
		lstPaths.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		GridBagConstraints c = new GridBagConstraints();
		c.gridheight = 3;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 0.1;
		c.weighty = 0.1;
    panel.add(new JScrollPane(lstPaths, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
								 JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), c);

		btnAdd = new JButton(Resources.get("resource.resourcesettings.btn.new.caption"));
		btnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnAddActionPerformed(evt);
				}
			});

		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 0;
		c.fill = java.awt.GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 0, 5, 5);
		c.weightx = 0.0;
		c.weighty = 0.0;
    panel.add(btnAdd, c);

		btnRemove = new JButton(Resources.get("resource.resourcesettings.btn.remove.caption"));
		btnRemove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnRemoveActionPerformed(evt);
				}
			});

		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(0, 0, 5, 5);
    panel.add(btnRemove, c);

		btnEdit = new JButton(Resources.get("resource.resourcesettings.btn.edit.caption"));
		btnEdit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnEditActionPerformed(evt);
				}
			});

		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(0, 0, 5, 5);
		c.anchor = GridBagConstraints.NORTH;
    panel.add(btnEdit, c);
    
    JTextArea comment = new JTextArea(Resources.get("resource.resourcesettings.comment"));
    comment.setEditable(false);
    comment.setWrapStyleWord(true);
    comment.setLineWrap(true);
    comment.setSelectionColor(panel.getBackground());
    comment.setSelectedTextColor(panel.getForeground());
    comment.setRequestFocusEnabled(false);
    comment.setBackground(panel.getBackground());
    comment.setSelectionColor(panel.getBackground());
    comment.setSelectedTextColor(panel.getForeground());
    comment.setFont(new JLabel().getFont());

    add(comment,BorderLayout.NORTH);
    add(panel,BorderLayout.CENTER);
    
    
		subAdapter = new ArrayList<PreferencesAdapter>(1);

		ResourcePathPreferences ppa = new ResourcePathPreferences(settings);
		ppa.addPath("ECheck:", "JECheckPanel.echeckEXE");
		ppa.addPath("Vorlage:", "JVorlage.vorlageFile");
		subAdapter.add(ppa);
		subAdapter.add(new ResourcePlugInPreferences(settings));
	}

  /**
   * @see magellan.client.swing.preferences.ExtendedPreferencesAdapter#getChildren()
   */
	public List<PreferencesAdapter> getChildren() {
		return subAdapter;
	}

  /**
   * 
   */
	private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {
		if(lstPaths.getSelectedValue() == null) {
			return;
		}

		Component parent = this.getTopLevelAncestor();
		URLWrapper w = (URLWrapper) lstPaths.getSelectedValue();

		if((w != null) && (w.url != null)) {
			Object selectionValues[] = { w.toString() };
			String input = (String) JOptionPane.showInputDialog(parent, Resources.get("resource.resourcesettings.msg.edit.text"),
																Resources.get("resource.resourcesettings.msg.edit.title"),
																JOptionPane.PLAIN_MESSAGE, null,
																null, selectionValues[0]);

			if(input != null) {
				if(input.startsWith("http")) {
					try {
						w.url = new URL(input);
					} catch(MalformedURLException mue) {
						JOptionPane.showMessageDialog(parent, Resources.get("resource.resourcesettings.msg.invalidformat.text"));
					}
				} else {
					File f = new File(input);

					if(!f.exists()) {
						if(JOptionPane.showConfirmDialog(parent,
															 Resources.get("resource.resourcesettings.msg.usenonexisting.text"),
															 Resources.get("resource.resourcesettings.msg.usenonexisting.title"),
															 JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
							try {
								w.url = new URL(input);
							} catch(MalformedURLException mue) {
								JOptionPane.showMessageDialog(parent,
															  Resources.get("resource.resourcesettings.msg.invalidformat.text"));
							}
						}
					} else {
						try {
							w.url = f.toURI().toURL();
						} catch(MalformedURLException mue) {
							JOptionPane.showMessageDialog(parent,
														  Resources.get("resource.resourcesettings.msg.invalidformat.text"));
						}
					}
				}
			}
		}
	}

	private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {
		if(this.lstPaths.getSelectedValue() != null) {
			((DefaultListModel) this.lstPaths.getModel()).removeElementAt(this.lstPaths.getSelectedIndex());
		}
	}

	private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {
		URLWrapper urlWrapper = null;
		Component parent = this.getTopLevelAncestor();

		javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
		fc.setFileSelectionMode(javax.swing.JFileChooser.FILES_AND_DIRECTORIES);

		if(fc.showOpenDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
			java.io.File file = fc.getSelectedFile();

			try {
				if(file.exists()) {
					if(file.isDirectory()) {
						urlWrapper = new URLWrapper(file.toURI().toURL());
					} else {
						urlWrapper = new URLWrapper(new URL("jar:" + file.toURI().toString() +
															"!/"));
					}
				} else {
					String name = file.getName();
					String parentName = "";

					if(file.getParentFile() != null) {
						parentName = file.getParentFile().getName();
					}

					if(!name.equals("") && name.equals(parentName)) {
						// in this case the user double clicked a directory instead of just selecting it
						urlWrapper = new URLWrapper(file.getParentFile().toURI().toURL());
					} else {
						JOptionPane.showMessageDialog(parent, Resources.get("resource.resourcesettings.msg.nonexistingfile.text"));
					}
				}
			} catch(MalformedURLException ex) {
				JOptionPane.showMessageDialog(parent,
											  Resources.get("resource.resourcesettings.msg.urlexception.text") + " " +
											  ex.toString());
			}
		}

		((DefaultListModel) this.lstPaths.getModel()).insertElementAt(urlWrapper, 0);
	}

    public void initPreferences() {
        // TODO: implement it
    }

	/**
	 * DOCUMENT-ME
	 */
	public void applyPreferences() {
		Collection<URL> resourcePaths = new LinkedList<URL>();
		ListModel listModel = this.lstPaths.getModel();

		for(int j = 0; j < listModel.getSize(); j++) {
			URLWrapper wrapper = (URLWrapper) listModel.getElementAt(j);

			if((wrapper != null) && (wrapper.url != null)) {
				resourcePaths.add(wrapper.url);
			}
		}

		Resources.setStaticPaths(resourcePaths);
    Resources.storePaths(resourcePaths, this.settings);
	}

	/**
	 * DOCUMENT-ME
	 *
	 * 
	 */
	public Component getComponent() {
		return this;
	}

	/**
	 * DOCUMENT-ME
	 *
	 * 
	 */
	public String getTitle() {
		return Resources.get("resource.resourcesettings.title");
	}

	private DefaultListModel getWrappedURLs(Collection urls) {
		DefaultListModel wrappers = new DefaultListModel();

		for(Iterator iter = urls.iterator(); iter.hasNext();) {
			wrappers.add(wrappers.getSize(), new URLWrapper((URL) iter.next()));
		}

		return wrappers;
	}

	private class URLWrapper {
		/** DOCUMENT-ME */
		public URL url;

		/**
		 * Creates a new URLWrapper object.
		 *
		 * 
		 */
		public URLWrapper(URL url) {
			this.url = url;
		}

		/**
		 * DOCUMENT-ME
		 *
		 * 
		 */
		public String toString() {
			if(url.getProtocol().equals("file")) {
				File f = new File(url.getPath());

				if(f.exists()) {
					return f.toString();
				} else {
					return url.toString();
				}
			} else {
				return url.toString();
			}
		}
	}
}