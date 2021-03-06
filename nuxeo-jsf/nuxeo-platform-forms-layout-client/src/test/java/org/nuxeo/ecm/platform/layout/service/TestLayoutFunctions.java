/*
 * (C) Copyright 2010-2017 Nuxeo (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Anahide Tchertchian
 */
package org.nuxeo.ecm.platform.layout.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.nuxeo.ecm.platform.forms.layout.api.Layout;
import org.nuxeo.ecm.platform.forms.layout.api.LayoutRow;
import org.nuxeo.ecm.platform.forms.layout.facelets.library.LayoutTagLibrary;
import org.nuxeo.ecm.platform.forms.layout.service.WebLayoutManager;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.NXRuntimeTestCase;

/**
 * @author Anahide Tchertchian
 */
public class TestLayoutFunctions extends NXRuntimeTestCase {

    private WebLayoutManager service;

    @Override
    public void setUp() throws Exception {
        deployBundle("org.nuxeo.ecm.platform.forms.layout.core");
        deployContrib("org.nuxeo.ecm.platform.forms.layout.client", "OSGI-INF/layouts-framework.xml");
        deployContrib("org.nuxeo.ecm.platform.forms.layout.client.tests", "layouts-listing-test-contrib.xml");
    }

    @Override
    protected void postSetUp() throws Exception {
        service = Framework.getService(WebLayoutManager.class);
        assertNotNull(service);
    }

    @Test
    public void testRowSelectionFunctions() {
        // row selection needs to be tested against a layout with all rows
        // selected by default
        Layout layout = service.getLayout(null, "search_listing_ajax", "edit_columns", "", null, true);
        assertNotNull(layout);

        List<LayoutRow> selectedRows = LayoutTagLibrary.getSelectedRows(layout, null, false);
        assertNotNull(selectedRows);
        assertEquals(3, selectedRows.size());
        assertEquals("title_link", selectedRows.get(0).getName());
        assertEquals("modification_date", selectedRows.get(1).getName());
        assertEquals("lifecycle", selectedRows.get(2).getName());

        // again with always selected rows
        selectedRows = LayoutTagLibrary.getSelectedRows(layout, null, true);
        assertNotNull(selectedRows);
        assertEquals(4, selectedRows.size());
        assertEquals("selection", selectedRows.get(0).getName());
        assertEquals("title_link", selectedRows.get(1).getName());
        assertEquals("modification_date", selectedRows.get(2).getName());
        assertEquals("lifecycle", selectedRows.get(3).getName());

        List<LayoutRow> notSelectedRows = LayoutTagLibrary.getNotSelectedRows(layout, null);
        assertEquals(3, notSelectedRows.size());
        assertEquals("description", notSelectedRows.get(0).getName());
        assertEquals("subjects", notSelectedRows.get(1).getName());
        assertEquals("rights", notSelectedRows.get(2).getName());

        // select some rows and re-do tests
        List<String> selectedRowNames = Arrays.asList("modification_date", "title_link", "description");

        selectedRows = LayoutTagLibrary.getSelectedRows(layout, selectedRowNames, false);
        assertNotNull(selectedRows);
        assertEquals(3, selectedRows.size());
        assertEquals("modification_date", selectedRows.get(0).getName());
        assertEquals("title_link", selectedRows.get(1).getName());
        assertEquals("description", selectedRows.get(2).getName());

        // again with always selected rows
        selectedRows = LayoutTagLibrary.getSelectedRows(layout, selectedRowNames, true);
        assertNotNull(selectedRows);
        assertEquals(4, selectedRows.size());
        assertEquals("selection", selectedRows.get(0).getName());
        assertEquals("modification_date", selectedRows.get(1).getName());
        assertEquals("title_link", selectedRows.get(2).getName());
        assertEquals("description", selectedRows.get(3).getName());

        notSelectedRows = LayoutTagLibrary.getNotSelectedRows(layout, selectedRowNames);
        assertEquals(3, notSelectedRows.size());
        assertEquals("lifecycle", notSelectedRows.get(0).getName());
        assertEquals("subjects", notSelectedRows.get(1).getName());
        assertEquals("rights", notSelectedRows.get(2).getName());
    }

}
