package ancilliaries;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class Outcome {

  BigDecimal principalOutcomeGood;
  BigDecimal principalOutcomeBad;

    BigDecimal contributionsOutcomeGood;
  BigDecimal contributionsOutcomeBad;

  BigDecimal totalOutcomeGood;
  BigDecimal totalOutcomeBad;



}
