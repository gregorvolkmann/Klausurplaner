package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import datamodel.Dozent;
import datamodel.Fach;
import datamodel.Raum;
import datamodel.Zeitraum;

public class ProfFrame extends JDialog {
	private JPanel panel;
	
	private JLabel prenameLabel;
	private JTextField prename;
	private JLabel surnameLabel;
	private JTextField surname;
	private JLabel tagLabel;
	private JTextField tag;
	
	private JLabel subjectsLabel;
	private LinkedList<Fach> subjects;
	private JList subjectsList;
	private JComboBox comboSubjects;
	private JButton subjectRem;
	private JButton subjectAdd;
	
	private JLabel timesLabel;
	private LinkedList<Zeitraum> times;
	private JList timesList;
	private JTextField timesStart;
	private JTextField timesEnd;
	private JButton timesRem;
	private JButton timesAdd;
	
	private JButton save;
	
	private ActionListener saveListener;

	public ProfFrame(final GUI gui) {
		super();
		
		setBounds(500, 100, 300, 500);
		setTitle("Dozent");
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		getContentPane().add(panel);
//		init comps
		prenameLabel = new JLabel("Vorname");
		prename = new JTextField(20);
		surnameLabel = new JLabel("Nachname");
		surname = new JTextField(20);
		tagLabel = new JLabel("Kuerzel");
		tag = new JTextField(5);
		
		subjectsLabel = new JLabel("Faecher");
		subjects = new LinkedList<Fach>();
		subjectsList = new JList();
		comboSubjects = new JComboBox(gui.getVerwaltung().getDaten().getFaecher().toArray());
		subjectRem = new JButton("-");
		subjectAdd = new JButton("+");
		
		timesLabel = new JLabel("Zeitraeume");
		times = new LinkedList<Zeitraum>();
		timesList = new JList();
		timesStart = new JTextField("dd.MM.yyyy");
		timesEnd = new JTextField("dd.MM.yyyy");
		timesRem = new JButton("-");
		timesAdd = new JButton("+");
		
		save = new JButton("Speichern");
		
//		init events
		subjectRem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!subjectsList.isSelectionEmpty()) {
					subjects.remove(subjectsList.getSelectedValue());
					reloadData();
				}
			}
		});
		subjectAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!subjects.contains((Fach)comboSubjects.getSelectedItem())) {
					subjects.add((Fach) comboSubjects.getSelectedItem());
					reloadData();
				}
			}
		});
		
		timesRem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!timesList.isSelectionEmpty()) {
					times.remove(timesList.getSelectedValue());
					reloadData();
				}
			}
		});
		timesAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				parse dateStrings
				SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
				boolean startValid = Pattern.matches("[0-9]{1,2}.[0-9]{1,2}.[0-9]{4}", timesStart.getText());
				boolean endValid = Pattern.matches("[0-9]{1,2}.[0-9]{1,2}.[0-9]{4}", timesStart.getText());
				
				Calendar c = Calendar.getInstance();
				Date date = null;
				
				if (startValid == true && endValid == true) {
					try {
						date = df.parse(timesStart.getText());
					} catch (ParseException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					c.setTime(date);
					int yStart = c.get(Calendar.YEAR);
					int mStart = c.get(Calendar.MONTH);
					int dStart = c.get(Calendar.DAY_OF_MONTH);
					
					try {
						date = df.parse(timesEnd.getText());
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					c.setTime(date);
					int yEnd = c.get(Calendar.YEAR);
					int mEnd = c.get(Calendar.MONTH);
					int dEnd = c.get(Calendar.DAY_OF_MONTH);

					times.add(new Zeitraum(new GregorianCalendar(yStart, mStart, dStart), new GregorianCalendar(yEnd, mEnd, dEnd)));
					reloadData();
				}
			}
		});
		saveListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean prenameValid = Pattern.matches("[A-Za-z]*", prename.getText());
				boolean surnameValid = Pattern.matches("[A-Za-z]*", surname.getText());

				if (prenameValid == true && surnameValid == true) {
					Dozent tmpD = new Dozent(prename.getText(), surname.getText(), tag.getText());
					for(Zeitraum z: times) {
						tmpD.addZeitraum(z);
					}
					for(Fach f: subjects) {
						tmpD.addFach(f);
					}
					gui.getVerwaltung().getDaten().getDozenten().add(tmpD);
					gui.addProf(tmpD);
					
					setVisible(false);
					dispose();
				}else {
					JOptionPane.showMessageDialog(null,
                            "Vor- und Nachname d\u00fcrfen nur Buchstaben enthalten",
                            "Ung\u00fcltige Eingabe",                                       
                            JOptionPane.WARNING_MESSAGE);
				}
			}
		};
		save.addActionListener(saveListener);
		
//		show comps
		panel.add(prenameLabel);
		panel.add(prename);
		panel.add(surnameLabel);
		panel.add(surname);
		panel.add(tagLabel);
		panel.add(tag);
		panel.add(subjectsLabel);
		panel.add(subjectsList);
		panel.add(subjectRem);
		panel.add(comboSubjects);
		panel.add(subjectAdd);
		
		panel.add(timesLabel);
		panel.add(timesList);
		panel.add(timesRem);
		panel.add(timesStart);
		panel.add(timesEnd);
		panel.add(timesAdd);
		
		panel.add(save);
	}
	
	public ProfFrame(final GUI gui, final Dozent d) {
		this(gui);
		
		prename.setText(d.getVorname());
		surname.setText(d.getName());
		tag.setText(d.getKuerzel());
		
		subjects = d.getFaecher();
		times = d.getZeitraum();
		
		save.removeActionListener(saveListener);
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				d.setVorname(prename.getText());
				d.setName(surname.getText());
				d.setKuerzel(tag.getText());
				
				d.setZeitraeume(times);
				d.setFaecher(subjects);
				
				setVisible(false);
				dispose();
			}
		});
		
		reloadData();
	}
	
	private void reloadData() {
		subjectsList.setListData(subjects.toArray());
		timesList.setListData(times.toArray());
	}
}