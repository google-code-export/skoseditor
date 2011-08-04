package org.sealife.skos.editor.frames;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.inference.ReasonerPreferences;
import org.protege.editor.owl.ui.editor.OWLObjectEditor;
import org.protege.editor.owl.ui.editor.OWLObjectPropertyIndividualPairEditor;
import org.protege.editor.owl.ui.frame.AbstractOWLFrameSection;
import org.protege.editor.owl.ui.frame.OWLFrame;
import org.protege.editor.owl.ui.frame.OWLFrameSectionRow;
import org.protege.editor.owl.ui.frame.OWLObjectPropertyIndividualPair;
import org.protege.editor.owl.ui.frame.individual.OWLObjectPropertyAssertionAxiomFrameSectionRow;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
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
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 30-Jan-2007<br><br>
 */
public class SKOSOtherObjectPropertyAssertionAxiomFrameSection extends AbstractOWLFrameSection<OWLIndividual, OWLObjectPropertyAssertionAxiom, OWLObjectPropertyIndividualPair> {

    public static final String LABEL = "Other property assertions";

    private Set<OWLObjectPropertyAssertionAxiom> added;

    Set<OWLObjectProperty> propertyFiltersSet;

    public SKOSOtherObjectPropertyAssertionAxiomFrameSection(OWLEditorKit owlEditorKit, OWLFrame<OWLNamedIndividual> frame, Set<OWLObjectProperty> propertyFilter) {
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

                OWLObjectPropertyAssertionAxiomFrameSectionRow row = new OWLObjectPropertyAssertionAxiomFrameSectionRow(getOWLEditorKit(),
                                                                          this,
                                                                          ontology,
                                                                          getRootObject(),
                                                                          ax);
                addRow(row);
                added.add(ax);
            }
        }
    }


    protected void refillInferred() {

        getOWLModelManager().getReasonerPreferences().executeTask(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_OBJECT_PROPERTY_ASSERTIONS, new Runnable() {
                public void run() {
                    OWLDataFactory factory = getOWLDataFactory();
                    if (!getRootObject().isAnonymous()){
                        for (OWLObjectProperty prop : getReasoner().getRootOntology().getObjectPropertiesInSignature(true)) {
                            if (prop.equals(factory.getOWLTopObjectProperty())) {
                                continue;
                            }
                            NodeSet<OWLNamedIndividual> values = getReasoner().getObjectPropertyValues(getRootObject().asOWLNamedIndividual(), prop);
                            for (OWLNamedIndividual ind : values.getFlattened()) {
                                OWLObjectPropertyAssertionAxiom ax = getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(prop,
                                                                                                                            getRootObject(),
                                                                                                                            ind);
                                if (!added.contains(ax)) {
                                    addRow(new OWLObjectPropertyAssertionAxiomFrameSectionRow(getOWLEditorKit(),
                                                                                              SKOSOtherObjectPropertyAssertionAxiomFrameSection.this,
                                                                                              null,
                                                                                              getRootObject(),
                                                                                              ax));
                                }
                            }
                        }
                    }
                }
            });
        // todo fix this

//        Map<OWLObjectProperty, Set<OWLNamedIndividual>> rels = getReasoner().getObjectPropertyRelationships(getRootObject());
//        for (OWLObjectProperty prop : rels.keySet()) {
//
//            for (Set<OWLObjectProperty> propSet : getReasoner().getAncestorProperties(prop)) {
//
//                for (OWLObjectProperty ancProp : propSet) {
//                    if (propertyFiltersSet.contains(ancProp)) {
//                        propertyFiltersSet.add(prop);
//                    }
//                }
//            }
//
//            if (!propertyFiltersSet.contains(prop)) {
//                for (OWLNamedIndividual ind : rels.get(prop)) {
//                    OWLObjectPropertyAssertionAxiom ax = getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(
//                            getRootObject(),
//                            prop,
//                            ind);
//                    if (!added.contains(ax)) {
//                        addRow(new OWLObjectPropertyAssertionAxiomFrameSectionRow(getOWLEditorKit(),
//                                this,
//                                null,
//                                getRootObject(),
//                                ax));
//                    }
//                }
//            }
//        }
    }


    protected OWLObjectPropertyAssertionAxiom createAxiom(OWLObjectPropertyIndividualPair object) {
        return getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(object.getProperty(),
                                                                      getRootObject(),
                                                                      object.getIndividual());
    }


    public OWLObjectEditor<OWLObjectPropertyIndividualPair> getObjectEditor() {
        return new OWLObjectPropertyIndividualPairEditor(getOWLEditorKit());
    }


    /**
     * Obtains a comparator which can be used to sort the rows
     * in this section.
     * @return A comparator if to sort the rows in this section,
     *         or <code>null</code> if the rows shouldn't be sorted.
     */


    public void visit(OWLObjectPropertyAssertionAxiom axiom) {
        if (axiom.getSubject().equals(getRootObject())) {
            reset();
        }
    }

    public Comparator<OWLFrameSectionRow<OWLIndividual, OWLObjectPropertyAssertionAxiom, OWLObjectPropertyIndividualPair>> getRowComparator() {
        return null;
    }
}
