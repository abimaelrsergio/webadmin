package br.com.abgi.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author abimael
 * @version 1
 * 
 */
@Cacheable(value = false)
@Entity
@Getter
@Setter
@ToString(of = { "usuarioId" })
@EqualsAndHashCode(callSuper = false, of = { "usuarioId" })
@Table(name = "tb_usuario")
@NamedQueries({ @NamedQuery(name = "UsuarioEntity.findByUsuarioId", query = "SELECT u FROM UsuarioEntity u WHERE u.usuarioId = :usuarioId"),
		@NamedQuery(name = "UsuarioEntity.findByLoginSenha", query = "SELECT u FROM UsuarioEntity u WHERE u.senha = :senha AND u.login = :login"),
		@NamedQuery(name = "UsuarioEntity.findByLogin", query = "SELECT u FROM UsuarioEntity u WHERE u.login = :login") })
public class UsuarioEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "usuario_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UsuarioGen")
	@SequenceGenerator(name = "UsuarioGen", sequenceName = "tb_usuario_usuario_id_SEQ", allocationSize = 1)
	private Long usuarioId;

	@Basic(optional = false)
	@Column(name = "senha")
	private String senha;

	@Basic(optional = false)
	@Column(name = "login")
	private String login;

	@Column(name = "excluido")
	private Boolean excluido;

	// @JoinColumn(name = "pessoa_id", referencedColumnName = "pessoa_id",
	// updatable = false)
	// @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade =
	// CascadeType.ALL)
	// private PessoaEntity pessoaId;
	//
	// @JoinColumn(name = "nivel_id", referencedColumnName = "nivel_id")
	// @ManyToOne(optional = false)
	// private NivelEntity nivelId;
	//
	// @JoinColumn(name = "cliente_id", referencedColumnName = "cliente_id",
	// updatable = false)
	// @ManyToOne(optional = false, fetch = FetchType.EAGER)
	// private ClienteEntity clienteEntity;
	//
	// @OneToMany(cascade = CascadeType.ALL,mappedBy = "usuarioId")
	// private List<IpAcessoEntity> ipAcessoId;

	/**
	 * Campo utilizado na tela
	 */
	@Transient
	private String codNivel;

	/**
	 * Campo utilizado na tela
	 */
	@Transient
	private String ordem;

	public boolean existe() {
		return (this.getUsuarioId() != null && this.getUsuarioId() > 0 && !this.getExcluido());
	}
}
