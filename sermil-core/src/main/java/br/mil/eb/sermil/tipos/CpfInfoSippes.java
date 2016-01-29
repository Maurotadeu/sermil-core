package br.mil.eb.sermil.tipos;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CpfInfoSippes implements Serializable {

   private static final long serialVersionUID = -8663177082543556326L;

   protected static final Logger log = LoggerFactory.getLogger(CpfInfoSippes.class);
   
   private Integer anoObito;
   
   private Date dataAtualizacao;
   
   private Date dataConsulta;
   
   private Date dataNascimento;
   
   private String erro;
   
   private String id;
   
   private String idUsuario;

   private String nome;
   
   private String nomeMae;
   
   private Byte residenteExterior;
   
   private Byte sexo;
   
   private Byte situacaoCadastral;
   
   private String tituloEleitor;
   
   private CpfInfoSippes() {
     super();  
   }
   
   @Override
   public String toString() {
      String info = null;
      if (this.getNome() != null && this.getNomeMae() != null && this.getDataNascimento() !=  null) {
         info = new StringBuilder("Nome: ").append(this.getNome())
                .append(" - Mãe: ").append(this.getNomeMae())
                .append(" - Data Nasc: ").append(this.getDtNascf())
                .toString();
      }
      return info;
   }

   public String getDtNascf() {
      return new SimpleDateFormat("dd/MM/yyyy").format(this.getDataNascimento());
   }
   
   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getNome() {
      return nome;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   public Byte getSituacaoCadastral() {
      return situacaoCadastral;
   }

   public void setSituacaoCadastral(Byte situacaoCadastral) {
      this.situacaoCadastral = situacaoCadastral;
   }

   public Byte getResidenteExterior() {
      return residenteExterior;
   }

   public void setResidenteExterior(Byte residenteExterior) {
      this.residenteExterior = residenteExterior;
   }

   public String getNomeMae() {
      return nomeMae;
   }

   public void setNomeMae(String nomeMae) {
      this.nomeMae = nomeMae;
   }

   public Date getDataAtualizacao() {
      return dataAtualizacao;
   }

   public void setDataAtualizacao(String dataAtualizacao) {
      if (dataAtualizacao != null) {
         try {
            this.dataAtualizacao = new SimpleDateFormat("yyyyMMdd").parse(dataAtualizacao);
         } catch (ParseException e) {
            log.error(e.getMessage());
         }
      }
   }

   public Date getDataNascimento() {
      return dataNascimento;
   }

   public void setDataNascimento(String dataNascimento) {
      if (dataNascimento != null) {
         try {
            this.dataNascimento = new SimpleDateFormat("yyyyMMdd").parse(dataNascimento);
         } catch (ParseException e) {
            log.error(e.getMessage());
         }
      }
   }

   public Byte getSexo() {
      return sexo;
   }

   public void setSexo(Byte sexo) {
      this.sexo = sexo;
   }

   public String getTituloEleitor() {
      return tituloEleitor;
   }

   public void setTituloEleitor(String tituloEleitor) {
      this.tituloEleitor = tituloEleitor;
   }

   public Integer getAnoObito() {
      return anoObito;
   }

   public void setAnoObito(Integer anoObito) {
      this.anoObito = anoObito;
   }

   public String getErro() {
      return erro;
   }

   public void setErro(String erro) {
      this.erro = erro;
   }

   public Date getDataConsulta() {
      return dataConsulta;
   }

   public void setDataConsulta(Date dataConsulta) {
      this.dataConsulta = dataConsulta;
   }

   public String getIdUsuario() {
      return idUsuario;
   }

   public void setIdUsuario(String idUsuario) {
      this.idUsuario = idUsuario;
   }
   
}
