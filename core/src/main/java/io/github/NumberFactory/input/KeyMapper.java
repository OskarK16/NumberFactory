package io.github.NumberFactory.input;

import com.badlogic.gdx.Input;
import java.util.HashMap;
import java.util.Map;

public class KeyMapper {

    private static final Map<Integer, GameAction> keyBinds = new HashMap<>();

    static {
        keyBinds.put(Input.Keys.ENTER, GameAction.EDIT_COMMIT);
        keyBinds.put(Input.Keys.ESCAPE, GameAction.CANCEL_OR_CLOSE);
        keyBinds.put(Input.Keys.SPACE, GameAction.TOGGLE_SIMULATION);
        keyBinds.put(Input.Keys.TAB, GameAction.CYCLE_HOTBAR_CATEGORY);

        keyBinds.put(Input.Keys.NUM_1, GameAction.HOTBAR_SLOT_1);
        keyBinds.put(Input.Keys.NUM_2, GameAction.HOTBAR_SLOT_2);
        keyBinds.put(Input.Keys.NUM_3, GameAction.HOTBAR_SLOT_3);
        keyBinds.put(Input.Keys.NUM_4, GameAction.HOTBAR_SLOT_4);
        keyBinds.put(Input.Keys.NUM_5, GameAction.HOTBAR_SLOT_5);
        keyBinds.put(Input.Keys.NUM_6, GameAction.HOTBAR_SLOT_6);
        keyBinds.put(Input.Keys.NUM_7, GameAction.HOTBAR_SLOT_7);
        keyBinds.put(Input.Keys.NUM_8, GameAction.HOTBAR_SLOT_8);
        keyBinds.put(Input.Keys.NUM_9, GameAction.HOTBAR_SLOT_9);
    }

    public static GameAction getAction(int keycode) {
        return keyBinds.getOrDefault(keycode, GameAction.ACTION_UNKNOWN);
    }

    // Jeśli chcemy np. TAB to należy zwyczajnie dodać coś typu Button.X (na PlayStation itd).
}
