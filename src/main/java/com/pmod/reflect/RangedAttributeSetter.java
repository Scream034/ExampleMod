package com.pmod.reflect;

import java.lang.reflect.Field;

import org.jetbrains.annotations.NotNull;

import com.pmod.Main;

public final class RangedAttributeSetter {
	public static transient final String FIELD_MIN_VALUE = "minValue";
	public static transient final String FIELD_MAX_VALUE = "maxValue";

	public static boolean setField(@NotNull final Object obj, final String fieldName, @NotNull final Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(obj, value);
			Main.Log.info("Successfully set field " + fieldName + " on " + obj.getClass().getName() + " to " + value);
			return true;
		} catch (Exception e) {
			Main.Log.error("Failed to set field " + fieldName + " on " + obj.getClass().getName() + ": " + e.getCause());
			return false;
		}
	}
}
