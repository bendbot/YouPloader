/* 
 * YouPloader Copyright (c) 2016 genuineparts (itsme@genuineparts.org)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package at.becast.youploader.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import at.becast.youploader.Main;
import at.becast.youploader.util.DesktopUtil;
import at.becast.youploader.util.UTF8ResourceBundle;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FrmAbout extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8005016163820525203L;
	private final JPanel contentPanel = new JPanel();
	private static final ResourceBundle LANG = UTF8ResourceBundle.getBundle("lang", Locale.getDefault());

	/**
	 * Create the dialog.
	 */
	public FrmAbout() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle(String.format(LANG.getString("About.title"), Main.APP_NAME));
		ResourceBundle bundle = ResourceBundle.getBundle("build");
		String rev = bundle.getString("git-sha-1");
		String build = bundle.getString("jenkins-build");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/yp.png")));
		setBounds(100, 100, 500, 397);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(
				new ColumnSpec[] { ColumnSpec.decode("max(166dlu;default)"), FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("210px:grow"), },
				new RowSpec[] { FormSpecs.UNRELATED_GAP_ROWSPEC, RowSpec.decode("20px"), FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("20px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("20px"),
						FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("20px"), FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("20px"), FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("20px"), FormSpecs.PARAGRAPH_GAP_ROWSPEC, RowSpec.decode("103px"), }));

		JLabel label = new JLabel("");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setIcon(new ImageIcon(getClass().getResource("/yp.png")));
		contentPanel.add(label, "1, 2, 1, 9, left, fill");

		JLabel lblYouploader = new JLabel("YouPloader " + Main.VERSION);
		lblYouploader.setFont(new Font("Arial Black", Font.PLAIN, 11));
		contentPanel.add(lblYouploader, "3, 2, left, default");

		JLabel lblGenuineparts = new JLabel(" © 2016-2018 BB");
		lblGenuineparts.setFont(new Font("Arial Black", Font.PLAIN, 11));
		contentPanel.add(lblGenuineparts, "3, 4, left, default");

		JLabel lblRevision = new JLabel("Revision " + rev);
		lblRevision.setFont(new Font("Arial", Font.PLAIN, 13));
		contentPanel.add(lblRevision, "3, 6");

		JLabel lblBuild = new JLabel("Build " + build);
		lblBuild.setFont(new Font("Arial", Font.PLAIN, 13));
		contentPanel.add(lblBuild, "3, 8");

		JLabel lblHttpsgithubcombecastyouploader = new JLabel("https://github.com/bendbot/YouPloader");
		lblHttpsgithubcombecastyouploader.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				DesktopUtil.openBrowser("https://github.com/bendbot/YouPloader");
			}
		});
		lblHttpsgithubcombecastyouploader.setForeground(Color.BLUE);
		lblHttpsgithubcombecastyouploader.setFont(new Font("Arial", Font.PLAIN, 13));
		contentPanel.add(lblHttpsgithubcombecastyouploader, "3, 10");

		JLabel lblCredits = new JLabel("Credits:");
		contentPanel.add(lblCredits, "1, 14");

		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, "1, 16, 3, 3, fill, fill");

		JTextPane txtCredits = new JTextPane();
		txtCredits.setText(
				"Thanks to everyone who contributed!\r\n\r\nGerman Translation\r\nMeduax - https://github.com/Meduax - https://www.youtube.com/user/Meduax\r\n\r\nTesting:\r\nWePlaydThis\r\n@WePlaydThis\r\nhttps://www.youtube.com/user/WePlaydThis\r\n\r\nTheJessaChannel\r\n@TheJessaChannel\r\nhttps://www.youtube.com/user/TheJessaChannel\r\n\r\nThe kind people of /r/letsplay\r\n");
		txtCredits.setCaretPosition(0);
		txtCredits.setEditable(false);
		scrollPane.setViewportView(txtCredits);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okButton();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

	}

	private void okButton() {
		this.dispose();
	}
}
