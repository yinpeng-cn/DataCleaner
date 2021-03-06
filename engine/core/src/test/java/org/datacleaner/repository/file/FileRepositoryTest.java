/**
 * DataCleaner (community edition)
 * Copyright (C) 2014 Free Software Foundation, Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.datacleaner.repository.file;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.datacleaner.repository.Repository;
import org.datacleaner.repository.RepositoryFile;
import org.datacleaner.repository.RepositoryFolder;
import org.datacleaner.repository.RepositoryNode;

import junit.framework.TestCase;

public class FileRepositoryTest extends TestCase {

    public void testSimpleNavigation() throws Exception {
        final Repository repository = new FileRepository(new File("src/test/resources/example_folders"));

        assertEquals("", repository.getName());
        assertEquals("/", repository.getQualifiedPath());

        final List<RepositoryFolder> folders = repository.getFolders();
        Collections.sort(folders);
        assertEquals("/folder1", folders.get(0).getQualifiedPath());

        assertEquals("/file1", repository.getFile("file1").getQualifiedPath());

        RepositoryNode node = repository.getRepositoryNode("/folder2/sub2");
        assertEquals("/folder2/sub2", node.getQualifiedPath());
        assertTrue(node instanceof RepositoryFolder);

        final List<RepositoryFile> files = ((RepositoryFolder) node).getFiles();
        assertEquals(2, files.size());
        assertEquals("/folder2/sub2/file1.xml", files.get(0).getQualifiedPath());
        assertEquals("/folder2/sub2/file2.xml", files.get(1).getQualifiedPath());
        assertEquals(RepositoryFile.Type.OTHER, files.get(0).getType());

        node = repository.getRepositoryNode("folder2/sub1/job1.analysis.xml");
        assertEquals("/folder2/sub1/job1.analysis.xml", node.getQualifiedPath());
        assertTrue(node instanceof RepositoryFile);
        assertEquals(RepositoryFile.Type.ANALYSIS_JOB, ((RepositoryFile) node).getType());
    }

    public void testCreateFileWithoutContents() throws Exception {
        final File rootFile = new File("target/example_repo");
        rootFile.mkdirs();

        if (new File(rootFile, "foo.bar").exists()) {
            new File(rootFile, "foo.bar").delete();
        }

        final Repository repository = new FileRepository(rootFile);
        final RepositoryFile file = repository.createFile("foo.bar", null);
        assertEquals("/foo.bar", file.getQualifiedPath());
        file.delete();
    }
}
