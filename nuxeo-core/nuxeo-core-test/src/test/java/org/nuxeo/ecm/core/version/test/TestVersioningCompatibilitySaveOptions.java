/*
 * (C) Copyright 2006-2017 Nuxeo (http://nuxeo.com/) and others.
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
 *     Laurent Doguin
 */
package org.nuxeo.ecm.core.version.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.VersioningOption;
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;
import org.nuxeo.runtime.test.runner.HotDeployer;
import org.nuxeo.runtime.test.runner.LocalDeploy;

public class TestVersioningCompatibilitySaveOptions extends AbstractTestVersioning {

    @Inject
    protected HotDeployer deployer;

    @Test
    public void testTypeSaveOptions() throws Exception {
        DocumentModel fileDoc = new DocumentModelImpl("File");
        fileDoc = session.createDocument(fileDoc);
        String versionLabel = fileDoc.getVersionLabel();
        assertEquals("0.0", versionLabel);
        List<VersioningOption> opts = service.getSaveOptions(fileDoc);
        assertEquals(3, opts.size());
        assertEquals(VersioningOption.NONE, opts.get(0));

        deployer.deploy("org.nuxeo.ecm.core.test.tests:test-versioning-contrib.xml");

        fileDoc = new DocumentModelImpl("File");
        fileDoc = session.createDocument(fileDoc);
        versionLabel = fileDoc.getVersionLabel();
        assertEquals("1.1+", versionLabel);
        opts = service.getSaveOptions(fileDoc);
        assertEquals(2, opts.size());
        assertEquals(VersioningOption.MINOR, opts.get(0));
        session.followTransition(fileDoc.getRef(), "approve");
        opts = service.getSaveOptions(fileDoc);
        assertEquals(0, opts.size());
        session.followTransition(fileDoc.getRef(), "backToProject");
        session.followTransition(fileDoc.getRef(), "obsolete");
        opts = service.getSaveOptions(fileDoc);
        assertEquals(3, opts.size());

        deployer.deploy("org.nuxeo.ecm.core.test.tests:test-versioning-override-contrib.xml");

        fileDoc = new DocumentModelImpl("File");
        fileDoc = session.createDocument(fileDoc);
        versionLabel = fileDoc.getVersionLabel();
        assertEquals("2.2+", versionLabel);
    }

    @Test
    @LocalDeploy("org.nuxeo.ecm.core.test.tests:test-versioning-nooptions.xml")
    public void testNoOptions() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "doc", "File");
        doc = session.createDocument(doc);

        // no options according to config
        List<VersioningOption> opts = service.getSaveOptions(doc);
        assertEquals(0, opts.size());

        doc.setPropertyValue("dc:title", "A");
        doc = session.saveDocument(doc);

        assertVersion("0.0", doc);
        assertVersionLabel("0.0", doc);
        assertLatestVersion(null, doc);
    }

}
