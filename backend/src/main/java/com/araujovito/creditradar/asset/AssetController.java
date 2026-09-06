package com.araujovito.creditradar.asset;
import java.util.List;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/assets")
public class AssetController { private final TrackedAssetRepository repository; public AssetController(TrackedAssetRepository repository){this.repository=repository;} @GetMapping public List<TrackedAsset> list(){return repository.findAll();} }
