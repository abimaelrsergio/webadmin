package br.com.abgi.web.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.Getter;
import lombok.Setter;
import br.com.abgi.base.utils.Constants;
import br.com.abgi.model.UsuarioEntity;
import br.com.abgi.service.SessaoBean;

/**
 * 
 * @author abimael
 * @version 1
 *
 */
@ManagedBean
@SessionScoped
public class SessaoMBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// @EJB
	// private ServicoDeEmail servicoEmail;
	// @EJB
	// private IClienteBean clienteBean;
	@EJB
	private SessaoBean sessaoBean;
	// @EJB
	// private IUsuarioBean usuarioBean;
	// @EJB
	// private INivelService nivelService;
	@Getter
	@Setter
	private UsuarioEntity usuarioLogado;
	@Getter
	@Setter
	private boolean abrirLogin = true;
	@Getter
	@Setter
	private String login;
	@Getter
	@Setter
	private String senha;
	@Getter
	private String webSessionId;
	@Getter
	private String ip;

	private boolean ipPermitido = true;

	// public Boolean getClienteExiste() {
	// return this.getUsuarioLogado() != null &&
	// this.getUsuarioLogado().getClienteEntity() != null &&
	// this.getUsuarioLogado().getClienteEntity().existe();
	// }

	public void login(final ActionEvent actionEvent) {
		FacesMessage msg = null;

		if (this.efetuarLogin()) {
			abrirLogin = false;
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, String.format("%s, bem vindo ao WebAdmin.", login), login);
			webSessionId = obterIdSessaoUsuario();
		} else {
			abrirLogin = true;
			if (ipPermitido) {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login ou senha inválido", "Login");
			} else {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Acessando de local não autorizado !", "Login");
			}
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
		if (!abrirLogin) {
			final FacesContext facesContext = FacesContext.getCurrentInstance();
			try {
				facesContext.getExternalContext().redirect("index.jsf");
				facesContext.responseComplete();
			} catch (IOException ex) {
				Logger.getLogger(SessaoMBean.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public boolean efetuarLogin() {
		boolean ok = true;
		usuarioLogado = sessaoBean.findByLoginSenha(login, senha);

		if (usuarioLogado == null) {
			ok = Boolean.FALSE;
		} else {
			this.ipPermitido = ipPermitido();
			ok = usuarioLogado.existe() && ipPermitido;
		}

		return ok;
	}

	public String obterIdSessaoUsuario() {
		final HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		return session.getId();
	}

	private boolean ipPermitido() {
		this.ip = obterIpDoUsuarioRemoto();

		boolean permitido = true;

		// if(usuarioLogado != null && usuarioLogado.getIpAcessoId() != null){
		// final List<IpAcessoEntity> ipsPermitidos =
		// usuarioLogado.getIpAcessoId();
		//
		// for (final Iterator<IpAcessoEntity>iterator =
		// ipsPermitidos.iterator(); iterator.hasNext();) {
		// final IpAcessoEntity ipAcessoEntity = iterator.next();
		// if(ip.trim().equals(ipAcessoEntity.getIp().trim())){
		// permitido = true;
		// break;
		// }else{
		// permitido = false;
		// }
		// }
		// }
		return permitido;
	}

	public void logout() {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		this.abrirLogin = true;
//		this.setUsuarioLogado(null);
		this.login = Constants.BLANK;
		this.senha = Constants.BLANK;
		this.webSessionId = Constants.BLANK;

		try {
			facesContext.getExternalContext().redirect("login.jsf");
			facesContext.responseComplete();
			final HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
			session.invalidate();
		} catch (IOException ex) {
			Logger.getLogger(SessaoMBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	// public void esqueciSenha() {
	// FacesMessage msg;
	// final UsuarioEntity usuarioEntity =
	// sessaoBean.findUsuariosByLogin(login);
	// if (usuarioEntity == null || usuarioEntity.getExcluido()) {
	// msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
	// "Usuário não encontrado", login);
	// } else {
	// final String novaSenha = usuarioBean.resetSenha(usuarioEntity);
	// servicoEmail.enviarEmaiEsqueciSenha(usuarioEntity, novaSenha);
	// msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
	// "Uma nova senha foi encaminhada para o seu email cadastrado", login);
	// }
	// FacesContext.getCurrentInstance().addMessage(null, msg);
	// }

	private String obterIpDoUsuarioRemoto() {
		final HttpServletRequest httpServReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		return httpServReq.getRemoteAddr();
	}

}
