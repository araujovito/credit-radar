package com.araujovito.creditradar.credit;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SourceDocumentRepository extends JpaRepository<SourceDocument,Long> { List<SourceDocument> findAllByOrderByReferenceDateDesc(); }
