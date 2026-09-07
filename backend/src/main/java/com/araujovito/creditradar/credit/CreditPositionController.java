package com.araujovito.creditradar.credit;
import com.araujovito.creditradar.asset.TrackedAssetRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/positions") public class CreditPositionController {
 private final CreditPositionRepository repository;private final TrackedAssetRepository assets;private final SourceDocumentRepository documents;
 public CreditPositionController(CreditPositionRepository repository,TrackedAssetRepository assets,SourceDocumentRepository documents){this.repository=repository;this.assets=assets;this.documents=documents;}
 @GetMapping public List<CreditPosition> list(@RequestParam(required=false) String issuer){return issuer==null?repository.findAllByOrderByPortfolioWeightDesc():repository.findByIssuerContainingIgnoreCaseOrderByReferenceDateDesc(issuer);}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public CreditPosition create(@Valid @RequestBody CreateCreditPositionRequest request){var asset=assets.findByTickerIgnoreCase(request.ticker()).orElseThrow(()->new IllegalArgumentException("Ativo não acompanhado"));var document=documents.findById(request.sourceDocumentId()).orElseThrow(()->new IllegalArgumentException("Documento não encontrado"));if(!document.getAsset().getId().equals(asset.getId()))throw new IllegalArgumentException("O documento não pertence ao ativo informado");return repository.save(new CreditPosition(asset,document,request.instrumentType(),request.issuer(),request.sector(),request.indexer(),request.rating(),request.maturity(),request.portfolioWeight(),request.amountMillions(),request.guarantees(),request.referenceDate()));}
}
