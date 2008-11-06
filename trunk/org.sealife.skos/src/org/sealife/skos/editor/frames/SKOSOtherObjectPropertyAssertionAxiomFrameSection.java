package org.sealife.skos.editor.frames;

import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.protege.editor.owl.ui.frame.*;
import org.protege.editor.owl.OWLEditorKit;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Comparator;
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
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 30-Jan-2007<br><br>
 */
public class SKOSOtherObjectPropertyAssertionAxiomFrameSection extends AbstractOWLFrameSection<OWLIndividual, OWLObjectPropertyAssertionAxiom, OWLObjectPropertyIndividualPair> {

    public static final String LABEL = "Other property assertions";

    private Set<OWLObjectPropertyAssertionAxiom> added;

    Set<OWLObjectProperty> propertyFiltersSet;

    public SKOSOtherObjectPropertyAssertionAxiomFrameSection(OWLEditorKit owlEditorKit, OWLFrame<OWLIndividual> frame, Set<OWLObjectProperty> propertyFilter) {
        super(owlEditorKit, LABEL, "Other property assertion", frame);
        added = new HashSet<OWLObjectPropertyAssertionAxiom>();
        this.propertyFiltersSet = propertyFilter;
    }


    protected void clear() {

    }


    /**
     * Refills the section with rows.  This method will be called
     * by the system and should be directly called.
     */
    protected void refill(OWLOntology ontology) {
        added.clear();
        for (OWLObjectPropertyAssertionAxiom ax : ontology.getObjectPropertyAssertionAxioms(getRootObject())) {
            if (!propertyFiltersSet.contains(ax.getProperty().asOWLObjectProperty())) {
                addRow(new OWLObjectPropertyAssertionAxiomFrameSectionRow(getOWLEditorKit(),
                                                                          this,
                                                                          ontology,
                                                                          getRootObject(),
                                                                          ax));
                added.add(ax);
            }
        }
    }


    protected void refillInferred() throws OWLReasonerException {
        Map<OWLObjectProperty, Set<OWLIndividual>> rels = getReasoner().getObjectPropertyRelationships(getRootObject());
        for (OWLObjectProperty prop : rels.keySet()) {

            for (Set<OWLObjectProperty> propSet : getReasoner().getAncestorProperties(prop)) {

                for (OWLObjectProperty ancProp : propSet) {
                    if (propertyFiltersSet.contains(ancProp)) {
                        propertyFiltersSet.add(prop);
                    }
                }
            }

            if (!propertyFiltersSet.contains(prop)) {
                for (OWLIndividual ind : rels.get(prop)) {
                    OWLObjectPropertyAssertionAxiom ax = getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(
                            getRootObject(),
                            prop,
                            ind);
                    if (!added.contains(ax)) {
                        addRow(new OWLObjectPropertyAssertionAxiomFrameSectionRow(getOWLEditorKit(),
                                this,
                                null,
                                getRootObject(),
                                ax));
                    }
                }
            }
        }
    }


    protected OWLObjectPropertyAssertionAxiom createAxiom(OWLObjectPropertyIndividualPair object) {
        return getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(getRootObject(),
                                                                      object.getProperty(),
                                                                      object.getIndividual());
    }


    public OWLFrameSectionRowObjectEditor<OWLObjectPropertyIndividualPair> getObjectEditor() {
        return new OWLObjectPropertyIndividualPairEditor(getOWLEditorKit());
    }


    /**
     * Obtains a comparator which can be used to sort the rows
     * in this section.
     * @return A comparator if to sort the rows in this section,
     *         or <code>null</code> if the rows shouldn't be sorted.
     */
    public Comparator<OWLFrameSectionRow<OWLIndividual, OWLObjectPropertyAssertionAxiom, OWLObjectPropertyIndividualPair>> getRowComparator() {
        return null;
    }


    public void visit(OWLObjectPropertyAssertionAxiom axiom) {
        if (axiom.getSubject().equals(getRootObject())) {
            reset();
        }
    }
}
