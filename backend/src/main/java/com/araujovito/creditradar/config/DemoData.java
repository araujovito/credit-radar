package com.araujovito.creditradar.config;
import com.araujovito.creditradar.asset.*;
import com.araujovito.creditradar.credit.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
@Configuration public class DemoData {
 @Bean CommandLineRunner load(TrackedAssetRepository assets,CreditPositionRepository positions,SourceDocumentRepository documents){return args->{if(assets.count()>0)return; var kncr=assets.save(new TrackedAsset("KNCR11","Kinea Rendimentos Imobiliários",AssetType.FII,"Kinea")); assets.save(new TrackedAsset("MXRF11","Maxi Renda",AssetType.FII,"BTG Pactual")); assets.save(new TrackedAsset("CPTS11","Capitânia Securities II",AssetType.FII,"Capitânia")); assets.save(new TrackedAsset("IRIM11","Iridium",AssetType.FII,"Iridium")); assets.save(new TrackedAsset("RURA11","Itaú Asset Rural FIAGRO",AssetType.FIAGRO,"Itaú Asset")); assets.save(new TrackedAsset("BISE11","Bradesco Isento",AssetType.FI_INFRA,"Bradesco Asset")); assets.save(new TrackedAsset("ISNN11","Itaú Isento Julho 28",AssetType.FI_INFRA,"Itaú Asset")); var date=LocalDate.of(2024,11,30); var report=documents.save(new SourceDocument(kncr,"Carta do Gestor — novembro de 2024",date,"https://www.kinea.com.br/wp-content/uploads/2024/12/KNCR_Carta-do-Gestor_11-2024.pdf")); positions.save(new CreditPosition(kncr,report,InstrumentType.CRI,"Hotel Hilton Copacabana","Hotelaria","CDI + 2,00%",null,null,null,new BigDecimal("90.10"),"Alienação fiduciária do imóvel e cotas, cessão fiduciária e fundo de reserva",date)); positions.save(new CreditPosition(kncr,report,InstrumentType.CRI,"JSRE — Ed. Rochaverá","Escritórios","CDI + 2,60%",null,null,null,new BigDecimal("12.90"),"Cessão fiduciária de recebíveis de aluguéis",date));};}
}
