package com.calexluke;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShapeToolTest {

    @Test
    void calculateRelativeShapeParameters() {
        ShapeTool tool = new RectTool();
        Canvas canvas = new Canvas(200, 200);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        tool.startX = 100;
        tool.startY = 100;

        tool.calculateRelativeShapeParameters(150, 150, gc);
        assertEquals(100.0 / 200.0, tool.relativeTopLeftX);
        assertEquals(100.0 / 200.0, tool.relativeTopLeftY);
        assertEquals(50.0 / 200.0, tool.relativeWidth);
        assertEquals(50.0 / 200.0, tool.relativeHeight);

        tool.calculateRelativeShapeParameters(50, 50, gc);
        assertEquals(50.0 / 200.0, tool.relativeTopLeftX);
        assertEquals(50.0 / 200.0, tool.relativeTopLeftY);

        tool.calculateRelativeShapeParameters(125, 50, gc);
        assertEquals(100.0 / 200.0, tool.relativeTopLeftX);
        assertEquals(50.0 / 200.0, tool.relativeTopLeftY);
        assertEquals(25.0 / 200.0, tool.relativeWidth);
        assertEquals(50.0 / 200.0, tool.relativeHeight);

        tool.calculateRelativeShapeParameters(50, 125, gc);
        assertEquals(50.0 / 200.0, tool.relativeTopLeftX);
        assertEquals(100.0 / 200.0, tool.relativeTopLeftY);
        assertEquals(50.0 / 200.0, tool.relativeWidth);
        assertEquals(25.0 / 200.0, tool.relativeHeight);
    }
}