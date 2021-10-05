package com.calexluke;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaintFxToolbarTest {

    // hacky work-around from https://www.youtube.com/watch?v=vr3D3cUGeis
    // force JavaFX to initialize so the test can build
    private JFXPanel panel = new JFXPanel();

    @Test
    void getTimerString() {
        StateManager stateManager = new StateManager();
        PaintFxToolbar toolbar = new PaintFxToolbar(stateManager);

        String result = toolbar.getTimerString(122);
        assertEquals("2:02", result);

        result = toolbar.getTimerString(131);
        assertEquals("2:11", result);

        result = toolbar.getTimerString(0);
        assertEquals("0:00", result);

        result = toolbar.getTimerString(119);
        assertEquals("1:59", result);
    }
}