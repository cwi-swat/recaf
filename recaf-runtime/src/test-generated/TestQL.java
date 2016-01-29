
import recaf.ql.QL;


public class TestQL {

  Void  questionnaire(QL  ql) {
  return (Void )ql.Method(ql.Question((Boolean hasSoldHouse) -> { return ql.Question((Boolean hasBoughtHouse) -> { return ql.Question((Boolean hasMaintLoan) -> { return ql.If(ql.Exp(() -> { return hasSoldHouse; }), ql.Question((Double sellingPrice) -> { return ql.Question((Double privateDebt) -> { return ql.Question(ql.Exp(() -> { return sellingPrice - privateDebt; }), (Double valueResidue) -> ql.Empty()); }); })); }); }); }));
}
}