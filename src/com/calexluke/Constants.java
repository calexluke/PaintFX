package com.calexluke;

public class Constants {

    static public String logoImageFilePath = "/com/calexluke/Assets/PAIN(t).png";
    static public int defaultSceneHeight = 1000;
    static public int defaultSceneWidth = 1000;
    static public int imageInsetValue = 40;
    static public int DEFAULT_FONT_SIZE = 20;
    static public String TIMER_PREFS_KEY = "timerValue";

    static public String aboutAlertTitle = "About Pain(t)";
    static public String helpAlertTitle = "How to Use";

    static public String helpAlertText = """
                                        
                                        Select the pencil or line tools to draw on an image.
                                        
                                        Choose the width and color of the line with the color
                                        picker and combo box. 
                                        
                                        Image load and save options can be found in the File Menu.
                                        
                                        Use the view menu to adjust the size of the image in the frame. 
                                   
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
