import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;


public class SetupFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private Flock f;
	private JFrame options;
	public static JSlider rad = new JSlider();
	public static JSlider avoidRad = new JSlider();
	public static JSlider maxSpeed = new JSlider();
	public static JSlider avoidW = new JSlider();
	public static JSlider COAW = new JSlider();
	public static JSlider COMW = new JSlider();

	private boolean GUI = false;

	SetupFrame(){


		if(GUI) {
			options = new JFrame();
			GridLayout fl = new GridLayout(12,1);
			options.setLayout(fl);

			options.add(new JLabel("radius"));
			rad.setMajorTickSpacing(10);
			rad.setMinorTickSpacing(1);
			rad.setMaximum(100);
			rad.setMinimum(0);
			rad.setPaintLabels(true);
			options.add(rad);

			//no multiply
			options.add(new JLabel("avoid radius"));
			avoidRad.setMajorTickSpacing(10);
			avoidRad.setMinorTickSpacing(1);
			avoidRad.setMaximum(25);
			avoidRad.setMinimum(0);
			avoidRad.setPaintLabels(true);
			options.add(avoidRad);

			//no multiply
			options.add(new JLabel("max speed"));
			maxSpeed.setMajorTickSpacing(1);
			maxSpeed.setMaximum(10);
			maxSpeed.setMinimum(0);
			maxSpeed.setPaintLabels(true);
			options.add(maxSpeed);

			//multiply by 1/100
			options.add(new JLabel("avoid weight"));
			avoidW.setMajorTickSpacing(10);
			avoidW.setMinorTickSpacing(1);
			avoidW.setMaximum(50);
			avoidW.setMinimum(0);
			avoidW.setPaintLabels(true);
			options.add(avoidW);

			//mult by 1/1000
			options.add(new JLabel("align weight"));
			COAW.setMajorTickSpacing(10);
			COAW.setMinorTickSpacing(1);
			COAW.setMaximum(100);
			COAW.setMinimum(0);
			COAW.setPaintLabels(true);
			options.add(COAW);

			//1/1000
			options.add(new JLabel("center weight"));
			COMW.setMajorTickSpacing(1);
			COMW.setMaximum(10);
			COMW.setMinimum(0);
			COMW.setPaintLabels(true);
			options.add(COMW);


			options.pack();
			options.setVisible(true);
		}

		//f = new Flock(GUI);
		f = new Flock(GUI);
		//this.setSize(800,800);
		this.setBounds(300, 0, 800, 800);
		this.add(f);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}



	public static void main(String[] args) {
		new SetupFrame();




	}

}
