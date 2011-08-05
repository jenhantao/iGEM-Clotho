
package org.clothocore.widget.fabdash;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.clothocore.api.core.Collator;
import org.clothocore.api.core.wrapper.ToolWrapper;
import org.openide.util.actions.Presenter;

/**
 *
 * @author jcanderson_Home
 */
public class FileMenuPopulator extends AbstractAction implements Presenter.Menu {

    @Override
    public JMenuItem getMenuPresenter() {
        JMenu m = new JMenu("File");
        ArrayList<ToolWrapper> listy = Collator.getAllTools();
        //Additional ArrayList and HashMap is for sorting apps alphabetically
        HashMap<String, ToolWrapper> hm = new HashMap(listy.size());
        ArrayList<String> listyNames = new ArrayList(listy.size());
        for (ToolWrapper tw : listy) {
            hm.put(tw.getDisplayName(), tw);
            listyNames.add(tw.getDisplayName());
        }
        Collections.sort(listyNames);

        for (final String name : listyNames) {
            final ToolWrapper wrappedTool = hm.get(name);
            JMenuItem toolitem = new JMenuItem(wrappedTool.getDisplayName());
            toolitem.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    wrappedTool.launchTool();
                }
            });
            m.add(toolitem);
        }
        return m;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
