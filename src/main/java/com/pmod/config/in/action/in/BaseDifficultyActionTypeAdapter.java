package com.pmod.config.in.action.in;

import com.google.errorprone.annotations.DoNotCall;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.pmod.Main;

import java.io.IOException;

public final class BaseDifficultyActionTypeAdapter extends TypeAdapter<BaseDifficultyAction<?>> {
	private final Gson gson = new Gson();

	@DoNotCall
	@Override
	public void write(final JsonWriter out, final BaseDifficultyAction<?> value) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public BaseDifficultyAction<?> read(final JsonReader in) throws IOException {
		Main.Log.info("Reading difficulty action from JSON");

		final JsonElement jsonElement = gson.fromJson(in, JsonElement.class);
		final JsonObject jsonObject = jsonElement.getAsJsonObject();
		final String typeStr = jsonObject.get("type").getAsString();
		BaseDifficultyAction.Type type = BaseDifficultyAction.Type.valueOf(typeStr.toUpperCase());

		Main.Log.info("Type: {}", type);
		BaseDifficultyAction<?> action = null;
		switch (type) {
			case EFFECT:
				action = gson.fromJson(jsonObject, EffectDifficultyAction.class);
				break;
			case ITEM:
				action = gson.fromJson(jsonObject, ItemDifficultyAction.class);
				break;
			case MODIFIER:
				action = gson.fromJson(jsonObject, ModifierDifficultyAction.class);
				break;
			default:
				Main.Log.error("Unknown difficulty action type: {}", type);
				return null;
		}
		
		// Set type because GSON deserialization doesn't set it
		action.type = type;
		return action;
	}
}