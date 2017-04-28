/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.Color;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import static ui.NewsLetterUI.jColorChooser;
import static ui.NewsLetterUI.jTextFieldColorPicked;

/**
 *
 * @author Daniel
 */
public class ColorSelection implements ChangeListener {
    public void stateChanged(ChangeEvent e) {
        Color color = jColorChooser.getColor();
        String rgb = Integer.toHexString(color.getRGB());
        rgb = rgb.substring(2, rgb.length());
        jTextFieldColorPicked.setText("#"+rgb);
        //System.out.println(rgb);
    }
}