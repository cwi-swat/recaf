package recaf.tests;

import java.io.PrintWriter;

import org.apache.logging.log4j.LogManager;
import org.junit.BeforeClass;
import org.rascalmpl.values.ValueFactoryFactory;

public class BaseTestPartial extends BaseTest{
	private static final String RECAF_SRC = "/../recaf-desugar/src/";
	private static final String RECAF_GENERATED_DIR = "cwd:///" + GENERATED_DIR;
	private static final String RECAF_INPUT = "cwd:///../recaf-desugar/input";
	private static final String RECAF_PARTIAL_DESUGARING = "lang::recaf::DesugarMain";
	
	private static boolean generated_sources = false;
	
	@BeforeClass
	public static void init() {
		if (!generated_sources) {
			LogManager.getLogger().info("Generating source files with rascal...");

			RascalModuleRunner runner = new RascalModuleRunner(new PrintWriter(System.out, false),
					new PrintWriter(System.err, false));

			try {
				runner.addRascalSearchPathContributor(
						ValueFactoryFactory.getValueFactory().sourceLocation("cwd", "", RECAF_SRC));
				runner.run(RECAF_PARTIAL_DESUGARING, new String[] { RECAF_INPUT, RECAF_GENERATED_DIR });
			} catch (Exception e) {
				LogManager.getLogger().error("Generation failed", e);
				LogManager.getLogger().info("Exiting...");
				System.exit(-1);
			}
			generated_sources = true;
		}
	}
}
