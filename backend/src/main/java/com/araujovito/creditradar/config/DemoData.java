package com.araujovito.creditradar.config;
import com.araujovito.creditradar.asset.*;
import com.araujovito.creditradar.credit.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
@Configuration public class DemoData {
 @Bean CommandLineRunner load(TrackedAssetRepository assets,CreditPositionRepository positions){return args->{if(assets.count()>0)return; var kncr=assets.save(new TrackedAsset("KNCR11","Kinea Rendimentos Imobiliários",AssetType.FII,"Kinea")); var mxrf=assets.save(new TrackedAsset("MXRF11","Maxi Renda",AssetType.FII,"BTG Pactual")); var cpts=assets.save(new TrackedAsset("CPTS11","Capitânia Securities II",AssetType.FII,"Capitânia")); var irim=assets.save(new TrackedAsset("IRIM11","Iridium",AssetType.FII,"Iridium")); assets.save(new TrackedAsset("RURA11","Itaú Asset Rural FIAGRO",AssetType.FIAGRO,"Itaú Asset")); assets.save(new TrackedAsset("BISE11","Bradesco Isento",AssetType.FI_INFRA,"Bradesco Asset")); assets.save(new TrackedAsset("ISNN11","Itaú Isento Julho 28",AssetType.FI_INFRA,"Itaú Asset")); var date=LocalDate.now(); positions.save(new CreditPosition(kncr,InstrumentType.CRI,"Posição a importar","Imobiliário","CDI",null,null,new BigDecimal("0"),date)); positions.save(new CreditPosition(mxrf,InstrumentType.CRI,"Posição a importar","Imobiliário","IPCA",null,null,new BigDecimal("0"),date)); positions.save(new CreditPosition(cpts,InstrumentType.CRI,"Posição a importar","Imobiliário","IPCA",null,null,new BigDecimal("0"),date)); positions.save(new CreditPosition(irim,InstrumentType.CRI,"Posição a importar","Imobiliário","IPCA",null,null,new BigDecimal("0"),date));};}
}
