package GUI;

import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import main.*;

public class StatsPanel extends JPanel 
{
	private PlayerPanel m_playerPanel1;
	private PlayerPanel m_playerPanel2;
	private Players players;

	public StatsPanel(Players players) 
	{
		this.players = players;
		players.getPlayer(1);
		add(m_playerPanel1 = new PlayerPanel(players.getPlayer(1), "Player 1"));
		add(m_playerPanel2 = new PlayerPanel(players.getPlayer(2), "Player 2"));
	}

	public void setChangableName(boolean editable) 
	{
		m_playerPanel1.setChangableName(editable);
		m_playerPanel2.setChangableName(editable);
	}
	
	public void reset() 
	{
		players.reset();
		m_playerPanel1.reset(players.getPlayer(1), "Player 1");
		m_playerPanel2.reset(players.getPlayer(2), "Player 2");
	}

	public boolean hasLegalNames() 
	{
		return m_playerPanel1.hasLegalName() && m_playerPanel2.hasLegalName();
	}
	
	protected void updateStats() 
	{
			m_playerPanel1.updateStats(players.getPlayer(1));
			m_playerPanel2.updateStats(players.getPlayer(2));
	}
	
	private class PlayerPanel extends JPanel
	{
		private JTextField m_nameField;
		private JLabel m_wins;
		private JLabel m_losses;
		private Player m_player;

		private PlayerPanel(Player player, String displayName) 
		{
			super(new GridLayout(3,2));
			this.m_player = player;
			add(new JLabel("Name:"));
			add(m_nameField = new JTextField(player.getName(),8));
			add(new JLabel("Wins:"));
			add(m_wins = new JLabel("0"));
			add(new JLabel("Losses:"));
			add(m_losses = new JLabel("0"));
			TitledBorder tb1 = BorderFactory.createTitledBorder(displayName + " (" + player.getPiece() + "):");
			setBorder(tb1);
		} 

		private void setChangableName(boolean editable) 
		{
			m_nameField.setEditable(editable);
			if(!editable)
				m_player.setName(m_nameField.getText().trim());
		}

		private void reset(Player player, String displayName) 
		{
			m_nameField.setText(displayName);
			setChangableName(true);
			m_wins.setText("0");
			m_losses.setText("0");
		}
		
		private void updateStats(Player player) 
		{
			m_wins.setText("" + player.getWins());
			m_losses.setText("" + player.getLosses());	
		}
		
		private boolean hasLegalName() 
		{
			return !m_nameField.getText().trim().isEmpty();
		}
	}

}
