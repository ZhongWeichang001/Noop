package org.apache.flink.runtime.microbatch;

import org.apache.flink.core.memory.DataInputView;
import org.apache.flink.core.memory.DataOutputView;
import org.apache.flink.runtime.event.RuntimeEvent;

import java.io.IOException;

public class EndOfBatchEvent extends RuntimeEvent {

    /**
     * The singleton instance of this event.
     */
    Boolean rescale = false;
    public static final String RESCALE_STR = "rescale";
    public static final String NOT_RESCALE_STR = "not_rescale";
    // ------------------------------------------------------------------------

    // not instantiable
    public EndOfBatchEvent() {
    }

    public void setRescale() {
        this.rescale = true;
    }
    // ------------------------------------------------------------------------

    @Override
    public void read(DataInputView in) throws IOException {
        // Nothing to do here
        this.rescale = in.readBoolean();
    }

    @Override
    public void write(DataOutputView out) throws IOException {
        // Nothing to do here
        out.writeBoolean(rescale);
    }

    // ------------------------------------------------------------------------

    @Override
    public int hashCode() {
        return 1965146672;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == EndOfBatchEvent.class;
    }

    @Override
    public String toString() {
        return rescale ? RESCALE_STR : NOT_RESCALE_STR;
    }
}
