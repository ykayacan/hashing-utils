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

import com.ykayacan.hashing.api.HashFunction;
import com.ykayacan.hashing.api.Node;
import java.util.Collection;
import java.util.Optional;

/**
 * The interface Rendezvous strategy.
 *
 * @param <N> the type parameter
 */
public interface RendezvousStrategy<N extends Node> {

  /**
   * Gets node.
   *
   * @param key the key
   * @param ring the ring
   * @param hashFunction the hash function
   * @return the node
   */
  Optional<N> getNode(String key, Collection<N> ring, HashFunction hashFunction);
}
