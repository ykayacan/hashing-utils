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

package io.github.ykayacan.hashing.consistent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.ykayacan.hashing.api.NodeRouter;
import io.github.ykayacan.hashing.consistent.util.StreamUtil;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class ConsistentNodeRouterTest {

  @Test
  void testEmpty() {
    NodeRouter<PhysicalNode> router = createConsistentRouter();
    assertFalse(router.getNode("key").isPresent());
  }

  /** Ensure the same node returned for same key after a large change to the pool of nodes */
  @Test
  void testConsistentAfterRemove() {
    NodeRouter<PhysicalNode> router = createConsistentRouter();
    IntStream.range(0, 1000)
        .parallel()
        .mapToObj(index -> PhysicalNode.of("node" + index))
        .forEach(router::addNode);

    Optional<PhysicalNode> node = router.getNode("key");

    Random random = new Random();
    IntStream.range(0, 250)
        .mapToObj(value -> "node" + random.nextInt(1000))
        .filter(nodeId -> node.isPresent() && !nodeId.equals(node.get().getNodeId()))
        .forEach(router::removeNode);

    assertEquals(node, router.getNode("key"));
  }

  /** Ensure that a new node returned after deleted */
  @Test
  void testPreviousDeleted() {
    NodeRouter<PhysicalNode> router = createConsistentRouter();
    router.addNodes(Arrays.asList(PhysicalNode.of("node1"), PhysicalNode.of("node2")));

    Optional<PhysicalNode> node = router.getNode("key");
    node.ifPresent(physicalNode -> router.removeNode(physicalNode.getNodeId()));

    Set<String> set = new HashSet<>();
    set.add("node1");
    set.add("node2");

    router
        .getNode("key")
        .ifPresent(physicalNode -> assertTrue(set.contains(physicalNode.getNodeId())));

    assertNotEquals(node, router.getNode("key"));
  }

  /** Ensure same node will still be returned if removed/readded */
  @Test
  void testReAdd() {
    NodeRouter<PhysicalNode> router = createConsistentRouter();
    router.addNodes(Arrays.asList(PhysicalNode.of("node1"), PhysicalNode.of("node2")));

    Optional<PhysicalNode> node = router.getNode("key");
    node.ifPresent(
        physicalNode -> {
          router.removeNode(physicalNode.getNodeId());
          router.addNode(physicalNode);
        });
    assertEquals(node, router.getNode("key"));
  }

  /** Ensure 2 hashes if have nodes added in different order will have same results */
  @Test
  void testDifferentOrder() {
    NodeRouter<PhysicalNode> router1 = createConsistentRouter();
    NodeRouter<PhysicalNode> router2 = createConsistentRouter();

    IntStream.range(0, 1000)
        .parallel()
        .mapToObj(index -> PhysicalNode.of("node" + index))
        .forEach(router1::addNode);
    StreamUtil.reverseRange(0, 1000)
        .parallel()
        .mapToObj(index -> PhysicalNode.of("node" + index))
        .forEach(router2::addNode);

    assertEquals(router2.getNode("key"), router1.getNode("key"));
  }

  private NodeRouter<PhysicalNode> createConsistentRouter() {
    return ConsistentNodeRouter.create(15, MurMurHashFunction.create());
  }
}
