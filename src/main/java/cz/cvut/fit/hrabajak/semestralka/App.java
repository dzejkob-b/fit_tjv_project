package cz.cvut.fit.hrabajak.semestralka;

import org.springframework.boot.SpringApplication;

/**
 * vstupni bod serverove aplikace
 */
public class App {

    public static void main( String[] args ) {

        SpringApplication.run(AppRunner.class, args);

        /*
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppTestConfig.class);

        // je treba inicializovat pres config - jinak nezafunguji autowired a jine anotace
        ((App)ctx.getBean(App.class)).logic();
        ((App)ctx.getBean(App.class)).destroy();
         */

    }
}
