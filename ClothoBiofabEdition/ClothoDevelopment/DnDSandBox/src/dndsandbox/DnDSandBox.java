/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dndsandbox;

import java.io.Serializable;

/**
 *
 * @author jenhan
 */
public class DnDSandBox {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DnDFrame someFrame = new DnDFrame();
        System.out.println(someFrame instanceof MyInterface);
        Object o=someFrame;
        ((MyInterface) o).moo();
        someFrame.pack();
        someFrame.setVisible(true);
    }
}
