package br.com.abgi.web.listener;

import java.io.IOException;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

import br.com.abgi.web.controller.SessaoMBean;

/**
 * 
 * @author Abimael
 */
//@Log4j
public class LoginPhaseListener implements PhaseListener {

	private static final long serialVersionUID = 1L;
	private static final String LOGIN = "login";
	private static final String VIEW_ID = "/login.xhtml";

	@Override
	public void afterPhase(PhaseEvent event) {

		FacesContext context = event.getFacesContext();

		if (isTelaDeLogin(context)) {
			return;
		} else if (isAjaxRequest(context)) {
			if (isSessaoExpirada(context)) {
				redirecionarParaTelaDeLogin();
				return;
			}
		} else if (isSessaoExpirada(context)) {
			navegarParaTelaDeLogin(context);
			return;
		}

		if (isUsuarioConectado(context)) {
			navegarParaTelaDeLogin(context);
			return;
		}

	}

	@Override
	public void beforePhase(PhaseEvent pe) {
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

	private void redirecionarParaTelaDeLogin() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(LOGIN + ".jsf");
		} catch (IOException e) {
			//log.debug("Erro ao redirecionar!");
		}
	}

	private void navegarParaTelaDeLogin(FacesContext context) {
		NavigationHandler handler = context.getApplication().getNavigationHandler();
		handler.handleNavigation(context, null, LOGIN);

		context.renderResponse();
	}

	private boolean isTelaDeLogin(FacesContext context) {
		return VIEW_ID.equals(context.getViewRoot().getViewId());
	}

	private boolean isAjaxRequest(FacesContext context) {
		return context.getPartialViewContext().isAjaxRequest();
	}

	private boolean isSessaoExpirada(FacesContext context) {
		HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
		return (session == null) || (session.isNew());
	}

	private boolean isUsuarioConectado(FacesContext context) {
		SessaoMBean sessaoMBean = context.getApplication().evaluateExpressionGet(context, "#{sessaoMBean}", SessaoMBean.class);
		return false; //sessaoMBean.getUsuarioLogado() == null || sessaoMBean.getUsuarioLogado().getUsuarioId() == null;
	}

}
