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
	public Element value;
	public int dependsOn;
	public transient Field field;

	private void writeObject(ObjectOutputStream o) throws IOException {

		o.defaultWriteObject();
		o.writeObject(field.getDeclaringClass());
		o.writeObject(field.getName());
	}

	private void readObject(ObjectInputStream o) throws IOException, ClassNotFoundException {

		o.defaultReadObject();
		Class<?> fieldClass = (Class<?>) o.readObject();
		String fieldName = (String) o.readObject();
		try {
			field = fieldClass.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		if (writeGen > dependsOn)
			dependsOn = writeGen;
		// if (value != null)
		// return;
		try {
			field.setAccessible(true);
			value = HeapWalker.serialize(field.get(null));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		if (value == null) {
			System.out.println(
					"StaticField.markConflictAndSerialize() Value is null for " + field + " in write gen " + writeGen);
		}
	}

	public String getValue() {
		StringWriter sw = new StringWriter();
		XMLOutputter out = new XMLOutputter();

		try {
			if (value != null) {
				Element e = (Element) value.getContent().get(0);
				out.output(new Document(e.detach()), sw);
				out.setFormat(Format.getPrettyFormat());
			} else {
				System.out.println("StaticField.getValue() Null value for " + this);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sw.toString();
	}

	@Override
	public String toString() {
		return "StaticField [field=" + field + ", conflict=" + conflict + ", value=" + value + ", dependsOn="
				+ dependsOn + "]";
	}

	public void clearConflict() {
		conflict = false;
		value = null;
		dependsOn = 0;
	}
}
