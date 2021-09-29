package org.apache.flink.runtime.state.mirgration;

import org.apache.flink.core.memory.DataInputDeserializer;
import org.apache.flink.core.memory.DataOutputSerializer;
import org.apache.flink.runtime.state.KeyedStateBackend;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public class BinaryState {
    Map<StateDescriptor, OperatorStatePartition> states;

    public BinaryState(Map<StateDescriptor, OperatorStatePartition> s) {
        this.states = s;
    }

    public byte[] toBytes() throws IOException {
        if (states.size() == 0)
            return new byte[0];
        DataOutputSerializer ds = new DataOutputSerializer(1024);
        ds.writeInt(states.size());
        for (Entry<StateDescriptor, OperatorStatePartition> entry : states.entrySet()) {
            ds.write(entry.getKey().toBytes());
            byte[] s = entry.getValue().toBytes();
            ds.writeInt(s.length);
            ds.write(s);
        }
        return ds.getCopyOfBuffer();
    }

    public Map<StateDescriptor, OperatorStatePartition> getStates() {
        return states;
    }

}
