package com.calexluke;

public class CutAndDragTool extends LassoTool {

    protected LassoDrawOperation createLassoDrawOperation() {
        return new CutAndDragDrawOperation(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight);
    }

    public String toString() {
        return "Cut and Drag tool";
    }

}
