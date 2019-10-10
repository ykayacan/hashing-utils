/*
 * Copyright 2019 Yasin Sinan Kayacan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ykayacan.hashing.rendezvous;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ykayacan.hashing.rendezvous.strategy.DefaultRendezvousStrategy;
import com.ykayacan.hashing.rendezvous.util.StreamUtil;
import com.ykayacan.hashing.api.NodeRouter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class RendezvousNodeRouterTest {

  @Test
  void testEmpty() {
    NodeRouter<WeightedNode> router = createRendezvousRouter();
    assertFalse(router.getNode("key").isPresent());
  }

  /** Ensure the same node returned for same key after a large change to the pool of nodes */
  @Test
  void testConsistentAfterRemove() {
    NodeRouter<WeightedNode> router = createRendezvousRouter();
    IntStream.range(0, 1000)
        .parallel()
        .mapToObj(index -> WeightedNode.newBuilder().nodeId("node" + index).build())
        .forEach(router::addNode);

    Optional<WeightedNode> node = router.getNode("key");

    Random random = new Random();
    for (int i = 0; i < 250; i++) {
      String nodeId = "node" + random.nextInt(1000);
      if (node.isPresent() && !nodeId.equals(node.get().getNodeId())) {
        router.removeNode(nodeId);
      }
    }

    assertEquals(node, router.getNode("key"));
  }

  /** Ensure that a new node returned after deleted */
  @Test
  void testPreviousDeleted() {
    NodeRouter<WeightedNode> router = createRendezvousRouter();
    router.addNodes(
        Arrays.asList(
            WeightedNode.newBuilder().nodeId("node1").build(),
            WeightedNode.newBuilder().nodeId("node2").build()));

    Optional<WeightedNode> node = router.getNode("key");
    node.ifPresent(weightedNode -> router.removeNode(weightedNode.getNodeId()));

    Set<String> set = new HashSet<>();
    set.add("node1");
    set.add("node2");

    router
        .getNode("key")
        .ifPresent(weightedNode -> assertTrue(set.contains(weightedNode.getNodeId())));

    assertNotEquals(node, router.getNode("key"));
  }

  /** Ensure same node will still be returned if removed/readded */
  @Test
  void testReAdd() {
    NodeRouter<WeightedNode> router = createRendezvousRouter();
    router.addNodes(
        Arrays.asList(
            WeightedNode.newBuilder().nodeId("node1").build(),
            WeightedNode.newBuilder().nodeId("node2").build()));

    Optional<WeightedNode> node = router.getNode("key");
    node.ifPresent(
        weightedNode -> {
          router.removeNode(weightedNode.getNodeId());
          router.addNode(weightedNode);
        });
    assertEquals(node, router.getNode("key"));
  }

  /** Ensure 2 hashes if have nodes added in different order will have same results */
  @Test
  void testDifferentOrder() {
    NodeRouter<WeightedNode> router1 = createRendezvousRouter();
    NodeRouter<WeightedNode> router2 = createRendezvousRouter();

    IntStream.range(0, 1000)
        .parallel()
        .mapToObj(index -> WeightedNode.newBuilder().nodeId("node" + index).build())
        .forEach(router1::addNode);
    StreamUtil.reverseRange(0, 1000)
        .parallel()
        .mapToObj(index -> WeightedNode.newBuilder().nodeId("node" + index).build())
        .forEach(router2::addNode);

    assertEquals(router2.getNode("key"), router1.getNode("key"));
  }

  private NodeRouter<WeightedNode> createRendezvousRouter() {
    return RendezvousNodeRouter.create(
        MurMurHashFunction.create(), DefaultRendezvousStrategy.create());
  }
}
