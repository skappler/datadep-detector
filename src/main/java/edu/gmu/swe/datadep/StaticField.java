package edu.gmu.swe.datadep;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.Field;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class StaticField implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean conflict;

	// TODO What's this?
	public Element value;

	public int dependsOn;
	// TODO What's this?
	public transient Field field;

	// TODO: Why do we need serialization in the first place ?
	// This is for serialization
	private void writeObject(ObjectOutputStream o) throws IOException {
		o.defaultWriteObject();
		o.writeObject(field.getDeclaringClass());
		o.writeObject(field.getName());
	}

	// This is for serialization
	private void readObject(ObjectInputStream o) throws IOException, ClassNotFoundException {

		o.defaultReadObject();
		Class<?> fieldClass = (Class<?>) o.readObject();
		String fieldName = (String) o.readObject();
		try {
			field = fieldClass.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("WARNING StaticField.readObject() " + e.getMessage());
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("WARNING StaticField.readObject() " + e.getMessage());
		}
	}

	public StaticField(Field f) {
		this.field = f;
	}

	public boolean isConflict() {
		return conflict;
	}

	public void markConflictAndSerialize(int writeGen) {
		conflict = true;
		// Update the write of the first conflicting test
		if (writeGen > dependsOn) {
			dependsOn = writeGen;
		}
		// if (value != null)
		// return;
		try {

			// TODO Here we can prevent SF to be stored, or at least we can ONLY
			// store the relevant parts, i.e., dependsOn
			// but this might be tricky since the dependsOn might be nested down
			// somewhere !

			field.setAccessible(true);

			// TODO: Let's not serialize a thing and see ...
			// value = HeapWalker.serialize(field.get(null)); // This invoke the
			// shared XStream
			// stuff which is
			// configured to
			// } catch (IllegalArgumentException | IllegalAccessException e) {
			// e.printStackTrace();
			// }
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		// if (value == null) {
		// System.out.println(
		// "StaticField.markConflictAndSerialize() Value is null for " + field +
		// " in write gen " + writeGen);
		// }
	}

	public void clearValue() {
		if (value == null)
			return;
		// TODO: Not sure this will remove all the stuff,
		value = null;
	}

	public String getValue() {
		StringWriter sw = new StringWriter();
		XMLOutputter out = new XMLOutputter();

		try {
			if (value != null && value.getContent().size() > 0) {
				Element e = (Element) value.getContent().get(0);
				out.output(new Document(e.detach()), sw);
				out.setFormat(Format.getPrettyFormat());
			} else {
				if (value == null) {
					System.out.println("StaticField.getValue() Null value for " + this);
				} else {
					System.out.println("StaticField.getValue() Empty value for " + this);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sw.toString();
	}

	@Override
	public String toString() {
		return "StaticField [field=" + field + ", conflict=" + conflict + ", value="
				+ ((value != null || value.getContent().size() > 0) ? "present" : "null") + ", dependsOn=" + dependsOn
				+ "]";
	}

	public void clearConflict() {
		conflict = false;
		// value = null; --> Use clear value instead
		dependsOn = 0;
		clearValue();
	}
}
