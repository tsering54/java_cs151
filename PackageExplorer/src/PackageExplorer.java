import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PackageExplorer {

	private String packageName;
	private List<ClassData> classes;
	Scanner scanner;
	private Map<Class, ClassData> classMap;
	private Map<String, List<String>> clientMap = new HashMap<String, List<String>>();// storing
																						// clients
																						// of
																						// classes

	public PackageExplorer(String packageName) {
		this.packageName = packageName;
		this.classes = new ArrayList<ClassData>();
		this.scanner = new Scanner(System.in);
		this.classMap = new HashMap<Class, ClassData>();
	}

	// adding metadata of classes to ClassData
	private ClassData saveClassInfo(Class clazz) {
		ClassData meta = new ClassData(clazz.getName(), clazz.getSuperclass().getTypeName());
		classes.add(meta);
		for (Class tmp : clazz.getInterfaces()) {
			meta.addInterface(tmp.getTypeName());
		}

		for (Field field : clazz.getDeclaredFields()) {
			FieldData fieldMeta = new FieldData(field.getName(), field.getType().getName());
			meta.addField(fieldMeta);
		}

		for (Method method : clazz.getDeclaredMethods()) {
			meta.addMethod(method.getName());
		}

		return meta;
	}

	// loading initial data
	private void init() throws ClassNotFoundException, MalformedURLException {
		File packageDir = new File(packageName);
		File[] files = packageDir.listFiles();
		for (File f : files) {
			String fname = f.getName();
			if (!fname.endsWith(".java")) {
				continue;
			}

			String className = fname.substring(0, fname.lastIndexOf('.')); // getting
																			// the
																			// class
																			// name
			Class clazz = this.getClass().getClassLoader().loadClass(className); // loading
																					// class
			ClassData meta = saveClassInfo(clazz); // add meta data of class
			classMap.put(clazz, meta); // add class and meta data to a hashmap
		}

		Set<String> packageClasses = new HashSet<String>(); // adding the
															// classes in the
															// package to list
		for (Class clazz : classMap.keySet()) {
			packageClasses.add(clazz.getTypeName());
		}
		// for every class in classmap load the meta data,get the field type
		// from the metadata. if the field type ia a class in package, add it to
		// the list of providers
		for (Class clazz : classMap.keySet()) {
			ClassData meta = classMap.get(clazz);
			for (Field field : clazz.getDeclaredFields()) {
				if (packageClasses.contains(field.getType().getTypeName())) {
					meta.addProvider(field.getType().getName());
				}
			}
		}
		// take the provider of each class, add the class as client to the
		// clients list of the provider
		for (ClassData meta : classes) {
			for (String provider : meta.getProviders()) {
				List<String> clients = clientMap.get(provider);
				if (clients == null) { // if no clients list is present, create
										// it
					clients = new ArrayList<String>();
					clientMap.put(provider, clients);
				}
				clients.add(meta.getName());
			}
		}

	}

	private void saveClassInfoToXML(ClassData[] metaObjects, String fname) {
		FileOutputStream fos = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			Element root = doc.createElement("classes");
			doc.appendChild(root);

			for (ClassData meta : metaObjects) {
				Element classElem = doc.createElement("class");
				root.appendChild(classElem);

				classElem.setAttribute("name", meta.getName());
				classElem.setAttribute("superclass", meta.getSuperClassName());

				Element interfaceElement = doc.createElement("interfaces");
				classElem.appendChild(interfaceElement);
				for (String interfaceName : meta.getInterfaces()) {
					Element child = doc.createElement("interface");
					interfaceElement.appendChild(child);
					child.setAttribute("name", interfaceName);
				}

				Element methodElement = doc.createElement("methods");
				classElem.appendChild(methodElement);
				for (String method : meta.getMethods()) {
					Element child = doc.createElement("method");
					methodElement.appendChild(child);
					child.setAttribute("name", method);
				}

				Set<String> providers = new HashSet<String>(meta.getProviders());
				Element fieldElement = doc.createElement("fields");
				classElem.appendChild(fieldElement);
				for (FieldData field : meta.getFields()) {
					Element child = doc.createElement("field");
					fieldElement.appendChild(child);
					child.setAttribute("name", field.getName());
					child.setAttribute("type", field.getType());
					boolean isProvider = providers.contains(field.getType());
					if (isProvider) {
						child.setAttribute("provider", "true");
					}
				}
			}

			Transformer trans = TransformerFactory.newInstance().newTransformer();
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			fos = new FileOutputStream(fname);
			trans.transform(new DOMSource(doc), new StreamResult(fos));
			System.out.println("Saved class information as " + fname);
		} catch (Exception e) {
			System.exit(-1);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
		}

	}

	private void saveClasses() { // save class mrtadata to xml
		System.out.print("Enter file name: ");
		String fname = scanner.next();
		saveClassInfoToXML(classes.toArray(new ClassData[0]), fname);
	}

	private void showClass(ClassData meta) {
		System.out.println("Class Details:");
		System.out.printf("Name: %s\n", meta.getName());
		System.out.printf("Superclass: %s\n", meta.getSuperClassName());
		System.out.println("Interfaces:");
		for (String interfaceName : meta.getInterfaces()) {
			System.out.printf("      %s\n", interfaceName);
		}
		System.out.println("Fields:");
		for (FieldData field : meta.getFields()) {
			System.out.printf("      %s\n", field.getName());
		}
		System.out.println("Methods:");
		for (String method : meta.getMethods()) {
			System.out.printf("      %s\n", method);
		}
		System.out.println("Providers:");
		for (String provider : meta.getProviders()) {
			System.out.printf("      %s\n", provider);
		}
		System.out.println("Clients:");
		List<String> clients = clientMap.get(meta.getName());
		if (clients != null) {
			for (String client : clients) {
				System.out.printf("      %s\n", client);
			}
		}

		System.out.print("Enter s to save or m for main menu: ");
		String choice = scanner.next();
		if (choice.equals("m")) {
			return;
		} else if (choice.equals("s")) {
			saveClassInfoToXML(new ClassData[] { meta }, meta.getName() + ".xml");
		} else {
			System.exit(-1);
		}
	}

	private void listClasses() {
		System.out.println("List of classes");
		System.out.println("---------------");
		for (int i = 0; i < classes.size(); i++) {
			System.out.printf("%2d. %s\n", i + 1, classes.get(i).getName());
		}

		System.out.printf("Enter (1-%d) to view details or m for main menu: ", classes.size());
		String choice = scanner.next();
		if (choice.equals("m")) {
			return;
		} else {
			int indx = Integer.parseInt(choice) - 1;
			ClassData meta = classes.get(indx);
			showClass(meta);
		}
	}

	private void viewClass() {
		System.out.print("Enter class name: ");
		String name = scanner.next();

		ClassData meta = null;
		for (ClassData tmp : classes) {
			if (tmp.getName().equals(name)) {
				meta = tmp;
				break;
			}
		}

		if (meta != null) {
			showClass(meta);
		} else {
			System.exit(-1);
		}
	}

	private void startInteraction() {

		while (true) {
			System.out.println("Welcome to the PackageExplorer - Main Menu");
			System.out.println("-------------------------------------------");
			System.out.println("1. List all classes");
			System.out.println("2. View a class");
			System.out.println("3. Save all classes");
			System.out.println("4. Load class info from xml");
			System.out.print("Enter your choice (1-4); or q to quit: ");

			String choice = scanner.next();
			if (choice.equals("1")) {
				listClasses();
			} else if (choice.equals("2")) {
				viewClass();
			} else if (choice.equals("3")) {
				saveClasses();
			} else if (choice.equals("4")) {
				System.out.println("Not implemented");
			} else if (choice.equals("q")) {
				System.out.println("Goodbye!");
				return;
			}
		}
	}

	public static void main(String[] args) throws Exception {

		String packageName = null;
		if (args.length == 0) {
			packageName = System.getProperty("user.dir");
		} else {
			packageName = args[0];
		}

		System.out.println("Exploring " + packageName);
		PackageExplorer explorer = new PackageExplorer(packageName);
		explorer.init();
		explorer.startInteraction();
	}

}
