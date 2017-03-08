import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class TicTacToe extends JFrame
{
	int ct = 0;
	boolean x = true;
	int[] board = {0,0,0,0,0,0,0,0,0};
	JButton[] jb = new JButton[9];
	JPanel panel;
	JRadioButton rbx;
	JRadioButton rbo;
	JPanel gPanel;
	JPanel informationPanel;
	JPanel controls;
	JPanel radioPanel;
	JButton ng;
	JButton quit;
	JLabel gap;

	public TicTacToe() //constructor
	{
		this.setTitle("Final Project: Tic Tac Toe");
		this.setSize(650,490);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.createPanel();
		this.add(panel);
	}

	private void createPanel() //creates board & panels
	{
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		gPanel = new JPanel();
		gPanel.setBorder(new TitledBorder("Tic Tac Toe"));
		gPanel.setLayout(new GridLayout(3,3));
		for (int c = 0; c < 9; c++)
		{
			jb[c] = new JButton();
			jb[c].setBorder(new EtchedBorder(new Color(220, 20, 60), new Color(220, 20, 60)));
			jb[c].addActionListener(new ButtonListener());
			jb[c].setBackground(Color.WHITE);
			jb[c].setActionCommand("" + c);
			gPanel.add(jb[c]);
			jb[c].setFont(new Font("sans serif", Font.PLAIN, 80));
		}

		controls = new JPanel();
		controls.setLayout(new BorderLayout());
		controls.setBorder(new TitledBorder("Options"));
		panel.add(gPanel, BorderLayout.CENTER);
		panel.add(controls, BorderLayout.EAST);
		ng= new JButton("New Game");
		controls.add(ng, BorderLayout.NORTH);
		ng.addActionListener(new NewButtonListener());
		informationPanel = new JPanel();
		informationPanel.setLayout(new GridLayout(3,1,5,5));
		gap = new JLabel("");
		informationPanel.add(gap);
		controls.add(informationPanel, BorderLayout.CENTER);
		rbx = new JRadioButton("PLAY AS X");
		rbx.setForeground(Color.BLUE);
		rbo = new JRadioButton("PLAY AS O");
		rbo.setForeground(Color.RED);
		rbx.setSelected(true);// sets player 'x' as default
		rbx.addActionListener(new RadioListener());
		rbo.addActionListener(new RadioListener());
		refresh();
		radioPanel= new JPanel();
		radioPanel.setLayout(new GridLayout(3,1));
		radioPanel.add(rbx);
		radioPanel.add(rbo);
		informationPanel.add(radioPanel);
		quit = new JButton("Quit Game");
		controls.add(quit, BorderLayout.SOUTH);
		quit.addActionListener(new QuitButtonListener());
	}

	private class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			String number = event.getActionCommand();
			JButton button = (JButton) event.getSource();
			if (button.getText() == "")
			{
				if (x)
				{
					button.setText ("X");
					board[Integer.parseInt(number)] = 1;
					init();
				}

				if (!x)
				{
					button.setText ("O");
					board[Integer.parseInt(number)] = 2;
					init();
				}
			}
		}
	}

	public class RadioListener implements ActionListener //see radio buttons
	{
		public void actionPerformed(ActionEvent event)
		{
			JRadioButton b = (JRadioButton) event.getSource();
			if (b.getText().equals("PLAY AS X"))
			{
				x = true;
				//makes sure both buttons aren't selected at the same time
					rbo.setSelected(false);
					rbx.setSelected(true);

			}

			if (b.getText().equals("PLAY AS O"))
			{
				x = false;
				//makes sure both buttons aren't selected at the same time
					rbo.setSelected(true);
					rbx.setSelected(false);
			}

			refresh();
		}
	}

	private class QuitButtonListener implements ActionListener //goes with 'quit' button
	{
		public void actionPerformed(ActionEvent event)
		{
			System.exit(0);
		}
	}

	private class NewButtonListener implements ActionListener //for the purpose of new game
	{
		public void actionPerformed(ActionEvent event)
		{
			ct = 0;
			for (int q = 0; q < 9; q++)
			{
				jb[q].setText("");
				board[q] = 0;
			}

			if (x == false)
			{
				refresh();
			}
		}
	}


	private void init() //initiates the computer's moves
	{
		if (!isDraw())
		{
			isWinner();
			if (canWin() == 0)
			{
				if (!attack())
				{
					if (!block())
					{
						randomizeMove();
					}
				}
				isWinner();
				isDraw();
			}
		}
	}

	private int canWin() // looks for a winner- returns 0 if no such win is found.
	{
		for (int i = 1; i < 3; i++)
		{
			for (int j = 0; j < 7; j+=3)
			{
				if(board[j] == i && board[j + 1] == i && board[j + 2] == i)
				{
					return i;
				}
			}
		}

		for (int i = 1; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				if(board[j] == i && board[j + 3] == i && board[j + 6] == i)
				{
					return i;
				}
			}
		}

		for (int i = 1; i < 3; i++)
		{
			if(board[0] == i && board[4] == i && board[8] == i)
			{
				return i;
			}
		}

		for (int i = 1; i < 3; i++)
		{
			if(board[2] == i && board[4] == i && board[6] == i)
			{
				return i;
			}
		}

		return 0;
	}

	private boolean isDraw()// decides whether end game is a draw or not
	{
		if (isFull() && canWin() == 0)
		{
			JOptionPane.showMessageDialog(null,"DRAW GAME");
			refresh();
			return true;
		}

		else
		{
			return false;
		}
	}

	private void isWinner() //decides who is the ultimate winner
	{
		if (canWin() == 1)
		{
			JOptionPane.showMessageDialog(null,"PLAYER X WINS THE GAME!");
			refresh();
		}

		if (canWin() == 2)
		{
			JOptionPane.showMessageDialog(null,"PLAYER O WINS THE GAME!");
			refresh();
		}
	}

	private boolean attack() //attempts to win
	{
		int val;
		int m = -1;//most sensible move to make

		if (x)
		{
			val = 2;
		}

		else
		{
			val = 1;
		}

		m = toHorizWin(val);

		if (m == -1)
		{
			m = toVertWin(val);
		}

		if (m == -1)
		{
			m = toDiagWin(val);
		}

		if (m > -1 && m < 9)
		{
			move(m);
			return true;
		}

		else
		{
			return false;
		}
	}

	private boolean block()// helps computer win
	{
		int value;
		int m = -1;

		if (x)
		{
			value = 1;
		}
		else
		{
			value = 2;
		}

		m = toHorizWin(value);

		if (mode(value) > 0)
		{
			if (m == -1)
			{
				m = mode(value);
			}
		}


		if (m == -1)
		{
			m = toVertWin(value);
		}


		if (m == -1)
		{
			m = toDiagWin(value);
		}


		if (m == -1 && board[4]==0 && x)
		{
			m = 4;
		}

		if (m == -1)
		{
			m = randomizeCorner();
		}

		if (m > -1 && m < 9)
		{
			move(m);
			return true;
		}

		else
		{
			return false;
		}
	}

	private void randomizeMove() //moves to random places
	{
		Random rndGenerator = new Random();
		int rand = rndGenerator.nextInt(8);

		if (!isFull())
		{
			move (rand);
		}
	}

	private boolean isFull() // checks to see if board is totally occupied
	{
		for (int i = 0; i < 9; i++)
		{
			if (board[i] == 0)
			{
				return false;
			}
		}

		return true;
	}

	private int toHorizWin(int value) //horizontal
	{
		for (int i = 0; i < 7; i+=3)
		{
			if(board[i] == value && board[i + 1] == value && board[i + 2] == 0)
			{
				return i + 2;
			}

			if(board[i] == value && board[i + 2] == value && board[i + 1] == 0)
			{
				return i + 1;
			}

			if(board[i + 1] == value && board[i + 2] == value && board[i] == 0)
			{
				return i;
			}
		}

		return -1;
	}


	private int toVertWin(int value) //vertical
	{
		for (int i = 0; i < 3; i++)
		{
			if(board[i] == value && board[i + 3] == value && board[i + 6] == 0)
			{
				return i + 6;
			}
			if(board[i] == value && board[i + 6] == value && board[i + 3] == 0)
			{
				return i + 3;
			}
			if(board[i + 3] == value && board[i + 6] == value && board[i] == 0)
			{
				return i;
			}
		}


		return -1;
	}

	private int toDiagWin(int value) //diagonol
	{
		if(board[0] == value && board[4] == value && board[8] == 0)
		{
			return 8;
		}

		if(board[0] == value && board[8] == value && board[4] == 0)
		{
			return 4;
		}

		if(board[4] == value && board[8] == value && board[0] == 0)
		{
			return 0;
		}

		if(board[2] == value && board[4] == value && board[6] == 0)
		{
			return 6;
		}

		if(board[2] == value && board[6] == value && board[4] == 0)
		{
			return 4;
		}

		if(board[6] == value && board[4] == value && board[2] == 0)
		{
			return 2;
		}

		return -1;
	}

	private int mode(int value)//preferred moving spaces
	{
		if(board[0] == value && board[8] == value && board[3] == 0)
		{
			return 3;
		}

		if(board[2] == value && board[6] == value && board[5] == 0)
		{
			return 5;
		}

		return -1;
	 }

	private int randomizeCorner()//move to a random corner
	{
		Random gen = new Random();
		int rand = gen.nextInt(3);

		switch (rand)
		{
			case 0:
				if (board[0]==0)
				{
					return 0;
				}
				if (board[2]==0)
				{
					return 2;
				}
				if (board[6]==0)
				{
					return 6;
				}
				if (board[8]==0)
				{
					return 8;
				}

			break;

			case 1:
				if (board[4]==0 && !x)
				{
					return 4;
				}

				if (board[2]==0)
				{
					return 2;
				}

				if (board[6]==0)
				{
					return 6;
				}

				if (board[8]==0)
				{
					return 8;
				}

				if (board[0]==0)
				{
					return 0;
				}

			break;

				case 2:
					if (board[6]==0)
					{
						return 6;
					}

					if (board[8]==0)
					{
						return 8;
					}

					if (board[0]==0)
					{
						return 0;
					}

					if (board[2]==0)
					{
						return 2;
					}

			break;

			case 3:
				if (board[8]==0)
				{
					return 8;
				}

				if (board[0]==0)
				{
					return 0;
				}

				if (board[2]==0)
				{
					return 2;
				}

				if (board[6]==0)
				{
					return 6;
				}

			break;
		}

		return -1;
	}

	private void move(int num)// how the computer mvoes
	{
		if (x)
		{
			jb[num].setText("O");
			board[num] = 2;
		}

		else
		{
			jb[num].setText("X");
			board[num] = 1;
		}
	}

	private void refresh() //clears the board
	{
		for (int j = 0; j < 9; j++)
		{
			jb[j].setText("");
			board[j] = 0;
		}
	}

	public static void main(String [] args)
	{
		 TicTacToe frame = new TicTacToe();
		 frame.setVisible(true);
	}
}
