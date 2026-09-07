package com.araujovito.creditradar.dashboard;
import com.araujovito.creditradar.credit.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/analytics") public class CreditAnalyticsController {
 private final CreditPositionRepository positions; public CreditAnalyticsController(CreditPositionRepository positions){this.positions=positions;}
 @GetMapping("/concentration") public Concentration concentration(@RequestParam String ticker){var rows=positions.findByAssetTickerIgnoreCase(ticker);return new Concentration(sum(rows,CreditPosition::getIssuer),sum(rows,CreditPosition::getSector),sum(rows,CreditPosition::getIndexer));}
 @GetMapping("/compare") public Comparison compare(@RequestParam String ticker,@RequestParam LocalDate from,@RequestParam LocalDate to){var before=byIssuer(positions.findByAssetTickerIgnoreCaseAndReferenceDate(ticker,from));var after=byIssuer(positions.findByAssetTickerIgnoreCaseAndReferenceDate(ticker,to));var issuers=new TreeSet<String>();issuers.addAll(before.keySet());issuers.addAll(after.keySet());var changes=issuers.stream().map(i->new Change(i,before.getOrDefault(i,BigDecimal.ZERO),after.getOrDefault(i,BigDecimal.ZERO),after.getOrDefault(i,BigDecimal.ZERO).subtract(before.getOrDefault(i,BigDecimal.ZERO)))).toList();return new Comparison(from,to,changes);}
 private Map<String,BigDecimal> sum(List<CreditPosition> rows,java.util.function.Function<CreditPosition,String> key){var out=new TreeMap<String,BigDecimal>();rows.forEach(p->out.merge(key.apply(p),p.getAmountMillions()==null?BigDecimal.ZERO:p.getAmountMillions(),BigDecimal::add));return out;}
 private Map<String,BigDecimal> byIssuer(List<CreditPosition> rows){return sum(rows,CreditPosition::getIssuer);}
 public record Concentration(Map<String,BigDecimal> byIssuer,Map<String,BigDecimal> bySector,Map<String,BigDecimal> byIndexer){} public record Change(String issuer,BigDecimal before,BigDecimal after,BigDecimal delta){} public record Comparison(LocalDate from,LocalDate to,List<Change> changes){}
}
