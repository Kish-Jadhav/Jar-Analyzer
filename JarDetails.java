import java.io.FileInputStream;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JarDetails {
	private static String jarFile = null;
	private static ArrayList<String> listOfNoClassDef = null;
	private static ArrayList<String> listOfMethods = null;
	// listOfClasses
	private static ArrayList<String> listOfClasses = null;
	private static ArrayList<String> listOfClassNotFound = null;
	private static ArrayList<String> unInitializedClasses = null;
	

	@SuppressWarnings("resource")
	public static ArrayList<String> getClassNamesFromJar() {
		ArrayList<String> classes = new ArrayList<String>();
		try {
			JarInputStream JarFileIS = new JarInputStream(
					new FileInputStream(jarFile));
			JarEntry jarEntry;

			while (true) {
				jarEntry = JarFileIS.getNextJarEntry();
				if (jarEntry == null) {
					break;
				}
				if ((jarEntry.getName().endsWith(".class"))) {
					String className = jarEntry.getName().replaceAll("/",
							"\\.");
					String myClass = className.substring(0,
							className.lastIndexOf('.'));
					classes.add(myClass);

				}
			}
		} catch (Exception e) {
			System.out.println("Oops.. Encounter an issue while parsing jar"
					+ e.toString());
		}
		return classes;
	}

	public static ArrayList<String> getPublicMethodNamesFromClasses(
			ArrayList<String> listOfClasses) {
		ArrayList<String> publicMethods = new ArrayList<String>();
		listOfNoClassDef = new ArrayList<String>();

		for (String className : listOfClasses) {

			Class<?> actualClass = null;
			try {
				//System.out.println(className);
				actualClass = Class.forName(className);
				if (actualClass != null) {
					Method[] main = actualClass.getDeclaredMethods();
					for (Method method : main) {
						if (Modifier.toString(method.getModifiers()).equals(
								"public")) {
							publicMethods.add("public " + method.getName());
						}
					}
				}
			}  catch (ClassNotFoundException e) {
				continue;
				//listOfClassNotFound.add(e.getMessage());
			} catch (NoClassDefFoundError e) {
				listOfNoClassDef.add(e.getMessage());
				continue;
			}catch(ExceptionInInitializerError e){
				//unInitializedClasses.add(className);
				continue;
			} catch(Exception e){
				continue;
			}
			
		}

		return publicMethods;
	}

	public static String printIntoFile(ArrayList<String> values, String fileName) {

		String outFile = null;
		try {
			outFile = Files.write(Paths.get(fileName), values).getFileName()
					.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outFile;

	}

	public static void main(String[] args) {

		if (args.length < 1) {
			showUsage();
		}
		extractProperties(args);
		listOfClasses = getClassNamesFromJar();
		listOfMethods = getPublicMethodNamesFromClasses(listOfClasses);
		listOfMethods.size();

		System.out.println("#####################################");
		System.out.println("Total classes = " + listOfClasses.size());
		System.out.println("Total public methods = " + listOfMethods.size());

		System.out.println("#####################################");
		System.out.println("List of classes is stored into :"
				+ printIntoFile(listOfClasses, "classes.txt"));
		printIntoFile(listOfMethods, "publicmethods.txt");
		System.out.println("List of public methods is stored into :"
				+ printIntoFile(listOfMethods, "publicmethods.txt"));
		System.out
				.println("List of 'NoDefinitionFound' classes is stored into :"
						+ printIntoFile(listOfNoClassDef,
								"nodeffoundclasses.txt"));
		/*System.out.println("List of 'ClassNotFound' classes is stored into :"
				+ printIntoFile(listOfClassNotFound, "classNotFound.txt"));*/

		System.out.println("#####################################");
	}

	/**
	 * Sets required variables with passed command line arguments.
	 * 
	 * @param utilityArgs
	 *            String array of command line arguments.
	 */
	private static void extractProperties(String[] utilityArgs) {
		int idx = 0;
		while ((idx < utilityArgs.length) && utilityArgs[idx].startsWith("--")) {
			String viewerArg = utilityArgs[idx++].substring(2);
			String viewerOption = viewerArg;
			String optionValue = null;
			int equalsPos = viewerArg.indexOf('=');
			if (equalsPos >= 0) {
				viewerOption = viewerArg.substring(0, equalsPos);
				optionValue = viewerArg.substring(equalsPos + 1);
			}
			if (viewerOption.equals("jarFile")) {
				jarFile = optionValue;
			} else {
				if (!viewerOption.equals("help")) {
					System.out.println("Invalid viewer option: --"
							+ viewerOption + "\n");
				}
				showUsage();
			}
		}
		if (utilityArgs[idx - 1].equals("-?")) {
			showUsage();
		}
	}

	private static void showUsage() {
		System.out.println("\t[JarDetails options] \\\n\t"
				+ "<JarDetails> [options] [selection]");
		System.out.println("\n where [JarDetails options] are:\n");
		System.out.println("\t--jarFile=<Full path of .jar file>");
		System.out.println("\t--help (display this message and exit)");
		System.exit(1);
	}
}