package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  // test("string take") {
  //   val message = "hello, world"
  //   assert(message.take(5) == "hello")
  // }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements of each set") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("diff contains leftover elements") {
    new TestSets {
      val u = union(s1, s2)
      val s = diff(u,s1)
      assert(!contains(s, 1), "Diff should not contain 1")
      assert(contains(s, 2), "Diff should contain 2")
    }
  }

  test("intersect contains common elements") {
    new TestSets {
      val u = union(s1, s2)
      val s = intersect(u,s1)
      assert(contains(u, 1), "Intersect should contain 1")
      assert(!contains(s, 2), "Intersect should not contain 2")
    }
  }

  test("filter should return elements that satisfy filter function") {
    new TestSets {
      val u = union(union(s1, s2),s3)
      def even(i:Int) = i%2==0
      val fltr = filter(u,even)
      assert(!contains(fltr, 1), "Filter should not contain 1")
      assert(contains(fltr, 2), "Filter should contain 2")
      assert(!contains(fltr, 3), "Fitler should not contain 3")
    }
  }

  test("Forall should return true if all elements satisfy function") {
    new TestSets {
      val u = union(union(s1, s2),s3)
      def even(i:Int) = i%2==0
      val res = forall(u,even)
      assert(!res, "Forall should be false for even check")

      def lessthan4(i:Int) = i<4
      val res2 = forall(u,lessthan4)
      assert(res2, "Forall should be true for lessthan4 check")
    }
  }

  test("Exists should return true if at least one element satisfies function") {
    new TestSets {
      val u = union(union(s1, s2),s3)
      def even(i:Int) = i%2==0
      val res = exists(u,even)
      assert(res, "Exists should be true for even check")

      def morethan4(i:Int) = i>4
      val res2 = exists(u,morethan4)
      assert(!res2, "Exists should be false for morethan4 check")
    }
  }

  test("Map to square elements") {
    new TestSets {
      val u = union(s1, s2)
      val sq = map(u, x => x*x)
      assert(contains(sq,1), "map((1,2), x=>x*x) should contain 1")
      assert(contains(sq,4), "map((1,2), x=>x*x) should contain 4")
      assert(!contains(sq,2), "map((1,2), x=>x*x) should not contain 2")
    }
  }
}
