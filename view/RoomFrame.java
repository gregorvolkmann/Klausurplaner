package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import datamodel.Raum;

@SuppressWarnings("serial")
public class RoomFrame extends JDialog {
	private JPanel panel;
	
	private JLabel roomNoLabel;
	private JTextField roomNo;
	private JLabel roomSizeLabel;
	private JTextField roomSize;
	
	private JButton delete;
	private JButton save;
	
	public RoomFrame(final GUI gui) {
		super();
		
		setBounds(500, 100, 150, 300);
		setTitle("Raum");
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		getContentPane().add(panel);
		
//		init
		roomNoLabel = new JLabel("Raumnummer");
		roomNo = new JTextField(3);
		roomSizeLabel = new JLabel("Raumkapazitaet");
		roomSize = new JTextField(3);

		delete = new JButton("Loeschen");
		save = new JButton("Speichern");
//		events
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Raum r = new Raum(Integer.parseInt(roomNo.getText()), Integer.parseInt(roomSize.getText()));
				gui.getVerwaltung().getDaten().getRaeume().add(r);
				gui.addRoom(r);

				setVisible(false);
				dispose();
			}
		});
		
//		view
		panel.add(roomNoLabel);
		panel.add(roomNo);
		panel.add(roomSizeLabel);
		panel.add(roomSize);
		panel.add(save);
	}
	
	public RoomFrame(final GUI gui, final Raum r) {
		this(gui);
		
		roomNo.setText(String.valueOf(r.getNummer()));
		roomSize.setText(String.valueOf(r.getKapazitaet()));
//		events
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				r.setNummer(Integer.parseInt(roomNo.getText()));
				r.setKapazitaet(Integer.parseInt(roomSize.getText()));

				setVisible(false);
				dispose();
			}
		});
				
		panel.add(delete);
	}
}
