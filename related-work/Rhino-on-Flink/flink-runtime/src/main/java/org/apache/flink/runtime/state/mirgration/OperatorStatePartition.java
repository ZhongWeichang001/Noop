package org.apache.flink.runtime.state.mirgration;

import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.core.memory.DataInputDeserializer;
import org.apache.flink.core.memory.DataOutputSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OperatorStatePartition<N, K, V> {
    final Map<N, Map<K, V>> state;

    TypeSerializer<N> namespaceSerializer;
    TypeSerializer<K> keySerializer;
    TypeSerializer<V> stateSerializer;

    public OperatorStatePartition(
            Map<N, Map<K, V>> state,
            TypeSerializer<N> namespaceSerializer,
            TypeSerializer<K> keySerializer,
            TypeSerializer<V> stateSerializer
    ) {
        this.state = state;
        this.namespaceSerializer = namespaceSerializer;
        this.keySerializer = keySerializer;
        this.stateSerializer = stateSerializer;
    }


    public byte[] toBytes() throws IOException {
        DataOutputSerializer ds = new DataOutputSerializer(1024);
        int nr = 0;
        for (Map<K, V> value : state.values()) {
            nr += value.size();
        }
        ds.writeInt(nr);
        for (Map.Entry<N, Map<K, V>> nMapEntry : state.entrySet()) {
            Map<K, V> kvs = nMapEntry.getValue();
            N ns = nMapEntry.getKey();
            for (Map.Entry<K, V> entry : kvs.entrySet()) {
                namespaceSerializer.serialize(ns, ds);
                keySerializer.serialize(entry.getKey(), ds);
                stateSerializer.serialize(entry.getValue(), ds);
            }
        }
        return ds.getCopyOfBuffer();
    }

    public static <N, K, V> Map<N, Map<K, V>> fromBytes(TypeSerializer<N> namespaceSerializer, TypeSerializer<K> keySerializer,
                                                        TypeSerializer<V> stateSerializer, byte[] bytes) throws IOException {
        Map<N, Map<K, V>> state = new HashMap<>();
        DataInputDeserializer dd = new DataInputDeserializer(bytes);
        int nstate = dd.readInt();
        for (int i = 0; i < nstate; i++) {
            int nentry = dd.readInt();
            for (int j = 0; j < nentry; j++) {
                N ns = namespaceSerializer.deserialize(dd);
                K key = keySerializer.deserialize(dd);
                V value = stateSerializer.deserialize(dd);
                state.computeIfAbsent(ns, n -> new HashMap<K, V>()).put(key, value);
            }
        }
        return state;
    }
}
