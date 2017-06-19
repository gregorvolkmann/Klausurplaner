package view;

import java.awt.Dimension;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.TransferHandler;

import klausurmodel.Terminkalender;
import controller.Verwaltung;
import datamodel.Klausur;
import dragndrop.KlausurTransferable;


@SuppressWarnings("serial")
public class ExamSidebar extends JScrollPane{
	private final Verwaltung verwaltung;
	
	private JButton addButton;
	private JButton editButton;
	private JButton remButton;
	private JList examList;
	
	public ExamSidebar(LinkedList<Klausur> exams, final GUI gui) {
		super();
		this.verwaltung = gui.getVerwaltung();
		
//		init view components
		addButton = new JButton("+");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFrame examFrame = new ExamFrame(gui, null);
				examFrame.setVisible(true);
				gui.reloadData();
			}
		});
		editButton = new JButton("edit");
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!examList.isSelectionEmpty()) {
					JFrame examFrame = new ExamFrame(gui, (Klausur) examList.getSelectedValue());
					examFrame.setVisible(true);
					gui.reloadData();
				}
			}
		});
		remButton = new JButton("-");
		remButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!examList.isSelectionEmpty()) {
					verwaltung.getDaten().getKlausuren().remove(examList.getSelectedIndex());
					gui.reloadData();
				}
			}
		});

//		init Toolbar
		JToolBar tBar = new JToolBar();
		tBar.add(addButton);
		tBar.add(editButton);
		tBar.add(remButton);
		tBar.addSeparator();
		tBar.add(new JLabel("Klausuren"));
		
		setColumnHeaderView(tBar);
		
		
		examList = new JList(verwaltung.getDaten().getKlausuren().toArray());
		examList.setDragEnabled(true);
		examList.setCellRenderer(new KlausurCell());
		examList.setTransferHandler(new TransferHandler(){
			
			public Transferable createTransferable(JComponent c){
				JList lis = (JList)c;
				Klausur k = (Klausur)lis.getSelectedValue();
				
				Transferable trans = new KlausurTransferable(k);
				return trans;
			}
			
			public int getSourceActions(JComponent c){
				return COPY_OR_MOVE;
			}
			
			public void exportDone(JComponent c, Transferable t, int action){
				((Terminkalender)verwaltung.getCal()).setzeNeutraleFarben();
				((Terminkalender)verwaltung.getCal()).checkTermine();
			}
			
			public Icon getVisualRepresentation(Transferable t){
				return super.getVisualRepresentation(t);
			}

		});
		examList.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
//				((Terminkalender)verwaltung.getCal()).setzeNeutraleFarben();
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				Klausur k = (Klausur)examList.getSelectedValue();
//				calTable.setDefaultRenderer(CalendarColorCell.class, new CalendarColorCell());
//				calTable.setModel(((Terminkalender)verwaltung.getCal()).checkKlausur(k));
				((Terminkalender)verwaltung.getCal()).checkKlausur(k);
				((Terminkalender)verwaltung.getCal()).setzeTermineNeutral();
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
			}
		});
		
		setViewportView(examList);
		setPreferredSize(new Dimension(200, 700));
	}
	
	public void reloadData() {
		examList = new JList(verwaltung.getDaten().getKlausuren().toArray());
		examList.setCellRenderer(new KlausurCell());
		examList.setDragEnabled(true);
		examList.setTransferHandler(new TransferHandler(){
			
			public Transferable createTransferable(JComponent c){
				JList lis = (JList)c;
				Klausur k = (Klausur)lis.getSelectedValue();
				
				Transferable trans = new KlausurTransferable(k);
				return trans;
			}
			
			public int getSourceActions(JComponent c){
				return COPY_OR_MOVE;
			}
			
			public void exportDone(JComponent c, Transferable t, int action){
				((Terminkalender)verwaltung.getCal()).setzeNeutraleFarben();
				((Terminkalender)verwaltung.getCal()).checkTermine();
			}
			
			public Icon getVisualRepresentation(Transferable t){
				return super.getVisualRepresentation(t);
			}

		});
		
		examList.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
//				((Terminkalender)verwaltung.getCal()).setzeNeutraleFarben();
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				Klausur k = (Klausur)examList.getSelectedValue();
//				calTable.setDefaultRenderer(CalendarColorCell.class, new CalendarColorCell());
//				calTable.setModel(((Terminkalender)verwaltung.getCal()).checkKlausur(k));
				((Terminkalender)verwaltung.getCal()).checkKlausur(k);
				((Terminkalender)verwaltung.getCal()).setzeTermineNeutral();
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
			}
		});
		
		setViewportView(examList);
	}
}
