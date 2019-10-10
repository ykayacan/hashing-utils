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

package com.ykayacan.hashing.rendezvous.strategy;

import com.ykayacan.hashing.rendezvous.WeightedNode;
import com.ykayacan.hashing.api.HashFunction;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * The type Default rendezvous strategy.
 *
 * @param <N> the type parameter
 */
public class DefaultRendezvousStrategy<N extends WeightedNode> implements RendezvousStrategy<N> {

  private DefaultRendezvousStrategy() {}

  /**
   * Create rendezvous strategy.
   *
   * @param <N> the type parameter
   * @return the rendezvous strategy
   */
  public static <N extends WeightedNode> RendezvousStrategy<N> create() {
    return new DefaultRendezvousStrategy<>();
  }

  @Override
  public Optional<N> getNode(String key, Collection<N> ring, HashFunction hashFunction) {
    Objects.requireNonNull(key);
    Objects.requireNonNull(ring);
    Objects.requireNonNull(hashFunction);

    if (ring.isEmpty()) {
      return Optional.empty();
    }

    double highestScore = Long.MIN_VALUE;
    N champion = null;
    for (N node : ring) {
      double score = hashFunction.hash(node.getNodeId() + ":" + key);
      if (score > highestScore) {
        champion = node;
        highestScore = score;
      }
    }

    return Optional.ofNullable(champion);
  }
}
