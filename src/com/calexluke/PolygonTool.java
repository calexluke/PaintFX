package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class PolygonTool extends ShapeTool {

    @Override
    protected ShapeDrawOperation createShapeOperation(GraphicsContext graphicsContext) {
        PaintFxCanvas canvas = (PaintFxCanvas) graphicsContext.getCanvas();
        int nPoints = canvas.getStateManager().getSelectedPolygonSides();
        Paint strokeColor = graphicsContext.getStroke();
        Paint fillColor = graphicsContext.getFill();

        PolygonDrawOperation polygonOp = new PolygonDrawOperation(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight,
                relativeLineWidth, strokeColor, fillColor, nPoints);
        return polygonOp;
    }
}
