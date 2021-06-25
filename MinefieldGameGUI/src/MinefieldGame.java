import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

public class MinefieldGame implements MouseListener {
	private int gameCompleteCounter = 0;
	private int wait = 0;
	
	private JFrame frame;
	private JPanel mainPanel;
	private JPanel gamePanel;
	private JLabel scoreLabel;
	private JButton restartButton;
	private ButtonGroup difficulty;
	Button[][] board = new Button[5][5];

	
	public MinefieldGame() {
		generateFrame();
		generateMine();
		updateCount();
		gameCompleteCounter = 0;
		wait = 0;
	}

	private void restart() {
		mainPanel.setVisible(false);
		gamePanel.setVisible(false);
		generateMainPanel();
		generateGamePanel();
		generateMine();
		updateCount();
		gameCompleteCounter = 0;
		wait = 0;
	}

	private void restartDif(int row, int col) {
		mainPanel.setVisible(false);
		gamePanel.setVisible(false);
		changeBoard(row, col);
		generateMainPanel();
		generateGamePanel();
		generateMine();
		updateCount();
		gameCompleteCounter = 0;
		wait = 0;
	}
	
	private void generateFrame() {
		
		frame = new JFrame("Minesweeper");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 535, 630);
		frame.setLayout(null);
		frame.setResizable(false);
		
		generateMainPanel();
		generateGamePanel();
		
		frame.setVisible(true);
	}
	
	private void generateMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setBounds(10, 10, 500, 50);
		mainPanel.setLayout(new GridLayout(1,4));
		
		generateRestartButton();
		generateScoreLabel();	
		generateDifficulty();
		
		mainPanel.setVisible(true);
		
		frame.getContentPane().add(mainPanel);
	}

	private void generateRestartButton() {
		restartButton = new JButton("Restart");
		mainPanel.add(restartButton);
		
		restartButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				restart();
			}
		} );
	}
	
	private void generateScoreLabel() {
		scoreLabel = new JLabel("0");
		scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainPanel.add(scoreLabel);	
	}
	
	private void generateDifficulty() {
		JPanel difPanel = new JPanel(new GridLayout(3,1));
		JRadioButton difEasy = new JRadioButton("Easy");
		difEasy.setActionCommand("k");
		difPanel.add(difEasy);

		JRadioButton difMedium = new JRadioButton("Medium");
		difMedium.setActionCommand("o");
		difPanel.add(difMedium);

		JRadioButton difHard = new JRadioButton("Hard");
		difHard.setActionCommand("z");
		difPanel.add(difHard);

		difficulty = new ButtonGroup();
		difficulty.add(difEasy);
		difficulty.add(difMedium);
		difficulty.add(difHard);
		
		JButton chooseDif = new JButton("Choose");
		
		chooseDif.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(difMedium.isSelected()) {
					restartDif(8, 8);			
				}else if(difHard.isSelected()) {
					restartDif(10, 10);

				}else {
					restartDif(5, 5);
				}
			}
		});
		
		mainPanel.add(difPanel);
		mainPanel.add(chooseDif);
	}

	private void generateGamePanel() {
		gamePanel = new JPanel();
		gamePanel.setBounds(10, 70, 500, 500);
		gamePanel.setVisible(true);
		gamePanel.setLayout(new GridLayout(board.length, board[0].length, 0, 0));
		
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[0].length; col++) {
				Button button = new Button(row, col);
				gamePanel.add(button);
				button.addMouseListener(this);
				board[row][col] = button;
			}
		}
		
		frame.getContentPane().add(gamePanel);
	}

	private void generateMine() {
		int i = 0;
		while(i < ((board.length * board[0].length) / 10)) {
			int row = (int) (Math.random() * board.length);
			int col = (int) (Math.random() * board[0].length);
			if(!board[row][col].isMine()) {
				board[row][col].setMine(true);
				i++;
			}
		}
	}

	private void updateCount() {
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[0].length; col++) {
				if(board[row][col].isMine()) {
					count(row, col);
				}
			}
		}
	}

	private void count(int row, int col) {
		for(int r = row - 1 ; r <= row + 1; r++) {
			for(int c = col - 1; c <= col + 1; c++) {
				try {
					int value = board[r][c].getCount();
					value++;
					board[r][c].setCount(value);
				}catch(Exception e) {
					
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Button clickedButton = (Button) e.getComponent();
		if(e.getButton() == 1) {
			if(clickedButton.isMine()) {
				if(clickedButton.isFlag() && wait == 0) {
					clickedButton.setFlag(false);
					clickedButton.setIcon(null);
				}
				if(wait == 0) {
					int i = JOptionPane.showOptionDialog(frame,"Try Again!", "Game Over!", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
					if(i == 0) {
						wait = 1;
						restart();
					}else if(i == 1) {
						wait = 1;
						printMine();
					}
				}
			}
			else {
				int value = (board.length * board[0].length) - ((board.length * board[0].length) / 10);
				openTheMinefield(clickedButton.getRow(), clickedButton.getCol());
				if(gameCompleteCounter == value & wait == 0) {
					int i = JOptionPane.showOptionDialog(frame,"Do you want to play again?", "Winner!", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
					if(i == 0) {
						wait = 1;
						restart();
					}else if(i == 1) {
						wait = 1;
						printMine();
					}
				}
			}
		}else if(e.getButton() == 3 & wait == 0 && clickedButton.isEnabled()) {
			if(!clickedButton.isFlag()) {
				clickedButton.setIcon(new ImageIcon("11.png"));
				clickedButton.setFlag(true);
			}
			else {
				clickedButton.setIcon(null);
				clickedButton.setFlag(false);
			}
		}	
	}

	private void openTheMinefield(int row, int col) {
		if(row < 0 || row >= board.length || col < 0 || col >= board[0].length ||
				board[row][col].getText().length() > 0 || board[row][col].isEnabled() == false) {
			return;
		}else if(board[row][col].getCount() != 0) {
			if(board[row][col].isFlag()) {
				board[row][col].setFlag(false);
				board[row][col].setIcon(null);
			}
			board[row][col].setText(board[row][col].getCount() +"");
			board[row][col].setEnabled(false);
			gameCompleteCounter++;
		}else {
			if(board[row][col].isFlag()) {
				board[row][col].setFlag(false);
				board[row][col].setIcon(null);
			}
			board[row][col].setEnabled(false);
			openTheMinefield(row - 1, col);
			openTheMinefield(row + 1, col);
			openTheMinefield(row, col - 1);
			openTheMinefield(row, col + 1);
			gameCompleteCounter++;
		}
		scoreLabel.setText(gameCompleteCounter+"");
	}

	private void printMine() {
		for(int r = 0; r < board.length; r++) {
			for(int c = 0; c < board[0].length; c++) {
				if(board[r][c].isMine()) {
					board[r][c].setIcon(new ImageIcon("9.png"));
				}
				else if(board[r][c].getCount() != 0 && board[r][c].isFlag()) {
					board[r][c].setIcon(null);
					board[r][c].setFlag(false);
					board[r][c].setText(board[r][c].getCount() + "");
					board[r][c].setEnabled(false);
				}
				else {
					if(board[r][c].getCount() != 0) {
						board[r][c].setText(board[r][c].getCount() + "");
					}
					board[r][c].setEnabled(false);
				}
			}
		}
	}
	
	private void changeBoard(int row, int col) {
		board = new Button[row][col];
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
}






