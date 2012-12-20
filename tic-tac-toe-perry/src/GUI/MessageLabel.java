package GUI;

import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class MessageLabel extends JLabel 
{
	public MessageLabel() 
	{
		super("Welcome to Tic-Tac-Toe!");
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	}
	
	public void reset() 
	{
		setText("Welcome to Tic-Tac-Toe!");
	}

}
