package org.ebaloo.itkeeps.core.database;

import com.google.common.collect.ImmutableList;
import com.orientechnologies.common.collection.OMultiCollectionIterator;
import com.orientechnologies.orient.core.command.OCommandContext;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.sql.filter.OSQLFilterItem;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import java.util.List;
import java.util.stream.Collectors;

public class TraverseField implements OSQLFilterItem {
    private final OrientBaseGraph graph;
    private final Direction direction;
    private final List<String> classes;

    public TraverseField(OrientBaseGraph graph, Direction direction, String relation) {
        this.graph = graph;
        this.direction = direction;
        this.classes = ImmutableList.<String>builder()
                .add(relation)
                .addAll(graph.getEdgeType(relation).getBaseClasses().stream().map(OClass::getName).collect(Collectors.toList()))
                .build();
    }

    @Override
    public Object getValue(OIdentifiable iRecord, Object iCurrentResult, OCommandContext iContetx) {
        final OMultiCollectionIterator<Vertex> iterable = new OMultiCollectionIterator<>();
        iterable.setAutoConvertToRecord(false); //faster ?
        //TODO Eviter de passer par les graphs ?
        OrientVertex v = graph.getVertex(iRecord);
        //TODO: ITA-194 - Fix en attendant une solution au probleme de lock au niveau du schema pour les Edges (Traverse)
        v.getEdges(direction)
                .forEach(edge -> {
                    if (classes.contains(edge.getLabel())) {
                        iterable.add(edge.getVertex(direction.opposite()));
                    }
                });
        return iterable;
    }

    @Override
    public String toString() {
        return "TraverseField";
    }
}
