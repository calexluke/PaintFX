package com.calexluke;

public class CopyAndDragTool extends LassoTool {

    protected LassoDrawOperation createLassoDrawOperation() {
        return new CopyAndDragDrawOperation(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight);
    }
}
