import ancilliaries.Outcome;
import ancilliaries.Scenario;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;


public class CompoundInterestCalculator {

//  Compound interest for principal:
//  P(1+r/n)^(nt)
//  Future value of a series:
//  PMT × (((1 + r/n)^nt - 1) / (r/n))

// P = principal investment
// r = annual interest rate
// n = number of compounds per period (usually in months)
// t = time the money is invested (usually in years
// PMT = regular contribution amount


  public static void main(String[] args) {


    ObjectMapper mapper = new ObjectMapper();
    Outcome outcome = new Outcome();

    mapper.enable(SerializationFeature.INDENT_OUTPUT);

    DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
    pp = pp.withObjectIndenter(new DefaultIndenter("  ", "\n"));
    mapper.setDefaultPrettyPrinter(pp);

    getCompoundInterestPrincipal(BigDecimal.valueOf(5000), BigDecimal.valueOf(5.2), 12, 5, BigDecimal.valueOf(100), Scenario.GOOD, outcome);

    getCompoundInterestPrincipal(BigDecimal.valueOf(5000), BigDecimal.valueOf(1.2), 12, 5, BigDecimal.valueOf(100), Scenario.BAD, outcome);

    try {

      System.out.println(mapper.writeValueAsString(outcome));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    };

  }

  public static void getCompoundInterestPrincipal(BigDecimal principalInput, BigDecimal annualRateInput, Integer numberOfTimesCompoundedInput, Integer yearsInput, BigDecimal monthlyContributionInput, Scenario scenario, Outcome outcome) {

    MathContext mc = MathContext.DECIMAL32;

    annualRateInput = annualRateInput.divide(BigDecimal.valueOf(100));

    BigDecimal preliminarynumber = BigDecimal.valueOf(1).add(annualRateInput.divide(BigDecimal.valueOf(numberOfTimesCompoundedInput), mc));

    Integer toThePower = numberOfTimesCompoundedInput * yearsInput;

    BigDecimal poweredVal = preliminarynumber.pow(toThePower);

    BigDecimal compoundInterestPlusPrincipal = principalInput.multiply(poweredVal);

    BigDecimal onePlus = (BigDecimal.valueOf(1).add(annualRateInput.divide(BigDecimal.valueOf(numberOfTimesCompoundedInput), mc)));

    Integer raisedToPower2 = ((numberOfTimesCompoundedInput * yearsInput));

    BigDecimal rateDividedByNumberOfTimes = annualRateInput.divide(BigDecimal.valueOf(numberOfTimesCompoundedInput), mc);

    BigDecimal halfDone = (((onePlus.pow(raisedToPower2)).subtract(BigDecimal.valueOf(1))).divide(rateDividedByNumberOfTimes, mc));

    BigDecimal futureValueWithDeposits = monthlyContributionInput.multiply(halfDone);

    BigDecimal totalAmount = compoundInterestPlusPrincipal.add(futureValueWithDeposits);

    compoundInterestPlusPrincipal = compoundInterestPlusPrincipal.setScale(2, RoundingMode.HALF_EVEN);
    futureValueWithDeposits = futureValueWithDeposits.setScale(2, RoundingMode.HALF_EVEN);
    totalAmount = totalAmount.setScale(2, RoundingMode.HALF_EVEN);


    if (scenario.equals(Scenario.GOOD)) {
      outcome.setPrincipalOutcomeGood(compoundInterestPlusPrincipal);
      outcome.setContributionsOutcomeGood(futureValueWithDeposits);
      outcome.setTotalOutcomeGood(totalAmount);
    }

    if (scenario.equals(Scenario.BAD)) {
      outcome.setPrincipalOutcomeBad(compoundInterestPlusPrincipal);
      outcome.setContributionsOutcomeBad(futureValueWithDeposits);
      outcome.setTotalOutcomeBad(totalAmount);
    }


  }

}
