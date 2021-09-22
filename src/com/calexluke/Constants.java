package com.calexluke;

public class Constants {

    static public String logoImageFilePath = "/com/calexluke/Assets/PAIN(t).png";
    static public int defaultSceneHeight = 1000;
    static public int defaultSceneWidth = 1000;
    static public int imageInsetValue = 40;
    static public int DEFAULT_FONT_SIZE = 20;

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
            PaintFX Version 1.1.0 09/16/2021
                                                    
            New Features:
            	•	User can draw a line and use pencil tool on the image
            	•	User can the line's width and color
            	•	User can save (and save as) the image to file
            	•	Scroll bars to ensure image stays in the frame
            	•	“help” and “about” menu options
            	•	Auto-scale image to scene size
                                                    
            Known Issues:
            	•	Zoom menu items not currently functional
            	•	The pencil look produces some jagged-looking lines when making very sharp curves on the widest line-width setting
                                                    
            Expected Next Sprint:
            	•	Zoom in, zoom out
            	•	Undo
            	•	Eraser tool
                                                    
            Links:
            	•	https://github.com/calexluke/PaintFX
                                        """;



}
