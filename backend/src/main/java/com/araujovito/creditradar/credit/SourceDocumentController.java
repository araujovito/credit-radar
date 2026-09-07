package com.araujovito.creditradar.credit;
import com.araujovito.creditradar.asset.TrackedAssetRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/documents") public class SourceDocumentController {
  private final SourceDocumentRepository repository; private final TrackedAssetRepository assets; private final PortfolioSnapshotRepository snapshots;
  public SourceDocumentController(SourceDocumentRepository repository,TrackedAssetRepository assets,PortfolioSnapshotRepository snapshots){this.repository=repository;this.assets=assets;this.snapshots=snapshots;}
  @GetMapping public List<SourceDocument> list(){return repository.findAllByOrderByReferenceDateDesc();}
  @PostMapping @ResponseStatus(HttpStatus.CREATED) public SourceDocument importDocument(@Valid @RequestBody ImportDocumentRequest request){var asset=assets.findByTickerIgnoreCase(request.ticker()).orElseThrow(()->new IllegalArgumentException("Ativo não acompanhado"));var document=repository.save(new SourceDocument(asset,request.title(),request.referenceDate(),request.sourceUrl()));if(request.targetAssetsPercent()!=null||request.lciPercent()!=null||request.cashPercent()!=null)snapshots.save(new PortfolioSnapshot(asset,document,request.referenceDate(),request.targetAssetsPercent(),request.lciPercent(),request.cashPercent()));return document;}
}
