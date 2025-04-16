package com.pmod.config.in.action.in;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

public abstract class BaseDifficultyAction<V extends Object> {
	public static enum Type {
		NOT_INIT,
		EFFECT,
		ITEM,
		MODIFIER
	}

	protected Type type;

	public final @NotNull V value;

	public final @Nullable Float random;

	public BaseDifficultyAction(final V value, final @Nullable Float random) {
		this.type = Type.NOT_INIT;
		this.value = value;
		this.random = random;
	}

	public final Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return String.format("type=%s, value=%s, random=%s", type, value, random);
	}
}
