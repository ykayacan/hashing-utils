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

import com.ykayacan.hashing.rendezvous.strategy.RendezvousStrategy;
import com.ykayacan.hashing.api.HashFunction;
import com.ykayacan.hashing.api.NodeRouter;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * A high performance thread safe implementation of Rendezvous (Highest Random Weight, HRW) hashing
 * is an algorithm that allows clients to achieve distributed agreement on which node (or proxy) a
 * given * key is to be placed in. This implementation has the following properties. *
 *
 * <ul>
 *   <li>Non-blocking reads : Determining which node a key belongs to is always non-blocking. Adding
 *       and removing ring however blocks each other *
 *   <li>Low overhead: providing using a hash function of low overhead *
 *   <li>Load balancing: Since the hash function is randomizing, each of the n ring is equally
 *       likely to receive the key K. Loads are uniform across the sites. *
 *   <li>High hit rate: Since all clients agree on placing an key K into the same node N , each
 *       fetch or placement of K into N yields the maximum utility in terms of hit rate. The key K
 *       will always be found unless it is evicted by some replacement algorithm at N. *
 *   <li>Minimal disruption: When a node is removed, only the keys mapped to that node need to be
 *       remapped and they will be distributed evenly *
 * </ul>
 *
 * source: https://en.wikipedia.org/wiki/Rendezvous_hashing
 *
 * @param <N> the type parameter
 */
public class RendezvousNodeRouter<N extends WeightedNode> implements NodeRouter<N> {

  /** All the current ring in the pool */
  private final Set<N> ring;

  private final HashFunction hashFunction;

  private final RendezvousStrategy<N> strategy;

  private RendezvousNodeRouter(
      Collection<N> initialNodes, HashFunction hashFunction, RendezvousStrategy<N> strategy) {
    Objects.requireNonNull(initialNodes);
    Objects.requireNonNull(hashFunction);
    Objects.requireNonNull(strategy);

    this.ring = new ConcurrentSkipListSet<>(initialNodes);
    this.hashFunction = hashFunction;
    this.strategy = strategy;
  }

  /**
   * Create node router.
   *
   * @param <N> the type parameter
   * @param hashFunction the hash function
   * @param strategy the strategy
   * @return the node router
   */
  public static <N extends WeightedNode> NodeRouter<N> create(
      HashFunction hashFunction, RendezvousStrategy<N> strategy) {
    return create(Collections.emptyList(), hashFunction, strategy);
  }

  /**
   * Create node router.
   *
   * @param <N> the type parameter
   * @param initialNodes the initial nodes
   * @param hashFunction the hash function
   * @param strategy the strategy
   * @return the node router
   */
  public static <N extends WeightedNode> NodeRouter<N> create(
      Collection<N> initialNodes, HashFunction hashFunction, RendezvousStrategy<N> strategy) {
    return new RendezvousNodeRouter<>(initialNodes, hashFunction, strategy);
  }

  @Override
  public Optional<N> getNode(String nodeId) {
    return strategy.getNode(nodeId, ring, hashFunction);
  }

  @Override
  public void addNode(N node) {
    Objects.requireNonNull(node);

    ring.add(node);
  }

  @Override
  public void removeNode(String nodeId) {
    Objects.requireNonNull(nodeId);

    ring.removeIf(node -> node.getNodeId().equals(nodeId));
  }
}
