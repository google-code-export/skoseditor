package org.sealife.skos.editor.menu;

import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.ui.action.ProtegeOWLAction;
import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.SimpleURIMapper;

import java.awt.event.ActionEvent;
import java.net.URI;
/*
 * Copyright (C) 2007, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/**
 * Author: Simon Jupp<br>
 * Date: Oct 8, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class ImportSKOSDataModelAction extends ProtegeOWLAction {

    public void actionPerformed(ActionEvent actionEvent) {

        try {

            URI core = URI.create("http://www.w3.org/2004/02/skos/core");
            URI skos = URI.create("http://www.w3.org/2006/07/SWD/SKOS/reference/20081001/skos-dl.rdf");

            OWLOntologyManager man = getOWLModelManager().getOWLOntologyManager();

            man.addURIMapper(new SimpleURIMapper(core, skos));


            for (OWLOntology onto : man.getOntologies()) {
                OWLImportsDeclaration ax = man.getOWLDataFactory().getOWLImportsDeclarationAxiom(onto, core);
                man.applyChange(new AddAxiom(onto, ax));
                man.makeLoadImportRequest(ax);
            }

            getOWLModelManager().fireEvent(EventType.ONTOLOGY_LOADED);
            getOWLModelManager().rebuildOWLObjectPropertyHierarchy();

//            man.addURIMapper(new SimpleURIMapper(URI.create("http://www.w3.org/2004/02/skos/core"), URI.create("http://www.cs.man.ac.uk/~sjupp/skos/skos-core-2004.owl")));
//            getOWLModelManager().loadOntology(URI.create("http://www.w3.org/2004/02/skos/core"));

//            OWLOntologyInputSource localSKOS = new OWLOntologyInputSource () {
//
//
//                public boolean isReaderAvailable() {
//                    return false;
//                }
//
//                public Reader getReader() {
//                    return null;
//                }
//
//                public boolean isInputStreamAvailable() {
//                    return true;
//                }
//
//                public InputStream getInputStream() {
//                    ClassLoader loader = ImportSKOSDataModelAction.class.getClassLoader();
//                    InputStream is = loader.getResourceAsStream("skos-core-2004.owl");
//
//                    BufferedInputStream bs = new BufferedInputStream(is);
//                    System.err.println(bs.toString());
//
//                    return bs;
//                }
//
//                public URI getPhysicalURI() {
//                    return URI.create("http://www.w3.org/2004/02/skos/core");
//                }
//            };
//
//            man.loadOntology(localSKOS);

        } catch (OWLOntologyChangeException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void initialise() throws Exception {
    }

    public void dispose() throws Exception {
    }
}
