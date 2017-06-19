package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.GregorianCalendar;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.itextpdf.text.DocumentException;
import controller.Verwaltung;
import datamodel.Daten;
import datamodel.Dozent;
import datamodel.Fach;
import datamodel.Raum;
import datamodel.Zeitraum;
import error.ProjektLadenException;

@SuppressWarnings("serial")
public class GUI extends JFrame {
	private final Verwaltung verwaltung;
	private ExamSidebar examSidebar;
	private CalendarTable calendarTable;
	private JMenu profMenu;
	private JMenu roomMenu;
	private JMenu subjectMenu;

	public GUI(final Verwaltung verwaltung) {
		this.verwaltung = verwaltung;

		// init Frame
		setTitle("Klausurplaner");
		setSize(1280, 768);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setJMenuBar(initMenuBar());
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		getContentPane().add(panel);

		// init Calendar
		calendarTable = new CalendarTable(verwaltung.getCal());
		JScrollPane scrollpane = new JScrollPane(calendarTable);
		scrollpane.setPreferredSize(new Dimension(1070, 700));
		panel.add(scrollpane, BorderLayout.CENTER);

		// init ExamList
		examSidebar = new ExamSidebar(verwaltung.getDaten().getKlausuren(),
				this);
		panel.add(examSidebar, BorderLayout.EAST);

		// init Statusbar
		JLabel version = new JLabel(
				"version 1.0   \t\t\t\u00a9Andre P. Benedikt S. Carlo V. Gregor V.");
		panel.add(version, BorderLayout.SOUTH);
		
		reloadData();
	}

	private JMenuBar initMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenuItem menuItem;

		// menu Plan
		JMenu menu = new JMenu("Plan");
		menu.setMnemonic(KeyEvent.VK_P);
		menuBar.add(menu);

		menuItem = new JMenuItem("Neu");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame();
				Object[] option1 = { "Ok", "Abbrechen" };
				int n = JOptionPane.showOptionDialog(frame,
						"Wollen Sie einen neuen Klausurplaner erstellen "
								+ "und den alten Plan verwerfen?",
						"Neuer Klausurplaner erstellen",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE, null, option1, option1[1]);
				if (n == 0) {
					Object[] option2 = { "Ja", "Nein" };
					int m = JOptionPane
							.showOptionDialog(
									frame,
									"Wollen Sie Ihren Klausurplan vorher noch abspeichern?",
									"Klausurplan speichern",
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.WARNING_MESSAGE, null, option2,
									option2[0]);
					if (m == 0) {
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setFileFilter(new FileNameExtensionFilter(
								"XML Datei (*.xml)", "*.xml"));
						int returnVal = fileChooser.showSaveDialog(GUI.this);

						if (returnVal == JFileChooser.APPROVE_OPTION) {
							String pfad = fileChooser.getSelectedFile()
									.getPath();
							verwaltung.saveProjekt(pfad);
						}
					}
					dispose();

					String anfangsdatum = JOptionPane.showInputDialog(
							"Bitte Anfangsdatum eingeben", "YEAR:MONTH:DAY");
					String enddatum = JOptionPane.showInputDialog(
							"Bitte Enddatum eingeben", "YEAR:MONTH:DAY");

					GregorianCalendar start = parseDatum(anfangsdatum);
					GregorianCalendar end = parseDatum(enddatum);

					Verwaltung verwaltung = new Verwaltung(new Zeitraum(start,
							end));
					GUI gui = new GUI(verwaltung);
					gui.setVisible(true);
				}
			}

			private GregorianCalendar parseDatum(String anfangsdatum) {
				int year = Integer.parseInt(anfangsdatum.split(":")[0]);
				int month = Integer.parseInt(anfangsdatum.split(":")[1]);
				int dayOfMonth = Integer.parseInt(anfangsdatum.split(":")[2]);

				GregorianCalendar cal = new GregorianCalendar(year, month - 1,
						dayOfMonth);

				return cal;
			}
		});

		menuItem.setMnemonic(KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				java.awt.Event.CTRL_MASK, false));
		menu.add(menuItem);
		menuItem = new JMenuItem("Projekt importieren");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter(
						"XML Datei (*.xml)", "xml"));
				int returnVal = fileChooser.showOpenDialog(GUI.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					JFrame frame = new JFrame();
					Object[] option2 = { "Ja", "Nein" };
					int n = JOptionPane
							.showOptionDialog(
									frame,
									"Wollen Sie Ihren Klausurplan vorher noch abspeichern?",
									"Klausurplan speichern",
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.WARNING_MESSAGE, null, option2,
									option2[0]);

					if (n == 0) {
						JFileChooser fileChooser2 = new JFileChooser();
						fileChooser2.setFileFilter(new FileNameExtensionFilter(
								"XML Datei (*.xml)", "xml"));
						int returnVal2 = fileChooser2.showSaveDialog(GUI.this);

						if (returnVal2 == JFileChooser.APPROVE_OPTION) {
							String pfad = fileChooser2.getSelectedFile()
									.getPath();
							verwaltung.saveProjekt(pfad);
						}
					}
					String pfad = fileChooser.getSelectedFile().getPath();

					try {
						dispose();
						verwaltung.loadProjekt(pfad);
						calendarTable = new CalendarTable(verwaltung.getCal());
//						update(getGraphics());
						GUI gui = new GUI(verwaltung);
						gui.setVisible(true);
					} catch (ProjektLadenException e1) {
						showErrorDialog(e1.getMessage());
					}

				}
			}
		});
		menuItem.setMnemonic(KeyEvent.VK_I);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				java.awt.Event.CTRL_MASK, false));
		menu.add(menuItem);
		menuItem = new JMenuItem("Projekt exportieren");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter(
						"XML Datei (*.xml)", "xml"));
				int returnVal = fileChooser.showSaveDialog(GUI.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String pfad = fileChooser.getSelectedFile().getPath();
					verwaltung.saveProjekt(pfad);
				}
			}
		});
		menuItem.setMnemonic(KeyEvent.VK_E);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				java.awt.Event.CTRL_MASK, false));
		menu.add(menuItem);

		menuItem = new JMenuItem("Speichern");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter(
						"PDF-Document (*.pdf)", "pdf"));
				int returnVal = fileChooser.showSaveDialog(GUI.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String pfad = fileChooser.getSelectedFile().getPath();
					System.out.println(pfad);

					try {
						verwaltung.exportPDF(pfad, calendarTable);
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(null,
								"Fehler beim Exportieren!!", "ExportError",
								JOptionPane.WARNING_MESSAGE);
					} catch (DocumentException e1) {
						JOptionPane.showMessageDialog(null,
								"Fehler beim Exportieren!!", "ExportError",
								JOptionPane.WARNING_MESSAGE);
					}

				}
			}

		});
		menuItem.setMnemonic(KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				java.awt.Event.CTRL_MASK, false));
		menu.add(menuItem);

		// Menupunkt Importieren
		JMenu importieren = new JMenu("Importieren...");
		importieren.setMnemonic(KeyEvent.VK_K);
		menu.add(importieren);

		// Raeume importieren
		menuItem = new JMenuItem("R\u00e4ume importieren");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter(
						"XML Datei (*.xml)", "xml"));
				int returnVal = fileChooser.showOpenDialog(GUI.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					JFrame frame = new JFrame();
					Object[] option2 = { "Ja", "Nein" };
					int n = JOptionPane
							.showOptionDialog(
									frame,
									"Wollen Sie Ihren Klausurplan vorher noch abspeichern?",
									"Klausurplan speichern",
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.WARNING_MESSAGE, null, option2,
									option2[0]);

					if (n == 0) {
						JFileChooser fileChooser2 = new JFileChooser();
						fileChooser2.setFileFilter(new FileNameExtensionFilter(
								"XML Datei (*.xml)", "xml"));
						int returnVal2 = fileChooser2.showSaveDialog(GUI.this);

						if (returnVal2 == JFileChooser.APPROVE_OPTION) {
							String pfad = fileChooser2.getSelectedFile()
									.getPath();
							verwaltung.saveProjekt(pfad);
						}
					}
					String pfad = fileChooser.getSelectedFile().getPath();

					try {
						verwaltung.loadRaeume(pfad);

						// TODO calender clearen
						Daten.getInstance().getKlausuren().clear();
						calendarTable = new CalendarTable(verwaltung.getCal());

						update(getGraphics());
					} catch (ProjektLadenException e1) {
						showErrorDialog(e1.getMessage());
					}

				}
			}
		});
		menuItem.setMnemonic(KeyEvent.VK_R);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				java.awt.Event.CTRL_MASK, false));
		importieren.add(menuItem);

		// Faecher importieren
		menuItem = new JMenuItem("F\u00e4cher importieren");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter(
						"XML Datei (*.xml)", "xml"));
				int returnVal = fileChooser.showOpenDialog(GUI.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					JFrame frame = new JFrame();
					Object[] option2 = { "Ja", "Nein" };
					int n = JOptionPane
							.showOptionDialog(
									frame,
									"Wollen Sie Ihren Klausurplan vorher noch abspeichern?",
									"Klausurplan speichern",
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.WARNING_MESSAGE, null, option2,
									option2[0]);

					if (n == 0) {
						JFileChooser fileChooser2 = new JFileChooser();
						fileChooser2.setFileFilter(new FileNameExtensionFilter(
								"XML Datei (*.xml)", "xml"));
						int returnVal2 = fileChooser2.showSaveDialog(GUI.this);

						if (returnVal2 == JFileChooser.APPROVE_OPTION) {
							String pfad = fileChooser2.getSelectedFile()
									.getPath();
							verwaltung.saveProjekt(pfad);
						}
					}
					String pfad = fileChooser.getSelectedFile().getPath();

					try {
						verwaltung.loadFaecher(pfad);

						// TODO calender clearen
						Daten.getInstance().getKlausuren().clear();

						calendarTable = new CalendarTable(verwaltung.getCal());
						update(getGraphics());
					} catch (ProjektLadenException e1) {
						showErrorDialog(e1.getMessage());
					}

				}
			}
		});
		menuItem.setMnemonic(KeyEvent.VK_F);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				java.awt.Event.CTRL_MASK, false));
		importieren.add(menuItem);

		// Dozenten importieren
		menuItem = new JMenuItem("Dozenten importieren");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter(
						"XML Datei (*.xml)", "xml"));
				int returnVal = fileChooser.showOpenDialog(GUI.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					JFrame frame = new JFrame();
					Object[] option2 = { "Ja", "Nein" };
					int n = JOptionPane
							.showOptionDialog(
									frame,
									"Wollen Sie Ihren Klausurplan vorher noch abspeichern?",
									"Klausurplan speichern",
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.WARNING_MESSAGE, null, option2,
									option2[0]);

					if (n == 0) {
						JFileChooser fileChooser2 = new JFileChooser();
						fileChooser2.setFileFilter(new FileNameExtensionFilter(
								"XML Datei (*.xml)", "xml"));
						int returnVal2 = fileChooser2.showSaveDialog(GUI.this);

						if (returnVal2 == JFileChooser.APPROVE_OPTION) {
							String pfad = fileChooser2.getSelectedFile()
									.getPath();
							verwaltung.saveProjekt(pfad);
						}
					}
					String pfad = fileChooser.getSelectedFile().getPath();

					try {
						verwaltung.loadDozenten(pfad);

						// TODO calender clearen
						Daten.getInstance().getKlausuren().clear();

						calendarTable = new CalendarTable(verwaltung.getCal());
						update(getGraphics());
					} catch (ProjektLadenException e1) {
						showErrorDialog(e1.getMessage());
					}

				}
			}
		});
		menuItem.setMnemonic(KeyEvent.VK_D);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
				java.awt.Event.CTRL_MASK, false));
		importieren.add(menuItem);

		// Menupunkt Exportieren
		JMenu exportieren = new JMenu("Exportieren...");
		importieren.setMnemonic(KeyEvent.VK_K);
		menu.add(exportieren);

		// Raeume exportieren
		menuItem = new JMenuItem("R\u00e4ume exportieren");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter(
						"XML Datei (*.xml)", "xml"));
				int returnVal = fileChooser.showSaveDialog(GUI.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String pfad = fileChooser.getSelectedFile().getPath();
					verwaltung.speichereRaeume(pfad);
				}
			}
		});
		menuItem.setMnemonic(KeyEvent.VK_R);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				java.awt.Event.ALT_MASK, false));
		exportieren.add(menuItem);

		// Faecher exportieren
		menuItem = new JMenuItem("F\u00e4cher exportieren");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter(
						"XML Datei (*.xml)", "xml"));
				int returnVal = fileChooser.showSaveDialog(GUI.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String pfad = fileChooser.getSelectedFile().getPath();
					verwaltung.speichereFaecher(pfad);
				}
			}
		});
		menuItem.setMnemonic(KeyEvent.VK_F);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				java.awt.Event.ALT_MASK, false));
		exportieren.add(menuItem);

		// Dozenten exportieren
		menuItem = new JMenuItem("Dozenten exportieren");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter(
						"XML Datei (*.xml)", "xml"));
				int returnVal = fileChooser.showSaveDialog(GUI.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String pfad = fileChooser.getSelectedFile().getPath();
					verwaltung.speichereDozenten(pfad);
				}
			}
		});
		menuItem.setMnemonic(KeyEvent.VK_D);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
				java.awt.Event.ALT_MASK, false));
		exportieren.add(menuItem);

		// menu Verwalten
		menu = new JMenu("Verwalten");
		menu.setMnemonic(KeyEvent.VK_V);
		menuBar.add(menu);
//		PROF
		profMenu = new JMenu("Dozenten");
		JMenuItem newButton = new JMenuItem("Neu...");
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JDialog editProfFrame = new ProfFrame(GUI.this);
				editProfFrame.setVisible(true);
			}
		});
		profMenu.add(newButton);

		for (final Dozent d : this.verwaltung.getDaten().getDozenten()) {
			addProf(d);
		}
		menu.add(profMenu);
//		ROOM
		roomMenu = new JMenu("R\u00e4ume");
		newButton = new JMenuItem("Neu...");
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JDialog editRoomFrame = new RoomFrame(GUI.this);
				editRoomFrame.setVisible(true);
			}
		});
		roomMenu.add(newButton);

		for (final Raum r : this.verwaltung.getDaten().getRaeume()) {
			addRoom(r);
		}
		menu.add(roomMenu);
//		SUBJECT
		subjectMenu = new JMenu("Faecher");
		newButton = new JMenuItem("Neu...");
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JDialog editSubjectFrame = new SubjectFrame(GUI.this);
				editSubjectFrame.setVisible(true);
			}
		});
		subjectMenu.add(newButton);

		for (final Fach f : this.verwaltung.getDaten().getFaecher()) {
			addSubject(f);
		}
		menu.add(subjectMenu);
		
		return menuBar;
	}

	public void addRoom(final Raum r) {
		JMenuItem subMenuItem = new JMenuItem(String.valueOf(r.getNummer()));
		subMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JDialog editRoomFrame = new RoomFrame(GUI.this, r);
				editRoomFrame.setVisible(true);
			}
		});
		roomMenu.add(subMenuItem);
	}

	public void addProf(final Dozent d) {
		JMenuItem subMenuItem = new JMenuItem(d.getName());
		subMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JDialog editProfFrame = new ProfFrame(GUI.this, d);
				editProfFrame.setVisible(true);
			}
		});
		profMenu.add(subMenuItem);
	}

	public void addSubject(final Fach f) {
		JMenuItem subMenuItem = new JMenuItem(f.getBezeichnung());
		subMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JDialog editSubjectFrame = new SubjectFrame(GUI.this, f);
				editSubjectFrame.setVisible(true);
			}
		});
		subjectMenu.add(subMenuItem);
	}

	public Verwaltung getVerwaltung() {
		return this.verwaltung;
	}

	public void reloadData() {
		examSidebar.reloadData();
	}

	private void showErrorDialog(String errorMessage) {
		JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Fehler",
				JOptionPane.OK_OPTION);
	}
}
