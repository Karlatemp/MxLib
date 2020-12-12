package util;

import io.github.karlatemp.mxlib.utils.NoLock;
import io.github.karlatemp.mxlib.utils.NodeConcurrentLinkedList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestNCLL {
    @Test
    void run() {
        var h = new NodeConcurrentLinkedList<String>(NoLock.INSTANCE);
        h.getHead()
                .insertAfter("1")
                .insertAfter("2")
                .insertAfter("3")
                .insertAfter("4");
        System.out.println(h);
        Assertions.assertEquals(h.toString(), "[1, 2, 3, 4]");
        h.getTail().getPrev().remove();
        Assertions.assertEquals(h.toString(), "[1, 2, 3]");
        h.getHead().getNext().remove();
        Assertions.assertEquals(h.toString(), "[2, 3]");
    }
}
