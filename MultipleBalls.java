/*
 * Author:McClain Marchman
 * */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class MultipleBalls extends JPanel implements Runnable
{
	
	
	private int delay = 10;
    private int x = 0;
    private int y = 0; // Current ball position
    private int radius = 5; // Ball radius
    private int dx = 2; // Increment on balls' x-coordinate
    private int dy = 2; // Increment on balls' y-coordinate
    static List<MultipleBalls> ballList = new ArrayList<MultipleBalls>();
    static boolean suspend = false;
    static boolean resume = false;
    static final Object SUSPEND_LOCK = new Object();//hold suspend object
  
    
    
  

    public MultipleBalls() 
    {
    	
        new Thread(this).start();
        
    }

    public void run() 
    {
    	
        try 
        {
            while (true) 
            {
            	//allow the suspend_lock object to be sync with the MultipleBalls thread
            	synchronized(SUSPEND_LOCK)
            	{
                   if (suspend==true) 
                    {
                    	SUSPEND_LOCK.wait();
                    	
                    }
                    
                }

            	repaint();
            	Thread.sleep(25);
            }
            
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        g.setColor(Color.red);
       
       
        	//for every MultipleBalls object created draw
        	for (MultipleBalls balls: ballList)
        	{

        		// Check boundaries

        		if (balls.x < 0 || balls.x > getWidth()) 

        		{
        			balls.dx *= -1;
        			
        		}

        		if (balls.y < 0 || balls.y > getHeight()) 
        		{
        			balls.dy *= -1;
        			
        		}
        		
        		// Adjust ball position
        		balls.x += balls.dx;
        		balls.y += balls.dy;
        		g.fillOval(balls.x - radius, balls.y - radius, radius * 2, radius * 2);
        	}
        
    }
    
   
    

    public void setDelay(int delay) 
    {
        this.delay = delay;

    }
    
	public static void main(String[] args) 
	{
		 new MultipleBalls();
		 new BallWindow();
    
	}

	
}
    

class BallWindow extends JFrame 
{
    private JButton suspendButton; //stop the movement of the balls
    private JButton resumeButton; // resume the movement of the balls
    private JButton plusOne; // Add one more ball
    private JButton minusOne; // Delete one ball
    private JFrame frame = new JFrame(); //Frame
    
  
    
    private MultipleBalls ball = new MultipleBalls();
    JPanel buttonPanel = new JPanel();
    JPanel ballPanel = new JPanel();
    
    
	BallWindow()
	{
		
		frame.setTitle("Multiple Balls");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		//Create suspend, resume, plusOne, and minusOne buttons
		suspendButton = new JButton("Suspend");
		resumeButton = new JButton("Resume");
		plusOne = new JButton("+1");
		minusOne = new JButton("-1");
		
		//Add event listeners to the buttons
		suspendButton.addActionListener(new SuspendButtonListener());
		resumeButton.addActionListener(new ResumeButtonListener());
		plusOne.addActionListener(new PlusOneListener());
		minusOne.addActionListener(new MinusOneListener());
		
		//Add a GridLayout Manager
		frame.setLayout(new GridLayout(2,1));
		frame.setSize(800, 800);
		
		
		
		 MultipleBalls.ballList.add(ball);
		 

		//add components to the panels
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(suspendButton);
		buttonPanel.add(resumeButton);
		buttonPanel.add(plusOne);
		buttonPanel.add(minusOne);
		
		buttonPanel.setBorder(BorderFactory.createLineBorder(Color.black));
	
		
		frame.add(ball);
		frame.add(buttonPanel);
		frame.revalidate();
		
		frame.setVisible(true);
		

	}
	
	//4 actionlisteners for the  buttons
	private class SuspendButtonListener implements ActionListener
    {

		//set suspend = true to cause the thread to wait
		public void actionPerformed(ActionEvent e) 
		{
			MultipleBalls.suspend = true;
			
		}
    	
    }
	
	private class ResumeButtonListener implements ActionListener
    {

		
		public void actionPerformed(ActionEvent e) 
		{
			
			MultipleBalls.suspend = false;
			
			//resume the multipleballs thread
			synchronized (MultipleBalls.SUSPEND_LOCK)
			{
				
				MultipleBalls.SUSPEND_LOCK.notifyAll();
		
				
			}
			
		}
    	
    }
	
	private class PlusOneListener implements ActionListener
    {

		
		public void actionPerformed(ActionEvent e) 
		{
			
			
			MultipleBalls.ballList.add(new MultipleBalls());
			
			
		}
    	
    }
	
	private class MinusOneListener implements ActionListener
    {

		
		public void actionPerformed(ActionEvent e) 
		{
			
			if(MultipleBalls.ballList.size() > 0)
			{
				MultipleBalls.ballList.remove(MultipleBalls.ballList.size() - 1);
			}
		}
    	
    }
	
	 
	
}

