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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

import org.junit.Before;
import org.junit.BeforeClass;
import org.rascalmpl.values.ValueFactoryFactory;

public class BaseTest {
	protected PrintWriter out;
	protected PrintWriter err;

	private static boolean generate_sources = false;

	private static final String RECAF_DESUGAR_SRC = "/../recaf-desugar/src/";
	private static final String SRC_TEST_RESOURCES = "src/test-generated/";

	@BeforeClass
	public static void init() {
		if (!generate_sources) {
			RascalModuleRunner runner = new RascalModuleRunner(new PrintWriter(System.out, false),
					new PrintWriter(System.err, false));
			try {
				runner.addRascalSearchPathContributor(
						ValueFactoryFactory.getValueFactory().sourceLocation("cwd", "", RECAF_DESUGAR_SRC));
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
			try {
				runner.run("lang::recaf::DesugarMain",
						new String[] { "cwd:///../recaf-desugar/input", "cwd:///" + SRC_TEST_RESOURCES });
			} catch (IOException e) {
				e.printStackTrace();
			}
			generate_sources = true;
		}
	}

	@Before
	public void setUp() throws Exception {
		out = new PrintWriter(System.out, false);
		err = new PrintWriter(System.err, false);
	}

	protected void runCompiler(String pathname) throws CompiletimeException {
		String[] compileOptions = new String[] { "-cp", "src/main/java/:" + SRC_TEST_RESOURCES };

		pathname = SRC_TEST_RESOURCES + pathname;

		Iterable<String> compilationOptions = asList(compileOptions);

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
			listArgs.add(SRC_TEST_RESOURCES + ":target/classes/");
			listArgs.add(test);

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