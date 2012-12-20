package GUI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import main.*;

public class GUIView extends JFrame implements Observer 
{
	private static final long serialVersionUID = 1L;
	private GUIPanel m_guiPanel;
	private StatsPanel m_statsPanel;
	private MessageLabel m_messageLabel;
	private static GUIView m_gui;

	public static GUIView getInstance() 
	{
		return getInstance("TellApart tic-tac-toe");
	}
	
	public static GUIView getInstance(String title) 
	{
		if(m_gui == null)
			m_gui = new GUIView(title);
		return m_gui;
	}
	
	private GUIView(String title) 
	{
		super(title);
		setSize(new Dimension(500,500));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_messageLabel = new MessageLabel();
		add(m_messageLabel, BorderLayout.SOUTH);
		
		GUIGame guiGame = new GUIGame();
		guiGame.addObserver(this);
		
		m_guiPanel = new GUIPanel(guiGame);
		m_statsPanel = new StatsPanel(guiGame.getPlayers());
		JPanel optionsPanel = createOptionsPanel();
	
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		container.add(m_guiPanel,BorderLayout.CENTER);
		container.add(optionsPanel,BorderLayout.SOUTH);
		container.add(m_statsPanel, BorderLayout.NORTH);
		add(container, BorderLayout.CENTER);
		add(m_messageLabel, BorderLayout.SOUTH);
		setVisible(true);
	}
	
	private JPanel createOptionsPanel() 
	{
		JPanel optionsPanel = new JPanel();
		JButton n = new JButton("<html><u>N</u>ew Game");
		JButton r = new JButton("<html><u>R</u>eset");
		JButton e = new JButton("<html><u>E</u>xit");
		optionsPanel.add(n);
		optionsPanel.add(r);
		optionsPanel.add(e);
		n.addActionListener(new NewGameListener());
		r.addActionListener(new ResetListener());
		e.addActionListener(new ExitListener());
		return optionsPanel;
	}
	
	private class NewGameListener implements ActionListener 
	{
		public void actionPerformed(ActionEvent event) 
		{
			if(m_statsPanel.hasLegalNames())
				chooseState(GameState.GAME_IN_PROGRESS);
			else
				JOptionPane.showMessageDialog(m_gui, "Illegal player name(s).","Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private class ResetListener implements ActionListener 
	{
		public void actionPerformed(ActionEvent event) 
		{
			int respone = JOptionPane.showConfirmDialog(m_gui, "This will end the game and set the " +
					"win/loss stats to 0. Are you sure?", "Are you sure?", JOptionPane.YES_NO_OPTION);
			if(respone == JOptionPane.YES_OPTION)
				chooseState(GameState.NOT_YET_BEGUN);
		}
	}
	
	private class ExitListener implements ActionListener 
	{
		public void actionPerformed(ActionEvent event) 
		{
			dispose();
		}
	}

	private void chooseState(GameState state) 
	{
		switch (state) 
		{
			case NOT_YET_BEGUN:
				m_guiPanel.clearBoard();
				m_guiPanel.disableBoard();
				m_statsPanel.reset();
				m_messageLabel.reset();
				break;
			case GAME_IN_PROGRESS:
				m_statsPanel.setChangableName(false);
				m_guiPanel.clearBoard();
				m_guiPanel.enableBoard();
				break;
			case BETWEEN_GAMES:
				m_statsPanel.setChangableName(true);
				m_statsPanel.updateStats();
				m_guiPanel.disableBoard();
				break;
		}
	}

	public void update(Observable o, Object arg) 
	{
		if(arg == null)
			m_messageLabel.setText(((GUIGame) o).getCurrentPlayer().getName() + "'s turn");
		else 
		{
			chooseState(GameState.BETWEEN_GAMES);
			if(((Integer) arg).compareTo(new Integer(1)) == 0)
				m_messageLabel.setText(((GUIGame) o).getLastPlayer().getName() + " wins!");
			else if (((Integer) arg).compareTo(new Integer(2)) == 0)
				m_messageLabel.setText("The game ends in a tie.");
		}
		
	}

}
