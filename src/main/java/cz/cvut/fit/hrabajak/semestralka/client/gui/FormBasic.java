package cz.cvut.fit.hrabajak.semestralka.client.gui;

import javax.swing.*;
import java.awt.*;

public class FormBasic {

	protected JFrame parentFrame;

	public FormBasic() {
	}

	public void setParentFrame(JFrame parentFrame) {
		this.parentFrame = parentFrame;
	}

	protected void FrameToCenter(JFrame frame) {

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

	}

	protected void FrameToParent(JFrame frame) {

		int dec_width = ((frame.getSize().width - this.parentFrame.getSize().width) / 2);
		int dec_height =  ((frame.getSize().height - this.parentFrame.getHeight()) / 2);

		frame.setLocation(this.parentFrame.getLocation().x - dec_width, this.parentFrame.getLocation().y - dec_height);

	}

}
