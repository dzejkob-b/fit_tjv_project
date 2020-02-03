package cz.cvut.fit.hrabajak.semestralka.client;

import cz.cvut.fit.hrabajak.semestralka.client.gui.EntryForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;

/**
 * entrypoint klientske swingove aplikace
 */
public class ClientApp {

	private static ApplicationContext ctx;
	@Autowired
	private EntryForm ent;

	public void logic() {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				ent.initialize();

			}
		});
	}

	public static void main( String[] args ) {

		ClientApp.ctx = new AnnotationConfigApplicationContext(ClientConfig.class);

		((ClientApp)ClientApp.ctx.getBean(ClientApp.class)).logic();

	}
}
