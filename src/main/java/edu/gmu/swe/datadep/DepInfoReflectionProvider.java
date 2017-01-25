package edu.gmu.swe.datadep;

import java.lang.reflect.Field;
import java.util.Iterator;

import com.thoughtworks.xstream.converters.reflection.ObjectAccessException;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;

import edu.gmu.swe.datadep.struct.WrappedPrimitive;

/*
 * FIXME Shall we introduce a black mechanism here as well. This is to avoid plenty of: 
 * Could not get dep info field class java.lang.reflect.Field.iParam
 */
public class DepInfoReflectionProvider extends PureJavaReflectionProvider {
	@Override
	public void visitSerializableFields(Object object, Visitor visitor) {
		for (Iterator iterator = fieldDictionary.fieldsFor(object.getClass()); iterator.hasNext();) {
			Field field = (Field) iterator.next();
			if (!fieldModifiersSupported(field)) {
				continue;
			}

			// Can we filter out stuff from java.lang.reflect package?
			if (field.getClass().getPackage().getName().contains("java.lang.reflect"))
				continue;

			validateFieldAccess(field);
			try {
				Object value = field.get(object);
				if (field.getType().isPrimitive() || field.getType().isAssignableFrom(String.class)) {

					Field depField = field.getDeclaringClass().getDeclaredField(field.getName() + "__DEPENDENCY_INFO");
					depField.setAccessible(true);
					DependencyInfo dep = (DependencyInfo) depField.get(object);
					value = new WrappedPrimitive(value, dep);
				}
				visitor.visit(field.getName(), field.getType(), field.getDeclaringClass(), value);
			} catch (IllegalArgumentException e) {
				throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), e);
			} catch (IllegalAccessException e) {
				throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), e);
			} catch (NoSuchFieldException e) {
				System.out
						.println("[WARNING] Could not get dep info field " + field.getClass() + "." + field.getName());
				continue;
				// throw new ObjectAccessException("Could not get dep info field
				// " + field.getClass() + "." + field.getName(), e);
			} catch (SecurityException e) {
				throw new ObjectAccessException(
						"Could not get dep info field " + field.getClass() + "." + field.getName(), e);
			}
		}
	}
}
