package com.spribe.cleanup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CreatedPlayersRegistryPerTest {
    private CreatedPlayersRegistryPerTest() {
    }

    private static final ThreadLocal<ConcurrentLinkedQueue<Integer>> HOLDER =
            ThreadLocal.withInitial(ConcurrentLinkedQueue::new);

    public static void clear() {
        HOLDER.get().clear();
    }

    public static void register(Integer id) {
        if (id != null) HOLDER.get().add(id);
    }

    public static List<Integer> drainAll() {
        var q = HOLDER.get();
        List<Integer> all = new ArrayList<>(q.size());
        Integer id;
        while ((id = q.poll()) != null) all.add(id);
        return all;
    }
}
