package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import controller.Verwaltung;
import datamodel.Dozent;
import datamodel.Fach;
import datamodel.Klausur;
import datamodel.Raum;

@SuppressWarnings("serial")
public class ExamFrame extends JFrame {
	private final Verwaltung verwaltung;
	private final int MAXSTUDENTS = 120;
	private final int MINSTUDENTS = 1;
	
	private JPanel panel;

//	private JPanel testPanel;
//	FlowLayout testLayout = new FlowLayout();
	
	private JLabel examSemesterLabel;
	private JComboBox examSemester;
	
	private JLabel examPrfTypLabel;
	private JComboBox examPrfTyp;
	
	private JLabel examSubjectsLabel;
	private JComboBox comboSubjects;
	
	private JLabel examRoomsLabel;
	private JList examRoomsLabelList;
	private LinkedList<Raum> examRooms;
	private JComboBox comboRooms;
	private JButton examRoomRem;
	private JButton examRoomAdd;
	private JLabel examProfsLabel;
	private JList examProfsLabelList;
	private LinkedList<Dozent> examProfs;
	private JComboBox comboProfs;
	private JButton examProfRem;
	private JButton examProfAdd;
	private JLabel examStudentsLabel;
	private JTextField examStudents;
	private JLabel examTimeLabel;
	private JTextField examTime;
	private JButton save;
	
	private LinkedList<Fach> subjectList;
	private LinkedList<Dozent> profList;
	private LinkedList<Raum> roomList;

	public ExamFrame(final GUI gui, final Klausur exam) {
		super();

		boolean edit = false;
		if (exam != null)
			edit  = true;
		
		verwaltung = gui.getVerwaltung();
		setBounds(500, 100, 300, 600);
		setTitle("Klausur bearbeiten");
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

//		testPanel = new JPanel();
//		testPanel.setLayout(new FlowLayout());
//		testLayout.setAlignment(FlowLayout.TRAILING);
		
		getContentPane().add(panel);
		
//		init components
		examSemesterLabel = new JLabel("Semester");
		String[] semester = { "1", "2", "3", "4", "5", "6" };
		examSemester = new JComboBox(semester);
		if (edit == true)
			examSemester.setSelectedItem(String.valueOf(exam.getSemester()));

		examPrfTypLabel = new JLabel("Pruefungs-Typ");
		String[] prTyp = {"mdl.", "schriftl."};
		examPrfTyp = new JComboBox(prTyp);
		if (edit == true)
			examPrfTyp.setSelectedItem(exam.getTyp());
		
		if(exam == null) {
			examRooms = new LinkedList<Raum>();
			examProfs = new LinkedList<Dozent>();
		} else {
			examRooms = exam.getRaeume();
			examProfs = exam.getDozenten();
		}
		
		subjectList = gui.getVerwaltung().getDaten().getFaecher();
		comboSubjects = new JComboBox(subjectList.toArray());
		examSubjectsLabel = new JLabel("Fach");
		if (edit == true)
			comboSubjects.setSelectedItem(exam.getFach());
		
		roomList = gui.getVerwaltung().getDaten().getRaeume();
		comboRooms = new JComboBox(roomList.toArray());
		examRoomsLabel = new JLabel("Raeume");
		examRoomsLabelList = new JList(examRooms.toArray());
		
		examRoomRem = new JButton("-");
		examRoomAdd = new JButton("+");
		
		profList = gui.getVerwaltung().getDaten().getDozenten();
		comboProfs = new JComboBox(profList.toArray());
		examProfsLabel = new JLabel("Dozenten");
		examProfsLabelList = new JList(examProfs.toArray());

		
		examProfRem = new JButton("-");
		examProfAdd = new JButton("+");

		examStudentsLabel = new JLabel("Teilnehmer");
		if (edit == true)
			examStudents = new JTextField(String.valueOf(exam.getAnzStudenten()), 3);
		else
			examStudents = new JTextField(3);

		examTimeLabel = new JLabel("Dauer (min.)");
		if (edit == true)
			examTime = new JTextField(String.valueOf(exam.getLaenge()), 3);
		else
			examTime = new JTextField("90", 3);
		
		save = new JButton("Speichern");

		// init events
		examRoomRem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!examRoomsLabelList.isSelectionEmpty()) {
					examRooms.remove(examRoomsLabelList.getSelectedValue());
					updateData();
				}
			}
		});
		examRoomAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (examRooms.toArray().length < 3 && !examRooms.contains((Raum)comboRooms.getSelectedItem())) {
					examRooms.add((Raum)comboRooms.getSelectedItem());
					updateData();
				}
			}
		});
		
		examProfRem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!examProfsLabelList.isSelectionEmpty()) {
					examProfs.remove(examProfsLabelList.getSelectedValue());
					updateData();
				}
			}
		});
		examProfAdd.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (examProfs.toArray().length < 3 && !examProfs.contains((Dozent)comboProfs.getSelectedItem())) {
						examProfs.add((Dozent)comboProfs.getSelectedItem());
						updateData();
					}
				}
		});
		
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(exam != null) {
					verwaltung.getDaten().deleteKlausur(exam);
				} else {
					if(comboSubjects.getSelectedItem() == null) {
						return;
					}
				}
				boolean isNumber = Pattern.matches("[0-9]*", examStudents.getText());
				int examKapa = 0;
				for (Raum i : examRooms) {
					examKapa += i.getKapazitaet();
				}
				
				if (!examStudents.getText().isEmpty() && isNumber == true && Integer.parseInt(examStudents.getText()) > MINSTUDENTS && Integer.parseInt(examStudents.getText()) < MAXSTUDENTS) {
					if(examKapa > Integer.parseInt(examStudents.getText())) {
						verwaltung.getDaten().addKlausur(new Klausur(
								Integer.parseInt(examTime.getText()), 				
								String.valueOf(examPrfTyp.getSelectedItem()),												 
								Integer.parseInt(String.valueOf(examSemester.getSelectedItem())), 
								Integer.parseInt(examStudents.getText()), 
								examRooms,
								examProfs, 
								(Fach) comboSubjects.getSelectedItem()));
						gui.reloadData();
						setVisible(false);
						dispose();
					}else {
						JOptionPane.showMessageDialog(null,
	                            "Die der Kapazit\u00e4t der gew\u00e4hlten R\u00e4ume reicht nicht aus ",
	                            "Ung\u00fcltige Eingabe",                                       
	                            JOptionPane.WARNING_MESSAGE);
					}
				}else {
					JOptionPane.showMessageDialog(null,
                            "Anzahl der Studenten muss zwischen "
							+ MINSTUDENTS + " und " + MAXSTUDENTS + " liegen!",
                            "Ung\u00fcltige Eingabe",                                       
                            JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		// init layout
//		testPanel.add(examSemesterLabel);
//		testPanel.add(examSemester);
//		testPanel.add(examPrfTypLabel);
//		testPanel.add(examPrfTyp);
		
		panel.add(examSemesterLabel);
		panel.add(examSemester);

		panel.add(examPrfTypLabel);
		panel.add(examPrfTyp);
		
		panel.add(examSubjectsLabel);
		panel.add(comboSubjects);
		
		panel.add(examRoomsLabel);
		panel.add(examRoomsLabelList);
		panel.add(comboRooms);
		panel.add(examRoomRem);
		panel.add(examRoomAdd);

		panel.add(examProfsLabel);
		panel.add(examProfsLabelList);
		panel.add(comboProfs);
		panel.add(examProfRem);
		panel.add(examProfAdd);

		panel.add(examStudentsLabel);
		panel.add(examStudents);

		panel.add(examTimeLabel);
		panel.add(examTime);

		panel.add(save);
	}
	
	private void updateData() {
		examRoomsLabelList.setListData(examRooms.toArray());
		examProfsLabelList.setListData(examProfs.toArray());
	}
}
