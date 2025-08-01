package guru.qa.niffler.jupiter.extensions;

import guru.qa.niffler.api.GhApiClient;
import guru.qa.niffler.jupiter.annotations.DisabledByIssue;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.SearchOption;

public class IssueExtension implements ExecutionCondition {
  private final GhApiClient ghApiClient = new GhApiClient();

  @Override
  public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
    return AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(),
        DisabledByIssue.class
    ).or(() -> AnnotationSupport.findAnnotation(
            context.getRequiredTestClass(),
            DisabledByIssue.class,
            SearchOption.INCLUDE_ENCLOSING_CLASSES
        )
    ).map(
        byIssue -> ghApiClient.getIssueState(byIssue.value()).equals("open")
            ? ConditionEvaluationResult.disabled("Disable by issue: " + byIssue.value())
            : ConditionEvaluationResult.enabled("Issue closed")
    ).orElseGet(
        () -> ConditionEvaluationResult.enabled("Annotation @DisabledByIssue not found")
    );
  }
}
