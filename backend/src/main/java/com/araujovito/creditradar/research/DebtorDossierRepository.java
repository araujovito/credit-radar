package com.araujovito.creditradar.research;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DebtorDossierRepository extends JpaRepository<DebtorDossier,Long>{Optional<DebtorDossier> findBySlug(String slug);}
