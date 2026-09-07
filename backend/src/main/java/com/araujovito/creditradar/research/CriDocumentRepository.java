package com.araujovito.creditradar.research;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CriDocumentRepository extends JpaRepository<CriDocument,Long>{
 java.util.Optional<CriDocument> findByCreditCode(String creditCode);
}
