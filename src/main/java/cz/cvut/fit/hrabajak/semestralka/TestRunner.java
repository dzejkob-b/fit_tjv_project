package cz.cvut.fit.hrabajak.semestralka;

import org.springframework.context.annotation.Import;

/**
 * vychozi bod pro testy (nespousti se AppLogic)
 */
@Import({ AppTestConfig.class })
public class TestRunner extends AppRunner {

	public TestRunner() {

	}

	@Override
	public void run(String[] args)  {

	}

}
