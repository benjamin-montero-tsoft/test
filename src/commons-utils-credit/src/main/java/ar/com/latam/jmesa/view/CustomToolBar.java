package ar.com.latam.jmesa.view;

import java.util.List;

import org.jmesa.view.ViewUtils;
import org.jmesa.view.component.Row;
import org.jmesa.view.html.HtmlConstants;
import org.jmesa.view.html.toolbar.AbstractToolbar;
import org.jmesa.view.html.toolbar.ToolbarItemType;

public class CustomToolBar extends AbstractToolbar {

    @SuppressWarnings("unchecked")
    @Override
    public String render() {
                
        if (hasToolbarItems()) { // already has items
            return super.render();
        }
        
        addToolbarItem(ToolbarItemType.PREV_PAGE_ITEM);

        addToolbarItem(ToolbarItemType.NEXT_PAGE_ITEM);
        
        boolean exportable = ViewUtils.isExportable(getExportTypes());

        if (exportable && enableSeparators) {
            addToolbarItem(ToolbarItemType.SEPARATOR);
        }
        
        if (exportable) {
            addExportToolbarItems(getExportTypes());
        }
        
        Row row = getTable().getRow();
        List columns = row.getColumns();
        
        boolean filterable = ViewUtils.isFilterable(columns);

        if (filterable && enableSeparators) {
            addToolbarItem(ToolbarItemType.SEPARATOR);
        }

        if (filterable) {
            addToolbarItem(ToolbarItemType.FILTER_ITEM);
            addToolbarItem(ToolbarItemType.CLEAR_ITEM);
        }
        
        boolean editable = ViewUtils.isEditable(getCoreContext().getWorksheet());
        if (editable) {
            if (enableSeparators) {
                addToolbarItem(ToolbarItemType.SEPARATOR);
            }

            addToolbarItem(ToolbarItemType.SAVE_WORKSHEET_ITEM);
            addToolbarItem(ToolbarItemType.CLEAR_WORKSHEET_ITEM);
            if (getCoreContext().getPreference(HtmlConstants.TOOLBAR_ADD_WORKSHEET_ROW_ENABLED).equals("true")) {
                addToolbarItem(ToolbarItemType.ADD_WORKSHEET_ROW_ITEM);
            }
            addToolbarItem(ToolbarItemType.FILTER_WORKSHEET_ITEM);
        }

        return super.render();
    }
}
