/*
 * Copyright (c) 2017 - present Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package codetoanalyze.java.checkers;

import javax.annotation.Nullable;

public class NullableViolation {

  class T {
    int x;
    void doSomething() {}
  }

  native static @Nullable T returnsNullable();

  void dereferenceNullableReturnValueBad() {
    T t = returnsNullable();
    t.x = 42; // reports here
  }

  void dereferenceNullableReturnValueOkay() {
    T t = returnsNullable();
    if (t != null) {
      t.x = 42; // does not report here
    }
  }

  void dereferenceNullableMethodBad() {
    returnsNullable().doSomething(); // reports here
  }

  void dereferenceNullableMethodCheckedForNullOkay() {
    if (returnsNullable() != null) {
      returnsNullable().doSomething(); // does not report here
    }
  }

  void dereferenceNullableMethodIncorrectlyCheckedForNullBad() {
    if (returnsNullable() == null) {
      returnsNullable().doSomething(); // reports here
    }
  }

  void dereferenceNullableMethodInElseBranchBad() {
    if (returnsNullable() != null) {
    } else {
      returnsNullable().doSomething(); // reports here
    }
  }

  native boolean star();

  void dereferenceNullableMethodAlwaysCheckedForNullOkay() {
    if (star() && returnsNullable() != null) {
      returnsNullable().doSomething(); // does not report here
    }
  }

  void dereferenceNullableMethodNotAlwaysCheckedForNullBad() {
    if (star() || returnsNullable() != null) {
      returnsNullable().doSomething(); // reports here
    }
  }

}
