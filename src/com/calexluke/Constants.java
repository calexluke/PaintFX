package com.calexluke;

public class Constants {

    static public String ASSETS_FILE_PATH = "/com/calexluke/Assets/";
    static public String LOGS_FILE_PATH = "Logs/log.txt";

    static public String LOGO_IMAGE_PATH = ASSETS_FILE_PATH + "PAIN(t).png";

    // open source icons from https://www.flaticon.com/
    static public String CURSOR_ICON_PATH = ASSETS_FILE_PATH + "cursor.png";
    static public String PENCIL_ICON_PATH = ASSETS_FILE_PATH + "pencil.png";
    static public String ERASER_ICON_PATH = ASSETS_FILE_PATH + "double-sided-eraser.png";
    static public String LINE_ICON_PATH = ASSETS_FILE_PATH + "diagonal-line.png";
    static public String SHAPE_ICON_PATH = ASSETS_FILE_PATH + "shapes.png";
    static public String TEXT_ICON_PATH = ASSETS_FILE_PATH + "font.png";
    static public String LASSO_ICON_PATH = ASSETS_FILE_PATH + "selection.png";
    static public String COLOR_GRAB_ICON_PATH = ASSETS_FILE_PATH + "color-picker.png";

    static public int defaultSceneHeight = 1000;
    static public int defaultSceneWidth = 1000;
    static public int imageInsetValue = 40;
    static public int DEFAULT_FONT_SIZE = 20;
    static public int DEFAULT_ICON_HEIGHT = 25;

    static public String TIMER_PREFS_KEY = "timerValue";

    static public String SQUARE_SHAPE = "Square";
    static public String RECTANGLE_SHAPE = "Rectangle";
    static public String ROUNDED_RECT_SHAPE = "Rounded Rectangle";
    static public String CIRCLE_SHAPE = "Circle";
    static public String OVAL_SHAPE = "Oval";
    static public String POLYGON_SHAPE = "Polygon";

    static public String CUT_AND_DRAG = "Cut and Drag";
    static public String COPY_AND_DRAG = "Copy and Drag";
    static public String CROP = "Crop";

    static public String aboutAlertTitle = "About Pain(t)";
    static public String helpAlertTitle = "How to Use";

    static public String helpAlertText = """
                                        
                                        Select one of the drawing tools on the left to draw lines or shapes on the image.
                                        Hover over any tool for more info.
                                        
                                        Choose the width and color of the line with the color
                                        picker and combo box, or the number of sides for the polygon tool.
                                        
                                        Image load and save options can be found in the File Menu.
                                        Zoom and scale options are found in the view menu.
                                        
                                        The program will auto-save every 2 minutes by default.
                                        You can adjust this auto-save time with the slider at the bottom of the toolbar.
                                   
                                        """;

    static public String aboutAlertText = """
            PaintFX Version 1.2.0 09/24/2021
                        
            New Features:
            	•	User can draw a shapes on the image (square, rectangle, circle, oval)
            	•	User can choose stroke and fill color for shapes
            	•	User can type text on the image
            	•	App will prompt user to save if they try to quit with unsaved changes
            	•	Added support for .bmp file type
            	•	Keyboard shortcuts for save, quit, zoom in, and zoom out
                •	Added color grabber tool
                •	User can dynamically resize the image and drawings, as well as zoom in and out
                •	User can open images in multiple tabs
                        
            Known Issues:
            	•	The text tool text box extends offscreen to the right
                        
            Expected Next Sprint:
            	•	Undo
            	•	Eraser tool
                        
            Links:
            	•	https://github.com/calexluke/PaintFX
                                        """;



}
