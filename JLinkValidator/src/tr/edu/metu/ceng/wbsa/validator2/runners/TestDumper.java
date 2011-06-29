package tr.edu.metu.ceng.wbsa.validator2.runners;

import tr.edu.metu.ceng.wbsa.validator2.JLinkValidator;
import tr.edu.metu.ceng.wbsa.validator2.domain.ResourceType;
import tr.edu.metu.ceng.wbsa.validator2.domain.ResourceValidationInfo;

public class TestDumper {

	public static void main(String[] args) {
		TestDumper dumper = new TestDumper();
		dumper.init();
		JLinkValidator.getInstance().setVisible(true);
	}

	public void init() {
		for (int j = 0; j < 100; j++)
			for (int i = 0; i < 10000; i++) {
				ResourceValidationInfo rvi = new ResourceValidationInfo(
						"http://" + i + ".com/", ((i % 2) == 0) ? true : false,
						ResourceType.image, "OK");
				// add rvi to table...
				addNewRVIToTable(rvi);
			}

	}

	private void addNewRVIToTable(ResourceValidationInfo rvi) {
		new Thread(this.new RVIDumperThread(rvi)).start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}

	// JLinkValidator.getInstance().addResourceValidationInfo(rvi);

	public class RVIDumperThread implements Runnable {

		private Thread thread;

		private ResourceValidationInfo rvi;

		public RVIDumperThread(ResourceValidationInfo rvi) {
			this.rvi = rvi;
			thread = new Thread();
			thread.start();
		}

		@Override
		public void run() {
			// add rvi to table...
			JLinkValidator.getInstance().addResourceValidationInfo(rvi);
			System.out.println("added...");
		}

	}

}
