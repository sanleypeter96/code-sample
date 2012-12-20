package GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import main.Constants;
import main.Game;
import main.Piece;
import main.Player;

public class GUIPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton[][] m_buttons;	
	private GUIGame m_guiGame;	
	private int m_row = Constants.DEFAULT_BOARD_ROWS;
	private int m_column = Constants.DEFAULT_BOARD_COLUMNS;
	
	public GUIPanel(GUIGame guiGame) 
	{
		setLayout(new GridLayout(m_row, m_column));
		m_guiGame = guiGame;
		m_buttons = new JButton[m_row][m_column];
		for(int i = 0; i < m_row; i++) 
		{
			for(int j = 0; j < m_column; j++) 
			{
				m_buttons[i][j] = new JButton();
				m_buttons[i][j].setFont(new Font(null,Font.BOLD,24));
				m_buttons[i][j].setEnabled(false);
				m_buttons[i][j].addActionListener(new SpotListener(m_buttons[i][j], i , j));
				add(m_buttons[i][j]);
			}
		}
	}
	
	public void clearBoard() 
	{
		for(int i = 0; i < m_row; i++)
			for(int j = 0; j < m_column; j++)
				m_buttons[i][j].setText("");
		m_guiGame.reset();
	}
	
	public void disableBoard() 
	{
		for(int i = 0; i < m_row; i++)
			for(int j = 0; j < m_column; j++)
				m_buttons[i][j].setEnabled(false);
	}
	
	public void enableBoard() 
	{
		for(int i = 0; i < m_row; i++)
			for(int j = 0; j < m_column; j++)
				m_buttons[i][j].setEnabled(true);
	}
	
	private class SpotListener implements ActionListener
	{
		private JButton m_button;
		private int m_rowIndex;
		private int m_columnIndex;
		
		private SpotListener(JButton button, int rowIndex, int columnIndex) 
		{
			m_button = button;
			m_rowIndex = rowIndex;
			m_columnIndex = columnIndex;
		}
		
		public void actionPerformed(ActionEvent event) 
		{
			if(m_guiGame.isLocationEmpty(m_rowIndex, m_columnIndex)) 
			{  
				m_button.setText(m_guiGame.getCurrentPlayer().getPiece().toString());
				m_guiGame.placePiece(m_rowIndex, m_columnIndex);
			}
		}
	}

}
