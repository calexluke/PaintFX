package com.calexluke;

public class CropTool extends LassoTool {
    protected LassoDrawOperation createLassoDrawOperation() {
        return new CutAndDragDrawOperation(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight);
    }
}
