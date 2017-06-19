package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import datamodel.Fach;
import datamodel.Raum;

@SuppressWarnings("serial")
public class SubjectFrame extends JDialog {
	private JPanel panel;
	
	private JLabel subjectNoLabel;
	private JTextField subjectNo;
	private JLabel subjectNameLabel;
	private JTextField subjectName;
	
	private JButton delete;
	private JButton save;
	
	public SubjectFrame(final GUI gui) {
		super();
		
		setBounds(500, 100, 150, 300);
		setTitle("Raum");
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		getContentPane().add(panel);
		
//		init
		subjectNoLabel = new JLabel("Fachnummer");
		subjectNo = new JTextField(3);
		subjectNameLabel = new JLabel("FachBezeichnung");
		subjectName = new JTextField(3);

		delete = new JButton("Loeschen");
		save = new JButton("Speichern");
//		events
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Fach r = new Fach(Integer.parseInt(subjectNo.getText()), subjectName.getText());
				gui.getVerwaltung().getDaten().getFaecher().add(r);
				gui.addSubject(r);

				setVisible(false);
				dispose();
			}
		});
		
//		view
		panel.add(subjectNoLabel);
		panel.add(subjectNo);
		panel.add(subjectNameLabel);
		panel.add(subjectName);
		panel.add(save);
	}
	
	public SubjectFrame(final GUI gui, final Fach r) {
		this(gui);
		
		subjectNo.setText(String.valueOf(r.getvNr()));
		subjectName.setText(String.valueOf(r.getBezeichnung()));
//		events
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				r.setvNr(Integer.parseInt(subjectNo.getText()));
				r.setBezeichnung(subjectName.getText());

				setVisible(false);
				dispose();
			}
		});
				
		panel.add(delete);
	}
}
