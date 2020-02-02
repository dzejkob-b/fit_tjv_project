package cz.cvut.fit.hrabajak.semestralka.client;

import ch.qos.logback.core.net.server.Client;
import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;
import cz.cvut.fit.hrabajak.semestralka.client.consume.ConsumeProduct;
import cz.cvut.fit.hrabajak.semestralka.client.gui.ProductEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;

public class ClientApp {

	private static ApplicationContext ctx;
	@Autowired
	private ProductEditor ed;

	public void logic() {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				ed.initialize();

			}
		});
	}

	public static void main( String[] args ) {

		ClientApp.ctx = new AnnotationConfigApplicationContext(ClientConfig.class);

		((ClientApp)ClientApp.ctx.getBean(ClientApp.class)).logic();

	}
}
