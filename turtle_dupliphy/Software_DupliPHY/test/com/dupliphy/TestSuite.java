package com.dupliphy;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.dupliphy.units.BinaryTreeTest;
import com.dupliphy.units.DefaultMatrixTest;
import com.dupliphy.units.MatrixTest;

/**
 * Test suite to the run the tests in this section
 * @author ryanames
 *
 */
@RunWith(Suite.class)
@SuiteClasses({BinaryTreeTest.class, MatrixTest.class, DefaultMatrixTest.class })
public class TestSuite {

}
