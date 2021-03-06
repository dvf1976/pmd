/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java.ast;

import org.junit.Assert;
import org.junit.Test;

import net.sourceforge.pmd.lang.ast.ParseException;
import net.sourceforge.pmd.lang.java.JavaParsingHelper;

public class Java12Test {


    private final JavaParsingHelper java11 =
        JavaParsingHelper.WITH_PROCESSING.withDefaultVersion("11")
                                         .withResourceContext(Java12Test.class, "jdkversiontests/java12/");

    private final JavaParsingHelper java12p = java11.withDefaultVersion("12-preview");


    @Test(expected = ParseException.class)
    public void testMultipleCaseLabelsJava11() {
        java11.parseResource("MultipleCaseLabels.java");
    }

    @Test
    public void testMultipleCaseLabels() {
        ASTCompilationUnit compilationUnit = java12p.parseResource("MultipleCaseLabels.java");
        ASTSwitchStatement switchStatement = compilationUnit.getFirstDescendantOfType(ASTSwitchStatement.class);
        Assert.assertTrue(switchStatement.jjtGetChild(0) instanceof ASTExpression);
        Assert.assertTrue(switchStatement.jjtGetChild(1) instanceof ASTSwitchLabel);
        ASTSwitchLabel switchLabel = switchStatement.getFirstChildOfType(ASTSwitchLabel.class);
        Assert.assertEquals(3, switchLabel.findChildrenOfType(ASTExpression.class).size());
    }

    @Test(expected = ParseException.class)
    public void testSwitchRulesJava11() {
        java11.parseResource("SwitchRules.java");
    }

    @Test
    public void testSwitchRules() {
        ASTCompilationUnit compilationUnit = java12p.parseResource("SwitchRules.java");
        ASTSwitchStatement switchStatement = compilationUnit.getFirstDescendantOfType(ASTSwitchStatement.class);
        Assert.assertTrue(switchStatement.jjtGetChild(0) instanceof ASTExpression);
        Assert.assertTrue(switchStatement.jjtGetChild(1) instanceof ASTSwitchLabeledExpression);
        ASTSwitchLabeledExpression switchLabeledExpression = (ASTSwitchLabeledExpression) switchStatement.jjtGetChild(1);
        Assert.assertEquals(2, switchLabeledExpression.jjtGetNumChildren());
        Assert.assertTrue(switchLabeledExpression.jjtGetChild(0) instanceof ASTSwitchLabel);
        Assert.assertTrue(switchLabeledExpression.jjtGetChild(1) instanceof ASTExpression);

        ASTSwitchLabeledBlock switchLabeledBlock = (ASTSwitchLabeledBlock) switchStatement.jjtGetChild(4);
        Assert.assertEquals(2, switchLabeledBlock.jjtGetNumChildren());
        Assert.assertTrue(switchLabeledBlock.jjtGetChild(0) instanceof ASTSwitchLabel);
        Assert.assertTrue(switchLabeledBlock.jjtGetChild(1) instanceof ASTBlock);

        ASTSwitchLabeledThrowStatement switchLabeledThrowStatement = (ASTSwitchLabeledThrowStatement) switchStatement.jjtGetChild(5);
        Assert.assertEquals(2, switchLabeledThrowStatement.jjtGetNumChildren());
        Assert.assertTrue(switchLabeledThrowStatement.jjtGetChild(0) instanceof ASTSwitchLabel);
        Assert.assertTrue(switchLabeledThrowStatement.jjtGetChild(1) instanceof ASTThrowStatement);
    }

    @Test(expected = ParseException.class)
    public void testSwitchExpressionsJava11() {
        java11.parseResource("SwitchExpressions.java");
    }

    @Test
    public void testSwitchExpressions() {
        ASTCompilationUnit compilationUnit = java12p.parseResource("SwitchExpressions.java");

        ASTSwitchExpression switchExpression = compilationUnit.getFirstDescendantOfType(ASTSwitchExpression.class);
        Assert.assertEquals(6, switchExpression.jjtGetNumChildren());
        Assert.assertTrue(switchExpression.jjtGetChild(0) instanceof ASTExpression);
        Assert.assertEquals(5, switchExpression.findChildrenOfType(ASTSwitchLabeledRule.class).size());

        ASTLocalVariableDeclaration localVar = compilationUnit.findDescendantsOfType(ASTLocalVariableDeclaration.class).get(1);
        ASTVariableDeclarator localVarDecl = localVar.getFirstChildOfType(ASTVariableDeclarator.class);
        Assert.assertEquals(Integer.TYPE, localVarDecl.getType());
        Assert.assertEquals(Integer.TYPE, switchExpression.getType());
    }

    @Test
    public void testSwitchExpressionsBreak() {
        ASTCompilationUnit compilationUnit = java12p.parseResource("SwitchExpressionsBreak.java");

        ASTSwitchExpression switchExpression = compilationUnit.getFirstDescendantOfType(ASTSwitchExpression.class);
        Assert.assertEquals(11, switchExpression.jjtGetNumChildren());
        Assert.assertTrue(switchExpression.jjtGetChild(0) instanceof ASTExpression);
        Assert.assertEquals(5, switchExpression.findChildrenOfType(ASTSwitchLabel.class).size());

        ASTBreakStatement breakStatement = switchExpression.getFirstDescendantOfType(ASTBreakStatement.class);
        Assert.assertEquals("SwitchExpressionsBreak.SIX", breakStatement.getImage());
        Assert.assertTrue(breakStatement.jjtGetChild(0) instanceof ASTExpression);

        ASTLocalVariableDeclaration localVar = compilationUnit.findDescendantsOfType(ASTLocalVariableDeclaration.class).get(1);
        ASTVariableDeclarator localVarDecl = localVar.getFirstChildOfType(ASTVariableDeclarator.class);
        Assert.assertEquals(Integer.TYPE, localVarDecl.getType());
        Assert.assertEquals(Integer.TYPE, switchExpression.getType());
    }

}
