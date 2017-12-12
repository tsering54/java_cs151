
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class data
 *
 */
public class ClassData {

	private String name;
	private String superClassName;
	private List<String> interfaces = new ArrayList<String>();
	private List<FieldData> fields = new ArrayList<FieldData>();
	private List<String> methods = new ArrayList<String>();
	private Set<String> providers = new HashSet<String>();

	public ClassData(String name, String superClassName) {
		this.name = name;
		this.superClassName = superClassName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSuperClassName() {
		return superClassName;
	}

	public void setSuperClassName(String superClassName) {
		this.superClassName = superClassName;
	}

	public Collection<String> getInterfaces() {
		return interfaces;
	}

	public void addInterface(String interfaceName) {
		this.interfaces.add(interfaceName);
	}

	public Collection<FieldData> getFields() {
		return fields;
	}

	public void addField(FieldData field) {
		this.fields.add(field);
	}

	public void addMethod(String method) {
		this.methods.add(method);
	}

	public Collection<String> getMethods() {
		return this.methods;
	}

	public Collection<String> getProviders() {
		return providers;
	}

	public void addProvider(String provider) {
		this.providers.add(provider);
	}

}
