package edu.gmu.swe.datadep;

import java.lang.reflect.Field;

public class StaticFieldDependency {
	public Field field;
	public String value;
	public int depGen;
	public String on; // Textual description

	@Override
	public String toString() {
		return field.toString() + ", dependsOn " + on + "(" + depGen + ")" + ", value: " + value;
	}
}
