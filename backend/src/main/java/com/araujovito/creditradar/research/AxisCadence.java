package com.araujovito.creditradar.research;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Recência e frequência de um eixo do score, para os eixos que se repetem no tempo.
 *
 * <p>Nem todo eixo tem cadência. Devedora, termo e garantias descrevem a operação uma vez: a
 * identidade não se repete e o termo não é republicado todo mês. Para eles não existe frequência
 * a medir, e inventar um esperado só produziria um número com aparência de rigor.
 *
 * <p>Os que se repetem são três — cobertura, demonstrações e acompanhamento — e cada um tem a
 * própria periodicidade e o próprio prazo de validade. Onde a periodicidade exigida não está
 * estabelecida em fonte aberta, {@code expectedPerYear} é nulo: não medido, que não é a mesma
 * coisa que zero.
 */
@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"slug","axis"}))
public class AxisCadence {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private String slug;
 @Column(nullable=false) private String axis;
 /** Quantas apurações ou documentos por ano a operação deve publicar; nulo quando não se sabe. */
 private Integer expectedPerYear;
 /** Competências distintas efetivamente publicadas nos últimos doze meses. */
 private Integer published12m;
 /** Data de referência da evidência mais recente; nulo quando não há nenhuma. */
 private LocalDate latestReference;
 /** A partir de quantos meses a evidência deixa de valer como verificação corrente. */
 @Column(nullable=false) private int staleMonths;
 @Column(nullable=false,length=800) private String basis;

 protected AxisCadence(){}
 public AxisCadence(String slug,String axis,Integer expectedPerYear,Integer published12m,LocalDate latestReference,int staleMonths,String basis){
  this.slug=slug;this.axis=axis;this.expectedPerYear=expectedPerYear;this.published12m=published12m;
  this.latestReference=latestReference;this.staleMonths=staleMonths;this.basis=basis;}

 public boolean isMeasured(){return expectedPerYear!=null&&published12m!=null;}
 public Double getRegularity(){return isMeasured()&&expectedPerYear>0?(double)published12m/expectedPerYear:null;}
 public Long getAgeMonths(LocalDate today){return latestReference==null?null:ChronoUnit.MONTHS.between(latestReference,today);}
 public boolean isStale(LocalDate today){var age=getAgeMonths(today);return age!=null&&age>=staleMonths;}
 /** Abaixo de metade do exigido, publicar é evento isolado e não regime de acompanhamento. */
 public boolean isIrregular(){var regularity=getRegularity();return regularity!=null&&published12m>0&&regularity<0.5;}
 /** Nível de topo só se sustenta com evidência recente e cadência compatível com o exigido. */
 public boolean capsToPartial(LocalDate today){return isStale(today)||isIrregular();}

 public Long getId(){return id;} public String getSlug(){return slug;} public String getAxis(){return axis;}
 public Integer getExpectedPerYear(){return expectedPerYear;} public Integer getPublished12m(){return published12m;}
 public LocalDate getLatestReference(){return latestReference;} public int getStaleMonths(){return staleMonths;}
 public String getBasis(){return basis;}
}
