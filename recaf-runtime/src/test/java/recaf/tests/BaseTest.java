package recaf.tests;

import static java.util.Arrays.asList;
import static javax.tools.ToolProvider.getSystemJavaCompiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

import org.apache.logging.log4j.LogManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.rascalmpl.values.ValueFactoryFactory;

public class BaseTest {

	private static final String COMPILE_OUTPUT_DIR = "target/test-classes/";
	private static final String COMPILETIME_CP = "src/main/java/:src/test-generated/"; // :";
	private static final String RUNTIME_CP = "target/classes/:target/test-classes/";
	private static final String PACKAGE_NAME = "generated.";
	protected static final String GENERATED_DIR = "src/test-generated/generated/";

	protected PrintWriter out;
	protected PrintWriter err;

	@Rule
	public TestWatcher watchman = new Log4jTestWatcher();

	
	private static final String RECAF_SRC = "/../recaf-desugar/src/";
	private static final String RECAF_GENERATED_DIR = "cwd:///" + GENERATED_DIR;
	private static final String RECAF_INPUT = "cwd:///../recaf-desugar/exprinput";
	private static final String RECAF_FULL_DESUGARING = "lang::recaf::DesugarMain";
	
	private static boolean generated_sources = true;
	
	@BeforeClass
	public static void init() {
		if (!generated_sources) {
			LogManager.getLogger().info("Generating source files with rascal...");

			RascalModuleRunner runner = new RascalModuleRunner(new PrintWriter(System.out, false),
					new PrintWriter(System.err, false));

			try {
				runner.addRascalSearchPathContributor(
						ValueFactoryFactory.getValueFactory().sourceLocation("cwd", "", RECAF_SRC));
				runner.run(RECAF_FULL_DESUGARING, new String[] { RECAF_INPUT, RECAF_GENERATED_DIR });
			} catch (Exception e) {
				LogManager.getLogger().error("Generation failed", e);
				LogManager.getLogger().info("Exiting...");
				System.exit(-1);
			}
			generated_sources = true;
		}
	}
	
	@Before
	public void setUp() throws Exception {
		out = new PrintWriter(System.out, false);
		err = new PrintWriter(System.err, false);
	}

	protected void runCompiler(String pathname) throws CompiletimeException {
		String dependenciesString = "";
		
		try {
			dependenciesString = Files
					.walk(Paths.get("/Users/tvdstorm/.m2/repository/"))
					.filter(Files::isRegularFile)
					.map(p -> p.toString())
					.reduce("", (String s, String a) -> s + ":" + a);
		} catch (IOException e1) {
			throw new CompiletimeException("Cannot read directory with dependencies");
		}

		String[] compileOptions = new String[] { "-cp", COMPILETIME_CP + dependenciesString, "-d", COMPILE_OUTPUT_DIR };

		pathname = GENERATED_DIR + pathname;

		Iterable<String> compilationOptions = asList(compileOptions);

//		 LogManager.getLogger().info("Running the compiler with the following command: " + 
//				 Stream.of(compileOptions).reduce("", (String s, String a) -> s + " " + a));

		JavaCompiler compiler = getSystemJavaCompiler();

		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

		StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, Locale.getDefault(), null);

		Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(new File(pathname));

		Boolean result = compiler.getTask(null, fileManager, diagnostics, compilationOptions, null, compilationUnits)
				.call();

		if (!result)
			throw new CompiletimeException(diagnostics.getDiagnostics());

		try {
			fileManager.close();
		} catch (IOException e) {
			throw new CompiletimeException(e);
		}
	}

	// Inspired by JTREG
	// (http://hg.openjdk.java.net/code-tools/jtreg/file/tip/src/share/classes/com/sun/javatest/regtest/Main.java)
	protected String runJVM(String test) throws IOException, InterruptedException {
		Runtime r = Runtime.getRuntime();
		Process p = null;

		try {
			List<String> listArgs = new ArrayList<String>();

			String jdk = discoverJDK();

			listArgs.add(jdk);
			listArgs.add("-cp");
			listArgs.add(RUNTIME_CP);
			listArgs.add(PACKAGE_NAME + test);

			String[] res = new String[listArgs.size()];

			// Execute java runtime.
			p = r.exec(listArgs.toArray(res));

			// Hold the output in a string.
			StringWriter sw = new StringWriter();
			PrintWriter accumulator = new PrintWriter(sw);

			InputStream childOut = p.getInputStream();
			StreamCopier childOutCopier = new StreamCopier(childOut, accumulator);
			childOutCopier.start();

			InputStream childErr = p.getErrorStream();
			StreamCopier childErrCopier = new StreamCopier(childErr, err);
			childErrCopier.start();

			OutputStream childIn = p.getOutputStream();
			if (childIn != null)
				childIn.close();

			childOutCopier.waitUntilDone();
			childErrCopier.waitUntilDone();

			int exitCode = p.waitFor();

			if (exitCode != 0)
				throw new RuntimeException("Exit Value: " + p.exitValue());

			p = null;

			childOut.close();
			childErr.close();

			return sw.toString().trim();
		} catch (IOException | InterruptedException e) {
			throw e;
		} finally {
			if (p != null)
				p.destroy();
		}
	}

	public void compile(String test) throws CompiletimeException {
		runCompiler(test + ".java");
	}

	public String compileAndRun(String test) throws CompiletimeException, RuntimeException {
		compile(test);
		try {
			return runJVM(test);
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private String discoverJDK() {
		String s = System.getenv("JAVA_HOME");

		if (s == null || s.length() == 0)
			s = System.getProperty("java.home");

		s += "/bin/java";
		return s;
	}

	// Inspired by JTREG
	// (http://hg.openjdk.java.net/code-tools/jtreg/file/tip/src/share/classes/com/sun/javatest/regtest/Main.java)
	static class StreamCopier extends Thread {
		private static int serial;
		private BufferedReader in;
		private PrintWriter out;
		private boolean done;

		/**
		 * Create one.
		 * 
		 * @param from
		 *            the stream to copy from
		 * @param to
		 *            the stream to copy to
		 */
		StreamCopier(InputStream from, PrintWriter to) {
			super(Thread.currentThread().getName() + "_StreamCopier_" + (serial++));
			in = new BufferedReader(new InputStreamReader(from));
			out = to;
		}

		/**
		 * Set the thread going.
		 */
		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			try {
				String line;
				while ((line = in.readLine()) != null) {
					out.println(line);
				}
			} catch (IOException e) {
			}
			synchronized (this) {
				done = true;
				notifyAll();
			}
		}

		public synchronized boolean isDone() {
			return done;
		}

		/**
		 * Blocks until the copy is complete, or until the thread is interrupted
		 */
		public synchronized void waitUntilDone() throws InterruptedException {
			boolean interrupted = false;

			// poll interrupted flag, while waiting for copy to complete
			while (!(interrupted = Thread.interrupted()) && !done)
				wait(1000);

			if (interrupted)
				throw new InterruptedException();
		}

	}

}
